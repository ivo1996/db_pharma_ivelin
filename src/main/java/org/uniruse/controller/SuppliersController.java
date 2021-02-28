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
import org.uniruse.data.contragents.Supplier;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;


/**
 * @author ivelin.dimitrov
 */
public class SuppliersController extends GoBack implements Initializable {

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
    private TableView<Supplier> tableView;

    @FXML
    private TableColumn<Supplier, Integer> idColumn;

    @FXML
    private TableColumn<Supplier, String> phoneColumn;

    @FXML
    private TableColumn<Supplier, String> emailColumn;

    @FXML
    private TableColumn<Supplier, Integer> bankNumColumn;

    @FXML
    private TableColumn<Supplier, Integer> creditCardColumn;

    @FXML
    private TableColumn<Supplier, String> streetColumn;

    @FXML
    private TableColumn<Supplier, String> cityColumn;

    @FXML
    private TableColumn<Supplier, String> provinceColumn;

    @FXML
    private TableColumn<Supplier, String> zipCodeColumn;

    @FXML
    private TableColumn<Supplier, String> companyNameColumn;

    @FXML
    private TableColumn<Supplier, String> vatColumn;

    @FXML
    private void insertButton() {
        try {
            contragentsRepository.insertSupplier(getSelectedSupplier());
            showSuppliers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Supplier getSelectedSupplier() {
        return new Supplier(
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
            contragentsRepository.updateSupplier(getSelectedSupplier());
            showSuppliers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            contragentsRepository.deleteContragent(getSelectedSupplier());
            showSuppliers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Supplier supplier = tableView.getSelectionModel().getSelectedItem();
        if (supplier != null) {
            idField.setText(String.valueOf(supplier.getId()));
            phoneField.setText(supplier.getPhone());
            emailField.setText(supplier.getEmail());
            bankNumField.setText(supplier.getBankNum());
            creditCardField.setText(supplier.getCreditCard());
            streetField.setText(supplier.getStreet());
            cityField.setText(supplier.getCity());
            provinceField.setText(supplier.getProvince());
            zipCodeField.setText(supplier.getZipCode());
            companyNameField.setText(supplier.getCompanyName());
            vatField.setText(supplier.getVat());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showSuppliers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Supplier> getSupplierList() throws Exception {
        ObservableList<Supplier> suppliers = FXCollections.observableArrayList();
        suppliers.addAll(contragentsRepository.findAllSuppliers());
        return suppliers;
    }

    public void showSuppliers() throws Exception {
        ObservableList<Supplier> list = getSupplierList();
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
