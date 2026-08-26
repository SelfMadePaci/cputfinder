/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;

/**
 *
 * @author paci
 */

public class Room {
    private int roomId;
    private String roomNumber;
    private int floorNumber;
    private int buildingId;
    
    private String buildingName;

    public Room() {}

    public Room(int roomId, String roomNumber, int floorNumber, int buildingId) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.buildingId = buildingId;
    }

    public Room(int roomId, String roomNumber, int floorNumber, int buildingId, String buildingName) {
        this.roomId = roomId;
        this.roomNumber = roomNumber;
        this.floorNumber = floorNumber;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
    }

    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }

    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    public int getFloorNumber() { return floorNumber; }
    public void setFloorNumber(int floorNumber) { this.floorNumber = floorNumber; }

    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    @Override
    public String toString() {
        return "Room{" +
                "roomId=" + roomId +
                ", roomNumber='" + roomNumber + '\'' +
                ", floorNumber=" + floorNumber +
                ", buildingId=" + buildingId +
                (buildingName != null ? ", buildingName='" + buildingName + '\'' : "") +
                '}';
    }
}
