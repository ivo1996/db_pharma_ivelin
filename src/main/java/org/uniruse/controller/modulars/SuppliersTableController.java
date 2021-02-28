package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.SuppliesController;
import org.uniruse.data.contragents.Supplier;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class SuppliersTableController {

    private final DBUtil dbUtil = new DBUtil();
    private final ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private TableView<Supplier> suppliersTableView;

    @FXML
    private TableColumn<Supplier, Integer> suppliersIdColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersPhoneColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersEmailColumn;

    @FXML
    private TableColumn<Supplier, Integer> suppliersBankNumColumn;

    @FXML
    private TableColumn<Supplier, Integer> suppliersCreditCardColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersStreetColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersCityColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersProvinceColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersZipCodeColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersCompanyNameColumn;

    @FXML
    private TableColumn<Supplier, String> suppliersVatColumn;

    SuppliesController suppliesController;

    public void fillTable(SuppliesController suppliesController) throws SQLException {
        this.suppliesController = suppliesController;
        setSuppliers();
    }

    private void setSuppliers() throws SQLException {
        ObservableList<Supplier> suppliersList = FXCollections.observableArrayList();
        suppliersList.addAll(contragentsRepository.findAllSuppliers());
        suppliersIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        suppliersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        suppliersEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        suppliersBankNumColumn.setCellValueFactory(new PropertyValueFactory<>("bankNum"));
        suppliersCreditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        suppliersStreetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        suppliersCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        suppliersProvinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        suppliersZipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        suppliersCompanyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        suppliersVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));

        suppliersTableView.setItems(suppliersList);
    }

    @FXML
    private void setSupplierFields() {
        Supplier customer = suppliersTableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            suppliesController.setLabel(customer.getId());
            suppliesController.getSuppliersStage().close();
        }
    }

}
