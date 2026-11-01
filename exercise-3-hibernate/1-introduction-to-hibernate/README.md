# 1. Introduction to Hibernate

Hibernate is a Java ORM framework. You work with ordinary Java objects and Hibernate writes the
JDBC and SQL for you, so there is no `ResultSet` code to maintain.

What this program shows, in order:

1. `hibernate.cfg.xml` holds the database settings and the list of mapped classes
2. `Student` is an `@Entity` - its fields become the columns of the `students` table
3. `session.save(...)` inserts, `session.get(...)` selects by primary key
4. changing a loaded object and committing runs an `UPDATE` by itself (dirty checking)

`hibernate.show_sql` is on, so every SQL statement Hibernate generates is printed.
The database is H2 in memory, so there is nothing to install (the MySQL settings are in the
comment of `hibernate.cfg.xml`).

## Run

```
mvn -q compile exec:java
```
