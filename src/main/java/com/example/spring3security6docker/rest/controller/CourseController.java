package com.example.spring3security6docker.rest.controller;

import com.example.spring3security6docker.dao.entity.Course;
import com.example.spring3security6docker.dao.entity.Status;
import com.example.spring3security6docker.dto.request.CourseCreateRequestDto;
import com.example.spring3security6docker.dto.CourseDto;
import com.example.spring3security6docker.dto.PagerQueryDto;
import com.example.spring3security6docker.mapper.CourseMapper;
import com.example.spring3security6docker.repository.CourseRepository;
import com.example.spring3security6docker.service.CourseService;
import com.example.spring3security6docker.service.impl.CourseServiceImplR2;
import com.example.spring3security6docker.support.PageSupport;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import javax.validation.Valid;
import java.text.ParseException;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping(path = "/api/course")
public class CourseController {

    private final CourseRepository repository;
    private final CourseServiceImplR2 service;
    private final Logger logger;
    private final CourseMapper mapper;

    @Autowired
    public CourseController(
            CourseRepository repository,
            CourseServiceImplR2 service,
            CourseMapper mapper
    ) {
        this.repository = repository;
        this.service = service;
        this.logger = LoggerFactory.getLogger(CourseController.class);
        this.mapper = mapper;
    }

//    @GetMapping("/last5")
//    public ResponseEntity<List<CourseDto>> last5() throws Exception {
//        return ResponseEntity.ok(service.last5());
//    }

//    @PostMapping("/all")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public ResponseEntity<Page<CourseDto>> getAllCourses(
//            @RequestBody PagerQueryDto pagerQueryDto
//    ) throws ParseException {
//        System.out.println("PagerQueryDto => ");
//        System.out.println(pagerQueryDto.toString());
//        Page<CourseDto> courses = service.getCourses(pagerQueryDto);
//
//        return ResponseEntity.ok(courses);
//    }
//
//    @GetMapping("/{id}")
//    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
//    public ResponseEntity<Optional<CourseDto>> getCourseById(
//        @PathVariable Long id
//    ) throws Exception {
//        Optional<CourseDto> courseDto = null;
//
//        // можно обойтись и без try-catch, тогда будет возвращаться полная ошибка (stacktrace)
//        // здесь показан пример, как можно обрабатывать исключение и отправлять свой текст/статус
//        try {
//            courseDto = service.getCourseById(id).map(mapper::toDto);
//        } catch (NoSuchElementException e) { // если объект не будет найден
//            throw new Exception(e.getMessage());
//        }
//
//        return ResponseEntity.ok(courseDto);
//    }
//
//    @PostMapping("/create")
//    @PreAuthorize("hasRole('MODERATOR') or hasRole('ADMIN')")
//    public ResponseEntity<CourseDto> create(@RequestBody @Valid CourseCreateRequestDto request) throws Exception {
//        return ResponseEntity.ok(mapper.toDto(service.save(request)));
//    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @PostMapping("/list/pager")
    public Mono<PageSupport<Course>> getCourseAllByPager(
            @RequestBody PagerQueryDto pagerQueryDto
    ) {
        Sort sort = pagerQueryDto.getSortDirection().equals("asc") ? Sort.by(pagerQueryDto.getSortBy()).ascending() : Sort.by(pagerQueryDto.getSortBy()).descending();
        return service.getCourseAllByPager(PageRequest.of(pagerQueryDto.getPage() - 1, pagerQueryDto.getPageSize(), sort));
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/active")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Course> getAllCoursesByActive() {
        return service.findAllByStatus(Status.ACTIVE);
    }

    @GetMapping("/last5")
    @ResponseStatus(HttpStatus.OK)
    public Flux<Course> getAllCoursesTop5() {
        return service.findTop5ByStatus(Status.ACTIVE);
    }

    @PreAuthorize("hasRole('USER') or hasRole('MODERATOR') or hasRole('ADMIN')")
    @GetMapping("/{id}")
    @ResponseStatus(HttpStatus.OK)
    public Mono<Course> getAllCoursesById(@PathVariable("id") int id) {
        return service.findAllById(id);
    }
}
