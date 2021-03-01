package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.OrdersController;
import org.uniruse.data.transactions.Shippings;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.sql.Timestamp;

/**
 * @author ivelin.dimitrov
 */
public class ShippingsTableController {

    private final DBUtil dbUtil = new DBUtil();
    private final TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);

    @FXML
    private TableView<Shippings> shippingsTableView;

    @FXML
    private TableColumn<Shippings, String> shippingsIdColumn;

    @FXML
    private TableColumn<Shippings, Double> shippingsCreatedAtColumn;

    @FXML
    private TableColumn<Shippings, Double> shippingsDistrictColumn;

    @FXML
    private TableColumn<Shippings, String> shippingsShipperColumn;

    @FXML
    private TableColumn<Shippings, Timestamp> shippingsShippDateColumn;

    @FXML
    private TableColumn<Shippings, Timestamp> shippingsDeliveryDateColumn;

    OrdersController ordersController;

    public void fillTable(OrdersController ordersController) {
        this.ordersController = ordersController;
        setShippingss();
    }

    private void setShippingss() {
        ObservableList<Shippings> shippingsList = FXCollections.observableArrayList();
        shippingsList.addAll(transactionsRepository.findAllShippings());
        shippingsIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shippingsCreatedAtColumn.setCellValueFactory(new PropertyValueFactory<>("createdAt"));
        shippingsDistrictColumn.setCellValueFactory(new PropertyValueFactory<>("district"));
        shippingsShipperColumn.setCellValueFactory(new PropertyValueFactory<>("shipperId"));
        shippingsShippDateColumn.setCellValueFactory(new PropertyValueFactory<>("shippDate"));
        shippingsDeliveryDateColumn.setCellValueFactory(new PropertyValueFactory<>("deliveryDate"));

        shippingsTableView.setItems(shippingsList);
    }

    @FXML
    private void setShippingFields() {
        Shippings shipping = shippingsTableView.getSelectionModel().getSelectedItem();
        if (shipping != null) {
            ordersController.setShippingIdLabel(shipping.getId());
            ordersController.getShippingsStage().close();
        }
    }

}
