package com.jfsd.hibernate;

import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.query.Query;

import java.util.List;

public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        insertSampleData(factory);

        try (Session session = factory.openSession()) {

            title("1. Select all objects");
            print(session.createQuery("from Employee", Employee.class).list());

            title("2. WHERE with a named parameter (salary > 50000)");
            print(session.createQuery("from Employee e where e.salary > :min", Employee.class)
                    .setParameter("min", 50000.0)
                    .list());

            title("3. ORDER BY");
            print(session.createQuery("from Employee e order by e.salary desc", Employee.class).list());

            title("4. LIKE (names starting with A)");
            print(session.createQuery("from Employee e where e.name like :p", Employee.class)
                    .setParameter("p", "A%")
                    .list());

            title("5. BETWEEN and IN");
            print(session.createQuery(
                            "from Employee e where e.salary between 40000 and 55000 and e.city in ('Pune', 'Chennai')",
                            Employee.class).list());

            title("6. Selecting only some fields (each row is an Object[])");
            List<Object[]> rows = session.createQuery("select e.name, e.salary from Employee e", Object[].class).list();
            for (Object[] row : rows) {
                System.out.println(row[0] + " earns " + row[1]);
            }

            title("7. Aggregate functions");
            Object[] stats = session.createQuery(
                    "select count(e), sum(e.salary), avg(e.salary), min(e.salary), max(e.salary) from Employee e",
                    Object[].class).uniqueResult();
            System.out.println("count=" + stats[0] + "  sum=" + stats[1] + "  avg=" + stats[2]
                    + "  min=" + stats[3] + "  max=" + stats[4]);

            title("8. GROUP BY ... HAVING (departments with more than one employee)");
            List<Object[]> groups = session.createQuery(
                    "select e.department, count(e), avg(e.salary) from Employee e "
                            + "group by e.department having count(e) > 1 order by e.department",
                    Object[].class).list();
            for (Object[] g : groups) {
                System.out.println(g[0] + ": " + g[1] + " employees, average salary " + g[2]);
            }

            title("9. Pagination (page 2, 3 rows per page)");
            Query<Employee> paged = session.createQuery("from Employee e order by e.id", Employee.class);
            paged.setFirstResult(3);
            paged.setMaxResults(3);
            print(paged.list());

            title("10. Named query defined with @NamedQuery on the entity");
            print(session.createNamedQuery("Employee.byCity", Employee.class)
                    .setParameter("city", "Hyderabad")
                    .list());

            title("11. Single result");
            Employee top = session.createQuery("from Employee e order by e.salary desc", Employee.class)
                    .setMaxResults(1)
                    .uniqueResult();
            System.out.println("Highest paid: " + top);
        }

        // UPDATE and DELETE in HQL change many rows with one statement
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();

            title("12. UPDATE - 10% raise for the IT department");
            int updated = session.createQuery("update Employee e set e.salary = e.salary * 1.10 where e.department = :d")
                    .setParameter("d", "IT")
                    .executeUpdate();
            System.out.println(updated + " rows updated");

            title("13. DELETE - salary below 36000");
            int deleted = session.createQuery("delete from Employee e where e.salary < :min")
                    .setParameter("min", 36000.0)
                    .executeUpdate();
            System.out.println(deleted + " rows deleted");

            tx.commit();

            title("Table after the update and delete");
            print(session.createQuery("from Employee", Employee.class).list());
        }

        HibernateUtil.shutdown();
    }

    private static void insertSampleData(SessionFactory factory) {
        try (Session session = factory.openSession()) {
            Transaction tx = session.beginTransaction();
            session.save(new Employee("Asha", "IT", "Hyderabad", 55000));
            session.save(new Employee("Ravi", "HR", "Chennai", 42000));
            session.save(new Employee("Meena", "IT", "Hyderabad", 61000));
            session.save(new Employee("Kiran", "Finance", "Pune", 48000));
            session.save(new Employee("Divya", "IT", "Bengaluru", 72000));
            session.save(new Employee("Suresh", "HR", "Hyderabad", 39000));
            session.save(new Employee("Anita", "Finance", "Chennai", 53000));
            session.save(new Employee("Vikram", "Sales", "Pune", 35000));
            tx.commit();
        }
    }

    private static void title(String text) {
        System.out.println();
        System.out.println("--- " + text);
    }

    private static void print(List<Employee> list) {
        list.forEach(System.out::println);
    }
}
