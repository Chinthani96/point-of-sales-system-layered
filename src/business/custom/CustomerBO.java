package business.custom;

import business.SuperBO;
import entity.Customer;
import util.CustomerTM;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface CustomerBO extends SuperBO {
    List<CustomerTM> getAllCustomers();
    boolean saveCustomer(String id, String name, String address);
    boolean updateCustomer(String id, String name, String address);
    boolean deleteCustomer(String id);
    String generateNewCustomerId();
}
