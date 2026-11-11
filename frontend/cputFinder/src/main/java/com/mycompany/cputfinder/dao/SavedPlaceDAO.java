/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.SavedPlace;
import java.util.List;

public interface SavedPlaceDAO {
    boolean create(SavedPlace savedPlace);
    SavedPlace read(int savedId);
    List<SavedPlace> readAll();
    boolean update(SavedPlace savedPlace);
    boolean delete(int savedId);

    List<SavedPlace> findByStudentId(int studentId);
}
