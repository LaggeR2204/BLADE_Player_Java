package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.layout.FlowPane;
import sample.model.Library;


public class PanelPlaylistController {

    private Library currentLibrary;

    @FXML
    FlowPane fpnlListPL;

    @FXML
    private void initialize() {
        currentLibrary = Library.getInstance();
    }
}
