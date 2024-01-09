package com.example.androidproject.model;

public class Course {
    private String courseName;
    private String courseTime;
    private String professorName;

    public Course(String courseName, String courseTime, String professorName) {
        this.courseName = courseName;
        this.courseTime = courseTime;
        this.professorName = professorName;
    }

    // Getters
    public String getCourseName() {
        return courseName;
    }

    public String getCourseTime() {
        return courseTime;
    }

    public String getProfessorName() {
        return professorName;
    }

    // Setters
    public void setCourseName(String courseName) {
        this.courseName = courseName;
    }

    public void setCourseTime(String courseTime) {
        this.courseTime = courseTime;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }
}
