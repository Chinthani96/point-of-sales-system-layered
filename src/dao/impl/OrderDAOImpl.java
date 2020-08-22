package dao.impl;

import dao.CrudUtil;
import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl {
    public List<Order> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM `Order`");
        ArrayList<Order> orders = new ArrayList<>();
        for (Order order: orders) {
            orders.add(new Order(resultSet.getString(1),resultSet.getDate(2),resultSet.getString(3)));
        }
        return orders;
    }

    public Order find(String pk) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM `Order` WHERE id=?",pk);
        return new Order(resultSet.getString(1),resultSet.getDate(2),resultSet.getString(3));
    }

    public boolean save(Order entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO `Order` VALUES (?,?,?)");
        if(!result){
            return false;
        }
        return true;
    }

    public boolean update(Order entity) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE `Order` SET date=?,customerId=? WHERE id=?", entity.getDate(), entity.getCustomerId(), entity.getId());
        if (!result) {
            return false;
        }
        return true;
    }

    public boolean delete(String pk) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM `Order` WHERE id=?", pk);
        if(!result){
            return false;
        }
        return true;
    }
}

