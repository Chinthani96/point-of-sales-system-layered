package business.custom;

import business.SuperBO;
import util.OrderDetailsTM;
import util.SearchOrderTM;

import java.sql.Date;
import java.sql.SQLException;
import java.util.List;

public interface OrderBO extends SuperBO {
    boolean saveOrder(String id, Date date, String customerId);
    boolean saveOrderDetail(String orderId, String itemCode, int qty, double unitPrice);
    String generateNewOrderId();
    boolean saveNewQty(int orderQty,String code);
    List<SearchOrderTM> getOrderDetails();
}
