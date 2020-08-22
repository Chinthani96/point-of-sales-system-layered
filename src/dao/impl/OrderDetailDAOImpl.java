package dao.impl;

import dao.CrudUtil;
import entity.Order;
import entity.OrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderDetailDAOImpl {
    public List<OrderDetail> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM OrderDetail");
        ArrayList<OrderDetail> orderDetails = new ArrayList<>();
        for (OrderDetail orderDetail : orderDetails) {
            orderDetails.add(new OrderDetail(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getBigDecimal(4)));
        }
    return orderDetails;
    }

    public OrderDetail find(String pk) throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM OrderDetail WHERE orderId=?", pk);
        return new OrderDetail(resultSet.getString(1),resultSet.getString(2),resultSet.getInt(3),resultSet.getBigDecimal(4));
    }

    public boolean save(OrderDetail entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO OrderDetail VALUES (?,?,?,?)");
        if(!result){
            return false;
        }
        return true;
    }

    public boolean update(OrderDetail entity) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE OrderDetail SET itemCode=?,qty=?,unitPrice=? WHERE orderId=?");
        if(!result){
            return false;
        }
        return true;
    }

    public boolean delete(String pk) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM OrderDetail WHERE orderId=?", pk);
        if (!result) {
            return false;
        }
        return true;
    }

}

