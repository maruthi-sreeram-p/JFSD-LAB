package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.math.BigDecimal;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(new Account("Asha", new BigDecimal("1000.00")));
            session.save(new Account("Ravi", new BigDecimal("500.00")));
            tx.commit();
        }

        BankService bank = new BankService(factory);
        bank.printBalances("Start:");

        // 1. everything works -> commit
        System.out.println("\n1. Transfer 300 from Asha to Ravi");
        bank.transfer(1, 2, new BigDecimal("300.00"), false);
        bank.printBalances("   committed, balances now:");

        // 2. business rule fails -> rollback
        System.out.println("\n2. Transfer 5000 from Asha to Ravi (not enough money)");
        try {
            bank.transfer(1, 2, new BigDecimal("5000.00"), false);
        } catch (IllegalStateException e) {
            System.out.println("   failed: " + e.getMessage());
        }
        bank.printBalances("   rolled back, balances unchanged:");

        // 3. failure after the debit was already sent to the database -> rollback undoes it
        System.out.println("\n3. Transfer 200 from Asha to Ravi, crash after the debit");
        try {
            bank.transfer(1, 2, new BigDecimal("200.00"), true);
        } catch (RuntimeException e) {
            System.out.println("   failed: " + e.getMessage());
        }
        bank.printBalances("   rolled back, the debit was undone:");

        HibernateUtil.shutdown();
    }
}
