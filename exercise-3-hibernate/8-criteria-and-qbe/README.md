# 8. Other forms of querying - Criteria, QBE and more

Instead of writing a query string, these APIs build the query from Java objects, so mistakes are
found at compile time and conditions can be added step by step.

| Part of `Main` | API |
|----------------|-----|
| Criteria 1-5 | `session.createCriteria()` with `Restrictions` (`eq`, `gt`, `like`, `between`, `in`, `or`), `Order`, paging |
| Criteria 6-7 | `Projections` - `rowCount`, `avg`, `max`, `sum`, `groupProperty` |
| QBE | `Example.create(object)` - fill in a sample object and Hibernate matches rows that look like it; `excludeZeroes()`, `enableLike()`, `ignoreCase()` |
| Native SQL | `session.createNativeQuery(sql, Employee.class)` |
| JPA Criteria | `CriteriaBuilder` / `CriteriaQuery` / `Root` |

Note: the `org.hibernate.Criteria` API and `Example` are deprecated since Hibernate 5.2 and removed
in Hibernate 6, which is why this project uses Hibernate 5.6. New code should use the JPA
Criteria API shown at the end.

## Run

```
mvn -q compile exec:java
```
