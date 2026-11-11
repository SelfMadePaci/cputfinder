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
import com.mycompany.cputfinder.domain.Student;
import java.util.List;

public interface StudentDAO {
    boolean create(Student student) throws DatabaseException;
    Student read(int studentId) throws DatabaseException;
    List<Student> readAll() throws DatabaseException;
    boolean update(Student student) throws DatabaseException;
    boolean delete(int studentId) throws DatabaseException;

    Student findByEmail(String studentEmail) throws DatabaseException;
}
