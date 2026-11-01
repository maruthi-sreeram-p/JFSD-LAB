# 5. Session tracking

HTTP is stateless, so the server uses a session to recognise the same browser between requests.
`SessionServlet` (`/session`) uses `HttpSession`:

- `request.getSession(true)` creates the session (the browser gets a `JSESSIONID` cookie)
- `setAttribute` / `getAttribute` keep the visit count and the user name on the server
- `session.invalidate()` ends the session (the "End session" button)

Reload the page and the visit count goes up; open it in another browser and you get a different session id.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/session-tracking/
