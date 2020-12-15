package sample.Controllers;

import javafx.event.ActionEvent;

public class PanelChartController {
    private PanelHomeController panelHomeController;

    public void setParentController(PanelHomeController _panelHomeController){
        this.panelHomeController = _panelHomeController;
    }

    public void btnReturn_Clicked(ActionEvent actionEvent) {
        if (panelHomeController != null){
            panelHomeController.returnHome();
        }
    }

}
