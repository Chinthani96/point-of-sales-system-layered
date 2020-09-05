package dao.custom.impl;

import com.sun.org.apache.bcel.internal.generic.Select;
import dao.CrudUtil;
import dao.custom.OrderDAO;
import entity.Customer;
import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDAOImpl implements OrderDAO {
    public List<Order> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM `Order`");
        ArrayList<Order> orders = new ArrayList<>();
        while(resultSet.next()){
            orders.add(new Order(resultSet.getString(1),resultSet.getDate(2),resultSet.getString(3)));
        }
        return orders;
    }

    public Order find(String pk) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM `Order` WHERE id=?",pk);
        return new Order(resultSet.getString(1),resultSet.getDate(2),resultSet.getString(3));
    }

    public boolean save(Order entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO `Order` VALUES (?,?,?)",entity.getId(),entity.getDate(),entity.getCustomerId());
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

    public String lastOrderId() throws SQLException {
        ResultSet resultset = CrudUtil.execute("SELECT id FROM `Order` ORDER BY id DESC LIMIT 1");
        if(resultset.next()) {
            return resultset.getString(1);
        }
        return null;
    }

}

