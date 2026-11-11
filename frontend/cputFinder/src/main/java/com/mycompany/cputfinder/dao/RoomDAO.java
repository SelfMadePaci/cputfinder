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
import java.util.List;

public interface RoomDAO {
    boolean create(Room room);
    Room read(int roomId);
    List<Room> readAll();
    boolean update(Room room);
    boolean delete(int roomId);
    
    // Search across room_number and joined building_name
    List<Room> searchByKeyword(String keyword);
}
