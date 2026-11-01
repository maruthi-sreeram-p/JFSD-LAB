# 4. Persisting objects and mapping with XML and annotations

Hibernate needs to know which class goes to which table. There are two ways to tell it, and this
program uses both side by side:

| | XML mapping | Annotation mapping |
|---|---|---|
| Class | `Student` (plain class, no annotations) | `Employee` (`@Entity`, `@Table`, `@Id`, `@Column`) |
| Mapping | `src/main/resources/Student.hbm.xml` | on the class itself |
| Listed in `hibernate.cfg.xml` as | `<mapping resource="Student.hbm.xml"/>` | `<mapping class="...Employee"/>` |

`Main` persists (`save`), reads (`get`), updates (change + commit) and deletes (`delete`) one object
of each, so the output shows that both styles give the same result. Look at the two tables in
the SQL: `students_xml` and `employees_annotated`.

XML keeps the class clean and lets you change the mapping without recompiling; annotations keep
the mapping next to the code and are shorter.

## Run

```
mvn -q compile exec:java
```
