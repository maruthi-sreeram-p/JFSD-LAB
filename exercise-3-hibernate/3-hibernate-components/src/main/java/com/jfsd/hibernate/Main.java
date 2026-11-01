package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Walks through the main Hibernate components in the order they are used.
 */
public class Main {

    // Hibernate prints a lot of INFO text at start-up; only show real errors
    private static final Logger HIBERNATE_LOG = Logger.getLogger("org.hibernate");

    public static void main(String[] args) {
        HIBERNATE_LOG.setLevel(Level.SEVERE);

        // 1. Configuration - reads hibernate.cfg.xml (connection, dialect, mappings)
        Configuration configuration = new Configuration().configure();
        System.out.println("1. Configuration   : loaded hibernate.cfg.xml");
        System.out.println("                     url     = " + configuration.getProperty("hibernate.connection.url"));
        System.out.println("                     dialect = " + configuration.getProperty("hibernate.dialect"));

        // 2. SessionFactory - built once from the Configuration, thread safe, creates sessions
        SessionFactory factory = configuration.buildSessionFactory();
        System.out.println("2. SessionFactory  : built (one per application, creates the Sessions)");

        // 3. Session - one unit of work, wraps a JDBC connection, not thread safe
        Session session = factory.openSession();
        System.out.println("3. Session         : opened (one per unit of work)");

        // 4. Transaction - groups the operations into one atomic unit
        Transaction tx = session.beginTransaction();
        System.out.println("4. Transaction     : begun");

        // 5. Persistent object - the three states of an entity
        Employee emp = new Employee("Asha", "IT", 55000);
        System.out.println("5. Object states   : new Employee -> transient, in session? " + session.contains(emp));
        session.save(emp);
        System.out.println("                     after save() -> persistent, in session? " + session.contains(emp));
        session.save(new Employee("Ravi", "HR", 42000));
        session.save(new Employee("Meena", "IT", 61000));

        tx.commit();
        System.out.println("   Transaction     : committed (rows are now in the table)");

        // 6. Query - HQL works on classes and fields, not tables and columns
        List<Employee> list = session.createQuery("from Employee where department = :dept", Employee.class)
                .setParameter("dept", "IT")
                .list();
        System.out.println("6. Query (HQL)     : employees in IT");
        list.forEach(e -> System.out.println("                     " + e));

        // detached - the session is closed but the object still has its id
        session.close();
        System.out.println("7. Session closed  : session open? " + session.isOpen() + " - the Employee object is now detached");

        // a detached object can be attached to a new session again
        Session second = factory.openSession();
        Transaction tx2 = second.beginTransaction();
        emp.setSalary(60000);
        second.update(emp);
        tx2.commit();
        System.out.println("   update(emp)     : re-attached in a new session, salary now " +
                second.get(Employee.class, emp.getId()).getSalary());
        second.close();

        factory.close();
    }
}
