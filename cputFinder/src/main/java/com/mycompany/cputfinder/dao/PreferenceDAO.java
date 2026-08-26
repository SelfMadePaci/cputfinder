/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Preference;
import java.util.List;

public interface PreferenceDAO {
    boolean create(Preference preference);
    Preference read(int preferenceId);
    List<Preference> readAll();
    boolean update(Preference preference);
    boolean delete(int preferenceId);

    Preference findByStudentId(int studentId);
}
