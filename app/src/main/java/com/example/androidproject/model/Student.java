package com.example.androidproject.model;

public class Student {
    private String name;
    private String studentID;
    private String Email;
    private String Pass;

    public Student(String name,String studentID, String email, String pass) {
        this.name=name;
        this.studentID = studentID;
        Email = email;
        Pass = pass;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }

    public void setEmail(String email) {
        Email = email;
    }

    public void setPass(String pass) {
        Pass = pass;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getStudentID() {
        return studentID;
    }

    public String getEmail() {
        return Email;
    }

    public String getPass() {
        return Pass;
    }
}
