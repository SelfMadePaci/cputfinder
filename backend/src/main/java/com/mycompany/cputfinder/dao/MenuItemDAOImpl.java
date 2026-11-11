package com.mycompany.cputfinder.dao;

import com.mycompany.cputfinder.connection.DBConnection;
import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.MenuItem;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.List;

public class MenuItemDAOImpl implements MenuItemDAO {

    private static final String SELECT_ALL =
            "SELECT menu_item_id, category, item_name, price, display_order " +
            "FROM Cafeteria_Menu_Item " +
            "ORDER BY CASE category WHEN 'Breakfast' THEN 1 WHEN 'Lunch' THEN 2 WHEN 'Dinner' THEN 3 ELSE 4 END, " +
            "display_order, menu_item_id";

    @Override
    public boolean create(MenuItem menuItem) throws DatabaseException {
        String sql = "INSERT INTO Cafeteria_Menu_Item (category, item_name, price, display_order) VALUES (?, ?, ?, ?)";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql, Statement.RETURN_GENERATED_KEYS)) {
            bind(statement, menuItem);
            if (statement.executeUpdate() == 0) {
                return false;
            }
            try (ResultSet keys = statement.getGeneratedKeys()) {
                if (!keys.next()) {
                    return false;
                }
                menuItem.setMenuItemId(keys.getInt(1));
            }
            return true;
        } catch (SQLException exception) {
            throw DatabaseException.wrap(exception);
        }
    }

    @Override
    public List<MenuItem> readAll() throws DatabaseException {
        List<MenuItem> menuItems = new ArrayList<>();
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(SELECT_ALL);
             ResultSet results = statement.executeQuery()) {
            while (results.next()) {
                menuItems.add(extractMenuItem(results));
            }
        } catch (SQLException exception) {
            throw DatabaseException.wrap(exception);
        }
        return menuItems;
    }

    @Override
    public boolean update(MenuItem menuItem) throws DatabaseException {
        String sql = "UPDATE Cafeteria_Menu_Item SET category = ?, item_name = ?, price = ?, display_order = ? " +
                "WHERE menu_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            bind(statement, menuItem);
            statement.setInt(5, menuItem.getMenuItemId());
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw DatabaseException.wrap(exception);
        }
    }

    @Override
    public boolean delete(int menuItemId) throws DatabaseException {
        String sql = "DELETE FROM Cafeteria_Menu_Item WHERE menu_item_id = ?";
        try (Connection conn = DBConnection.getConnection();
             PreparedStatement statement = conn.prepareStatement(sql)) {
            statement.setInt(1, menuItemId);
            return statement.executeUpdate() > 0;
        } catch (SQLException exception) {
            throw DatabaseException.wrap(exception);
        }
    }

    private void bind(PreparedStatement statement, MenuItem menuItem) throws SQLException {
        statement.setString(1, menuItem.getCategory());
        statement.setString(2, menuItem.getItemName());
        statement.setBigDecimal(3, menuItem.getPrice());
        statement.setInt(4, menuItem.getDisplayOrder());
    }

    private MenuItem extractMenuItem(ResultSet results) throws SQLException {
        return new MenuItem(
                results.getInt("menu_item_id"),
                results.getString("category"),
                results.getString("item_name"),
                results.getBigDecimal("price"),
                results.getInt("display_order"));
    }
}
