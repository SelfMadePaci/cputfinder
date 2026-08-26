/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;

/**
 *
 * @author paci
 */

public class Route {
    private int routeId;
    private int startBuildingId;
    private int endBuildingId;
    private int estimatedTime; 
    private String description;

    private String startBuildingName;
    private String endBuildingName;

    public Route() {}

    public Route(int routeId, int startBuildingId, int endBuildingId, int estimatedTime, String description) {
        this.routeId = routeId;
        this.startBuildingId = startBuildingId;
        this.endBuildingId = endBuildingId;
        this.estimatedTime = estimatedTime;
        this.description = description;
    }

    public int getRouteId() { return routeId; }
    public void setRouteId(int routeId) { this.routeId = routeId; }

    public int getStartBuildingId() { return startBuildingId; }
    public void setStartBuildingId(int startBuildingId) { this.startBuildingId = startBuildingId; }

    public int getEndBuildingId() { return endBuildingId; }
    public void setEndBuildingId(int endBuildingId) { this.endBuildingId = endBuildingId; }

    public int getEstimatedTime() { return estimatedTime; }
    public void setEstimatedTime(int estimatedTime) { this.estimatedTime = estimatedTime; }

    public String getDescription() { return description; }
    public void setDescription(String description) { this.description = description; }

    public String getStartBuildingName() { return startBuildingName; }
    public void setStartBuildingName(String startBuildingName) { this.startBuildingName = startBuildingName; }

    public String getEndBuildingName() { return endBuildingName; }
    public void setEndBuildingName(String endBuildingName) { this.endBuildingName = endBuildingName; }
}
