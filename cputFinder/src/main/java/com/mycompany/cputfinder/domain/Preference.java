/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;
/**
 *
 * @author paci
 */
public class Preference {
    private int preferenceId;
    private int studentId;
    private String accessibleRoute; 
    private String notification;   

    public Preference() {}

    public Preference(int studentId, String accessibleRoute, String notification) {
        this.studentId = studentId;
        this.accessibleRoute = accessibleRoute;
        this.notification = notification;
    }

    public Preference(int preferenceId, int studentId, String accessibleRoute, String notification) {
        this.preferenceId = preferenceId;
        this.studentId = studentId;
        this.accessibleRoute = accessibleRoute;
        this.notification = notification;
    }

    public int getPreferenceId() { return preferenceId; }
    public void setPreferenceId(int preferenceId) { this.preferenceId = preferenceId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public String getAccessibleRoute() { return accessibleRoute; }
    public void setAccessibleRoute(String accessibleRoute) { this.accessibleRoute = accessibleRoute; }
    public String getNotification() { return notification; }
    public void setNotification(String notification) { this.notification = notification; }

    @Override
    public String toString() {
        return "Preference{" +
                "preferenceId=" + preferenceId +
                ", studentId=" + studentId +
                ", accessibleRoute='" + accessibleRoute + '\'' +
                ", notification='" + notification + '\'' +
                '}';
    }
}
