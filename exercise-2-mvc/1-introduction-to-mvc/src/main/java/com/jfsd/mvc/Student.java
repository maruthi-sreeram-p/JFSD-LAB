package com.jfsd.mvc;

/**
 * MODEL - holds the data. It knows nothing about how the data is displayed.
 */
public class Student {

    private String rollNo;
    private String name;
    private String grade;

    public Student(String rollNo, String name, String grade) {
        this.rollNo = rollNo;
        this.name = name;
        this.grade = grade;
    }

    public String getRollNo() {
        return rollNo;
    }

    public void setRollNo(String rollNo) {
        this.rollNo = rollNo;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getGrade() {
        return grade;
    }

    public void setGrade(String grade) {
        this.grade = grade;
    }
}
