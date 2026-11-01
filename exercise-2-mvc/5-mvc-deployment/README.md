# 5. MVC deployment in Java

A complete MVC web application (Student Registration) that is configured through the deployment
descriptor `WEB-INF/web.xml` and packaged as a WAR file.

| Layer | Files |
|-------|-------|
| Model | `model/Student`, `model/StudentStore` |
| View | `WEB-INF/views/students.jsp`, `error404.jsp`, `error500.jsp` |
| Controller | `controller/StudentController` (mapped to `/students` in `web.xml`) |

`web.xml` declares the servlet and its mapping, a `context-param` used by the JSP, the session
timeout, the welcome file and the error pages.

## Structure of the WAR

```
mvc-deployment.war
|-- index.jsp, style.css
`-- WEB-INF
    |-- web.xml
    |-- views/*.jsp
    |-- classes/com/jfsd/mvc/...   (compiled model + controller)
    `-- lib/                       (JSTL jars)
```

## Build and run

```
mvn package          # creates target/mvc-deployment.war
mvn cargo:run        # starts Tomcat 10.1 with the WAR
```

Open http://localhost:8080/mvc-deployment/
