package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.OrderLinesController;
import org.uniruse.controller.SupplyLinesController;
import org.uniruse.data.item.Item;
import org.uniruse.repository.ItemRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class ItemsTableController {

    DBUtil dbUtil = new DBUtil();
    ItemRepository itemRepository = new ItemRepository(dbUtil);

    @FXML
    private TableView<Item> tableView;

    @FXML
    private TableColumn<Item, String> skuColumn;

    @FXML
    private TableColumn<Item, String> descriptionColumn;

    @FXML
    private TableColumn<Item, Double> priceColumn;

    @FXML
    private TableColumn<Item, Double> salePriceColumn;

    @FXML
    private TableColumn<Item, Double> deletedColumn;

    OrderLinesController orderLinesController;
    SupplyLinesController supplyLinesController;

    @FXML
    public void setItemFields() throws SQLException {
        Item item = tableView.getSelectionModel().getSelectedItem();
        if (item != null) {
            if (orderLinesController != null) {
                orderLinesController.setItemIdLabel(item.getSku());
                orderLinesController.getItemsStage().close();
            }
            if (supplyLinesController != null) {
                supplyLinesController.setItemIdLabel(item.getSku());
                supplyLinesController.getItemsStage().close();
            }
        }
    }

    public void setItems() throws SQLException {
        ObservableList<Item> medicine = FXCollections.observableArrayList();
        ObservableList<Item> supplements = FXCollections.observableArrayList();
        medicine.addAll(itemRepository.findAllMedicine());
        supplements.addAll(itemRepository.findAllSupplement());
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        deletedColumn.setCellValueFactory(new PropertyValueFactory<>("deleted"));

        supplements.addAll(medicine);
        tableView.setItems(supplements);
    }

    public void fillTable(OrderLinesController orderLinesController) throws SQLException {
        this.orderLinesController = orderLinesController;
        setItems();
    }

    public void fillTable(SupplyLinesController supplyLinesController) throws SQLException {
        this.supplyLinesController = supplyLinesController;
        setItems();
    }

}
