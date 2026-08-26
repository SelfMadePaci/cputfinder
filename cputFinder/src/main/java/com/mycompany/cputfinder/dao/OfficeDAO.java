/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;

/**
 *
 * @author paci
 */

import com.mycompany.cputfinder.domain.Office;
import java.util.List;

public interface OfficeDAO {
    boolean create(Office office);
    Office read(int officeId);
    List<Office> readAll();
    boolean update(Office office);
    boolean delete(int officeId);
    List<Office> searchByKeyword(String keyword);
}
