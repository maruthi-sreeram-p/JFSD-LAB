# 4. Controller layer in MVC

`TaskController` (`/tasks`) is the only entry point of the application. For each request it

1. reads the `action` parameter,
2. calls the model (`TaskRepository`),
3. picks the view: **forward** to a JSP to show a page, or **redirect** after a change
   (redirect-after-POST, so refreshing the browser does not submit the form twice).

The controller also validates input - saving an empty title goes back to the form with an error.

The model is `Task` / `TaskRepository`, the views are `list.jsp` and `form.jsp` (in `WEB-INF/views`).

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/controller-layer/
