# 7. Querying with HQL (Hibernate Query Language)

HQL looks like SQL but uses **class and field names** (`Employee`, `e.salary`) instead of tables
and columns, and Hibernate translates it to SQL for the database in use.

`Main` runs 13 queries on eight employees:

| # | Feature |
|---|---------|
| 1-3 | `from Employee`, `where` with a named parameter, `order by` |
| 4-5 | `like`, `between`, `in` |
| 6 | selecting some fields only (`Object[]` rows) |
| 7-8 | `count`, `sum`, `avg`, `min`, `max`, `group by ... having` |
| 9 | pagination with `setFirstResult` / `setMaxResults` |
| 10 | named query (`@NamedQuery` on `Employee`) |
| 11 | `uniqueResult()` |
| 12-13 | bulk `update` and `delete` with `executeUpdate()` inside a transaction |

Always pass values with `setParameter(...)` and never join them into the query string - that
protects against SQL injection.

## Run

```
mvn -q compile exec:java
```
