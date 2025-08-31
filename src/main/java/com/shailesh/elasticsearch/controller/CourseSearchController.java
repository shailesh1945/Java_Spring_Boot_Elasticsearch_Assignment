package com.shailesh.elasticsearch.controller;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.shailesh.elasticsearch.service.CourseSearchService;
import lombok.RequiredArgsConstructor;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/search")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class CourseSearchController {

    private final CourseSearchService courseSearchService;

    @GetMapping
    public CourseSearchService.SearchResult searchCourses(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return courseSearchService.searchCourses(q, minAge, maxAge, category, type,
                minPrice, maxPrice, startDate, sort, page, size);
    }

    @GetMapping("/all")
    public List<CourseDocument> getAllCourses() {
        // For testing purposes
        return courseSearchService.searchCourses(null, null, null, null, null,
                null, null, null, "upcoming", 0, 100).courses();
    }

    // Add this method to the controller
    @GetMapping("/fuzzy")
    public CourseSearchService.SearchResult searchCoursesWithFuzzy(
            @RequestParam(required = false) String q,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice,
            @RequestParam(required = false) @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime startDate,
            @RequestParam(defaultValue = "upcoming") String sort,
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {

        return courseSearchService.searchCoursesWithFuzzy(q, minAge, maxAge, category, type,
                minPrice, maxPrice, startDate, sort, page, size);
    }

}
