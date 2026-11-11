/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Room;
import com.mycompany.cputfinder.dao.exception.*;
import java.util.List;

public interface RoomDAO {
    boolean create(Room room) throws DatabaseException;
    Room read(int roomId) throws DatabaseException;
    List<Room> readAll() throws DatabaseException;
    boolean update(Room room) throws DatabaseException;
    boolean delete(int roomId) throws DatabaseException;
    
    // Search across room_number and joined building_name
    List<Room> searchByKeyword(String keyword) throws DatabaseException;
}
