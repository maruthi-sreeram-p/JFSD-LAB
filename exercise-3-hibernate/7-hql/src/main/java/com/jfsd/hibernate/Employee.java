package com.jfsd.hibernate;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.NamedQuery;
import javax.persistence.Table;

// a query written once and given a name, used in Main with getNamedQuery()
@NamedQuery(name = "Employee.byCity",
        query = "from Employee e where e.city = :city order by e.name")
@Entity
@Table(name = "employees")
public class Employee {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int id;

    private String name;

    private String department;

    private String city;

    private double salary;

    public Employee() {
    }

    public Employee(String name, String department, String city, double salary) {
        this.name = name;
        this.department = department;
        this.city = city;
        this.salary = salary;
    }

    public int getId() {
        return id;
    }

    public String getName() {
        return name;
    }

    public String getDepartment() {
        return department;
    }

    public String getCity() {
        return city;
    }

    public double getSalary() {
        return salary;
    }

    @Override
    public String toString() {
        return String.format("%-3d %-8s %-8s %-10s %9.2f", id, name, department, city, salary);
    }
}
