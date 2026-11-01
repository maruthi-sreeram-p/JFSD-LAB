# 6. Servlet to insert, delete and update records in a database

- `DbUtil` - JDBC connection (H2 database stored in your home folder, no server to install;
  the comment in the file shows the MySQL settings)
- `Student` - the record
- `StudentDao` - the SQL: `INSERT`, `UPDATE`, `DELETE`, `SELECT` with `PreparedStatement`
- `StudentServlet` - `/students`; reads the form, calls the DAO, redirects back to the list

Use **Edit** on a row to fill the form and update it, **Delete** to remove it.

## Run

```
mvn package cargo:run
```

Open http://localhost:8080/student-crud/
