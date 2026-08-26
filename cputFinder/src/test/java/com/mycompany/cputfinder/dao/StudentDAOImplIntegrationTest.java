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
import org.junit.jupiter.api.*;

import static org.junit.jupiter.api.Assertions.*;

/**
 * INTEGRATION test — requires Derby Network Server to be running on
 * localhost:1527 with the CampusNavigatorDB database available (same setup
 * NetBeans uses). This exercises the full stack: real SQL sent to a real
 * database, not mocks. Unlike StudentDAOImplUnitTest, this test class will
 * fail with a connection error if Derby isn't started first — that's
 * expected and is what makes it an integration test rather than a unit test.
 *
 * Test order matters here (create -> read -> update -> delete), so
 * @TestMethodOrder is used to run them as a lifecycle rather than
 * independently, since delete() depends on the ID created earlier.
 */
@TestMethodOrder(MethodOrderer.OrderAnnotation.class)
class StudentDAOImplIntegrationTest {

    private static StudentDAO studentDAO;
    private static int createdStudentId;

    // Unique test data so repeated test runs don't collide with existing
    // UNIQUE constraints on student_number / student_email
    private static final String TEST_NUMBER = "999888777";
    private static final String TEST_EMAIL = "999888777@mycput.ac.za";

    @BeforeAll
    static void setUp() {
        studentDAO = new StudentDAOImpl();
        // Defensive cleanup: if a previous run crashed or failed before its
        // own delete() step ran, a leftover row with this email/number would
        // cause create() to fail on UNIQUE constraint and cascade every
        // subsequent test into a false failure. Clear it before we start.
        deleteTestStudentIfExists();
    }

    @AfterAll
    static void tearDown() {
        // Runs even if an assertion fails mid-test, so a crash here doesn't
        // leave data behind for the next run either.
        deleteTestStudentIfExists();
    }

    private static void deleteTestStudentIfExists() {
        Student existing = studentDAO.findByEmail(TEST_EMAIL);
        if (existing != null) {
            studentDAO.delete(existing.getStudentId());
        }
    }

    @Test
    @Order(1)
    void create_insertsNewStudent_intoRealDatabase() {
        Student newStudent = new Student(TEST_NUMBER, "Integration Test Student", TEST_EMAIL, "temp_pw");

        boolean result = studentDAO.create(newStudent);

        assertTrue(result, "Expected create() to succeed against the live Derby database");
        assertTrue(newStudent.getStudentId() > 0, "Expected Derby to generate a student_id");

        createdStudentId = newStudent.getStudentId();
    }

    @Test
    @Order(2)
    void read_retrievesTheStudentJustCreated() {
        Student result = studentDAO.read(createdStudentId);

        assertNotNull(result, "Expected to read back the student inserted in step 1");
        assertEquals(TEST_NUMBER, result.getStudentNumber());
        assertEquals(TEST_EMAIL, result.getStudentEmail());
    }

    @Test
    @Order(3)
    void findByEmail_locatesStudent_byEmailLookup() {
        Student result = studentDAO.findByEmail(TEST_EMAIL);

        assertNotNull(result, "findByEmail should locate the test student — this backs the login flow");
        assertEquals(createdStudentId, result.getStudentId());
    }

    @Test
    @Order(4)
    void update_changesFullName_andPersistsToDatabase() {
        Student student = studentDAO.read(createdStudentId);
        student.setFullName("Updated Integration Test Name");

        boolean result = studentDAO.update(student);
        assertTrue(result, "Expected update() to succeed");

        Student reread = studentDAO.read(createdStudentId);
        assertEquals("Updated Integration Test Name", reread.getFullName(),
            "Update should be visible on a fresh read — confirms it was actually persisted, not just returned true");
    }

    @Test
    @Order(5)
    void delete_removesStudent_fromRealDatabase() {
        boolean result = studentDAO.delete(createdStudentId);
        assertTrue(result, "Expected delete() to succeed");

        Student reread = studentDAO.read(createdStudentId);
        assertNull(reread, "Student should no longer exist after delete — confirms it wasn't a soft/partial delete");
    }
}