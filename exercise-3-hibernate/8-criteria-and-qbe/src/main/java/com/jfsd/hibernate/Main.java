package com.jfsd.hibernate;

import org.hibernate.Criteria;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.Transaction;
import org.hibernate.criterion.Example;
import org.hibernate.criterion.MatchMode;
import org.hibernate.criterion.Order;
import org.hibernate.criterion.ProjectionList;
import org.hibernate.criterion.Projections;
import org.hibernate.criterion.Restrictions;

import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.criteria.CriteriaQuery;
import javax.persistence.criteria.Root;
import java.util.List;

/**
 * Other ways of querying: Criteria (queries built with objects), Query By
 * Example, native SQL and the JPA Criteria API.
 */
public class Main {

    public static void main(String[] args) {
        SessionFactory factory = HibernateUtil.getSessionFactory();
        insertSampleData(factory);

        try (Session session = factory.openSession()) {

            title("CRITERIA - 1. all employees");
            print(session.createCriteria(Employee.class).list());

            title("CRITERIA - 2. Restrictions: department = IT and salary > 58000 (conditions are ANDed)");
            Criteria c = session.createCriteria(Employee.class);
            c.add(Restrictions.eq("department", "IT"));
            c.add(Restrictions.gt("salary", 58000.0));
            print(c.list());

            title("CRITERIA - 3. OR: city = Pune or salary < 40000");
            print(session.createCriteria(Employee.class)
                    .add(Restrictions.or(
                            Restrictions.eq("city", "Pune"),
                            Restrictions.lt("salary", 40000.0)))
                    .list());

            title("CRITERIA - 4. like, between, in");
            print(session.createCriteria(Employee.class)
                    .add(Restrictions.like("name", "a", MatchMode.ANYWHERE).ignoreCase())
                    .add(Restrictions.between("salary", 40000.0, 60000.0))
                    .add(Restrictions.in("city", "Chennai", "Hyderabad", "Pune"))
                    .list());

            title("CRITERIA - 5. order by salary desc, first page of 3 rows");
            print(session.createCriteria(Employee.class)
                    .addOrder(Order.desc("salary"))
                    .setFirstResult(0)
                    .setMaxResults(3)
                    .list());

            title("CRITERIA - 6. Projections: count, average and highest salary");
            Object[] stats = (Object[]) session.createCriteria(Employee.class)
                    .setProjection(Projections.projectionList()
                            .add(Projections.rowCount())
                            .add(Projections.avg("salary"))
                            .add(Projections.max("salary")))
                    .uniqueResult();
            System.out.println("count=" + stats[0] + "  avg=" + stats[1] + "  max=" + stats[2]);

            title("CRITERIA - 7. Projections: group by department");
            ProjectionList byDepartment = Projections.projectionList()
                    .add(Projections.groupProperty("department"))
                    .add(Projections.rowCount())
                    .add(Projections.sum("salary"));
            List<?> groups = session.createCriteria(Employee.class)
                    .setProjection(byDepartment)
                    .addOrder(Order.asc("department"))
                    .list();
            for (Object g : groups) {
                Object[] row = (Object[]) g;
                System.out.println(row[0] + ": " + row[1] + " employees, total salary " + row[2]);
            }

            // QBE: build an example object and let Hibernate make the conditions from it
            title("QBE - 1. example: department = IT, city = Hyderabad");
            Employee probe = new Employee();
            probe.setDepartment("IT");
            probe.setCity("Hyderabad");
            Example example = Example.create(probe).excludeZeroes();     // salary 0 means "not set"
            print(session.createCriteria(Employee.class).add(example).list());

            title("QBE - 2. like on all String fields, ignoring case: name contains 'an'");
            Employee probe2 = new Employee();
            probe2.setName("AN");
            Example example2 = Example.create(probe2)
                    .excludeZeroes()
                    .enableLike(MatchMode.ANYWHERE)
                    .ignoreCase();
            print(session.createCriteria(Employee.class).add(example2).list());

            title("NATIVE SQL");
            print(session.createNativeQuery("select * from employees where city = :city order by name", Employee.class)
                    .setParameter("city", "Chennai")
                    .list());

            title("JPA CRITERIA API (the replacement for the old Criteria in newer Hibernate)");
            CriteriaBuilder cb = session.getCriteriaBuilder();
            CriteriaQuery<Employee> query = cb.createQuery(Employee.class);
            Root<Employee> root = query.from(Employee.class);
            query.select(root)
                    .where(cb.and(
                            cb.equal(root.get("department"), "Finance"),
                            cb.ge(root.<Double>get("salary"), 50000.0)))
                    .orderBy(cb.asc(root.get("name")));
            print(session.createQuery(query).list());
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

    private static void print(List<?> list) {
        list.forEach(System.out::println);
    }
}
