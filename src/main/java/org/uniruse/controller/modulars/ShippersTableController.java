package org.uniruse.controller.modulars;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import org.uniruse.controller.ShippingsController;
import org.uniruse.data.contragents.Shipper;
import org.uniruse.repository.ContragentsRepository;
import org.uniruse.util.DBUtil;

import java.sql.SQLException;

/**
 * @author ivelin.dimitrov
 */
public class ShippersTableController {

    private final DBUtil dbUtil = new DBUtil();
    private final ContragentsRepository contragentsRepository = new ContragentsRepository(dbUtil);

    @FXML
    private TableView<Shipper> shippersTableView;

    @FXML
    private TableColumn<Shipper, Integer> shippersIdColumn;

    @FXML
    private TableColumn<Shipper, String> shippersPhoneColumn;

    @FXML
    private TableColumn<Shipper, String> shippersEmailColumn;

    @FXML
    private TableColumn<Shipper, Integer> shippersBankNumColumn;

    @FXML
    private TableColumn<Shipper, Integer> shippersCreditCardColumn;

    @FXML
    private TableColumn<Shipper, String> shippersStreetColumn;

    @FXML
    private TableColumn<Shipper, String> shippersCityColumn;

    @FXML
    private TableColumn<Shipper, String> shippersProvinceColumn;

    @FXML
    private TableColumn<Shipper, String> shippersZipCodeColumn;

    @FXML
    private TableColumn<Shipper, String> shippersCompanyNameColumn;

    @FXML
    private TableColumn<Shipper, String> shippersVatColumn;

    ShippingsController shippingsController;

    public void fillTable(ShippingsController shippingsController) throws SQLException {
        this.shippingsController = shippingsController;
        setShippers();
    }

    private void setShippers() throws SQLException {
        ObservableList<Shipper> shippersList = FXCollections.observableArrayList();
        shippersList.addAll(contragentsRepository.findAllShippers());
        shippersIdColumn.setCellValueFactory(new PropertyValueFactory<>("id"));
        shippersPhoneColumn.setCellValueFactory(new PropertyValueFactory<>("phone"));
        shippersEmailColumn.setCellValueFactory(new PropertyValueFactory<>("email"));
        shippersBankNumColumn.setCellValueFactory(new PropertyValueFactory<>("bankNum"));
        shippersCreditCardColumn.setCellValueFactory(new PropertyValueFactory<>("creditCard"));
        shippersStreetColumn.setCellValueFactory(new PropertyValueFactory<>("street"));
        shippersCityColumn.setCellValueFactory(new PropertyValueFactory<>("city"));
        shippersProvinceColumn.setCellValueFactory(new PropertyValueFactory<>("province"));
        shippersZipCodeColumn.setCellValueFactory(new PropertyValueFactory<>("zipCode"));
        shippersCompanyNameColumn.setCellValueFactory(new PropertyValueFactory<>("companyName"));
        shippersVatColumn.setCellValueFactory(new PropertyValueFactory<>("vat"));

        shippersTableView.setItems(shippersList);
    }

    @FXML
    private void setShipperFields() {
        Shipper customer = shippersTableView.getSelectionModel().getSelectedItem();
        if (customer != null) {
            shippingsController.setLabel(customer.getId());
            shippingsController.getShippersStage().close();
        }
    }

}
