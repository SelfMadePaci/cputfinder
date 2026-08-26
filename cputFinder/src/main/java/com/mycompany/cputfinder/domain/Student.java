/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;
/**
 *
 * @author paci
 */
public class Student {
    private int studentId;
    private String studentNumber;
    private String fullName;
    private String studentEmail;
    private String password;

    public Student() {}

    public Student(String studentNumber, String fullName, String studentEmail, String password) {
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.studentEmail = studentEmail;
        this.password = password;
    }

    public Student(int studentId, String studentNumber, String fullName, String studentEmail, String password) {
        this.studentId = studentId;
        this.studentNumber = studentNumber;
        this.fullName = fullName;
        this.studentEmail = studentEmail;
        this.password = password;
    }

    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getStudentNumber() { return studentNumber; }
    public void setStudentNumber(String studentNumber) { this.studentNumber = studentNumber; }
    public String getFullName() { return fullName; }
    public void setFullName(String fullName) { this.fullName = fullName; }
    public String getStudentEmail() { return studentEmail; }
    public void setStudentEmail(String studentEmail) { this.studentEmail = studentEmail; }
    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    @Override
    public String toString() {
        return "Student{" +
                "studentId=" + studentId +
                ", studentNumber='" + studentNumber + '\'' +
                ", fullName='" + fullName + '\'' +
                ", studentEmail='" + studentEmail + '\'' +
                '}';
    }
}
