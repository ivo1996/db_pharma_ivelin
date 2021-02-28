package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.OrdersController;
import org.uniruse.data.contragents.Customer;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class CustomersTableController {

    private final DBUtil dbUtil = new DBUtil();
    private final ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private TableView<Customer> customersTableView;

    @FXML
    private TableColumn<Customer, Integer> customersIdColumn;

    @FXML
    private TableColumn<Customer, String> customersPhoneColumn;

    @FXML
    private TableColumn<Customer, String> customersEmailColumn;

    @FXML
    private TableColumn<Customer, Integer> customersBankNumColumn;

    @FXML
    private TableColumn<Customer, Integer> customersCreditCardColumn;

    @FXML
    private TableColumn<Customer, String> customersStreetColumn;

    @FXML
    private TableColumn<Customer, String> customersCityColumn;

    @FXML
    private TableColumn<Customer, String> customersProvinceColumn;

    @FXML
    private TableColumn<Customer, String> customersZipCodeColumn;

    @FXML
    private TableColumn<Customer, String> customersFirstNameColumn;

    @FXML
    private TableColumn<Customer, String> customersLastNameColumn;

    OrdersController ordersController;

    public void fillTable(OrdersController ordersController) throws SQLException {
        this.ordersController = ordersController;
        setCustomers();
    }

    private void setCustomers() throws SQLException {
        ObservableList<Customer> customerList = FXCollections.observableArrayList();
        customerList.addAll(contragentsRepository.findAllCustomers());
        customersIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        customersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        customersEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        customersBankNumColumn.setCellValueFactory(new PropertyValueFactory<>("bankNum"));
        customersCreditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        customersStreetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        customersCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        customersProvinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        customersZipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        customersFirstNameColumn.setCellValueFactory(new PropertyValueFactory<>("firstName"));
        customersLastNameColumn.setCellValueFactory(new PropertyValueFactory<>("lastName"));

        customersTableView.setItems(customerList);
    }

    @FXML
    private void setCustomerFields() {
        Customer customer = customersTableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            ordersController.setCustomerLabel(customer.getId());
            ordersController.getCustomerStage().close();
        }
    }

}
