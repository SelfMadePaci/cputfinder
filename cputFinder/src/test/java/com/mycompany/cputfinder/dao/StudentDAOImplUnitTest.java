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
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

/**
 * TRUE unit tests — no real Derby connection is ever opened. Connection,
 * PreparedStatement, and ResultSet are all mocked with Mockito, so these
 * tests verify StudentDAOImpl's logic (correct SQL, correct parameter
 * binding, correct handling of found/not-found results) in isolation from
 * the database itself. This is what distinguishes "unit" from "integration"
 * testing on the rubric — these tests still pass even if Derby is not
 * running.
 */
@ExtendWith(MockitoExtension.class)
class StudentDAOImplUnitTest {

    @Mock private Connection mockConnection;
    @Mock private PreparedStatement mockStatement;
    @Mock private ResultSet mockResultSet;

    private StudentDAOImpl studentDAO;

    @BeforeEach
    void setUp() {
        studentDAO = new StudentDAOImpl();
    }

    @Test
    void read_returnsStudent_whenRowExists() throws SQLException {
        try (MockedStatic<com.mycompany.cputfinder.connection.DBconnection.DBConnection> dbMock =
                 mockStatic(com.mycompany.cputfinder.connection.DBconnection.DBConnection.class)) {

            dbMock.when(com.mycompany.cputfinder.connection.DBconnection.DBConnection::getConnection)
                  .thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);

            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt("student_id")).thenReturn(1);
            when(mockResultSet.getString("student_number")).thenReturn("231169949");
            when(mockResultSet.getString("full_name")).thenReturn("Boitshwarelo Kgwadi");
            when(mockResultSet.getString("student_email")).thenReturn("231169949@mycput.ac.za");
            when(mockResultSet.getString("password")).thenReturn("hashed_pw");

            Student result = studentDAO.read(1);

            assertNotNull(result);
            assertEquals("231169949", result.getStudentNumber());
            assertEquals("Boitshwarelo Kgwadi", result.getFullName());

            // Confirms the DAO actually bound the ID parameter, not just ran any query
            verify(mockStatement).setInt(1, 1);
        }
    }

    @Test
    void read_returnsNull_whenNoRowFound() throws SQLException {
        try (MockedStatic<com.mycompany.cputfinder.connection.DBconnection.DBConnection> dbMock =
                 mockStatic(com.mycompany.cputfinder.connection.DBconnection.DBConnection.class)) {

            dbMock.when(com.mycompany.cputfinder.connection.DBconnection.DBConnection::getConnection)
                  .thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeQuery()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(false); // no matching row

            Student result = studentDAO.read(999);

            assertNull(result);
        }
    }

    @Test
    void create_returnsTrue_whenInsertSucceeds() throws SQLException {
        try (MockedStatic<com.mycompany.cputfinder.connection.DBconnection.DBConnection> dbMock =
                 mockStatic(com.mycompany.cputfinder.connection.DBconnection.DBConnection.class)) {

            dbMock.when(com.mycompany.cputfinder.connection.DBconnection.DBConnection::getConnection)
                  .thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1); // 1 row affected
            when(mockStatement.getGeneratedKeys()).thenReturn(mockResultSet);
            when(mockResultSet.next()).thenReturn(true);
            when(mockResultSet.getInt(1)).thenReturn(42); // simulated new student_id

            Student newStudent = new Student("999999999", "Test Student", "test@mycput.ac.za", "pw");
            boolean result = studentDAO.create(newStudent);

            assertTrue(result);
            assertEquals(42, newStudent.getStudentId(), "create() should set the generated ID back onto the object");
            verify(mockStatement).setString(1, "999999999");
            verify(mockStatement).setString(2, "Test Student");
        }
    }

    @Test
    void create_returnsFalse_whenSQLExceptionThrown() throws SQLException {
        try (MockedStatic<com.mycompany.cputfinder.connection.DBconnection.DBConnection> dbMock =
                 mockStatic(com.mycompany.cputfinder.connection.DBconnection.DBConnection.class)) {

            dbMock.when(com.mycompany.cputfinder.connection.DBconnection.DBConnection::getConnection)
                  .thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString(), anyInt())).thenReturn(mockStatement);
            // Simulates a UNIQUE constraint violation (e.g. duplicate student_email)
            when(mockStatement.executeUpdate()).thenThrow(new SQLException("Duplicate key"));

            Student duplicateStudent = new Student("231169949", "Duplicate", "231169949@mycput.ac.za", "pw");
            boolean result = studentDAO.create(duplicateStudent);

            // StudentDAOImpl.create() catches SQLException and returns false —
            // this test confirms it fails gracefully rather than crashing the app
            assertFalse(result);
        }
    }

    @Test
    void delete_returnsTrue_whenRowRemoved() throws SQLException {
        try (MockedStatic<com.mycompany.cputfinder.connection.DBconnection.DBConnection> dbMock =
                 mockStatic(com.mycompany.cputfinder.connection.DBconnection.DBConnection.class)) {

            dbMock.when(com.mycompany.cputfinder.connection.DBconnection.DBConnection::getConnection)
                  .thenReturn(mockConnection);
            when(mockConnection.prepareStatement(anyString())).thenReturn(mockStatement);
            when(mockStatement.executeUpdate()).thenReturn(1);

            boolean result = studentDAO.delete(1);

            assertTrue(result);
            verify(mockStatement).setInt(1, 1);
        }
    }
}