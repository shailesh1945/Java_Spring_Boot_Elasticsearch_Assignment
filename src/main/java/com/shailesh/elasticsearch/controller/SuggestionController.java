package com.shailesh.elasticsearch.controller;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.shailesh.elasticsearch.service.SuggestionService;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/suggestions")
@RequiredArgsConstructor
@CrossOrigin(origins = "*")
public class SuggestionController {

    private final SuggestionService suggestionService;

    @GetMapping("/autocomplete")
    public List<String> autocomplete(@RequestParam String prefix) {
        return suggestionService.getAutocompleteSuggestions(prefix);
    }

    @GetMapping("/fuzzy")
    public List<CourseDocument> fuzzySearch(@RequestParam String query) {
        return suggestionService.fuzzySearch(query);
    }

    @GetMapping("/fuzzy/filtered")
    public List<CourseDocument> fuzzySearchWithFilters(
            @RequestParam String query,
            @RequestParam(required = false) Integer minAge,
            @RequestParam(required = false) Integer maxAge,
            @RequestParam(required = false) String category,
            @RequestParam(required = false) String type,
            @RequestParam(required = false) Double minPrice,
            @RequestParam(required = false) Double maxPrice) {

        return suggestionService.fuzzySearchWithFilters(query, minAge, maxAge,
                category, type, minPrice, maxPrice);
    }
}
