package business.custom.impl;

import business.custom.OrderBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.SuperDAO;
import dao.custom.ItemDAO;
import dao.custom.OrderDAO;
import dao.custom.OrderDetailDAO;
import dao.custom.QueryDAO;
import entity.CustomEntity;
import entity.Order;
import entity.OrderDetail;
import util.OrderDetailsTM;
import util.SearchOrderTM;

import java.math.BigDecimal;
import java.sql.Date;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class OrderBOImpl implements OrderBO {
    private static OrderDAO orderDAO = DAOFactory.getInstance().getDAO(DAOType.ORDER);
    private static OrderDetailDAO orderDetailDAO = DAOFactory.getInstance().getDAO(DAOType.ORDERDETAIL);
    private static ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    private static QueryDAO queryDAO = DAOFactory.getInstance().getDAO(DAOType.QUERY);

    public boolean saveOrder(String id, Date date, String customerId){
        try {
           return orderDAO.save(new Order(id,date,customerId));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean saveOrderDetail(String orderId, String itemCode, int qty, double unitPrice){
        try {
            return orderDetailDAO.save(new OrderDetail(orderId,itemCode,qty,BigDecimal.valueOf(unitPrice)));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateNewOrderId(){
        try {
            String lastOrderId = orderDAO.lastOrderId();
            int lastNumber = Integer.parseInt(lastOrderId.substring(2, 5));
            if (lastNumber<=0) {
                lastNumber++;
                return "OD001";
            } else if (lastNumber<9) {
                lastNumber++;
                return "OD00" +lastNumber;
            } else if (lastNumber<99) {
                lastNumber++;
                return "OD0" +lastNumber;
            }
            else{
                lastNumber++;
                return "OD" +lastNumber;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
    public boolean saveNewQty(int orderQty,String code){
        try {
            int qty = itemDAO.getQty(code);
            int newQtyOnHand = qty - orderQty;
            return itemDAO.updateQty(newQtyOnHand,code);
        } catch (Exception e) {
            e.printStackTrace();
            return false;
        }
    }

    public List<SearchOrderTM> getOrderDetails(){
        try {
            List<SearchOrderTM> searchOrderTMS = new ArrayList<>();
            List<CustomEntity> orderDetails = queryDAO.getOrderDetails();
            for (CustomEntity orderDetail : orderDetails) {
                searchOrderTMS.add(new SearchOrderTM(orderDetail.getOrderId(),orderDetail.getOrderDate().toString(),orderDetail.getCustomerId(),orderDetail.getCustomerName(),orderDetail.getTotal().doubleValue()));
            }
            return searchOrderTMS;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }
}
