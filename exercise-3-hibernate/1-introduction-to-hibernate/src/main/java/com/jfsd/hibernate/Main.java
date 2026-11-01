package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.logging.Level;
import java.util.logging.Logger;

public class Main {

    // Hibernate prints a lot of INFO text at start-up; only show real errors
    private static final Logger HIBERNATE_LOG = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        HIBERNATE_LOG.setLevel(Level.SEVERE);

        // 1. read hibernate.cfg.xml and build the SessionFactory
        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        // 2. save objects - no SQL written by us
        Session session = factory.openSession();
        Transaction tx = session.beginTransaction();

        session.save(new Student("Asha", "asha@example.com", "B.Tech CSE"));
        session.save(new Student("Ravi", "ravi@example.com", "BCA"));

        tx.commit();
        session.close();

        // 3. read one back by its primary key
        session = factory.openSession();
        Student student = session.get(Student.class, 1);
        System.out.println("Loaded from the database: " + student);

        // 4. change it - Hibernate notices the change and runs an UPDATE on commit
        tx = session.beginTransaction();
        student.setCourse("B.Tech AI");
        tx.commit();
        session.close();

        session = factory.openSession();
        System.out.println("After update:            " + session.get(Student.class, 1));
        session.close();

        factory.close();
    }
}
