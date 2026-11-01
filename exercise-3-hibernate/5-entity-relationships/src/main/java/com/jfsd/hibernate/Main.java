package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;

import java.util.List;
import java.util.stream.Collectors;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();

        save(factory);
        read(factory);

        HibernateUtil.shutdown();
    }

    private static void save(SessionFactory factory) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();

            // many-to-many: courses are saved first, they are not cascaded from the student
            Course java = new Course("Java Full Stack");
            Course dbms = new Course("Database Systems");
            Course maths = new Course("Discrete Mathematics");
            session.save(java);
            session.save(dbms);
            session.save(maths);

            Department cse = new Department("Computer Science");

            // one-to-one: the id card is saved together with its student (cascade)
            Student asha = new Student("Asha");
            asha.setIdCard(new IdCard("CS-1001"));
            asha.enroll(java);
            asha.enroll(dbms);

            Student ravi = new Student("Ravi");
            ravi.setIdCard(new IdCard("CS-1002"));
            ravi.enroll(java);
            ravi.enroll(maths);

            // one-to-many: students are saved together with their department (cascade)
            cse.addStudent(asha);
            cse.addStudent(ravi);
            session.save(cse);

            tx.commit();
            System.out.println("Saved 1 department, 2 students, 2 id cards and 3 courses");
            System.out.println();
        }
    }

    private static void read(SessionFactory factory) {
        // a new session, so everything is loaded from the database
        try (Session session = factory.openSession()) {

            System.out.println("--- One-to-many: department -> students");
            Department dept = session.get(Department.class, 1);
            System.out.println(dept.getName() + " has: " +
                    dept.getStudents().stream().map(Student::getName).collect(Collectors.joining(", ")));

            System.out.println();
            System.out.println("--- One-to-one: student <-> id card");
            for (Student s : dept.getStudents()) {
                System.out.println(s.getName() + " has card " + s.getIdCard().getCardNumber());
            }
            IdCard card = session.get(IdCard.class, 2);
            System.out.println("Card " + card.getCardNumber() + " belongs to " + card.getStudent().getName());

            System.out.println();
            System.out.println("--- Many-to-one: student -> department");
            Student ravi = session.createQuery("from Student where name = :n", Student.class)
                    .setParameter("n", "Ravi").uniqueResult();
            System.out.println(ravi.getName() + " studies in " + ravi.getDepartment().getName());

            System.out.println();
            System.out.println("--- Many-to-many: students <-> courses");
            for (Student s : dept.getStudents()) {
                System.out.println(s.getName() + " takes: " +
                        s.getCourses().stream().map(Course::getTitle).sorted().collect(Collectors.joining(", ")));
            }
            List<Course> courses = session.createQuery("from Course order by id", Course.class).list();
            for (Course c : courses) {
                System.out.println(c.getTitle() + " has " + c.getStudents().size() + " student(s)");
            }
        }
    }
}
