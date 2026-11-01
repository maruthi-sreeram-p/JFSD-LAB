# 1. Introduction to MVC in Java

MVC (Model - View - Controller) splits an application into three parts so each can change
without breaking the others.

| Part | Job | In this program |
|------|-----|-----------------|
| Model | data and business rules | `Student` |
| View | what the user sees | `StudentView` |
| Controller | takes the user's action, updates the model, refreshes the view | `StudentController` |

`MvcDemo.main()` creates all three, shows the student, changes the name and grade through the
controller and shows it again.

## Run

```
mvn -q compile exec:java
```
