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
import org.uniruse.data.item.Medicine;
import org.uniruse.repository.ItemRepository;
import org.uniruse.util.DBUtil;

import java.net.URL;
import java.util.ResourceBundle;

import static org.uniruse.App.showErrorAlert;

/**
 * @author ivelin.dimitrov
 */
public class MedicineController extends GoBack implements Initializable {

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
    private TextField resipeRequiredField;

    @FXML
    private TextField minAgeField;

    @FXML
    private TextField contraindicationsField;

    @FXML
    private TableView<Medicine> tableView;

    @FXML
    private TableColumn<Medicine, String> skuColumn;

    @FXML
    private TableColumn<Medicine, String> descriptionColumn;

    @FXML
    private TableColumn<Medicine, Double> priceColumn;

    @FXML
    private TableColumn<Medicine, Double> salePriceColumn;

    @FXML
    private TableColumn<Medicine, Double> deletedColumn;

    @FXML
    private TableColumn<Medicine, Integer> resipeRequiredColumn;

    @FXML
    private TableColumn<Medicine, Integer> minAgeColumn;

    @FXML
    private TableColumn<Medicine, String> contraindicationsColumn;

    @FXML
    private void insertButton() {
        try {
            itemRepository.insertMedicine(getSelectedMedicine());
            showMedicine();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    private Medicine getSelectedMedicine() {
        return new Medicine(
                skuField.getText() == null || skuField.getText().isBlank() ? "" : skuField.getText(),
                descriptionField.getText().isBlank() ? "" : descriptionField.getText(),
                priceField.getText().isBlank() ? -1d : Double.parseDouble(priceField.getText() + "d"),
                salePriceField.getText().isBlank() ? -1d : Double.parseDouble(salePriceField.getText() + "d"),
                deletedField.getText().isBlank() ? -1d : Double.parseDouble(deletedField.getText() + "d"),
                resipeRequiredField.getText().isBlank() ? -1 : Integer.parseInt(resipeRequiredField.getText()),
                minAgeField.getText().isBlank() ? -1 : Integer.parseInt(minAgeField.getText()),
                contraindicationsField.getText().isBlank() ? "" : contraindicationsField.getText()
        );
    }

    @FXML
    private void updateButton() {
        try {
            itemRepository.updateMedicine(getSelectedMedicine());
            showMedicine();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void deleteButton() {
        try {
            itemRepository.deleteItem(getSelectedMedicine().getSku());
            showMedicine();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void setFields() {
        Medicine medicine = tableView.getSelectionModel().getSelectedItem();
        if (medicine != null) {
            skuField.setText(medicine.getSku());
            descriptionField.setText(medicine.getDescription());
            priceField.setText(String.valueOf(medicine.getPrice()));
            salePriceField.setText(String.valueOf(medicine.getSalePrice()));
            deletedField.setText(String.valueOf(medicine.getDeleted()));
            resipeRequiredField.setText(String.valueOf(medicine.getResipeRequired()));
            minAgeField.setText(String.valueOf(medicine.getMinAge()));
            contraindicationsField.setText(medicine.getContraindications());
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            showMedicine();
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    public ObservableList<Medicine> getMedicineList() throws Exception {
        ObservableList<Medicine> medicine = FXCollections.observableArrayList();
        medicine.addAll(itemRepository.findAllMedicine());
        return medicine;
    }

    public void showMedicine() throws Exception {
        ObservableList<Medicine> list = getMedicineList();
        skuColumn.setCellValueFactory(new PropertyValueFactory<>("sku"));
        descriptionColumn.setCellValueFactory(new PropertyValueFactory<>("description"));
        priceColumn.setCellValueFactory(new PropertyValueFactory<>("price"));
        salePriceColumn.setCellValueFactory(new PropertyValueFactory<>("salePrice"));
        deletedColumn.setCellValueFactory(new PropertyValueFactory<>("deleted"));
        resipeRequiredColumn.setCellValueFactory(new PropertyValueFactory<>("resipeRequired"));
        minAgeColumn.setCellValueFactory(new PropertyValueFactory<>("minAge"));
        contraindicationsColumn.setCellValueFactory(new PropertyValueFactory<>("contraindications"));

        tableView.setItems(list);
    }
}
