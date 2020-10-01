package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.ItemDAO;
import entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemDAOImpl implements ItemDAO {
    public List<Item> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item");
        ArrayList<Item> items = new ArrayList<>();

        while(resultSet.next()){
            items.add(new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getBigDecimal(3),resultSet.getInt(4)));
        }
        return items;
    }

    public Item find(String pk) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Item WHERE code=?", pk);
        return new Item(resultSet.getString(1),resultSet.getString(2),resultSet.getBigDecimal(3),resultSet.getInt(4));
    }

    public boolean save(Item entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO Item VALUES (?,?,?,?)",entity.getCode(),entity.getDescription(),entity.getQtyOnHand(),entity.getUnitPrice());
        if (!result) {
            return false;
        }
        return true;
    }

    public boolean update(Item entity) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE Item SET description=?,unitPrice=?,qtyOnHand=? WHERE code?",entity.getDescription(),entity.getUnitPrice(),entity.getQtyOnHand(),entity.getCode());
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

    public String getLastItemId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT code FROM Item ORDER BY code DESC LIMIT 1");
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }

    public int getQty(String pk) throws Exception {
        ResultSet resultSet = CrudUtil.execute("SELECT qtyOnHand FROM Item WHERE code=?", pk);
        if(resultSet.next()) {
            return Integer.parseInt(resultSet.getString(1));
        }
        return 0;
    }

    public boolean updateQty(int newQty, String code) throws SQLException {
        boolean result =  CrudUtil.execute("UPDATE Item set qtyOnhand=? WHERE code=?", newQty, code);
        if(result){
            return true;
        }
        return false;
    }
}
