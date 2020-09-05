package business.custom.impl;

import business.custom.ItemBO;
import dao.DAOFactory;
import dao.DAOType;
import dao.SuperDAO;
import dao.custom.ItemDAO;
import entity.Item;
import util.ItemTM;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class ItemBOImpl implements ItemBO {
    private static ItemDAO itemDAO = DAOFactory.getInstance().getDAO(DAOType.ITEM);
    public List<ItemTM> getAllItems(){
        List<ItemTM> items = new ArrayList<>();
        try {
            List<Item> allItems = itemDAO.findAll();
            for (Item item : allItems) {
                items.add(new ItemTM(item.getCode(),item.getDescription(),item.getUnitPrice().doubleValue(),item.getQtyOnHand()));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return items;
    }

    public boolean saveItem(String code, String description, double unitPrice, int qtyOnHand){
        try {
            return itemDAO.save(new Item(code,description,BigDecimal.valueOf(unitPrice),qtyOnHand));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean updateItem(String code, String description, double unitPrice, int qtyOnHand){
        try {
            return itemDAO.update(new Item(code,description,BigDecimal.valueOf(unitPrice),qtyOnHand));
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public boolean deleteItem(String code){
        try {
            return itemDAO.delete(code);
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }

    public String generateNewItemId(){
        try {
            String lastItemId= itemDAO.getLastItemId();
            int lastNumber = Integer.parseInt(lastItemId.substring(1, 4));
            if (lastNumber==0) {
                lastNumber++;
                return "I001";
            } else if (lastNumber<9) {
                lastNumber++;
                return "I00" +lastNumber;
            } else if (lastNumber<99) {
                lastNumber++;
                return "I0" +lastNumber;
            }
            else{
                lastNumber++;
                return "I" +lastNumber;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            return null;
        }
    }
}
