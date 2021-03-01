package org.uniruse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uniruse.App;
import org.uniruse.controller.modulars.ItemsTableController;
import org.uniruse.controller.modulars.OrdersTableController;
import org.uniruse.data.transactions.OrderLines;
import org.uniruse.data.transactions.Orders;
import org.uniruse.repository.ItemRepository;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class OrderLinesController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    ItemRepository itemRepository = new ItemRepository(dbUtil);
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<OrderLines> tableView;

    @FXML
    private TableColumn<OrderLines, Integer> orderColumn;

    @FXML
    private TableColumn<OrderLines, String> priceColumn;

    @FXML
    private TableColumn<OrderLines, String> itemColumn;

    @FXML
    private TableColumn<OrderLines, Integer> quantityColumn;

    @FXML
    private Label orderIdLabel;

    @FXML
    private Label itemIdLabel;

    private Stage ordersStage;
    private Stage itemsStage;

    @FXML
    private void insertButton() {
        try {
            OrderLines orderLine = getSelectedOrderLine();
            transactionsRepository.insertOrderLine(orderLine);
            AtomicReference<Double> totalPrice = new AtomicReference<>(0d);
            Orders order = transactionsRepository.findOrderById(orderLine.getOrderId());
            System.out.println(order.getChilds().size());
            order.getChilds().forEach(it -> totalPrice.updateAndGet(v -> v + it.getPrice()));
            order.setTotalPrice(totalPrice.get());
            transactionsRepository.updateOrders(order);
            showOrderLines();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private OrderLines getSelectedOrderLine() throws SQLException {
        return new OrderLines(
                orderIdLabel.getText().equals("ID:") ? -1 : Long.parseLong(orderIdLabel.getText().replace("ID:", "")),
                itemIdLabel.getText().isBlank() ? null :
                        itemRepository.findMedicineById(itemIdLabel.getText().replace("ID:", "")) == null ?
                                itemRepository.findSupplementById(itemIdLabel.getText().replace("ID:", "")) :
                                itemRepository.findMedicineById(itemIdLabel.getText().replace("ID:", "")),
                quantityField.getText().isBlank() ? 0 : Integer.parseInt(quantityField.getText())
        );
    }

    @FXML
    private void setFields() {
        OrderLines orderLines = tableView.getSelectionModel().getSelectedItem();
        if (orderLines != null) {
            orderIdLabel.setText("ID:" + orderLines.getOrderId());
            itemIdLabel.setText(orderLines.getItemSku());
            quantityField.setText(String.valueOf(orderLines.getQuantity()));
        }
    }

    @FXML
    private void selectOrder() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/ordersTable.fxml"));
        Parent root = fxmlLoader.load();
        OrdersTableController ordersTableController = fxmlLoader.getController();
        ordersTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        ordersStage = new Stage();
        ordersStage.setScene(new Scene(root));
        ordersStage.setTitle("Supplies");
        ordersStage.initModality(Modality.APPLICATION_MODAL);
        ordersStage.initOwner(App.getScene().getWindow());

        ordersStage.show();
    }


    @FXML
    private void selectItem() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/itemsTable.fxml"));
        Parent root = fxmlLoader.load();
        ItemsTableController itemsTableController = fxmlLoader.getController();
        itemsTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        itemsStage = new Stage();
        itemsStage.setScene(new Scene(root));
        itemsStage.setTitle("Items");
        itemsStage.initModality(Modality.APPLICATION_MODAL);
        itemsStage.initOwner(App.getScene().getWindow());

        itemsStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showOrderLines();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<OrderLines> getOrdersList() {
        ObservableList<OrderLines> orderLines = FXCollections.observableArrayList();
        List<Orders> orders = transactionsRepository.findAllOrders();
        orders.forEach(it ->
                orderLines.addAll(it.getChilds().toArray(new OrderLines[0]))
        );
        return orderLines;
    }

    public void showOrderLines() {
        ObservableList<OrderLines> list = getOrdersList();
        orderColumn.setCellValueFactory(new PropertyValueFactory<>("orderId"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemSku"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(list);
    }

    public void setOrderIdLabel(long id) {
        orderIdLabel.setText("ID:" + id);
    }

    public void setItemIdLabel(String sku) {
        itemIdLabel.setText("ID:" + sku);
    }

    public Stage getOrderStage() {
        return this.ordersStage;
    }

    public Stage getItemsStage() {
        return this.itemsStage;
    }
}
