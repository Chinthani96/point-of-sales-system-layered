package dao.custom;

import dao.CrudDAO;
import dao.CrudUtil;
import entity.Item;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemDAO extends CrudDAO<Item,String> {
    String getLastItemId() throws SQLException;
    int getQty(String pk) throws Exception;
    boolean updateQty(int newQty, String code) throws SQLException;
}
