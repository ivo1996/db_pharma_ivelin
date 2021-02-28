package org.uniruse.controller;

import javafx.fxml.FXML;
import org.uniruse.App;

import java.io.IOException;

import static org.uniruse.App.showErrorAlert;

public class PrimaryController {

    @FXML
    private void switchToCustomers() throws IOException {
        try {
            App.setRoot("customersPane");
            App.setTitle("Customers");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToShippers() throws IOException {
        try {
            App.setRoot("shippersPane");
            App.setTitle("Shippers");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSuppliers() throws IOException {
        try {
            App.setRoot("suppliersPane");
            App.setTitle("Suppliers");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToMedicine() throws IOException {
        try {
            App.setRoot("medicinePane");
            App.setTitle("Medicine");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSupplements() throws IOException {
        try {
            App.setRoot("supplementsPane");
            App.setTitle("Supplements");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToOrders() throws IOException {
        try {
            App.setRoot("ordersPane");
            App.setTitle("Orders");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSupplies() throws IOException {
        try {
            App.setRoot("suppliesPane");
            App.setTitle("Supplies");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToShippings() throws IOException {
        try {
            App.setRoot("shippingsPane");
            App.setTitle("Shippings");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToOrderLines() throws IOException {
        try {
            App.setRoot("orderLinesPane");
            App.setTitle("Order lines");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }

    @FXML
    private void switchToSupplyLines() throws IOException {
        try {
            App.setRoot("supplyLinesPane");
            App.setTitle("Supply lines");
        } catch (Exception e) {
            showErrorAlert();
            e.printStackTrace();
        }
    }
}
