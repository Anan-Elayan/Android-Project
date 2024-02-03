package com.example.androidproject.model;

import java.io.Serializable;

public class Course implements Serializable {
    private String courseID;
    private String startTime;
    private String professorName;
    private String date;
    private String endTime;

    public Course(String courseID, String startTime, String professorName, String date, String endTime) {
        this.courseID = courseID;
        this.startTime = startTime;
        this.professorName = professorName;
        this.date = date;
        this.endTime = endTime;
    }

    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getStartTime() {
        return startTime;
    }

    public void setStartTime(String startTime) {
        this.startTime = startTime;
    }

    public String getProfessorName() {
        return professorName;
    }

    public void setProfessorName(String professorName) {
        this.professorName = professorName;
    }

    public String getDate() {
        return date;
    }

    public void setDate(String date) {
        this.date = date;
    }

    public String getEndTime() {
        return endTime;
    }

    public void setEndTime(String endTime) {
        this.endTime = endTime;
    }

    @Override
    public String toString() {
        return "Course{" +
                "courseID='" + courseID + '\'' +
                ", startTime='" + startTime + '\'' +
                ", professorName='" + professorName + '\'' +
                ", date='" + date + '\'' +
                ", endTime='" + endTime + '\'' +
                '}';
    }
}
