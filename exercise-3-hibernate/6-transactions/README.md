# 6. Transactions in Hibernate

A transaction makes a group of database changes **atomic**: all of them are saved (`commit`) or
none of them are (`rollback`).

The program moves money between two accounts in `BankService.transfer()`:

```java
tx = session.beginTransaction();
// debit one account, credit the other
tx.commit();                       // success
...
catch (RuntimeException e) { tx.rollback(); throw e; }   // failure
```

`Main` runs three cases:

1. normal transfer - committed
2. not enough money - the rule throws, the transaction is rolled back, balances are unchanged
3. a crash after the debit has already been sent to the database (`session.flush()`) - the
   rollback undoes the debit, so no money disappears

Balances are read with a **new session** each time, so you see what is really in the database.

## Run

```
mvn -q compile exec:java
```
