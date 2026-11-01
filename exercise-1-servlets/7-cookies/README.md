# 7. Servlet to add a cookie for the selected value

- `index.html` - drop down with programming languages
- `AddCookieServlet` (`/add-cookie`) - creates `new Cookie("favLanguage", value)`, sets a one day
  life with `setMaxAge` and sends it with `response.addCookie()`
- `ShowCookieServlet` (`/show-cookies`) - reads it back with `request.getCookies()`; the delete
  button sends the same cookie with `maxAge = 0`

Close the browser and open the page again - the cookie is still there until it expires.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/cookie-demo/
