package sample.Controllers;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;
import javafx.scene.input.MouseButton;
import javafx.scene.layout.AnchorPane;

public class PanelSongQueueDetailController {

    @FXML
    AnchorPane pnlSongQueueDetail;
    @FXML
    private Button btnMenuSong;

    @FXML
    private void initialize() {
        ContextMenu contextMenu = new ContextMenu();
        MenuItem removeItem = new MenuItem("Remove from Queue");
        contextMenu.getItems().add(removeItem);

        btnMenuSong.setOnMouseClicked(event -> {
            if (event.getButton() == MouseButton.PRIMARY) {
                contextMenu.show(pnlSongQueueDetail, event.getScreenX(), event.getScreenY());
            }
        });

        removeItem.setOnAction(event -> {

        });
    }
}
