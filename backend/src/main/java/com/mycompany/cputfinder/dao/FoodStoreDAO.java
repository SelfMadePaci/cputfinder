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
import com.mycompany.cputfinder.domain.FoodStore;
import java.util.List;

public interface FoodStoreDAO {
    boolean create(FoodStore store) throws DatabaseException;
    FoodStore read(int storeId) throws DatabaseException;
    List<FoodStore> readAll() throws DatabaseException;
    boolean update(FoodStore store) throws DatabaseException;
    boolean delete(int storeId) throws DatabaseException;
    List<FoodStore> searchByKeyword(String keyword) throws DatabaseException;
}
