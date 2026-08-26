/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;
/**
 *
 * @author paci
 */
public class SavedPlace {
    private int savedId;
    private int studentId;
    private int buildingId;
    private String savedName;

    private String buildingName;

    public SavedPlace() {}

    public SavedPlace(int studentId, int buildingId, String savedName) {
        this.studentId = studentId;
        this.buildingId = buildingId;
        this.savedName = savedName;
    }

    public SavedPlace(int savedId, int studentId, int buildingId, String savedName) {
        this.savedId = savedId;
        this.studentId = studentId;
        this.buildingId = buildingId;
        this.savedName = savedName;
    }

    public SavedPlace(int savedId, int studentId, int buildingId, String savedName, String buildingName) {
        this(savedId, studentId, buildingId, savedName);
        this.buildingName = buildingName;
    }

    public int getSavedId() { return savedId; }
    public void setSavedId(int savedId) { this.savedId = savedId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }
    public String getSavedName() { return savedName; }
    public void setSavedName(String savedName) { this.savedName = savedName; }
    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    @Override
    public String toString() {
        return "SavedPlace{" +
                "savedId=" + savedId +
                ", studentId=" + studentId +
                ", buildingId=" + buildingId +
                ", savedName='" + savedName + '\'' +
                (buildingName != null ? ", buildingName='" + buildingName + '\'' : "") +
                '}';
    }
}
