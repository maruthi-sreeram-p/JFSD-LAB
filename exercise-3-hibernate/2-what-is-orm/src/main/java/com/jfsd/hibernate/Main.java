package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.cfg.Configuration;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * What is ORM? The same job done twice: save an Employee and read it back.
 * First by hand with JDBC, then with Hibernate.
 */
public class Main {

    // Hibernate prints a lot of INFO text at start-up; only show real errors
    private static final Logger HIBERNATE_LOG = Logger.getLogger("org.hibernate");

    public static void main(String[] args) throws SQLException {
        HIBERNATE_LOG.setLevel(Level.SEVERE);

        System.out.println("===== Without ORM: JDBC =====");
        withJdbc();

        System.out.println();
        System.out.println("===== With ORM: Hibernate =====");
        withHibernate();
    }

    private static void withJdbc() throws SQLException {
        try (Connection con = DriverManager.getConnection("jdbc:h2:mem:jdbcdb", "sa", "")) {

            try (Statement st = con.createStatement()) {
                st.execute("create table employees (id int auto_increment primary key, "
                        + "name varchar(100), department varchar(50), salary double)");
            }

            Employee asha = new Employee("Asha", "IT", 55000);

            // object -> row: every field is copied into the SQL by hand
            String insert = "insert into employees (name, department, salary) values (?, ?, ?)";
            try (PreparedStatement ps = con.prepareStatement(insert)) {
                ps.setString(1, asha.getName());
                ps.setString(2, asha.getDepartment());
                ps.setDouble(3, asha.getSalary());
                ps.executeUpdate();
            }
            System.out.println("1. we wrote the INSERT and copied 3 fields into it");

            // row -> object: every column is copied back by hand
            String select = "select id, name, department, salary from employees where id = ?";
            try (PreparedStatement ps = con.prepareStatement(select)) {
                ps.setInt(1, 1);
                try (ResultSet rs = ps.executeQuery()) {
                    rs.next();
                    Employee loaded = new Employee();
                    loaded.setId(rs.getInt("id"));
                    loaded.setName(rs.getString("name"));
                    loaded.setDepartment(rs.getString("department"));
                    loaded.setSalary(rs.getDouble("salary"));
                    System.out.println("2. we wrote the SELECT and copied 4 columns back: " + loaded);
                }
            }
        }
    }

    private static void withHibernate() {
        SessionFactory factory = new Configuration().configure().buildSessionFactory();

        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(new Employee("Asha", "IT", 55000));
            tx.commit();
            System.out.println("1. we only saved the object, Hibernate wrote the INSERT (printed above)");
        }

        // a new session, so the object is really read from the database
        try (Session session = factory.openSession()) {
            Employee loaded = session.get(Employee.class, 1);
            System.out.println("2. we only asked for the object, Hibernate wrote the SELECT (printed above): " + loaded);
        }

        factory.close();
    }
}
