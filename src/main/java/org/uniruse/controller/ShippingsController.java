package org.uniruse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
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
import org.uniruse.controller.modulars.ShippersTableController;
import org.uniruse.data.transactions.Shippings;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class ShippingsController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);
    ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private Label shipperIdLabel;

    @FXML
    private TextField idField;

    @FXML
    private TextField createdAtField;

    @FXML
    private TextField districtField;

    @FXML
    private DatePicker shippDateField;

    @FXML
    private DatePicker deliveryDateField;

    @FXML
    private TableView<Shippings> tableView;

    @FXML
    private TableColumn<Shippings, String> idColumn;

    @FXML
    private TableColumn<Shippings, Double> createdAtColumn;

    @FXML
    private TableColumn<Shippings, Double> districtColumn;

    @FXML
    private TableColumn<Shippings, String> shipperColumn;

    @FXML
    private TableColumn<Shippings, Timestamp> shippDateColumn;

    @FXML
    private TableColumn<Shippings, Timestamp> deliveryDateColumn;

    private Stage shippersStage;

    @FXML
    private void insertButton() {
        try {
            transactionsRepository.insertShipping(getSelectedShipping());
            showShippings();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Shippings getSelectedShipping() throws SQLException {
        return new Shippings(
                idField.getText() == null || idField.getText().isBlank() ? -1 : Long.parseLong(idField.getText()),
                districtField.getText().isBlank() ? "" : districtField.getText(),
                shipperIdLabel.getText().isBlank() ? null : contragentsRepository.findShipperById(Long.parseLong(shipperIdLabel.getText().replace("ID:", ""))),
                shippDateField.getValue() == null ? null : Timestamp.valueOf(shippDateField.getValue().atStartOfDay()),
                deliveryDateField.getValue() == null ? null : Timestamp.valueOf(deliveryDateField.getValue().atStartOfDay())
        );
    }

    @FXML
    private void updateButton() {
        try {
            transactionsRepository.updateShipping(getSelectedShipping());
            showShippings();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            transactionsRepository.deleteShipping(getSelectedShipping());
            showShippings();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Shippings shipping = tableView.getSelectionModel().getSelectedItem();
        if (shipping != null) {
            idField.setText(String.valueOf(shipping.getId()));
            createdAtField.setText(String.valueOf(shipping.getCreatedAt()));
            districtField.setText(shipping.getDistrict());
            shipperIdLabel.setText("ID:" + shipping.getShipper().getId());
            shippDateField.setValue(shipping.getShippDate().toLocalDateTime().toLocalDate());
            deliveryDateField.setValue(shipping.getDeliveryDate().toLocalDateTime().toLocalDate());
        }
    }

    @FXML
    private void selectShipper() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/shippersTable.fxml"));
        Parent root = fxmlLoader.load();
        ShippersTableController shippersTableController = fxmlLoader.getController();
        shippersTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        shippersStage = new Stage();
        shippersStage.setScene(new Scene(root));
        shippersStage.setTitle("Shippers");
        shippersStage.initModality(Modality.APPLICATION_MODAL);
        shippersStage.initOwner(App.getScene().getWindow());

        shippersStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showShippings();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Shippings> getShippingsList() {
        ObservableList<Shippings> orders = FXCollections.observableArrayList();
        orders.addAll(transactionsRepository.findAllShippings());
        return orders;
    }

    public void showShippings() {
        ObservableList<Shippings> list = getShippingsList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        createdAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        districtColumn.setCellValueFactory(new PropertyValueFactory<>("district"));
        shipperColumn.setCellValueFactory(new PropertyValueFactory<>("shipperId"));
        shippDateColumn.setCellValueFactory(new PropertyValueFactory<>("shippDate"));
        deliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        tableView.setItems(list);
    }

    public void setLabel(long id) {
        shipperIdLabel.setText("ID:" + id);
    }

    public Stage getShippersStage() {
        return this.shippersStage;
    }
}
