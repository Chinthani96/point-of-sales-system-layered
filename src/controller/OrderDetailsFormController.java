package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.*;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;

public class OrderDetailsFormController {
    public AnchorPane root;
    public TextField txtOrderID;
    public TextField txtQtyOnHand;
    public TextField txtOrderDate;
    public TextField txtCustomerName;
    public TextField txtQuantity;
    public TextField txtUnitPrice;
    public TextField txtDescription;
    public ComboBox<CustomerTM> cmbCustomerId;
    public ComboBox<ItemTM> cmbItemID;
    public Button btnDelete;
    public TableView<OrderDetailsTM> tblOrderDetails;
    public Label lblTotal;
    public Button btnPlaceOrder;
    public Button btnBack;
    public Button btnAdd;
    public Button btnAddOrder;

    public void initialize() {

        loadAllCustomers();
        loadAllItems();

        //Basic Initializations
        btnAdd.setDisable(true);
        btnDelete.setDisable(true);
        btnAdd.requestFocus();

        //map columns
        tblOrderDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblOrderDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblOrderDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("qty"));
        tblOrderDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblOrderDetails.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));

        //when a customer id is selected
        cmbCustomerId.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM selectedCustomer) {
                if(selectedCustomer==null){
                    return;
                }
                txtCustomerName.setText(selectedCustomer.getCustomerName());
            }
        });

        //when an item code is selected
        cmbItemID.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM selectedItem) {
                if(selectedItem==null){
                    cmbItemID.getSelectionModel().clearSelection();
                    txtDescription.clear();
                    txtUnitPrice.clear();
                    txtQtyOnHand.clear();
                    return;
                }

                btnAdd.setDisable(false);
                txtDescription.setText(selectedItem.getDescription());
                txtQtyOnHand.setText(String.valueOf(selectedItem.getQtyOnHand()));
                txtUnitPrice.setText(String.valueOf(selectedItem.getUnitPrice()));
            }
        });
        //When a row in the table is selected
        tblOrderDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<OrderDetailsTM>() {
            @Override
            public void changed(ObservableValue<? extends OrderDetailsTM> observable, OrderDetailsTM oldValue, OrderDetailsTM selectedOrderDetails) {
                btnDelete.setDisable(false);
                btnAdd.setText("Update");

                if(selectedOrderDetails==null){
                    return;
                }

                String itemId = selectedOrderDetails.getItemId();
                ObservableList<ItemTM> items = cmbItemID.getItems();

                for(ItemTM item:items){
                    if(itemId==item.getItemId()){
                        txtDescription.setText(item.getDescription());
                        txtQtyOnHand.setText(String.valueOf(item.getQtyOnHand()));
//                        txtQuantity.setText();
                    }
                }

                txtDescription.setText(selectedOrderDetails.getDescription());

            }
        });
    }
    public void btnAddOrder_OnAction(ActionEvent actionEvent) {
        generateId();
        LocalDate today = LocalDate.now();
        txtOrderDate.setText(today.toString());
        btnAdd.setDisable(true);
    }

    @SuppressWarnings("Duplicates")
    public void btnDelete_OnAction(ActionEvent actionEvent) {
        ObservableList<OrderDetailsTM> orderDetails = tblOrderDetails.getItems();
        OrderDetailsTM selectedDetail = tblOrderDetails.getSelectionModel().getSelectedItem();
        orderDetails.remove(selectedDetail);
        btnDelete.setDisable(true);
        btnAdd.setDisable(true);
        txtQuantity.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
        cmbItemID.getSelectionModel().clearSelection();
    }

    public void btnPlaceOrder_OnAction(ActionEvent actionEvent) {
        //add to orders table
        String orderId = txtOrderID.getText();
        String orderDate = txtOrderDate.getText();
        CustomerTM selectedCustomer= cmbCustomerId.getSelectionModel().getSelectedItem();
        String customerId = selectedCustomer.getCustomerId();
        addToOrders(orderId,customerId,orderDate);

        ObservableList<OrderDetailsTM> items = tblOrderDetails.getItems();
        for(OrderDetailsTM item:items) {

            String itemCode = item.getItemId();
            int qty = item.getQty();
            addToOrderDetail(orderId,itemCode,qty);
        }
        calculateOrderTotal();
    }

    private void generateId() {
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = stm.executeQuery("SELECT OrderID from Orders order by OrderID desc limit 1");
            resultSet.next();
            String orderId = resultSet.getString(1);

            int val = Integer.parseInt(orderId.substring(1, 4));
            val++;
            if (val < 10) {
                txtOrderID.setText("D00" + val);
            } else if (val < 100) {
                txtOrderID.setText("D0" + val);
            } else {
                txtOrderID.setText("D" + val);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    public void btnAdd_OnAction(ActionEvent actionEvent) {
        ItemTM item = cmbItemID.getSelectionModel().getSelectedItem();

        //table columns
        String itemId = item.getItemId();
        String description = item.getDescription();
        double unitPrice  = item.getUnitPrice();
        int qty = Integer.parseInt(txtQuantity.getText());
        double total = unitPrice * qty;

        ObservableList<OrderDetailsTM> orderDetails = tblOrderDetails.getItems();
        orderDetails.add(new OrderDetailsTM(itemId,description,qty,unitPrice,total));
        cmbItemID.getSelectionModel().clearSelection();
        txtQuantity.clear();

    }

    public void txtQuantity_OnAction(ActionEvent actionEvent) {
        btnAdd_OnAction(actionEvent);
    }

    @SuppressWarnings("Duplicates")
    private void addToOrders(String orderId, String customerId, String date){
        try {
            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("Insert into Orders VALUES (?,?,?)");
            pst.setObject(1,orderId);
            pst.setObject(2,date);
            pst.setObject(3,customerId);

//            int affectedRows = pst.executeUpdate();
//            if (affectedRows>0){
//                System.out.println("Table orders have been updated successfully.");
//            }
//            else{
//                System.out.println("Failed to update the order table");
//            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    @SuppressWarnings("Duplicates")
    private void addToOrderDetail(String orderId,String ItemCode,int OrderQty){
        try {
            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("Insert into OrderDetail values (?,?,?)");
            pst.setObject(1,orderId);
            pst.setObject(2,ItemCode);
            pst.setObject(3,OrderQty);
            int affectedRows = pst.executeUpdate();

//            if(affectedRows>0){
//                System.out.println("Table orders have been updated successfully.");
//            }
//            else{
//                System.out.println("Failed to update the order table");
//            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

    }
    private void loadAllCustomers(){
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("Select * from Customer");
            ObservableList<CustomerTM> customers = cmbCustomerId.getItems();

            while(rst.next()){
                String customerId = rst.getString(1);
                String customerName = rst.getString(2);
                String customerAddress = rst.getString(3);

                customers.add(new CustomerTM(customerId,customerName,customerAddress));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void loadAllItems(){
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("SELECT  * from Item");
            ObservableList<ItemTM> items = cmbItemID.getItems();

            while(rst.next()){
                String itemId = rst.getString(1);
                String description = rst.getString(2);
                int qtyOnHand = Integer.parseInt(rst.getString(4));
                double unitPrice = Double.parseDouble(rst.getString(3));

                items.add(new ItemTM(itemId,description,unitPrice,qtyOnHand));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void calculateOrderTotal(){
        ObservableList<OrderDetailsTM> orderDetails = tblOrderDetails.getItems();
        double netTotal = 0;

        for(OrderDetailsTM order : orderDetails){
            netTotal+=order.getTotal();
        }
        lblTotal.setText(String.valueOf(netTotal));
    }

    public void btnBack_OnAction(ActionEvent actionEvent) throws IOException {
        @SuppressWarnings("Duplicates")
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage = (Stage) this.root.getScene().getWindow();
        mainStage.setScene(mainScene);
        mainStage.centerOnScreen();
    }

//    public void initializeWithSearchOrderForm(String orderId){
//        txtOrderID.setText(orderId);
//        readOnly = true;
//
//    }
//    private void loadTable(){
//        try {
//            Statement stm = DBConnection.getInstance().getConnection().createStatement();
//            ResultSet rst = stm.executeQuery("select o.orderId,o.orderDate,o.CustomerID,c.customerName, Sum(d.OrderQTY*i.unitPrice) as Total\n" +
//                    "from orders o,orderDetail d,item i, customer c\n" +
//                    "where o.orderId = d.orderId && d.itemCode = i.itemCode && o.CustomerID = c.CustomerID group by o.orderId");
//            ObservableList<SearchOrderTM> items = tblOrders.getItems();
//            items.clear();
//
//            while (rst.next()){
//                String orderId = rst.getString(1);
//                String orderDate = rst.getString(2);
//                String customerId = rst.getString(3);
//                String customerName = rst.getString(4);
//                double total = Double.parseDouble(rst.getString(5));
//
//                items.add(new SearchOrderTM(orderId,orderDate,customerId,customerName,total));
//                ordersArray.add(new SearchOrderTM(orderId,orderDate,customerId,customerName,total));
//            }
//
//        } catch (SQLException e) {
//            e.printStackTrace();
//        }
//    }

}

