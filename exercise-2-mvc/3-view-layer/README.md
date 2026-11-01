# 3. View layer in MVC

The view only displays data it is given. It does not fetch or change anything.

- `ProductServlet` puts a list of `Product` objects in the request and forwards to the JSP
- `WEB-INF/views/products.jsp` - the view: JSTL `c:forEach`, `c:choose`, `c:out`,
  `fmt:formatNumber` and Expression Language (`${...}`)
- `header.jspf` / `footer.jspf` - page parts shared with `<%@ include %>`
- views are inside `WEB-INF` so the browser cannot open them directly

One product name contains HTML on purpose - `c:out` shows it as text instead of running it.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/view-layer/
