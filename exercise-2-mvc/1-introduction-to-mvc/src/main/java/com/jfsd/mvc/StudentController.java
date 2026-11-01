package com.jfsd.mvc;

/**
 * CONTROLLER - sits between the model and the view. The user's actions come
 * here; it updates the model and tells the view to redraw.
 */
public class StudentController {

    private final Student model;
    private final StudentView view;

    public StudentController(Student model, StudentView view) {
        this.model = model;
        this.view = view;
    }

    public void setStudentName(String name) {
        model.setName(name);
    }

    public void setStudentGrade(String grade) {
        model.setGrade(grade);
    }

    public void updateView() {
        view.printStudentDetails(model.getRollNo(), model.getName(), model.getGrade());
    }
}
