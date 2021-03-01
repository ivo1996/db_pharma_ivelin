package org.uniruse.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.uniruse.App;
import org.uniruse.controller.modulars.ItemsTableController;
import org.uniruse.controller.modulars.SuppliesTableController;
import org.uniruse.data.item.Item;
import org.uniruse.data.transactions.Supply;
import org.uniruse.data.transactions.SupplyLine;
import org.uniruse.repository.ItemRepository;
import org.uniruse.repository.TransactionsRepository;
import org.uniruse.util.DBUtil;

import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.concurrent.atomic.AtomicReference;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class SupplyLinesController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    ItemRepository itemRepository = new ItemRepository(dbUtil);
    TransactionsRepository transactionsRepository = new TransactionsRepository(dbUtil);

    @FXML
    private TextField idField;

    @FXML
    private TextField quantityField;

    @FXML
    private TableView<SupplyLine> tableView;

    @FXML
    private TableColumn<SupplyLine, Integer> idColumn;

    @FXML
    private TableColumn<SupplyLine, String> supplyColumn;

    @FXML
    private TableColumn<SupplyLine, String> itemColumn;

    @FXML
    private TableColumn<SupplyLine, Integer> quantityColumn;

    @FXML
    private TableColumn<SupplyLine, Integer> priceColumn;

    @FXML
    private Label supplyIdLabel;

    @FXML
    private Label itemIdLabel;

    private Stage supplyStage;
    private Stage itemsStage;

    @FXML
    private void insertButton() {
        try {
            SupplyLine line = getSelectedSupplyLine();
            transactionsRepository.insertSupplyLine(line);
            Supply supply = transactionsRepository.findSupplyById(line.getSupplyId());
            AtomicReference<Double> totalPrice = new AtomicReference<>(0d);
            System.out.println(supply.getChilds().size());
            supply.getChilds().forEach(it -> totalPrice.updateAndGet(v -> v + it.getPrice()));
            supply.setTotalPrice(totalPrice.get());
            transactionsRepository.updateSupply(supply);
            showSupplyLines();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private SupplyLine getSelectedSupplyLine() throws SQLException {
        try {
            Item item;
            if (itemRepository.findSupplementById(itemIdLabel.getText().replace("ID:", "")) == null) {
                item = itemRepository.findMedicineById(itemIdLabel.getText().replace("ID:", ""));
            } else {
                item = itemRepository.findSupplementById(itemIdLabel.getText().replace("ID:", ""));
            }
            return new SupplyLine(
                    idField.getText() == null || idField.getText().isBlank() ? -1 : Long.parseLong(idField.getText()),
                    supplyIdLabel.getText().equals("ID:") ? -1 : Long.parseLong(supplyIdLabel.getText().replace("ID:", "")),
                    itemIdLabel.getText().equals("ID:") ? null : item,
                    quantityField.getText().isBlank() ? 0 : Integer.parseInt(quantityField.getText())
            );
        } catch (Exception ignored) {
        }
        return null;
    }

    @FXML
    private void setFields() {
        SupplyLine supplyLine = tableView.getSelectionModel().getSelectedItem();
        if (supplyLine != null) {
            idField.setText(String.valueOf(supplyLine.getId()));
            supplyIdLabel.setText("ID:" + supplyLine.getSupplyId());
            itemIdLabel.setText("ID:" + supplyLine.getItemSku());
            quantityField.setText(String.valueOf(supplyLine.getQuantity()));
        }
    }

    @FXML
    private void selectSupplies() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/suppliesTable.fxml"));
        Parent root = fxmlLoader.load();
        SuppliesTableController suppliesTableController = fxmlLoader.getController();
        suppliesTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        supplyStage = new Stage();
        supplyStage.setScene(new Scene(root));
        supplyStage.setTitle("Supplies");
        supplyStage.initModality(Modality.APPLICATION_MODAL);
        supplyStage.initOwner(App.getScene().getWindow());

        supplyStage.show();
    }


    @FXML
    private void selectItem() throws IOException, SQLException {
        FXMLLoader fxmlLoader = new FXMLLoader(App.class.getResource("modulars/itemsTable.fxml"));
        Parent root = fxmlLoader.load();
        ItemsTableController itemsTableController = fxmlLoader.getController();
        itemsTableController.fillTable(this);
        root.prefHeight(310.0);
        root.prefWidth(850.0);
        itemsStage = new Stage();
        itemsStage.setScene(new Scene(root));
        itemsStage.setTitle("Items");
        itemsStage.initModality(Modality.APPLICATION_MODAL);
        itemsStage.initOwner(App.getScene().getWindow());

        itemsStage.show();
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showSupplyLines();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<SupplyLine> getSupplyLineList() {
        ObservableList<SupplyLine> supplyLines = FXCollections.observableArrayList();
        List<Supply> supplies = transactionsRepository.findAllSupplies();
        supplies.forEach(it ->
                supplyLines.addAll(it.getChilds().toArray(new SupplyLine[0]))
        );
        return supplyLines;
    }

    public void showSupplyLines() {
        ObservableList<SupplyLine> list = getSupplyLineList();
        idColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        supplyColumn.setCellValueFactory(new PropertyValueFactory<>("supplyId"));
        itemColumn.setCellValueFactory(new PropertyValueFactory<>("itemSku"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));

        tableView.setItems(list);
    }

    public void setSupplyIdLabel(long id) {
        supplyIdLabel.setText("ID:" + id);
    }

    public void setItemIdLabel(String sku) {
        itemIdLabel.setText("ID:" + sku);
    }

    public Stage getSupplyStage() {
        return this.supplyStage;
    }

    public Stage getItemsStage() {
        return this.itemsStage;
    }
}
