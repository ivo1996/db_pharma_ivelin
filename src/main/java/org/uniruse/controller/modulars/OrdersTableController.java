package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.OrderLinesController;
import org.uniruse.data.transactions.Orders;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class OrdersTableController {

    DBUtil dbUtil = new DBUtil();
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);

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

    OrderLinesController orderLinesController;

    @FXML
    public void setOrderFields() throws SQLException {
        Orders orders = tableView.getSelectionModel().getSelectedItem();
        if (orders != null) {
            orderLinesController.setOrderIdLabel(orders.getId());
            orderLinesController.getOrderStage().close();
        }
    }

    public void setItems() throws SQLException {
        ObservableList<Orders> orders = FXCollections.observableArrayList();
        orders.addAll(transactionsRepository.findAllOrders());
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

        tableView.setItems(orders);
    }

    public void fillTable(OrderLinesController orderLinesController) throws SQLException {
        this.orderLinesController = orderLinesController;
        setItems();
    }
}
