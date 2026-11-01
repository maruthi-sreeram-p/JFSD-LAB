# 2. Servlet to print current date and time

`DateTimeServlet` (`/datetime`) reads the server clock with `LocalDateTime.now()`,
formats it and writes it as HTML.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/date-time-servlet/
