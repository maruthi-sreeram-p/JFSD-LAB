package com.jfsd.mvc;

public class MvcDemo {

    public static void main(String[] args) {
        // model: the data
        Student model = new Student("CS101", "Abhiram", "B");

        // view: how it is shown
        StudentView view = new StudentView();

        // controller: connects the two
        StudentController controller = new StudentController(model, view);

        System.out.println("--- initial data ---");
        controller.updateView();

        // the user changes something; only the controller is called
        controller.setStudentName("Abhiram P");
        controller.setStudentGrade("A");

        System.out.println();
        System.out.println("--- after the update ---");
        controller.updateView();
    }
}
