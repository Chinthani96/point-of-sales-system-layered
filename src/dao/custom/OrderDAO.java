package dao.custom;

import dao.CrudDAO;
import dao.CrudUtil;
import entity.Order;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderDAO extends CrudDAO<Order,String> {
    String lastOrderId() throws SQLException;
}
