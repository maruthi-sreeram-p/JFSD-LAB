# 6. Rules for MVC mapping on the server side

In an MVC web application the container must decide which servlet (controller) handles each URL.
The `url-pattern` rules from the Servlet specification:

| Pattern kind | Example | Matches |
|--------------|---------|---------|
| Exact | `/home` | only `/home` |
| Path | `/admin/*` | `/admin`, `/admin/users`, `/admin/users/42` |
| Extension | `*.do` | any path ending in `.do` |
| Default | `/` | everything not matched by another pattern |

How the container chooses, in this order:

1. exact match
2. the longest path pattern (`/admin/reports/*` beats `/admin/*`)
3. extension pattern (`*.do`)
4. the default servlet (`/`)

Other rules: a pattern must start with `/` (or `*.`), only one servlet can own a given pattern,
and `/*` captures everything including static files, so use `/` for a front controller if you
still want JSP and static files to work.

`MappingServlet` is registered five times in `web.xml` with different patterns and prints
`requestURI`, `servletPath` and `pathInfo` so you can see how the URL is split.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/mapping-rules/ and click the links.
