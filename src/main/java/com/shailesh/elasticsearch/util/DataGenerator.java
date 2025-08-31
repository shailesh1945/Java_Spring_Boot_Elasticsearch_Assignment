package com.shailesh.elasticsearch.util;

import com.shailesh.elasticsearch.model.CourseDocument;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.core.io.ResourceLoader;

import java.io.File;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public class DataGenerator {

    private static final String[] CATEGORIES = {"Math", "Science", "Art", "Music", "Technology", "Sports", "Language", "History"};
    private static final String[] TYPES = {"ONE_TIME", "COURSE", "CLUB"};
    private static final String[] GRADE_RANGES = {"1st-3rd", "4th-6th", "7th-9th", "10th-12th"};

    private static final String[] TITLE_PREFIXES = {
            "Introduction to", "Advanced", "Fundamentals of", "Creative", "Exploratory",
            "Hands-on", "Interactive", "Comprehensive", "Beginner's", "Professional"
    };

    private static final String[] TITLE_SUBJECTS = {
            "Mathematics", "Science", "Art", "Music", "Coding", "Robotics", "Dance",
            "Theater", "Creative Writing", "Sports", "Languages", "History", "Geography"
    };

    public static void main(String[] args) throws Exception {
        List<CourseDocument> courses = new ArrayList<>();
        Random random = new Random();

        for (int i = 1; i <= 50; i++) {
            CourseDocument course = new CourseDocument();
            course.setId(String.valueOf(i));

            String prefix = TITLE_PREFIXES[random.nextInt(TITLE_PREFIXES.length)];
            String subject = TITLE_SUBJECTS[random.nextInt(TITLE_SUBJECTS.length)];
            course.setTitle(prefix + " " + subject);

            course.setDescription("A comprehensive course covering various aspects of " + subject.toLowerCase());
            course.setCategory(CATEGORIES[random.nextInt(CATEGORIES.length)]);
            course.setType(TYPES[random.nextInt(TYPES.length)]);
            course.setGradeRange(GRADE_RANGES[random.nextInt(GRADE_RANGES.length)]);

            int baseAge = 6 + random.nextInt(10);
            course.setMinAge(baseAge);
            course.setMaxAge(baseAge + 2 + random.nextInt(4));

            course.setPrice(100 + random.nextDouble() * 400); // $100-$500

            // Random date in the next 60 days
            LocalDateTime now = LocalDateTime.now();
            LocalDateTime sessionDate = now.plusDays(random.nextInt(60))
                    .withHour(9 + random.nextInt(8))
                    .withMinute(0);
            course.setNextSessionDate(sessionDate);

            courses.add(course);
        }

        ObjectMapper mapper = new ObjectMapper();
        mapper.writeValue(new File("src/main/resources/sample-courses.json"), courses);

        System.out.println("Generated 50 sample courses");
    }
}