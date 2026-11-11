/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.domain.Lecturer;
import java.util.List;

public interface LecturerDAO {
    boolean create(Lecturer lecturer);
    Lecturer read(int lecturerId);
    List<Lecturer> readAll();
    boolean update(Lecturer lecturer);
    boolean delete(int lecturerId);
    List<Lecturer> searchByKeyword(String keyword);
}
