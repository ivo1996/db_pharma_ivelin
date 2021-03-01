package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.SupplyLinesController;
import org.uniruse.data.transactions.Supply;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class SuppliesTableController {

    DBUtil dbUtil = new DBUtil();
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);

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

    SupplyLinesController supplyLinesController;

    @FXML
    public void setSupplyFields() throws SQLException {
        Supply supply = tableView.getSelectionModel().getSelectedItem();
        if (supply != null) {
            supplyLinesController.setSupplyIdLabel(supply.getId());
            supplyLinesController.getSupplyStage().close();
        }
    }

    public void setItems() throws SQLException {
        ObservableList<Supply> supplies = FXCollections.observableArrayList();
        supplies.addAll(transactionsRepository.findAllSupplies());
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        suppliedAtColumn.setCellValueFactory(new PropertyValueFactory<>("suppliedAt"));
        totalPriceColumn.setCellValueFactory(new PropertyValueFactory<>("totalPrice"));
        supplierColumn.setCellValueFactory(new PropertyValueFactory<>("supplierId"));

        tableView.setItems(supplies);
    }

    public void fillTable(SupplyLinesController supplyLinesController) throws SQLException {
        this.supplyLinesController = supplyLinesController;
        setItems();
    }

}
