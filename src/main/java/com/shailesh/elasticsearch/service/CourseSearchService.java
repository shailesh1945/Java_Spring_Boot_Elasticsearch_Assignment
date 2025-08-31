package com.shailesh.elasticsearch.service;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.shailesh.elasticsearch.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.data.elasticsearch.core.query.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CourseSearchService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CourseRepository courseRepository;

    /**
     * Standard search with filters
     */
    public SearchResult searchCourses(String query, Integer minAge, Integer maxAge, String category,
                                      String type, Double minPrice, Double maxPrice,
                                      LocalDateTime startDate, String sort, int page, int size) {

        Criteria criteria = new Criteria();

        // Full-text search (contains)
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria.and(
                    new Criteria("title").contains(query).or("description").contains(query)
            );
        }

        // Filters
        if (minAge != null) criteria = criteria.and(new Criteria("minAge").greaterThanEqual(minAge));
        if (maxAge != null) criteria = criteria.and(new Criteria("maxAge").lessThanEqual(maxAge));
        if (category != null && !category.trim().isEmpty()) criteria = criteria.and(new Criteria("category").is(category));
        if (type != null && !type.trim().isEmpty()) criteria = criteria.and(new Criteria("type").is(type));
        if (minPrice != null) criteria = criteria.and(new Criteria("price").greaterThanEqual(minPrice));
        if (maxPrice != null) criteria = criteria.and(new Criteria("price").lessThanEqual(maxPrice));
        if (startDate != null) criteria = criteria.and(new Criteria("nextSessionDate").greaterThanEqual(startDate));

        Query searchQuery = new CriteriaQuery(criteria).setPageable(PageRequest.of(page, size));

        // Sorting
        applySorting(sort, searchQuery);

        // Execute
        return executeSearch(searchQuery);
    }

    /**
     * Fuzzy search (typo-tolerant)
     */
    public SearchResult searchCoursesWithFuzzy(String query, Integer minAge, Integer maxAge, String category,
                                               String type, Double minPrice, Double maxPrice,
                                               LocalDateTime startDate, String sort, int page, int size) {

        Criteria criteria = new Criteria();

        // Fuzzy search
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria.and(
                    new Criteria("title").fuzzy(query).or("description").fuzzy(query)
            );
        }

        // Filters
        if (minAge != null) criteria = criteria.and(new Criteria("minAge").greaterThanEqual(minAge));
        if (maxAge != null) criteria = criteria.and(new Criteria("maxAge").lessThanEqual(maxAge));
        if (category != null && !category.trim().isEmpty()) criteria = criteria.and(new Criteria("category").is(category));
        if (type != null && !type.trim().isEmpty()) criteria = criteria.and(new Criteria("type").is(type));
        if (minPrice != null) criteria = criteria.and(new Criteria("price").greaterThanEqual(minPrice));
        if (maxPrice != null) criteria = criteria.and(new Criteria("price").lessThanEqual(maxPrice));
        if (startDate != null) criteria = criteria.and(new Criteria("nextSessionDate").greaterThanEqual(startDate));

        Query searchQuery = new CriteriaQuery(criteria).setPageable(PageRequest.of(page, size));

        // Sorting
        applySorting(sort, searchQuery);

        // Execute
        return executeSearch(searchQuery);
    }

    /**
     * Common method for sorting
     */
    private void applySorting(String sort, Query searchQuery) {
        if ("priceAsc".equals(sort)) {
            searchQuery.addSort(Sort.by("price").ascending());
        } else if ("priceDesc".equals(sort)) {
            searchQuery.addSort(Sort.by("price").descending());
        } else {
            searchQuery.addSort(Sort.by("nextSessionDate").ascending());
        }
    }

    /**
     * Common method for executing search
     */
    private SearchResult executeSearch(Query searchQuery) {
        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(searchQuery, CourseDocument.class);
        List<CourseDocument> courses = searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
        return new SearchResult(searchHits.getTotalHits(), courses);
    }

    public record SearchResult(long total, List<CourseDocument> courses) {}
}
