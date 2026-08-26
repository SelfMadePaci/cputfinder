/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Building;
import java.util.List;

public interface BuildingDAO {
    boolean create(Building building);
    Building read(int buildingId);
    List<Building> readAll();
    boolean update(Building building);
    boolean delete(int buildingId);
    List<Building> searchByKeyword(String keyword);
}