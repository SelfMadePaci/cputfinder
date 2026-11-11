/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.dao.exception.*;
import com.mycompany.cputfinder.domain.Route;
import java.util.List;

public interface RouteDAO {
    boolean create(Route route) throws DatabaseException;
    Route read(int routeId) throws DatabaseException;
    List<Route> readAll() throws DatabaseException;
    boolean update(Route route) throws DatabaseException;
    boolean delete(int routeId) throws DatabaseException;
    List<Route> findRouteBetween(int startBuildingId, int endBuildingId) throws DatabaseException;
}
