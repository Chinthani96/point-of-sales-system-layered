package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import util.CustomerTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class CustomerFormController {
    public Button btnDelete;
    public Button btnSave;
    public Button btnBack;
    public TextField txtCustomerId;
    public TextField txtCustomerName;
    public TextField txtCustomerAddress;
    public Button btnAddCustomer;
    public TableView<CustomerTM> tblCustomerDetails;
    public AnchorPane root;

    public void initialize(){
        //basic initializations
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnAddCustomer.requestFocus();

        //loading the Customers
        loadAllCustomer();

        //map the columns
        tblCustomerDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblCustomerDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblCustomerDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerAddress"));

        tblCustomerDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<CustomerTM>() {
            @Override
            public void changed(ObservableValue<? extends CustomerTM> observable, CustomerTM oldValue, CustomerTM selectedCustomer) {
                if(selectedCustomer==null){
                    return;
                }

                btnSave.setText("Update");
                btnSave.setDisable(false);
                btnDelete.setDisable(false);
                btnAddCustomer.setDisable(true);
                txtCustomerId.setText(selectedCustomer.getCustomerId());
                txtCustomerName.setText(selectedCustomer.getCustomerName());
                txtCustomerAddress.setText(selectedCustomer.getCustomerAddress());
            }
        });

    }

    public void btnAddCustomer_OnAction(ActionEvent actionEvent) {
        //btn and textfield initializations
        btnAddCustomer.setDisable(true);
        btnSave.setDisable(false);
        txtCustomerName.requestFocus();

        //TODO validations

        //generate the customer id
        generateId();

    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {


        CustomerTM selectedCustomer = tblCustomerDetails.getSelectionModel().getSelectedItem();

        try {
            PreparedStatement preparedStatement = DBConnection.getInstance().getConnection().prepareStatement("DELETE from Customer where CustomerID=?");
            preparedStatement.setObject(1,selectedCustomer.getCustomerId());
            int affectedRows = preparedStatement.executeUpdate();

            if(affectedRows>0){
                loadAllCustomer();
                tblCustomerDetails.getSelectionModel().clearSelection();
                System.out.println("Deleted Successfully");
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Delete Failed",ButtonType.OK).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnDelete.setDisable(true);
        btnSave.setDisable(true);
        btnSave.setText("Save");
        btnAddCustomer.setDisable(false);
        btnAddCustomer.requestFocus();
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();

    }

    public void btnSave_OnAction(ActionEvent actionEvent) {
        //btn and textfield initializations when save button is pressed
        btnSave.setDisable(true);
        btnAddCustomer.setDisable(false);
        btnAddCustomer.requestFocus();

        String customerId = txtCustomerId.getText();
        String customerName = txtCustomerName.getText();
        String customerAddress = txtCustomerAddress.getText();

        CustomerTM selectedCustomer = tblCustomerDetails.getSelectionModel().getSelectedItem();

        if(btnSave.getText().equals("Update")){
            try {
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Customer SET CustomerID=?,CustomerName=?,CustomerAddress=? where CustomerID='"+customerId+"'");
                pst.setObject(1,customerId);
                pst.setObject(2,customerName);
                pst.setObject(3,customerAddress);
                int affectedRows = pst.executeUpdate();

//                if(affectedRows>0){
//                    loadAllCustomer();
//                    tblCustomerDetails.getSelectionModel().clearSelection();
//                    System.out.println("Database updated successfully.");
//                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else {
            try {
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("INSERT  into Customer VALUES (?,?,?)");
                pst.setObject(1, customerId);
                pst.setObject(2, customerName);
                pst.setObject(3, customerAddress);
                int affectedRows = pst.executeUpdate();

//                if (affectedRows > 0) {
//                    loadAllCustomer();
//                    tblCustomerDetails.getSelectionModel().clearSelection();
//                    System.out.println("Customer added successfully");
//                } else {
//                    new Alert(Alert.AlertType.ERROR, "Failed to add Customer to the Database", ButtonType.OK).show();
//                }
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        //clear textfields
        txtCustomerId.clear();
        txtCustomerName.clear();
        txtCustomerAddress.clear();

    }

    @SuppressWarnings("Duplicates")
    public void btnBack_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage = (Stage)this.root.getScene().getWindow();
        mainStage.setScene(mainScene);
        mainStage.centerOnScreen();
    }

    private void generateId(){
        ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();

        int maxId =0;
        for(CustomerTM customer : customers){
            int id = Integer.parseInt(customer.getCustomerId().replace("C",""));
            if(id>maxId){
                maxId = id;
            }
        }
        System.out.println(maxId);
        maxId++;
        if(maxId<10){
            txtCustomerId.setText("C00"+maxId);
        }
        else if(maxId<100){
            txtCustomerId.setText("C0"+maxId);
        }
        else{
            txtCustomerId.setText("C"+maxId);
        }
    }

    private void loadAllCustomer(){
        try {
            Statement st = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = st.executeQuery("SELECT  * from Customer");
            ObservableList<CustomerTM> customers = tblCustomerDetails.getItems();
            customers.clear();

            while(rst.next()){
                String id = rst.getString(1);
                String name = rst.getNString(2);
                String address = rst.getString(3);
                customers.add(new CustomerTM(id,name,address));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
