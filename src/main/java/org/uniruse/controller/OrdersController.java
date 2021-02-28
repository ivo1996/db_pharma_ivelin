package org.uniruse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uniruse.App;
import org.uniruse.controller.modulars.CustomersTableController;
import org.uniruse.controller.modulars.ShippingsTableController;
import org.uniruse.data.contragents.Address;
import org.uniruse.data.transactions.Orders;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.LocalDate;
import java.time.ZoneId;
import java.util.Date;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class OrdersController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);
    ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private Label customerIdLabel;

    @FXML
    private Label shippingIdLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField createdAtField;

    @FXML
    private DatePicker shippingTimeField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField provinceField;

    @FXML
    private TextField zipCodeField;

    @FXML
    private TextField totalPriceField;

    @FXML
    private TableView<Orders> tableView;

    @FXML
    private TableColumn<Orders, String> idColumn;

    @FXML
    private TableColumn<Orders, String> customerColumn;

    @FXML
    private TableColumn<Orders, Double> createdAtColumn;

    @FXML
    private TableColumn<Orders, Double> shippingIdColumn;

    @FXML
    private TableColumn<Orders, Double> shippingTimeColumn;

    @FXML
    private TableColumn<Orders, Integer> streetColumn;

    @FXML
    private TableColumn<Orders, Integer> cityColumn;

    @FXML
    private TableColumn<Orders, String> provinceColumn;

    @FXML
    private TableColumn<Orders, String> zipCodeColumn;

    @FXML
    private TableColumn<Orders, String> totalPriceColumn;

    private Stage customerStage;
    private Stage shippingsStage;

    @FXML
    private void insertButton() {
        try {
            transactionsRepository.insertOrder(getSelectedOrder());
            showOrders();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Orders getSelectedOrder() throws SQLException {
        return new Orders(
                idField.getText() == null || idField.getText().isBlank() ? -1 : Long.parseLong(idField.getText()),
                customerIdLabel.getText().isBlank() ? null : contragentsRepository.findCustomerById(Long.parseLong(customerIdLabel.getText().replace("ID:", ""))),
                shippingIdLabel.getText().isBlank() ? -1 : Long.parseLong(shippingIdLabel.getText().replace("ID:", "")),
                shippingTimeField.getValue() == null ? null : Timestamp.valueOf(shippingTimeField.getValue().atStartOfDay()),
                new Address(streetField.getText().isBlank() ? "" : streetField.getText(),
                        cityField.getText().isBlank() ? "" : cityField.getText(),
                        provinceField.getText().isBlank() ? "" : provinceField.getText(),
                        zipCodeField.getText().isBlank() ? "" : zipCodeField.getText()
                ),
                totalPriceField.getText().isBlank() ? 0d : Double.parseDouble(totalPriceField.getText())
        );
    }

    @FXML
    private void updateButton() {
        try {
            transactionsRepository.updateOrders(getSelectedOrder());
            showOrders();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            transactionsRepository.deleteOrder(getSelectedOrder());
            showOrders();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Orders order = tableView.getSelectionModel().getSelectedItem();
        if (order != null) {
            idField.setText(String.valueOf(order.getId()));
            customerIdLabel.setText("ID:" + order.getCustomer().getId());
            createdAtField.setText(String.valueOf(order.getCreatedAt()));
            shippingIdLabel.setText("ID:" + order.getShippingId());
            shippingTimeField.setValue(order.getShippingTime().toLocalDateTime().toLocalDate());
            streetField.setText(String.valueOf(order.getAddress().getStreet()));
            cityField.setText(String.valueOf(order.getAddress().getCity()));
            provinceField.setText(order.getAddress().getProvince());
            zipCodeField.setText(order.getAddress().getZipCode());
            totalPriceField.setText(String.valueOf(order.getTotalPrice()));
        }
    }

    @FXML
    private void selectCustomer() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/customersTable.fxml"));
        Parent root = fxmlLoader.load();
        CustomersTableController customersTableController = fxmlLoader.getController();
        customersTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        customerStage = new Stage();
        customerStage.setScene(new Scene(root));
        customerStage.setTitle("Customers");
        customerStage.initModality(Modality.APPLICATION_MODAL);
        customerStage.initOwner(App.getScene().getWindow());

        customerStage.show();
    }


    @FXML
    private void selectShipping() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/shippingsTable.fxml"));
        Parent root = fxmlLoader.load();
        ShippingsTableController shippingsTableController = fxmlLoader.getController();
        shippingsTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        shippingsStage = new Stage();
        shippingsStage.setScene(new Scene(root));
        shippingsStage.setTitle("Customers");
        shippingsStage.initModality(Modality.APPLICATION_MODAL);
        shippingsStage.initOwner(App.getScene().getWindow());

        shippingsStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showOrders();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Orders> getOrdersList() {
        ObservableList<Orders> orders = FXCollections.observableArrayList();
        orders.addAll(transactionsRepository.findAllOrders());
        return orders;
    }

    public void showOrders() {
        ObservableList<Orders> list = getOrdersList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customerColumn.setCellValueFactory(new PropertyValueFactory<>("customerId"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        shippingIdColumn.setCellValueFactory(new PropertyValueFactory<>("shippingId"));
        shippingTimeColumn.setCellValueFactory(new PropertyValueFactory<>("shippingTime"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));

        tableView.setItems(list);
    }

    public void setCustomerLabel(long id) {
        customerIdLabel.setText("ID:" + id);
    }

    public void setShippingIdLabel(long id) {
        shippingIdLabel.setText("ID:" + id);
    }

    public Stage getCustomerStage() {
        return this.customerStage;
    }

    public Stage getShippingsStage() {
        return this.shippingsStage;
    }
}
