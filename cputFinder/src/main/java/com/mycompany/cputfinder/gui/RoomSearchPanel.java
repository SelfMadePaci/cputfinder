/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.mycompany.cputfinder.gui;

/**
 *
 * @author paci
 */
import com.mycompany.cputfinder.dao.RoomDAO;
import com.mycompany.cputfinder.dao.RoomDAOImpl;
import com.mycompany.cputfinder.domain.Room;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.KeyAdapter;
import java.awt.event.KeyEvent;
import java.util.List;

public class RoomSearchPanel extends JPanel {

    private JTextField searchField;
    private JTable roomTable;
    private DefaultTableModel tableModel;
    private RoomDAO roomDAO;

    public RoomSearchPanel() {
        this.roomDAO = new RoomDAOImpl();
        initComponents();
        loadRoomData(""); 
    }

    private void initComponents() {
        setLayout(new BorderLayout(10, 10));
        setBorder(BorderFactory.createEmptyBorder(15, 15, 15, 15));

        JPanel topPanel = new JPanel(new BorderLayout(10, 0));
        JLabel searchLabel = new JLabel("Search Room / Building: ");
        searchField = new JTextField();
        JButton searchBtn = new JButton("Search");

        topPanel.add(searchLabel, BorderLayout.WEST);
        topPanel.add(searchField, BorderLayout.CENTER);
        topPanel.add(searchBtn, BorderLayout.EAST);

        String[] columns = {"Room ID", "Room Number", "Floor", "Building ID", "Building Name"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        
        roomTable = new JTable(tableModel);
        JScrollPane scrollPane = new JScrollPane(roomTable);

        add(topPanel, BorderLayout.NORTH);
        add(scrollPane, BorderLayout.CENTER);

        searchField.addKeyListener(new KeyAdapter() {
            @Override
            public void keyReleased(KeyEvent e) {
                loadRoomData(searchField.getText());
            }
        });

        searchBtn.addActionListener(e -> loadRoomData(searchField.getText()));
    }

    private void loadRoomData(String keyword) {
        tableModel.setRowCount(0); 
        List<Room> rooms = keyword.trim().isEmpty() ? roomDAO.readAll() : roomDAO.searchByKeyword(keyword);

        for (Room room : rooms) {
            Object[] row = {
                room.getRoomId(),
                room.getRoomNumber(),
                room.getFloorNumber(),
                room.getBuildingId(),
                room.getBuildingName() != null ? room.getBuildingName() : "N/A"
            };
            tableModel.addRow(row);
        }
    }
}