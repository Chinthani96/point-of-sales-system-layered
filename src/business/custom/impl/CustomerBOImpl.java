package business.custom.impl;

import business.custom.CustomerBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.SuperDAO;
import dao.custom.CustomerDAO;
import entity.Customer;
import entity.SuperEntity;
import util.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class CustomerBOImpl implements CustomerBO {
    private static CustomerDAO customerDAO = DAOFactory.getInstance().getDAO(DAOType.CUSTOMER);
    public List<CustomerTM> getAllCustomers(){
        List<CustomerTM> customerTMS = new ArrayList<>();
        try {
            List<Customer> allCustomers = customerDAO.findAll();
            for (Customer customer : allCustomers) {
                customerTMS.add(new CustomerTM(customer.getId(),customer.getName(),customer.getAddress()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return customerTMS;
    }

    public boolean saveCustomer(String id, String name, String address){
        try {
            return customerDAO.save(new Customer(id,name,address));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateCustomer(String id, String name, String address){
        try {
            return customerDAO.update(new Customer(id, name , address));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteCustomer(String id){
        try {
            return customerDAO.delete(id);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public String generateNewCustomerId(){
        try {
            String lastCustomerId = customerDAO.getLastCustomerId();
            int lastNumber = Integer.parseInt(lastCustomerId.substring(1, 4));
            if (lastNumber==0) {
//                lastNumber++;
                return "C001";
            } else if (lastNumber<9) {
                lastNumber++;
                return "C00" +lastNumber;
            } else if (lastNumber<99) {
                lastNumber++;
                return "C0" +lastNumber;
            }
            else{
                lastNumber++;
                return "C" +lastNumber;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
