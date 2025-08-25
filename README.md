# URL Shortener

A simple URL Shortener built with **Spring Boot**, **MySQL**, and **Redis**.  
It allows you to shorten long URLs into short codes and retrieve the original URLs efficiently using Redis caching.

---

## 🚀 Features
- Generate short codes for long URLs
- Fast lookup using **Redis cache**
- Persistent storage in **MySQL**
- REST API endpoints for shortening and retrieving URLs
- Configurable TTL (default: 1 day) for cache entries

---

## 🛠️ Tech Stack
- **Java 21**
- **Spring Boot**
- **MySQL**
- **Redis**
- **Maven**

---

## 📂 Project Structure



---

## ⚙️ Setup Instructions

1. Clone the Repository
```bash
git clone https://github.com/<your-username>/url-shortener.git
cd url-shortener

2. Configure Database
Update src/main/resources/application.properties with your MySQL credentials:
spring.datasource.url=jdbc:mysql://localhost:3306/url_shortener
spring.datasource.username=<your-username>
spring.datasource.password=<your-password>
spring.jpa.hibernate.ddl-auto=update

3. Run Redis

If not already running:
redis-server
(Check with redis-cli ping → should return PONG)

4. Build and Run
mvn clean install
mvn spring-boot:run
📌 API Endpoints
Shorten a URL
POST /v1/shorten
{
  "longUrl": "https://www.example.com/very/long/url"
}
✅ Response:
{
  "shortCode": "ca159fa1"
}
Redirect to Original URL
GET http://localhost:8080/v1/{shortCode}
Example: http://localhost:8080/v1/ca159fa1 → redirects to original long URL.


🧪 Running Tests
mvn test
