/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.Building;
import java.util.List;

public interface BuildingDAO{
    boolean create(Building building)throws DatabaseException;;
    Building read(int buildingId)throws DatabaseException;;
    List<Building> readAll()throws DatabaseException;;
    boolean update(Building building)throws DatabaseException;;
    boolean delete(int buildingId)throws DatabaseException;;
    List<Building> searchByKeyword(String keyword)throws DatabaseException;;
}