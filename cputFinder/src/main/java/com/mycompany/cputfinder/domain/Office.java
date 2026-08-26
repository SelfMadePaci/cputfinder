/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;

/**
 *
 * @author paci
 */

public class Office {
    private int officeId;
    private String officeNumber;
    private int floorNumber;
    private int buildingId;
    private String buildingName;

    public Office() {}

    public Office(int officeId, String officeNumber, int floorNumber, int buildingId) {
        this.officeId = officeId;
        this.officeNumber = officeNumber;
        this.floorNumber = floorNumber;
        this.buildingId = buildingId;
    }

    public int getOfficeId() { return officeId; }
    public void setOfficeId(int officeId) { this.officeId = officeId; }

    public String getOfficeNumber() { return officeNumber; }
    public void setOfficeNumber(String officeNumber) { this.officeNumber = officeNumber; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }
}
