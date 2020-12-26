package sample;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import javafx.stage.StageStyle;


public class MainWindow extends Application {

    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        try{
            Parent root = FXMLLoader.load(getClass().getResource("Views/MainWindow.fxml"));
            Scene scene = new Scene(root, 1000, 700);
            scene.getStylesheets().add(getClass().getResource("stylesheet/MainWindow.css").toExternalForm());
            primaryStage.initStyle(StageStyle.UNDECORATED);
            primaryStage.setTitle("BLADE Player");
            primaryStage.setScene(scene);
            primaryStage.getIcons().add(new Image("sample/img/play_90px.png"));
            primaryStage.show();
        }
        catch (Exception e){
            System.out.println(e.toString());
        }
    }

    @Override
    public void stop() throws Exception {
        super.stop();
    }
}
