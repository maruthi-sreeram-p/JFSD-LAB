# 3. HTML and Servlet communication

`index.html` has two forms. The `action` attribute holds the servlet URL (`register`) and `method`
decides which servlet method runs:

- `method="post"` -> `doPost()` reads the fields with `getParameter()` / `getParameterValues()`
- `method="get"` -> `doGet()`, the data is visible in the URL

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/html-servlet/
