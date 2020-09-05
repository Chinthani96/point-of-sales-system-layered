package business.custom;

import business.SuperBO;
import entity.Item;
import util.ItemTM;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public interface ItemBO extends SuperBO {
    String generateNewItemId();
    List<ItemTM> getAllItems();
    boolean saveItem(String code, String description, double unitPrice, int qtyOnHand);
    boolean updateItem(String code, String description, double unitPrice, int qtyOnHand);
    boolean deleteItem(String code);

}
