package com.example.androidproject.model;

public class Course {
    private String courseID;
    private String courseTime;
    private String professorName;

    public Course(String courseID, String courseTime, String professorName) {
        this.courseID = courseID;
        this.courseTime = courseTime;
        this.professorName = professorName;
    }
    // Getters
    public String getCourseID() {
        return courseID;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public String getProfessorName() {
        return professorName;
    }

    // Setters
    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
}
