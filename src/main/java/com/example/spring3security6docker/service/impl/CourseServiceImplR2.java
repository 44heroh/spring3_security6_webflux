package com.example.spring3security6docker.service.impl;

import com.example.spring3security6docker.dao.entity.Course;
import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.mapper.CourseMapper;
import com.example.spring3security6docker.repository.CourseRepository;
import com.example.spring3security6docker.repository.CourseRepositoryR2;
import com.example.spring3security6docker.rest.controller.CourseController;
import com.example.spring3security6docker.service.CourseService;
import com.example.spring3security6docker.support.PageSupport;
//import jakarta.transaction.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.util.stream.Collectors;

// всегда нужно создавать отдельный класс Service для доступа к данным, даже если кажется,
// что мало методов или это все можно реализовать сразу в контроллере
// Такой подход полезен для будущих доработок и правильной архитектуры (особенно, если работаете с транзакциями)
@Service
// все методы класса должны выполниться без ошибки, чтобы транзакция завершилась
// если в методе возникнет исключение - все выполненные операции откатятся (Rollback)
@Transactional
public class CourseServiceImplR2 {
    private final CourseRepositoryR2 repository;
    public static final String ID_COLUMN = "id";
    public static final Integer PAGE_SIZE_DEFAULT = 10;
    private final Logger logger;
    private final CourseMapper mapper;

    public CourseServiceImplR2(
            CourseRepositoryR2 repository,
            CourseMapper mapper
    ) {
        this.repository = repository;
        this.logger = LoggerFactory.getLogger(CourseController.class);
        this.mapper = mapper;
    }

    public Flux<Course> findAll() {
        Flux<Course> ans = repository.findAll();
        System.out.println(this.getClass() + " ans -> ");
        System.out.println(ans);
        return ans;
    }

    public Mono<Course> findAllById(int id) {
        Mono<Course> ans = repository.findAllById(id);
        System.out.println(this.getClass() + " ans -> ");
        System.out.println(ans);
        return ans;
    }

    public Flux<Course> findAllByStatus(Status status) {
        Flux<Course> ans = repository.findAllByStatus(status);
        System.out.println(this.getClass() + " ans -> ");
        System.out.println(ans);
        return ans;
    }

    public Flux<Course> findTop5ByStatus(Status status) {
        Flux<Course> ans = repository.findTop5ByStatus(status);
        System.out.println(this.getClass() + " ans -> ");
        System.out.println(ans);
        return ans;
    }

    public Mono<PageSupport<Course>> getCourseAllByPager(Pageable page) {
        return repository.findAllByStatus(Status.ACTIVE)
                .collectList()
                .map(list -> new PageSupport<>(
                        list
                                .stream()
                                .skip(page.getPageNumber() * page.getPageSize())
                                .limit(page.getPageSize())
                                .collect(Collectors.toList()),
                        page.getPageNumber(), page.getPageSize(), list.size()
                ));
    }
}
