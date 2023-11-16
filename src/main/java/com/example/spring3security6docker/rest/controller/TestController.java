package com.example.spring3security6docker.rest.controller;

import com.example.spring3security6docker.dao.entity.Course;
import com.example.spring3security6docker.dao.entity.Role;
import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.dto.PagerQueryDto;
import com.example.spring3security6docker.dto.request.LoginRequest;
import com.example.spring3security6docker.dto.request.UsernameRequest;
import com.example.spring3security6docker.dto.response.AuthResultDto;
import com.example.spring3security6docker.dto.response.JwtResponse;
import com.example.spring3security6docker.mapper.UserMapper;
import com.example.spring3security6docker.repository.RoleRepository;
//import com.example.spring3security6docker.repository.UserRepository;
import com.example.spring3security6docker.security.SecurityService;
//import com.example.spring3security6docker.security.jwt.JwtUtils;
//import com.example.spring3security6docker.security.services.UserDetailsImpl;
import com.example.spring3security6docker.service.CourseService;
import com.example.spring3security6docker.service.UserService;
import com.example.spring3security6docker.service.impl.CourseServiceImplR2;
import com.example.spring3security6docker.service.impl.RoleServiceImpl;
import com.example.spring3security6docker.support.PageSupport;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Set;
import java.util.concurrent.ExecutionException;
import java.util.stream.Collectors;

import static com.example.spring3security6docker.support.PageSupport.DEFAULT_PAGE_SIZE;
import static com.example.spring3security6docker.support.PageSupport.FIRST_PAGE_NUM;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/test")
public class TestController {

//    private final UserRepository userRepository;

    private final RoleRepository roleRepository;

    private final PasswordEncoder encoder;

    private final UserService userService;

    private final RoleServiceImpl roleService;

//    private final JwtUtils jwtUtils;

    private final UserMapper userMapper;

    private final CourseServiceImplR2 courseService;

    private final SecurityService securityService;

    public TestController(
//                          UserRepository userRepository,
                          RoleRepository roleRepository,
                          PasswordEncoder encoder,
                          UserService userService,
                          RoleServiceImpl roleService,
//                          JwtUtils jwtUtils,
                          UserMapper userMapper,
                          CourseServiceImplR2 courseService,
                          SecurityService securityService
    ) {
//        this.userRepository = userRepository;
        this.roleRepository = roleRepository;
        this.encoder = encoder;
        this.userService = userService;
        this.roleService = roleService;
//        this.jwtUtils = jwtUtils;
        this.userMapper = userMapper;
        this.courseService = courseService;
        this.securityService = securityService;
    }

    @GetMapping("/all")
    public String allAccess() {
        return "Public Content.";
    }

    @GetMapping("/user")
    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    public String userAccess() {
        return "User Content.";
    }

    @GetMapping("/mod")
    @PreAuthorize("hasRole('MODERATOR')")
    public String moderatorAccess() {
        return "Moderator Board.";
    }

    @GetMapping("/admin")
    @PreAuthorize("hasRole('ADMIN')")
    public String adminAccess() {
        return "Admin Board.";
    }

    @GetMapping("/hello")
    public Mono<String> sayHello() {
        return Mono.just("Hello, World!");
    }

    @GetMapping("/numbers")
    public Flux<Integer> generateNumbers() {
        return Flux.range(1, 10); // Генерирует числа от 1 до 10
    }

//    @GetMapping("/courses")
//    @ResponseStatus(HttpStatus.OK)
//    public Flux<Course> getAllCourses() {
//        return courseService.findAll();
//    }

    @GetMapping("/courses")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Course> getAllCoursesByActive() {
        return courseService.findAllByStatus(Status.ACTIVE);
    }

    @GetMapping("/courses/pager")
    public Mono<PageSupport<Course>> getEntitiesPage(
            @RequestParam(name = "page", defaultValue = FIRST_PAGE_NUM) int page,
            @RequestParam(name = "size", defaultValue = DEFAULT_PAGE_SIZE) int size
    ) {

        return courseService.getCourseAllByPager(PageRequest.of(page, size));
    }

//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/courses/pager/post")
    public Mono<PageSupport<Course>> getCourseAllByPager(
            @RequestBody PagerQueryDto pagerQueryDto
    ) {
        Sort sort = pagerQueryDto.getSortDirection().equals("asc") ? Sort.by(pagerQueryDto.getSortBy()).ascending() : Sort.by(pagerQueryDto.getSortBy()).descending();
        return courseService.getCourseAllByPager(PageRequest.of(pagerQueryDto.getPage() - 1, pagerQueryDto.getPageSize(), sort));
    }

    @GetMapping("/courses/last5")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Course> getAllCoursesTop5() {
        return courseService.findTop5ByStatus(Status.ACTIVE);
    }

    @GetMapping("/courses/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Course> getAllCoursesById(@PathVariable("id") int id) {
        return courseService.findAllById(id);
    }


    @PostMapping("/login-test")
    public Mono<ResponseEntity<Object>> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {
        return securityService.authenticate(loginRequest.getUsername(), loginRequest.getPassword())
                .flatMap(tokenInfo -> Mono.just(ResponseEntity.ok(AuthResultDto.builder()
                        .userId(tokenInfo.getUserId())
                        .token(tokenInfo.getToken())
                        .issuedAt(tokenInfo.getIssuedAt())
                        .expiresAt(tokenInfo.getExpiresAt())
                        .build())));
    }

    @PostMapping("/getroles")
    public Mono<Set<Role>> getRolesByUsername(@Valid @RequestBody UsernameRequest usernameRequest) {
        try {
            System.out.println("getRolesByUsername roles => " + userService.getRolesByUsernameToSet(usernameRequest.getUsername()));
            System.out.println(userService.getRolesByUsernameToSet(usernameRequest.getUsername()).getClass());
        } catch (ExecutionException e) {
            e.printStackTrace();
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        return userService.getRolesByUsername(usernameRequest.getUsername());
    }
}
