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
import org.uniruse.data.contragents.Customer;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class CustomersController extends GoBack implements Initializable {

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
    private TextField firstNameField;

    @FXML
    private TextField lastNameField;

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> phoneColumn;

    @FXML
    private TableColumn<Customer, String> emailColumn;

    @FXML
    private TableColumn<Customer, Integer> bankNumColumn;

    @FXML
    private TableColumn<Customer, Integer> creditCardColumn;

    @FXML
    private TableColumn<Customer, String> streetColumn;

    @FXML
    private TableColumn<Customer, String> cityColumn;

    @FXML
    private TableColumn<Customer, String> provinceColumn;

    @FXML
    private TableColumn<Customer, String> zipCodeColumn;

    @FXML
    private TableColumn<Customer, String> firstNameColumn;

    @FXML
    private TableColumn<Customer, String> lastNameColumn;

    @FXML
    private void insertButton() {
        try {
            contragentsRepository.insertCustomer(getSelectedCustomer());
            showCustomers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Customer getSelectedCustomer() {
        return new Customer(
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
                firstNameField.getText().isBlank() ? "" : firstNameField.getText(),
                lastNameField.getText().isBlank() ? "" : lastNameField.getText()
        );
    }

    @FXML
    private void updateButton() {
        try {
            contragentsRepository.updateCustomer(getSelectedCustomer());
            showCustomers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            contragentsRepository.deleteContragent(getSelectedCustomer());
            showCustomers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Customer customer = tableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            idField.setText(String.valueOf(customer.getId()));
            phoneField.setText(customer.getPhone());
            emailField.setText(customer.getEmail());
            bankNumField.setText(customer.getBankNum());
            creditCardField.setText(customer.getCreditCard());
            streetField.setText(customer.getStreet());
            cityField.setText(customer.getCity());
            provinceField.setText(customer.getProvince());
            zipCodeField.setText(customer.getZipCode());
            firstNameField.setText(customer.getFirstName());
            lastNameField.setText(customer.getLastName());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showCustomers();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Customer> getCustomerList() throws Exception {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        customerList.addAll(contragentsRepository.findAllCustomers());
        return customerList;
    }

    public void showCustomers() throws Exception {
        ObservableList<Customer> list = getCustomerList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        phoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        emailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        bankNumColumn.setCellValueFactory(new PropertyValueFactory<>("bankNum"));
        creditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        streetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        cityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        provinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        zipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        firstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        lastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        tableView.setItems(list);
    }
}
