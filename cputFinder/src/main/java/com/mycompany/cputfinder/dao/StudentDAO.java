/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.dao;
/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.domain.Student;
import java.util.List;

public interface StudentDAO {
    boolean create(Student student);
    Student read(int studentId);
    List<Student> readAll();
    boolean update(Student student);
    boolean delete(int studentId);

    Student findByEmail(String studentEmail);
}
