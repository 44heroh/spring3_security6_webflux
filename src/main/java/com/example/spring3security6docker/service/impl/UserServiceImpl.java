package com.example.spring3security6docker.service.impl;

import com.example.spring3security6docker.dao.entity.ERole;
import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.dao.entity.User;
import com.example.spring3security6docker.dto.request.SignupRequest;
import com.example.spring3security6docker.mapper.UserMapper;
//import com.example.spring3security6docker.repository.UserRepository;
import com.example.spring3security6docker.repository.RoleRepositoryR2;
import com.example.spring3security6docker.repository.UserRepositoryR2;
import com.example.spring3security6docker.service.UserService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.data.crossstore.ChangeSetPersister;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.ExecutionException;

@Service
@Slf4j
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final PasswordEncoder encoder;
    private final UserRepositoryR2 userRepository;
    private final RoleRepositoryR2 roleRepository;
    private final UserMapper mapper;
    private final PasswordEncoder passwordEncoder;

    @Override
    public User registerUser(SignupRequest signUpRequest) {
//        // Create new user's account
//        User user = new User(signUpRequest, encoder.encode(signUpRequest.getPassword()));
//
//        Set<String> strRoles = signUpRequest.getRoles();
//        Set<Role> roles = new HashSet<>();
//
//        if (strRoles == null) {
//            Role userRole = roleRepository.findByName(String.valueOf(ERole.ROLE_USER))
//                    .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//            roles.add(userRole);
//        } else {
//            strRoles.forEach(role -> {
//                switch (role) {
//                    case "admin":
//                        Role adminRole = roleRepository.findByName(String.valueOf(ERole.ROLE_ADMIN))
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(adminRole);
//
//                        break;
//                    case "mod":
//                        Role modRole = roleRepository.findByName(String.valueOf(ERole.ROLE_MODERATOR))
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(modRole);
//
//                        break;
//                    default:
//                        Role userRole = roleRepository.findByName(String.valueOf(ERole.ROLE_USER))
//                                .orElseThrow(() -> new RuntimeException("Error: Role is not found."));
//                        roles.add(userRole);
//                }
//            });
//        }
//
//        user.setRoles(roles);
//        User ans = (User) userRepository.save(user).subscribe();
//
//        return ans;
        return new User();
    }

    public void createUser1(SignupRequest signUpRequest) {
        User user = new User(signUpRequest, encoder.encode(signUpRequest.getPassword()));
        Mono<Role> userRole = roleRepository.findByName(String.valueOf(ERole.ROLE_USER)).map(
               role -> {
                   System.out.println("role1 => " + role.toString());
                   return role;
               }
        );
        Role role = userRole.block();
        System.out.println("role2 => " + role);
        Set<Role> roles = new HashSet<>();
        roles.add(role);
        user.setRoles(roles);

        userRepository.save(user).subscribe();
    }

    public Mono<Void> createUser(SignupRequest signUpRequest) {
        System.out.println("signUpRequest.getPassword() => " + signUpRequest.getPassword());
        System.out.println("encoder.encode(signUpRequest.getPassword()) => " + encoder.encode(signUpRequest.getPassword()));
        User user = new User(signUpRequest, encoder.encode(signUpRequest.getPassword()));
        Set<Role> roles = new HashSet<>();
        Mono<Role> userRole = roleRepository.findByName(String.valueOf(ERole.ROLE_USER)).map(
                role -> {
                    roles.add(role);
                    return role;
                }
        );

        user.setRoles(roles);
        return userRepository.save(user).flatMap(savedUser -> {
                    // Затем сохраните роли и обновите коллекцию ролей пользователя
                    System.out.println("savedUser => " + savedUser.toString());
                    Flux<Role> savedRoles = roleRepository.saveAll(savedUser.getRoles());
                    return savedRoles.collectList().map(savedRolesList -> {
                        savedUser.setRoles(new HashSet<>(savedRolesList));
                        return savedUser;
                    });
                }).then();
    }

    public Mono<User> findById(int id) {
        return userRepository
                .findById(id)
                .switchIfEmpty(Mono.error(new Exception("User not found")))
                .flatMap(user -> {
                    return Mono.just(user);
                });
    }

    public Mono<User> getUserById(Long id) {
        return userRepository.findById(id)
                .switchIfEmpty(Mono.error(new Exception("User not found")))
                .flatMap(user -> {
                    return Mono.just(user);
                });
    }

    public Mono<User> findByUsernameTop1(String username) {
        return userRepository.findByUsernameTop1(username);
    }

    public Mono<User> getUserByUsername(String username) {
        return userRepository.findByUsername(username);
    }

    public Mono<Set<Role>> getRolesByUsername(String username) {
        Flux<Role> roleFlux = roleRepository.findRolesByUsername(username);
        // Конвертируем Flux в Set
        Mono<Set<Role>> rolesSetMono = roleFlux.collect(HashSet::new, Set::add);

        // Подписываемся на Mono для получения результата
        rolesSetMono.subscribe(
                dataSet -> {
                    System.out.println("Converted Set: " + dataSet);
                    // Далее можно выполнять дополнительные действия с полученным Set
                },
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Conversion completed")
        );
        return rolesSetMono;
    }

    public Mono<Set<Role>> getRolesByUserId(int userId) {
        Flux<Role> roleFlux = roleRepository.findRolesByUserId(userId);
        // Конвертируем Flux в Set
        Mono<Set<Role>> rolesSetMono = roleFlux.collect(HashSet::new, Set::add);

        // Подписываемся на Mono для получения результата
        rolesSetMono.subscribe(
                dataSet -> {
                    System.out.println("Converted Set: " + dataSet);
                    // Далее можно выполнять дополнительные действия с полученным Set
                },
                error -> System.err.println("Error: " + error),
                () -> System.out.println("Conversion completed")
        );
        return rolesSetMono;
    }

    public Set<Role> getRolesByUsernameToSet(String username) throws ExecutionException, InterruptedException {
        Flux<Role> roleFlux = roleRepository.findRolesByUsername(username).switchIfEmpty(Flux.error(new Exception("Roles not found")));
        // Конвертируем Flux в Set
        Mono<Set<Role>> rolesSetMono = roleFlux.collect(HashSet::new, Set::add);
        return rolesSetMono.toFuture().get();
    }

}
