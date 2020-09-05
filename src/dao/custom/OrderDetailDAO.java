package dao.custom;

import dao.CrudDAO;
import dao.CrudUtil;
import entity.OrderDetail;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface OrderDetailDAO extends CrudDAO<OrderDetail,String> {

}
