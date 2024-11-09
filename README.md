# Marketplace

This is a marketplace that allows people to self-publish and sell their clothes online.

## Application Setup

This application uses the following tools and technologies:

- **Language**: Java (Version 21)
- **Project Management Tool**: Maven
- **Framework**: Spring Boot
- **Database**: PostgreSQL

### Steps to Run the Application

1. Ensure that you have a PostgreSQL server running on your local machine.
2. Create a database called `marketplace` in PostgreSQL.
3. Update your `application.properties` file (located under the `resources` folder) with your PostgreSQL username and password.
4. Change the following line in the `application.properties` file to create the database tables:

   ```properties
   spring.jpa.hibernate.ddl-auto=update
   ```
   ```properties
   spring.jpa.hibernate.ddl-auto=create