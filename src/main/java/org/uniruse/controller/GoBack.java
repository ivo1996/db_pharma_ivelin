package org.uniruse.controller;

import javafx.fxml.FXML;
import org.uniruse.App;

import java.io.IOException;

/**
 * @author ivelin.dimitrov
 */
public class GoBack {

    @FXML
    private void switchToPrimary() throws IOException {
        App.setRoot("primary");
        App.setTitle("Primary");
    }
}
