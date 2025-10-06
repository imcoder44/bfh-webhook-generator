# bfh-webhook-generator

This is a Spring Boot 3 (Java 17) application for the BFHL assignment. It demonstrates how to:

- Automatically generate a webhook and access token via an HTTP API call
- Solve a SQL problem based on a sample database schema
- Submit the SQL solution to a test endpoint using a JWT token
- Print and save the response

## Features

1. **On startup**, the app sends a POST request to generate a webhook and access token.
2. **Reads a SQL query** (for SQL Question 1) from `src/main/resources/final-query-q1.sql`.
3. **Submits the SQL query** to the test endpoint with the access token as a JWT in the Authorization header.
4. **Prints the response** to the console and saves it to `results/response_<timestamp>.json`.
5. **No controllers or endpoints** are exposed; everything runs automatically on startup.

## Project Structure

```
bfh-webhook-generator/
├── pom.xml
├── README.md
├── database.json                # Sample database schema/data (for reference)
├── results/                     # Directory where responses are saved
├── src/
│   └── main/
│       ├── java/
│       │   └── com/example/bfhwebhookgenerator/
│       │       ├── Application.java
│       │       ├── WebhookClient.java
│       │       ├── SqlSubmissionClient.java
│       │       ├── GenerateWebhookRequest.java
│       │       └── GenerateWebhookResponse.java
│       └── resources/
│           └── final-query-q1.sql
```

## How it Works

1. **Generate Webhook & Token:**
   - Sends a POST request to:
     `https://bfhldevapigw.healthrx.co.in/hiring/generateWebhook/JAVA`
   - Body:
     ```json
     {
       "name": "John Doe",
       "regNo": "REG12347",
       "email": "john@example.com"
     }
     ```
   - Receives a `webhook` URL and `accessToken`.

2. **Read SQL Query:**
   - Loads the SQL from `src/main/resources/final-query-q1.sql`.
   - The query finds the highest salary credited not on the 1st day of any month, the employee's name, age, and department name.

3. **Submit SQL Solution:**
   - Sends a POST request to:
     `https://bfhlddevapigw.healthrx.co.in/hiring/testWebhook/JAVA`
   - Headers:
     - `Authorization: <accessToken>`
     - `Content-Type: application/json`
   - Body:
     ```json
     { "finalQuery": "<SQL_QUERY>" }
     ```

4. **Print & Save Response:**
   - Prints the response to the console.
   - Saves the response to `results/response_<timestamp>.json`.

## How to Run

### 1. Build the Project

```
mvn clean package
```

### 2. Run the Application

```
java -jar target/bfh-webhook-generator-0.0.1-SNAPSHOT.jar
```

Or, for development:

```
mvn spring-boot:run
```

### 3. View Output

- The console will show the webhook, access token, and the test response.
- The response is also saved in the `results/` directory.

## Customization

- To change your name, registration number, or email, edit the following lines in `Application.java`:

  ```java
  // Change name, regNo, email here if needed
  GenerateWebhookRequest request = new GenerateWebhookRequest("John Doe", "REG12347", "john@example.com");
  ```

## Dependencies

- Java 17
- Spring Boot 3.x
- spring-boot-starter-webflux

## Notes

- No REST endpoints are exposed; the logic runs automatically on startup.
- The SQL query is stored in a resource file for easy review and modification.
- The app works both from the IDE and as a packaged jar.

## License

MIT License (or as per your project requirements)