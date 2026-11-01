package com.jfsd.mvc;

/**
 * VIEW - decides how the data looks. Here it just prints to the console,
 * but it could be a web page or a desktop window without touching the model.
 */
public class StudentView {

    public void printStudentDetails(String rollNo, String name, String grade) {
        System.out.println("Student:");
        System.out.println("  Roll No : " + rollNo);
        System.out.println("  Name    : " + name);
        System.out.println("  Grade   : " + grade);
    }
}
