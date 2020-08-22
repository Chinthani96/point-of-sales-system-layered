package controller;

import db.DBConnection;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;
import javafx.stage.Window;
import util.SearchOrderTM;

import java.awt.event.MouseEvent;
import java.io.IOException;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.ArrayList;

public class SearchOrderFormController {
    public TableView<SearchOrderTM> tblOrders;
    public TextField txtSearch;
    public AnchorPane root;
    public Button btnBack;
    public static ArrayList<SearchOrderTM> ordersArray = new ArrayList<>();

    public void initialize(){
        //map columns
        tblOrders.getColumns().get(0).setCellValueFactory(new PropertyValueFactory<>("orderId"));
        tblOrders.getColumns().get(1).setCellValueFactory(new PropertyValueFactory<>("date"));
        tblOrders.getColumns().get(2).setCellValueFactory(new PropertyValueFactory<>("customerId"));
        tblOrders.getColumns().get(3).setCellValueFactory(new PropertyValueFactory<>("customerName"));
        tblOrders.getColumns().get(4).setCellValueFactory(new PropertyValueFactory<>("total"));
        loadTable();

        txtSearch.textProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                ObservableList<SearchOrderTM> orders = tblOrders.getItems();
                orders.clear();

                for (SearchOrderTM order :ordersArray){
                    if(order.getOrderId().contains(newValue)||order.getCustomerId().contains(newValue)||order.getCustomerName().contains(newValue)||order.getDate().contains(newValue)){
                        orders.add(order);
                    }
                }
            }
        });
    }

    @SuppressWarnings("Duplicates")
    public void btnBack_OnAction(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(this.getClass().getResource("/view/MainForm.fxml"));
        Scene mainscene = new Scene(root);
        Stage mainStage= (Stage)this.root.getScene().getWindow();
        mainStage.setScene(mainscene);
        mainStage.centerOnScreen();
        mainStage.setResizable(false);
    }

    public void txtSearch(ActionEvent actionEvent) {
    }

    private void loadTable(){
        try {
            Statement stm = DBConnection.getInstance().getConnection().createStatement();
            ResultSet rst = stm.executeQuery("select o.orderId,o.orderDate,o.CustomerID,c.customerName, Sum(d.OrderQTY*i.unitPrice) as Total\n" +
                    "from orders o,orderDetail d,item i, customer c\n" +
                    "where o.orderId = d.orderId && d.itemCode = i.itemCode && o.CustomerID = c.CustomerID group by o.orderId");
            ObservableList<SearchOrderTM> items = tblOrders.getItems();
            items.clear();

            while (rst.next()){
                String orderId = rst.getString(1);
                String orderDate = rst.getString(2);
                String customerId = rst.getString(3);
                String customerName = rst.getString(4);
                double total = Double.parseDouble(rst.getString(5));

                items.add(new SearchOrderTM(orderId,orderDate,customerId,customerName,total));
                ordersArray.add(new SearchOrderTM(orderId,orderDate,customerId,customerName,total));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void tblOrders_OnMouseClicked(javafx.scene.input.MouseEvent mouseEvent) throws IOException {
        if (tblOrders.getSelectionModel().getSelectedItem() == null){
            return;
        }
        if (mouseEvent.getClickCount() == 2){
            FXMLLoader fxmlLoader = new FXMLLoader(this.getClass().getResource("/view/OrderDetailsForm.fxml"));
            Parent root = fxmlLoader.load();
            OrderDetailsFormController controller = (OrderDetailsFormController) fxmlLoader.getController();
//            controller.initializeWithSearchOrderForm(tblOrders.getSelectionModel().getSelectedItem().getOrderId());
            Scene orderScene = new Scene(root);
            Stage stage = new Stage();
            stage.setScene(orderScene);
            stage.centerOnScreen();
            stage.show();
        }
    }
}
