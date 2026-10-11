F24 Task - File System



Backend stack: Java, Spring Boot
Frontend stack: React Vite (as suggested instead of a Create React App)
DB: Postgresql


Requirements:
Java 21
Maven/mvnw
Node.js
npm
Docker (for Postgresql container)

** I will push containerized version of the solution on separate branch **

----------------------------------------------------------------------------------------------

HOW TO RUN THE APP:

1. Open the console and run the postgresql container with command:
docker run --name f24-db-container \
  -e POSTGRES_DB=f24-task \
  -e POSTGRES_USER=postgres \
  -e POSTGRES_PASSWORD=1234 \
  -p 5433:5432 \
  -d postgres:17

OR navigate to a f24-task directory and use docker compose:
"docker compose up --build"

2. Navigate to backend directory (where pom.xml file is) and run:

  ./mvnw spring-boot:run

OR if using maven:

  mvn spring-boot:run


3. On new terminal navigate to frontend directory and run:

  npm install

and:

  npm run dev


Spring boot will run on port 8080 and react will run on port 5173 so these ports should be free.

EDIT: Normally we would want the sensitive data (like credentials and passwords) not to include in commits but instead use the env variables. 
