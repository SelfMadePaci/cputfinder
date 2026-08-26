/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.domain;
/**
 *
 * @author paci
 */
import java.sql.Date;
import java.sql.Time;

public class Schedule {
    private int scheduleId;
    private int studentId;
    private int courseId;
    private int roomId;
    private Date classDate;
    private Time startingTime;
    private Time endingTime;

    private String moduleCode;
    private String moduleName;
    private String roomNumber;

    public Schedule() {}

    public Schedule(int studentId, int courseId, int roomId, Date classDate, Time startingTime, Time endingTime) {
        this.studentId = studentId;
        this.courseId = courseId;
        this.roomId = roomId;
        this.classDate = classDate;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public Schedule(int scheduleId, int studentId, int courseId, int roomId,
                     Date classDate, Time startingTime, Time endingTime) {
        this.scheduleId = scheduleId;
        this.studentId = studentId;
        this.courseId = courseId;
        this.roomId = roomId;
        this.classDate = classDate;
        this.startingTime = startingTime;
        this.endingTime = endingTime;
    }

    public Schedule(int scheduleId, int studentId, int courseId, int roomId,
                     Date classDate, Time startingTime, Time endingTime,
                     String moduleCode, String moduleName, String roomNumber) {
        this(scheduleId, studentId, courseId, roomId, classDate, startingTime, endingTime);
        this.moduleCode = moduleCode;
        this.moduleName = moduleName;
        this.roomNumber = roomNumber;
    }

    public int getScheduleId() { return scheduleId; }
    public void setScheduleId(int scheduleId) { this.scheduleId = scheduleId; }
    public int getStudentId() { return studentId; }
    public void setStudentId(int studentId) { this.studentId = studentId; }
    public int getCourseId() { return courseId; }
    public void setCourseId(int courseId) { this.courseId = courseId; }
    public int getRoomId() { return roomId; }
    public void setRoomId(int roomId) { this.roomId = roomId; }
    public Date getClassDate() { return classDate; }
    public void setClassDate(Date classDate) { this.classDate = classDate; }
    public Time getStartingTime() { return startingTime; }
    public void setStartingTime(Time startingTime) { this.startingTime = startingTime; }
    public Time getEndingTime() { return endingTime; }
    public void setEndingTime(Time endingTime) { this.endingTime = endingTime; }
    public String getModuleCode() { return moduleCode; }
    public void setModuleCode(String moduleCode) { this.moduleCode = moduleCode; }
    public String getModuleName() { return moduleName; }
    public void setModuleName(String moduleName) { this.moduleName = moduleName; }
    public String getRoomNumber() { return roomNumber; }
    public void setRoomNumber(String roomNumber) { this.roomNumber = roomNumber; }

    @Override
    public String toString() {
        return "Schedule{" +
                "scheduleId=" + scheduleId +
                ", studentId=" + studentId +
                ", courseId=" + courseId +
                ", roomId=" + roomId +
                ", classDate=" + classDate +
                ", startingTime=" + startingTime +
                ", endingTime=" + endingTime +
                (moduleCode != null ? ", moduleCode='" + moduleCode + '\'' : "") +
                (roomNumber != null ? ", roomNumber='" + roomNumber + '\'' : "") +
                '}';
    }
}
