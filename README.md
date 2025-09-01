````markdown
# Course Search Application

A Spring Boot application for searching educational courses with Elasticsearch, featuring advanced search, filtering, pagination, sorting, autocomplete suggestions, and fuzzy matching.

---

## Features
✅ Full-text search on course titles and descriptions  
✅ Multiple filters (age, category, type, price, date)  
✅ Pagination and sorting  
✅ Autocomplete suggestions  
✅ Fuzzy matching for typo tolerance  
✅ Dockerized Elasticsearch setup  
✅ Sample data auto-loading  

---

## Prerequisites
- Java 17 or higher
- Docker and Docker Compose
- Maven

---

## Quick Start

### 1. Clone and Setup
```bash
git clone <your-repository>
cd course-search
````

## assignment video



### 2. Start Elasticsearch

```bash
docker-compose up -d
```

Verify Elasticsearch is running:

```bash
curl http://localhost:9200
```

### 3. Run the Application

```bash
./mvnw spring-boot:run
```

The application will start on: [http://localhost:8080](http://localhost:8080)

---

## API Endpoints

### 1. Search Courses

```text
GET /api/search?q=math&category=Math&minAge=8&maxAge=12&minPrice=50&maxPrice=200&sort=priceAsc&page=0&size=10
```

**Parameters:**

* `q` (optional): Search query
* `category` (optional): Filter by category
* `type` (optional): Filter by type (`ONE_TIME`, `COURSE`, `CLUB`)
* `minAge`, `maxAge` (optional): Age range filter
* `minPrice`, `maxPrice` (optional): Price range filter
* `startDate` (optional): Courses starting after this date (ISO format)
* `sort` (optional): Sorting option (`upcoming`, `priceAsc`, `priceDesc`)
* `page` (optional): Page number (default: 0)
* `size` (optional): Page size (default: 10)

### 2. Autocomplete Suggestions

```text
GET /api/suggestions/autocomplete?prefix=math
```

### 3. Fuzzy Search

```text
GET /api/suggestions/fuzzy?query=mathematix
```

### 4. Filtered Fuzzy Search

```text
GET /api/suggestions/fuzzy/filtered?query=sciense&category=Science&maxPrice=300
```

### 5. Get All Courses (for testing)

```text
GET /api/search/all
```

---

## Sample Data

The application automatically loads 50+ sample courses on startup with:

* Various categories (Math, Science, Art, Music, Technology, Sports, Language, History)
* Different types (`ONE_TIME`, `COURSE`, `CLUB`)
* Age ranges from 6-16 years
* Prices from \$100-\$500
* Session dates spanning the next 60 days

**To verify data loading, check application logs for:**

```text
Loaded XX courses into Elasticsearch
```

---

## Example Usage

### 1. Search for math courses under \$200

```bash
curl "http://localhost:8080/api/search?q=math&category=Math&maxPrice=200&sort=priceAsc"
```

### 2. Find courses for ages 8-12

```bash
curl "http://localhost:8080/api/search?minAge=8&maxAge=12"
```

### 3. Get upcoming courses

```bash
curl "http://localhost:8080/api/search?sort=upcoming"
```

### 4. Autocomplete for "prog"

```bash
curl "http://localhost:8080/api/suggestions/autocomplete?prefix=prog"
```

### 5. Fuzzy search with typo

```bash
curl "http://localhost:8080/api/suggestions/fuzzy?query=mathematix"
```

---

## Project Structure

```text
src/
├── main/
│   ├── java/
│   │   └── com/shailesh/elasticsearch/
│   │       ├── config/
│   │       │   └── ElasticsearchConfig.java
│   │       ├── controller/
│   │       │   ├── CourseSearchController.java
│   │       │   └── SuggestionController.java
│   │       ├── model/
│   │       │   └── CourseDocument.java
│   │       ├── repository/
│   │       │   ├── CourseRepository.java
│   │       │   └── CustomCourseRepository.java
│   │       ├── service/
│   │       │   ├── CourseSearchService.java
│   │       │   ├── DataLoaderService.java
│   │       │   └── SuggestionService.java
│   │       ├── util/
│   │       │   └── DataGenerator.java
│   │       └── ElasticSearchApplication.java
│   └── resources/
│       ├── elasticsearch/
│       │   └── custom-analyzer.json
│       ├── sample-courses.json
│       └── application.properties
├── test/
└── docker-compose.yml
```

---

## Data Indexing

Sample data is automatically indexed on application startup from `src/main/resources/sample-courses.json`.

The indexing process:

1. Deletes existing courses index (if any)
2. Creates new index with custom analyzer mapping
3. Bulk indexes all sample courses
4. Logs success/failure status

---

## Customization

### Adding New Fields

* Update `CourseDocument.java` with new field
* Add field to `sample-courses.json`
* Update search service to handle new filters

### Modifying Search Behavior

* Edit `CourseSearchService.java` to:

  * Change search logic
  * Add new filters
  * Modify sorting options

### Custom Analyzer

* Located at `src/main/resources/elasticsearch/custom-analyzer.json`
* Uses **edge n-grams** for autocomplete
* Case-insensitive matching
* Configurable min/max gram sizes

---

## Troubleshooting

### Elasticsearch Connection Issues

```bash
# Check if Elasticsearch is running
curl http://localhost:9200

# Check Docker containers
docker ps

# View Elasticsearch logs
docker logs elasticsearch
```

### Data Not Loading

* Check `sample-courses.json` exists and has valid JSON
* Verify file permissions
* Check application logs for errors

### Search Not Working

* Verify data was indexed successfully
* Check Elasticsearch cluster health
* Verify custom analyzer was applied

---

## Development

### Running Tests

```bash
./mvnw test
```

### Building JAR

```bash
./mvnw clean package
```

### Docker Build

```bash
docker build -t course-search-app .
```

## Support

For issues and questions:

* Check application logs
* Verify Elasticsearch cluster status
* Ensure all prerequisites are met
* Validate JSON syntax in sample data

---
```
