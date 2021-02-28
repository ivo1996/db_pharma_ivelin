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
import org.uniruse.data.item.Supplement;
import org.uniruse.repository.ItemRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class SupplementsController extends GoBack implements Initializable {

    DBUtil dbUtil = new DBUtil();
    ItemRepository itemRepository = new ItemRepository(dbUtil);

    @FXML
    private TextField skuField;

    @FXML
    private TextField descriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField salePriceField;

    @FXML
    private TextField deletedField;

    @FXML
    private TextField usefulForField;

    @FXML
    private Button insertButton;

    @FXML
    private Button updateButton;

    @FXML
    private Button deleteButton;

    @FXML
    private TableView<Supplement> tableView;

    @FXML
    private TableColumn<Supplement, String> skuColumn;

    @FXML
    private TableColumn<Supplement, String> descriptionColumn;

    @FXML
    private TableColumn<Supplement, Double> priceColumn;

    @FXML
    private TableColumn<Supplement, Double> salePriceColumn;

    @FXML
    private TableColumn<Supplement, Double> deletedColumn;

    @FXML
    private TableColumn<Supplement, String> usefulForColumn;

    @FXML
    private void insertButton() {
        try {
            itemRepository.insertSupplement(getSelectedSupplement());
            showSupplement();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Supplement getSelectedSupplement() {
        return new Supplement(
                skuField.getText() == null || skuField.getText().isBlank() ? "" : skuField.getText(),
                descriptionField.getText().isBlank() ? "" : descriptionField.getText(),
                priceField.getText().isBlank() ? -1d : Double.parseDouble(priceField.getText() + "d"),
                salePriceField.getText().isBlank() ? -1d : Double.parseDouble(salePriceField.getText() + "d"),
                deletedField.getText().isBlank() ? -1d : Double.parseDouble(deletedField.getText() + "d"),
                usefulForField.getText().isBlank() ? "" : usefulForField.getText()
        );
    }

    @FXML
    private void updateButton() {
        try {
            itemRepository.updateSupplement(getSelectedSupplement());
            showSupplement();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            itemRepository.deleteItem(getSelectedSupplement().getSku());
            showSupplement();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Supplement medicine = tableView.getSelectionModel().getSelectedItem();
        if (medicine != null) {
            skuField.setText(medicine.getSku());
            descriptionField.setText(medicine.getDescription());
            priceField.setText(String.valueOf(medicine.getPrice()));
            salePriceField.setText(String.valueOf(medicine.getSalePrice()));
            deletedField.setText(String.valueOf(medicine.getDeleted()));
            usefulForField.setText(medicine.getUsefulFor());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showSupplement();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Supplement> getSupplementList() throws Exception {
        ObservableList<Supplement> medicine = FXCollections.observableArrayList();
        medicine.addAll(itemRepository.findAllSupplement());
        return medicine;
    }

    public void showSupplement() throws Exception {
        ObservableList<Supplement> list = getSupplementList();
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        deletedColumn.setCellValueFactory(new PropertyValueFactory<>("deleted"));
        usefulForColumn.setCellValueFactory(new PropertyValueFactory<>("usefulFor"));

        tableView.setItems(list);
    }
}
