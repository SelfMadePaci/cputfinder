package com.mycompany.cputfinder.dao;

import com.mycompany.cputfinder.dao.exception.DatabaseException;
import com.mycompany.cputfinder.domain.MenuItem;
import java.util.List;

public interface MenuItemDAO {
    boolean create(MenuItem menuItem) throws DatabaseException;
    List<MenuItem> readAll() throws DatabaseException;
    boolean update(MenuItem menuItem) throws DatabaseException;
    boolean delete(int menuItemId) throws DatabaseException;
}
