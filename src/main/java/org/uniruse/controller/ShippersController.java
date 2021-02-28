package org.uniruse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.data.contragents.Address;
import org.uniruse.data.contragents.Shipper;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class ShippersController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private TextField idField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField emailField;

    @FXML
    private TextField bankNumField;

    @FXML
    private TextField creditCardField;

    @FXML
    private TextField streetField;

    @FXML
    private TextField cityField;

    @FXML
    private TextField provinceField;

    @FXML
    private TextField zipCodeField;

    @FXML
    private TextField companyNameField;

    @FXML
    private TextField vatField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Shipper> tableView;

    @FXML
    private TableColumn<Shipper, Integer> idColumn;

    @FXML
    private TableColumn<Shipper, String> phoneColumn;

    @FXML
    private TableColumn<Shipper, String> emailColumn;

    @FXML
    private TableColumn<Shipper, Integer> bankNumColumn;

    @FXML
    private TableColumn<Shipper, Integer> creditCardColumn;

    @FXML
    private TableColumn<Shipper, String> streetColumn;

    @FXML
    private TableColumn<Shipper, String> cityColumn;

    @FXML
    private TableColumn<Shipper, String> provinceColumn;

    @FXML
    private TableColumn<Shipper, String> zipCodeColumn;

    @FXML
    private TableColumn<Shipper, String> companyNameColumn;

    @FXML
    private TableColumn<Shipper, String> vatColumn;

    @FXML
    private void insertButton() {
        try {
            contragentsRepository.insertShipper(getSelectedShipper());
            showShippers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Shipper getSelectedShipper() {
        return new Shipper(
                idField.getText() == null || idField.getText().isBlank() ? -1 : Integer.parseInt(idField.getText()),
                phoneField.getText().isBlank() ? "" : phoneField.getText(),
                emailField.getText().isBlank() ? "" : emailField.getText(),
                bankNumField.getText().isBlank() ? "" : bankNumField.getText(),
                creditCardField.getText().isBlank() ? "" : creditCardField.getText(),
                new Address(
                        streetField.getText().isBlank() ? "" : streetField.getText(),
                        cityField.getText().isBlank() ? "" : cityField.getText(),
                        provinceField.getText().isBlank() ? "" : provinceField.getText(),
                        zipCodeField.getText().isBlank() ? "" : zipCodeField.getText()
                ),
                companyNameField.getText().isBlank() ? "" : companyNameField.getText(),
                vatField.getText().isBlank() ? "" : vatField.getText()
        );
    }

    @FXML
    private void updateButton() {
        try {
            contragentsRepository.updateShipper(getSelectedShipper());
            showShippers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            contragentsRepository.deleteContragent(getSelectedShipper());
            showShippers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Shipper shipper = tableView.getSelectionModel().getSelectedItem();
        if (shipper != null) {
            idField.setText(String.valueOf(shipper.getId()));
            phoneField.setText(shipper.getPhone());
            emailField.setText(shipper.getEmail());
            bankNumField.setText(shipper.getBankNum());
            creditCardField.setText(shipper.getCreditCard());
            streetField.setText(shipper.getStreet());
            cityField.setText(shipper.getCity());
            provinceField.setText(shipper.getProvince());
            zipCodeField.setText(shipper.getZipCode());
            companyNameField.setText(shipper.getCompanyName());
            vatField.setText(shipper.getVat());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showShippers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Shipper> getShipperList() throws Exception {
        ObservableList<Shipper> shippers = FXCollections.observableArrayList();
        shippers.addAll(contragentsRepository.findAllShippers());
        return shippers;
    }

    public void showShippers() throws Exception {
        ObservableList<Shipper> list = getShipperList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        bankNumColumn.setCellValueFactory(new PropertyValueFactory<>("bankNum"));
        creditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        companyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        vatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));

        tableView.setItems(list);
    }
}
