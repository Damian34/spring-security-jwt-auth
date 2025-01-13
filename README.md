# spring-security-jwt-auth
A Spring Boot application focused on implementing JWT authentication and refresh tokens, built as a personal project to explore and understand security mechanisms in web applications

## Application Assumptions

- **Simple security-focused application**  
  Minimal set of endpoints, primarily for login and authorization.

- **JWT with asymmetric or symmetric key and refresh token**  
  Authentication using JWT with a chosen encryption algorithm (asymmetric like RS256 or symmetric like HS256) and refresh tokens for session renewal.

- **PostgreSQL database**  
  User data stored in a `user` table in PostgreSQL.

- **PostgreSQL database in Docker**  
  Database running in a Docker container, configured via a `docker-compose.yml` file.

## Getting Started

To run this application, you'll need Docker and Docker Compose to manage the PostgreSQL database.
