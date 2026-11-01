# 4. Servlet to auto refresh a page

`AutoRefreshServlet` (`/refresh`) sets the HTTP header

```java
response.setHeader("Refresh", "5");
```

so the browser asks for the page again every 5 seconds. The page shows the server time and a
counter so you can see it reloading. `?seconds=2` changes the interval.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/auto-refresh/
