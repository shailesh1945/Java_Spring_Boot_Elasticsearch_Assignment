package com.shailesh.elasticsearch.service;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.shailesh.elasticsearch.repository.CourseRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.elasticsearch.core.ElasticsearchOperations;
import org.springframework.data.elasticsearch.core.SearchHit;
import org.springframework.data.elasticsearch.core.SearchHits;
import org.springframework.data.elasticsearch.core.query.Criteria;
import org.springframework.data.elasticsearch.core.query.CriteriaQuery;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class SuggestionService {

    private final ElasticsearchOperations elasticsearchOperations;
    private final CourseRepository courseRepository;

    /**
     * Simple autocomplete using contains matching
     */
    public List<String> getAutocompleteSuggestions(String prefix) {
        Criteria criteria = new Criteria("title").contains(prefix);
        CriteriaQuery query = new CriteriaQuery(criteria);

        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(query, CourseDocument.class);

        return searchHits.getSearchHits().stream()
                .map(hit -> hit.getContent().getTitle())
                .distinct()
                .limit(10)
                .collect(Collectors.toList());
    }

    /**
     * Fuzzy-like search using contains matching
     */
    public List<CourseDocument> fuzzySearch(String query) {
        Criteria criteria = new Criteria("title").contains(query).or("description").contains(query);
        CriteriaQuery searchQuery = new CriteriaQuery(criteria);

        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(searchQuery, CourseDocument.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }

    /**
     * Search with filters using Criteria API
     */
    public List<CourseDocument> fuzzySearchWithFilters(String query, Integer minAge, Integer maxAge,
                                                       String category, String type, Double minPrice, Double maxPrice) {

        Criteria criteria = new Criteria();

        // Text search
        if (query != null && !query.trim().isEmpty()) {
            criteria = criteria.and(new Criteria("title").contains(query).or("description").contains(query));
        }

        // Filters
        if (minAge != null) {
            criteria = criteria.and(new Criteria("minAge").greaterThanEqual(minAge));
        }
        if (maxAge != null) {
            criteria = criteria.and(new Criteria("maxAge").lessThanEqual(maxAge));
        }
        if (category != null && !category.isEmpty()) {
            criteria = criteria.and(new Criteria("category").is(category));
        }
        if (type != null && !type.isEmpty()) {
            criteria = criteria.and(new Criteria("type").is(type));
        }
        if (minPrice != null) {
            criteria = criteria.and(new Criteria("price").greaterThanEqual(minPrice));
        }
        if (maxPrice != null) {
            criteria = criteria.and(new Criteria("price").lessThanEqual(maxPrice));
        }

        CriteriaQuery searchQuery = new CriteriaQuery(criteria);

        SearchHits<CourseDocument> searchHits = elasticsearchOperations.search(searchQuery, CourseDocument.class);

        return searchHits.getSearchHits().stream()
                .map(SearchHit::getContent)
                .collect(Collectors.toList());
    }
}