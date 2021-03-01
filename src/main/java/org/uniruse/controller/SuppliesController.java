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
import org.uniruse.controller.modulars.SuppliersTableController;
import org.uniruse.data.transactions.Supply;
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
public class SuppliesController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);
    ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private Label supplierIdLabel;

    @FXML
    private TextField idField;

    @FXML
    private DatePicker suppliedAtField;

    @FXML
    private TextField totalPriceField;

    @FXML
    private DatePicker supplierField;


    @FXML
    private TableView<Supply> tableView;

    @FXML
    private TableColumn<Supply, String> idColumn;

    @FXML
    private TableColumn<Supply, Double> suppliedAtColumn;

    @FXML
    private TableColumn<Supply, Double> totalPriceColumn;

    @FXML
    private TableColumn<Supply, String> supplierColumn;

    private Stage suppliersStage;

    @FXML
    private void insertButton() {
        try {
            transactionsRepository.insertSupply(getSelectedSupply());
            showSupplies();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Supply getSelectedSupply() throws SQLException {
        return new Supply(
                idField.getText() == null || idField.getText().isBlank() ? -1 : Long.parseLong(idField.getText()),
                suppliedAtField.getValue() == null ? null : Timestamp.valueOf(suppliedAtField.getValue().atStartOfDay()),
                totalPriceField.getText().isBlank() ? 0d : Double.parseDouble(totalPriceField.getText()),
                supplierIdLabel.getText().equals("ID:") ? null : contragentsRepository.findSupplierById(Long.parseLong(supplierIdLabel.getText().replace("ID:", "")))
        );
    }

    @FXML
    private void updateButton() {
        try {
            transactionsRepository.updateSupply(getSelectedSupply());
            showSupplies();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            transactionsRepository.deleteSupply(getSelectedSupply());
            showSupplies();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Supply supply = tableView.getSelectionModel().getSelectedItem();
        if (supply != null) {
            idField.setText(String.valueOf(supply.getId()));
            suppliedAtField.setValue(supply.getSuppliedAt().toLocalDateTime().toLocalDate());
            totalPriceField.setText(String.valueOf(supply.getTotalPrice()));
            supplierIdLabel.setText("ID:" + supply.getSupplier().getId());
        }
    }

    @FXML
    private void selectShipper() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/suppliersTable.fxml"));
        Parent root = fxmlLoader.load();
        SuppliersTableController suppliersTableController = fxmlLoader.getController();
        suppliersTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        suppliersStage = new Stage();
        suppliersStage.setScene(new Scene(root));
        suppliersStage.setTitle("Shippers");
        suppliersStage.initModality(Modality.APPLICATION_MODAL);
        suppliersStage.initOwner(App.getScene().getWindow());

        suppliersStage.show();
    }


    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showSupplies();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Supply> getSupplyList() {
        ObservableList<Supply> supplies = FXCollections.observableArrayList();
        supplies.addAll(transactionsRepository.findAllSupplies());
        return supplies;
    }

    public void showSupplies() {
        ObservableList<Supply> list = getSupplyList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        suppliedAtColumn.setCellValueFactory(new PropertyValueFactory<>("suppliedAt"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        tableView.setItems(list);
    }

    public void setLabel(long id) {
        supplierIdLabel.setText("ID:" + id);
    }

    public Stage getSuppliersStage() {
        return this.suppliersStage;
    }
}
