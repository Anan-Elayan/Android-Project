package com.example.androidproject.model;

import java.io.Serializable;

public class Task implements Serializable {


    private String courseID;
    private String taskTitle;
    private String taskDescription;
    private String taskDate;
    private String taskTime;
    private String studentID;

    private  String taskID;


    public Task(String studentID, String courseID, String taskTitle, String taskDescription, String taskDate, String taskTime,String taskID) {
        this.studentID = studentID;
        this.courseID = courseID;
        this.taskTitle = taskTitle;
        this.taskDescription = taskDescription;
        this.taskDate = taskDate;
        this.taskTime = taskTime;
        this.taskID = taskID;
    }

    public Task() {
    }

    public String getTaskID() {
        return taskID;
    }

    public void setTaskID(String taskID) {
        this.taskID = taskID;
    }

    public String getStudentID() {
        return studentID;
    }

    public void setStudentID(String studentID) {
        this.studentID = studentID;
    }



    public String getCourseID() {
        return courseID;
    }

    public void setCourseID(String courseID) {
        this.courseID = courseID;
    }

    public String getTaskTitle() {
        return taskTitle;
    }

    public void setTaskTitle(String taskTitle) {
        this.taskTitle = taskTitle;
    }

    public String getTaskDescription() {
        return taskDescription;
    }

    public void setTaskDescription(String taskDescription) {
        this.taskDescription = taskDescription;
    }

    public String getTaskDate() {
        return taskDate;
    }

    public void setTaskDate(String taskDate) {
        this.taskDate = taskDate;
    }

    public String getTaskTime() {
        return taskTime;
    }

    public void setTaskTime(String taskTime) {
        this.taskTime = taskTime;
    }

    @Override
    public String toString() {
        return "Task{" + ", courseID='" + courseID + '\'' +
                ", taskTitle='" + taskTitle + '\'' +
                ", taskDescription='" + taskDescription + '\'' +
                ", taskDate='" + taskDate + '\'' +
                ", taskTime='" + taskTime + '\'' +
                ", studentID='" + studentID + '\'' +
                '}';
    }
}
