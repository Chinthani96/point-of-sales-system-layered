package dao.impl;

import dao.CrudUtil;
import entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl {
    public List<Item> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<Item> items = new ArrayList<>();

        for (Item item : items) {
            items.add(new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getBigDecimal(3),resultSet.getInt(4)));
        }
        return items;
    }

    public Item find(String pk) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item WHERE code=?", pk);
        return new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getBigDecimal(3),resultSet.getInt(4));
    }

    public boolean save(Item entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?)");
        if (!result) {
            return false;
        }
        return true;
    }

    public boolean update(Item entity) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE Item SET description=?,unitPrice=?,qtyOnHand=? WHERE code?");
        if(!result){
            return false;
        }
        return true;
    }

    public boolean delete(String pk) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM Item WHERE code=?", pk);
        if (!result) {
            return false;
        }
        return true;
    }
}
