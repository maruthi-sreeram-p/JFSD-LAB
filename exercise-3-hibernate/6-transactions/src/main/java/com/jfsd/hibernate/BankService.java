package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class BankService {

    private final SessionFactory factory;

    public BankService(SessionFactory factory) {
        this.factory = factory;
    }

    /**
     * Moves money between two accounts. The debit and the credit are one
     * transaction: both happen or neither does.
     *
     * @param failAfterDebit simulates a crash between the two updates
     */
    public void transfer(int fromId, int toId, BigDecimal amount, boolean failAfterDebit) {
        // no try-with-resources here: the session must still be open when we roll back
        Session session = factory.openSession();
        Transaction tx = null;
        try {
            tx = session.beginTransaction();

            Account from = session.get(Account.class, fromId);
            Account to = session.get(Account.class, toId);

            if (from.getBalance().compareTo(amount) < 0) {
                throw new IllegalStateException("Insufficient funds in " + from.getHolder() + "'s account");
            }

            from.setBalance(from.getBalance().subtract(amount));

            if (failAfterDebit) {
                // push the debit to the database first, so the rollback has something to undo
                session.flush();
                throw new RuntimeException("Simulated failure after the debit");
            }

            to.setBalance(to.getBalance().add(amount));

            tx.commit();
        } catch (RuntimeException e) {
            if (tx != null && tx.isActive()) {
                tx.rollback();
            }
            throw e;
        } finally {
            session.close();
        }
    }

    public void printBalances(String title) {
        try (Session session = factory.openSession()) {
            System.out.println(title);
            for (Account a : session.createQuery("from Account order by id", Account.class).list()) {
                System.out.println("   " + a.getHolder() + ": " + a.getBalance());
            }
        }
    }
}
