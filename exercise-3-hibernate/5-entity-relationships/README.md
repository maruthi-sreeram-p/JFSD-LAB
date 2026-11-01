# 5. Relationships between entities (associations)

One small university model that uses every association type:

| Relationship | Entities | Annotation |
|--------------|----------|------------|
| One-to-one | `Student` - `IdCard` | `@OneToOne` (owner `Student`, inverse `mappedBy` in `IdCard`) |
| One-to-many | `Department` -> many `Student` | `@OneToMany(mappedBy = "department")` |
| Many-to-one | many `Student` -> `Department` | `@ManyToOne` + `@JoinColumn` (holds the foreign key) |
| Many-to-many | `Student` - `Course` | `@ManyToMany` + `@JoinTable(student_course)` |

Points to notice in the code:

- the side **without** `mappedBy` is the owner and holds the foreign key / join table
- `cascade = ALL` saves the students and id cards when the department is saved
- helper methods (`addStudent`, `setIdCard`, `enroll`) update both sides of a bidirectional link
- `read()` uses a new session, so every association is really loaded from the database
  (lazy collections are fetched when first used) - check the SQL in the output

## Run

```
mvn -q compile exec:java
```
