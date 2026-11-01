# 3. Components of Hibernate

The main Hibernate components, in the order a program uses them:

| Component | Role | In `Main` |
|-----------|------|-----------|
| `Configuration` | reads `hibernate.cfg.xml` (connection, dialect, mappings) | `new Configuration().configure()` |
| `SessionFactory` | built once, thread safe, creates sessions | `buildSessionFactory()` |
| `Session` | one unit of work, wraps a JDBC connection | `openSession()` |
| `Transaction` | groups operations so they commit or roll back together | `beginTransaction()` / `commit()` |
| Persistent object | an entity that is transient, persistent or detached | `Employee` |
| `Query` | HQL queries on classes and fields | `createQuery(...)` |

`Main` prints a line for each component as it is created so you can follow the flow when you run it.
It also shows the three states of an object: **transient** (new, not in a session), **persistent**
(after `save()`, tracked by the session) and **detached** (the session is closed, the object still has
its id and can be attached again with `update()`).

## Run

```
mvn -q compile exec:java
```
