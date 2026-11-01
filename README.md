# JFSD Lab

One folder for every question of the lab sheet. Each folder is a separate project with its own `README.md` (what it shows and how to run it).

## Exercise 1 - Servlets (`exercise-1-servlets`)

| Folder | Question |
|--------|----------|
| `1-introduction-to-servlets` | Introduction to servlets |
| `2-current-date-time` | Servlet to print the current date and time |
| `3-html-servlet-communication` | Link HTML and a servlet |
| `4-auto-refresh` | Auto refresh a page |
| `5-session-tracking` | Session tracking |
| `6-database-crud` | Insert / delete / update a record in a database |
| `7-cookies` | Add a cookie for the selected value |

## Exercise 2 - MVC (`exercise-2-mvc`)

| Folder | Question |
|--------|----------|
| `1-introduction-to-mvc` | Introduction to MVC in Java |
| `2-model-layer` | Model layer |
| `3-view-layer` | View layer |
| `4-controller-layer` | Controller layer |
| `5-mvc-deployment` | MVC deployment |
| `6-mvc-mapping-rules` | Rules for MVC mapping on the server side |
| `7-web-server-deployment` | Using a web server for MVC deployment |

## Exercise 3 - Hibernate (`exercise-3-hibernate`)

| Folder | Question |
|--------|----------|
| `1-introduction-to-hibernate` | Introduction to Hibernate |
| `2-what-is-orm` | What is ORM |
| `3-hibernate-components` | Demonstrate the components of Hibernate |
| `4-xml-and-annotation-mapping` | Persisting objects, mapping with XML and annotations |
| `5-entity-relationships` | Relationships between entities |
| `6-transactions` | Transactions |
| `7-hql` | HQL |
| `8-criteria-and-qbe` | Criteria, QBE and other ways of querying |
| `9-debugging-tools` | Debugging tools (NetBeans, Eclipse, IntelliJ IDEA, VS Code) |

## Exercise 4 - Spring Boot case studies (`exercise-4-case-studies`)

| Folder | Case study |
|--------|-----------|
| `case-study-a-ocms` | Online Course Management System (Spring Boot + React) |
| `case-study-b-shms` | Smart Health Monitoring System (Spring Boot + React) |

## What you need

- JDK 17 or newer and Maven
- Node 20.19+ or 22.12+ (only for the two React front ends)
- No database or web server installation: the programs use H2, and the web projects can start Tomcat themselves

## How to run

Servlet and MVC web projects (Tomcat 10.1 is downloaded the first time):

```
mvn package cargo:run
```

Console projects (MVC intro, model layer, Hibernate, debugging):

```
mvn -q compile exec:java
```

Case studies: see the `README.md` inside each case study (backend with `mvn spring-boot:run`, front end with `npm install` and `npm run dev`).

The web projects and the Hibernate projects use Jakarta EE 10 (Tomcat 10+) and Hibernate 5.6 (the Criteria and QBE questions need the old Criteria API that Hibernate 6 removed).
