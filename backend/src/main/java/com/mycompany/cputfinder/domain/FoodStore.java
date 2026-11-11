/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;

/**
 *
 * @author paci
 */
public class FoodStore {
    private int storeId;
    private String storeName;
    private String operatingHours;
    private String foodType;
    private int buildingId;
    private String buildingName; 

    public FoodStore() {}

    public FoodStore(int storeId, String storeName, String operatingHours, String foodType, int buildingId) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.operatingHours = operatingHours;
        this.foodType = foodType;
        this.buildingId = buildingId;
    }

    public FoodStore(int storeId, String storeName, String operatingHours, String foodType, int buildingId, String buildingName) {
        this.storeId = storeId;
        this.storeName = storeName;
        this.operatingHours = operatingHours;
        this.foodType = foodType;
        this.buildingId = buildingId;
        this.buildingName = buildingName;
    }

    public int getStoreId() { return storeId; }
    public void setStoreId(int storeId) { this.storeId = storeId; }

    public String getStoreName() { return storeName; }
    public void setStoreName(String storeName) { this.storeName = storeName; }

    public String getOperatingHours() { return operatingHours; }
    public void setOperatingHours(String operatingHours) { this.operatingHours = operatingHours; }

    public String getFoodType() { return foodType; }
    public void setFoodType(String foodType) { this.foodType = foodType; }

    public int getBuildingId() { return buildingId; }
    public void setBuildingId(int buildingId) { this.buildingId = buildingId; }

    public String getBuildingName() { return buildingName; }
    public void setBuildingName(String buildingName) { this.buildingName = buildingName; }

    @Override
    public String toString() {
        return "FoodStore{" +
                "storeId=" + storeId +
                ", storeName='" + storeName + '\'' +
                ", operatingHours='" + operatingHours + '\'' +
                ", foodType='" + foodType + '\'' +
                ", buildingId=" + buildingId +
                (buildingName != null ? ", buildingName='" + buildingName + '\'' : "") +
                '}';
    }
}
