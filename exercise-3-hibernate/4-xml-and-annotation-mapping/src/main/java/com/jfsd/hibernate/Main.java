package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        System.out.println("===== Mapping with an XML file (Student.hbm.xml) =====");
        xmlMappedStudent(factory);

        System.out.println();
        System.out.println("===== Mapping with annotations (Employee) =====");
        annotatedEmployee(factory);

        HibernateUtil.shutdown();
    }

    // persist, read, update and delete an object that is mapped with XML
    private static void xmlMappedStudent(SessionFactory factory) {
        int id;

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = new Student("Asha", "asha@example.com", "B.Tech CSE");
            session.save(s);
            tx.commit();
            id = s.getId();
            System.out.println("Saved   : " + s);
        }

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Student s = session.get(Student.class, id);
            System.out.println("Loaded  : " + s);
            s.setCourse("B.Tech AI");
            tx.commit();
            System.out.println("Updated : " + s);
        }

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(session.get(Student.class, id));
            tx.commit();
            System.out.println("Deleted : student " + id + ", found again? " + (session.get(Student.class, id) != null));
        }
    }

    // the same operations for an object that is mapped with annotations
    private static void annotatedEmployee(SessionFactory factory) {
        int id;

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee e = new Employee("Ravi", "IT", 52000);
            e.setNote("this field has @Transient, it is not saved");
            session.save(e);
            tx.commit();
            id = e.getId();
            System.out.println("Saved   : " + e);
        }

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            Employee e = session.get(Employee.class, id);
            System.out.println("Loaded  : " + e + ", note = " + e.getNote());
            e.setSalary(58000);
            tx.commit();
            System.out.println("Updated : " + e);
        }

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.delete(session.get(Employee.class, id));
            tx.commit();
            System.out.println("Deleted : employee " + id + ", found again? " + (session.get(Employee.class, id) != null));
        }
    }
}
