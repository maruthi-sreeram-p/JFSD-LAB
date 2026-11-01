# 2. Model layer in MVC

The model layer holds the data and the business rules. It has no HTML and no `System.out` inside
the logic.

- `Book` - the entity
- `BookRepository` / `InMemoryBookRepository` - where the books are stored
- `BookService` - the rules (ISBN required, price above zero, no duplicate ISBN, discount range)
- `ModelDemo` - exercises the model on its own, without any view or controller

## Run

```
mvn -q compile exec:java
```
