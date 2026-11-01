# 2. What is ORM?

**ORM (Object Relational Mapping)** connects the object world of Java with the tables of a
relational database. A Java program works with objects, a database stores rows in tables, and
the two do not fit together naturally (this is called the object-relational mismatch). An ORM
tool such as Hibernate does the translation:

| Java | Database |
|------|----------|
| class `Employee` | table `employees` |
| object | row |
| field | column |
| reference to another object | foreign key |

With an ORM you save and load objects, and the SQL is generated for you. That means less code,
no `ResultSet` copying, and the same program can run on another database by changing the dialect.

`Main` does the same job twice, so you can compare:

1. **Without ORM** - JDBC: we write the `INSERT` and the `SELECT`, and copy every field and
   column between the object and the statement ourselves.
2. **With ORM** - Hibernate: `session.save(employee)` and `session.get(Employee.class, 1)`.
   The SQL that Hibernate generated is printed in the output.

## Run

```
mvn -q compile exec:java
```
