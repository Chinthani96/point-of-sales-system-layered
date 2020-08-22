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
import util.ItemTM;

import java.io.IOException;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class ItemFormController {
    public TextField txtItemID;
    public TextField txtDescription;
    public TextField txtQtyOnHand;
    public TextField txtUnitPrice;
    public TableView<ItemTM> tblItemDetails;
    public Button btnAddItem;
    public Button btnSave;
    public Button btnDelete;
    public Button btnBack;
    public AnchorPane root;

    public void initialize(){
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnDelete.requestFocus();

        //load all items
        loadAllItems();

        //mapping columns
        tblItemDetails.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("itemId"));
        tblItemDetails.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("description"));
        tblItemDetails.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        tblItemDetails.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("qtyOnHand"));

        tblItemDetails.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<ItemTM>() {
            @Override
            public void changed(ObservableValue<? extends ItemTM> observable, ItemTM oldValue, ItemTM selectedItem) {
                if(selectedItem==null){
                    return;
                }
                else{
                    btnSave.setText("Update");
                    btnSave.setDisable(false);
                    btnDelete.setDisable(false);

                    txtItemID.setText(selectedItem.getItemId()+"");
                    txtDescription.setText(selectedItem.getDescription()+"");
                    txtUnitPrice.setText(selectedItem.getUnitPrice()+"");
                    txtQtyOnHand.setText(selectedItem.getQtyOnHand()+"");
                }
            }
        });
    }

    @SuppressWarnings("Duplicates")
    public void btnBack_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene mainScene = new Scene(root);
        Stage mainStage = (Stage)this.root.getScene().getWindow();
        mainStage.setScene(mainScene);
        mainStage.centerOnScreen();
    }

    public void btnDelete_OnAction(ActionEvent actionEvent) {
        ItemTM selectedItem = tblItemDetails.getSelectionModel().getSelectedItem();

        if(selectedItem==null){
            return;
        }

        try {
            PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("DELETE from Item where ItemCode=?");
            pst.setObject(1,selectedItem.getItemId());
            int i = pst.executeUpdate();

            if(i>0){
                loadAllItems();
                tblItemDetails.getSelectionModel().clearSelection();
                System.out.println("Item deleted successfully");
            }
            else{
                new Alert(Alert.AlertType.ERROR,"Failed to delete item",ButtonType.OK).show();
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        btnSave.setText("Save");
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        txtDescription.clear();
        txtItemID.clear();
        txtUnitPrice.clear();
        txtQtyOnHand.clear();

    }

    @SuppressWarnings("Duplicates")
    public void btnSave_OnAction(ActionEvent actionEvent) {
        //TODO validations

        String itemId = txtItemID.getText();
        String description = txtDescription.getText();
        double price = Double.parseDouble(txtUnitPrice.getText());
        int qtyOnHand = Integer.parseInt(txtQtyOnHand.getText());

        if(btnSave.getText().equals("Update")){

            try {
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("UPDATE Item SET ItemCode=?,Description=?,UnitPrice=?,QtyOnHand=? where ItemCode='" + itemId + "'");
                pst.setObject(1,itemId);
                pst.setObject(2,description);
                pst.setObject(3,price);
                pst.setObject(4,qtyOnHand);
                int i = pst.executeUpdate();

//                  if(i>0){
//                    loadAllItems();
//                    System.out.println("Item updated successfully");
//                    tblItemDetails.getSelectionModel().clearSelection();
//                    btnSave.setText("Save");
//                }
//                else{
//                    new Alert(Alert.AlertType.ERROR,"Failed to update", ButtonType.OK).show();
//                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        else{
            try {
                PreparedStatement pst = DBConnection.getInstance().getConnection().prepareStatement("INSERT into Item VALUES (?,?,?,?)");
                pst.setObject(1,itemId);
                pst.setObject(2,description);
                pst.setObject(3,price);
                pst.setObject(4,qtyOnHand);
                int i = pst.executeUpdate();

//                if(i>0){
//                    loadAllItems();
//                    System.out.println("Item added successfully");
//                    tblItemDetails.getSelectionModel().clearSelection();
//                }

            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        btnSave.setDisable(true);
        btnDelete.setDisable(true);
        btnAddItem.setDisable(false);
        btnAddItem.requestFocus();
        txtItemID.clear();
        txtDescription.clear();
        txtQtyOnHand.clear();
        txtUnitPrice.clear();
    }
    public void btnAdd_OnAction(ActionEvent actionEvent) {
        btnAddItem.setDisable(true);
        txtDescription.requestFocus();
        btnSave.setDisable(false);

        generateId();
    }

    public void loadAllItems(){
        try {
            Statement statement = DBConnection.getInstance().getConnection().createStatement();
            ResultSet resultSet = statement.executeQuery("SELECT * from Item");
            ObservableList items = tblItemDetails.getItems();
            items.clear();

            while(resultSet.next()){
                String id = resultSet.getString(1);
                String description = resultSet.getString(2);
                double price = Double.parseDouble(resultSet.getString(3));
                int qtyOnHand = Integer.parseInt(resultSet.getString(4));
                items.add(new ItemTM(id,description,price,qtyOnHand));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
    private void generateId(){
        ObservableList<ItemTM> items = tblItemDetails.getItems();

        int maxId=0;

        for(ItemTM item: items){
            String id = item.getItemId().replace("I","");
            if(Integer.parseInt(id)>maxId){
                maxId = Integer.parseInt(id);
            }
        }
        maxId++;
        if(maxId<10){
            txtItemID.setText("I00"+maxId);
        }
        else if(maxId<100){
            txtItemID.setText("I0"+maxId);
        }
        else{
            txtItemID.setText("I"+maxId);
        }
    }

}
