package com.shailesh.elasticsearch.service;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.shailesh.elasticsearch.repository.CourseRepository;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.Resource;
import org.springframework.core.io.ResourceLoader;
import org.springframework.stereotype.Service;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;

@Service
@RequiredArgsConstructor
@Slf4j
public class DataLoaderService {

    private final CourseRepository courseRepository;
    private final ResourceLoader resourceLoader;
    private final ObjectMapper objectMapper;

    @PostConstruct
    public void loadSampleData() {
        try {
            Resource resource = resourceLoader.getResource("classpath:sample-courses.json");
            if (resource.exists()) {
                InputStream inputStream = resource.getInputStream();
                List<CourseDocument> courses = objectMapper.readValue(inputStream, new TypeReference<List<CourseDocument>>() {});

                courseRepository.deleteAll();
                courseRepository.saveAll(courses);

                log.info("Loaded {} courses into Elasticsearch", courses.size());
            } else {
                log.warn("Sample courses file not found");
            }
        } catch (IOException e) {
            log.error("Error loading sample data", e);
        }
    }
}