# 1. Introduction to Servlets

A servlet is a Java class that runs inside a web container (Tomcat) and handles HTTP requests.
The container manages its life cycle:

1. **Load and create** - the container loads the class and creates one object.
2. **`init()`** - called once, after creation.
3. **`service()` -> `doGet()` / `doPost()`** - called for every request (each in its own thread).
4. **`destroy()`** - called once, before the servlet is removed.

`HelloServlet` is mapped to `/hello` with `@WebServlet` and logs each life cycle call
(look in the Tomcat console).

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/intro-servlets/ (this downloads Tomcat 10.1 on the first run).
You can also copy `target/intro-servlets.war` into the `webapps` folder of your own Tomcat 10.
