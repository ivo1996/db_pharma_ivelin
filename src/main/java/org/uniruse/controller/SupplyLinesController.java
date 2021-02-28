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
import org.uniruse.data.contragents.Customer;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class SupplyLinesController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private TextField idField;

    @FXML
    private TextField titleField;

    @FXML
    private TextField authorField;

    @FXML
    private TextField yearField;

    @FXML
    private TextField pagesField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Customer> tableView;

    @FXML
    private TableColumn<Customer, Integer> idColumn;

    @FXML
    private TableColumn<Customer, String> titleColumn;

    @FXML
    private TableColumn<Customer, String> authorColumn;

    @FXML
    private TableColumn<Customer, Integer> yearColumn;

    @FXML
    private TableColumn<Customer, Integer> pagesColumn;

    @FXML
    private void insertButton() {
        try {
            showCustomers();
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    @FXML
    private void updateButton() {
        try {
            showCustomers();
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            showCustomers();
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showCustomers();
        } catch (SQLException e) {
            showErrorAlert();
        }
    }

    public ObservableList<Customer> getCustomerList() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        customerList.addAll(contragentsRepository.findAllCustomers());
        return customerList;
    }

    public void showCustomers() throws SQLException {
        ObservableList<Customer> list = getCustomerList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        titleColumn.setCellValueFactory(new PropertyValueFactory<>("title"));
        authorColumn.setCellValueFactory(new PropertyValueFactory<>("author"));
        yearColumn.setCellValueFactory(new PropertyValueFactory<>("year"));
        pagesColumn.setCellValueFactory(new PropertyValueFactory<>("pages"));

        tableView.setItems(list);
    }
}
