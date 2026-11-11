/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Route;
import java.util.List;

public interface RouteDAO {
    boolean create(Route route);
    Route read(int routeId);
    List<Route> readAll();
    boolean update(Route route);
    boolean delete(int routeId);
    List<Route> findRouteBetween(int startBuildingId, int endBuildingId);
}
