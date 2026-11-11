/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.domain.FoodStore;
import java.util.List;

public interface FoodStoreDAO {
    boolean create(FoodStore store);
    FoodStore read(int storeId);
    List<FoodStore> readAll();
    boolean update(FoodStore store);
    boolean delete(int storeId);
    List<FoodStore> searchByKeyword(String keyword);
}
