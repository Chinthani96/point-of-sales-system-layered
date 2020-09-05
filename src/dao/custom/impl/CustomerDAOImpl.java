package dao.custom.impl;

import dao.CrudUtil;
import dao.custom.CustomerDAO;
import entity.Customer;
import entity.SuperEntity;

import java.io.Serializable;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerDAOImpl implements CustomerDAO {
    public List<Customer> findAll() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT * FROM Customer");
        ArrayList<Customer> customers = new ArrayList<>();

        while(resultSet.next()){
            customers.add(new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3)));
        }
        return customers;
    }

    public Customer find(String pk) throws SQLException {
        ResultSet resultSet= CrudUtil.execute("SELECT * FROM Customer WHERE id=?", pk);
        return new Customer(resultSet.getString(1), resultSet.getString(2), resultSet.getString(3));
    }

    public boolean save(Customer entity) throws SQLException {
        boolean result = CrudUtil.execute("INSERT INTO Customer VALUES (?,?,?)",entity.getId(),entity.getName(), entity.getAddress());
        if(!result){
            return false;
        }
        return true;
    }

    public boolean update(Customer entity) throws SQLException {
        boolean result = CrudUtil.execute("UPDATE Customer SET name=?,address=? WHERE id=?", entity.getName(), entity.getAddress(), entity.getId());
        if (!result) {
            return false;
        }
        return true;
    }

    public boolean delete(String pk) throws SQLException {
        boolean result = CrudUtil.execute("DELETE FROM Customer WHERE id=?", pk);
        if(!result){
            return false;
        }
        return true;
    }

    public String getLastCustomerId() throws SQLException {
        ResultSet resultSet = CrudUtil.execute("SELECT id FROM Customer ORDER BY id DESC LIMIT 1");
        if(resultSet.next()) {
            return resultSet.getString(1);
        }
        return null;
    }
}
