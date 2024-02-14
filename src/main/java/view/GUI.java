package view;

import controller.Controller;
import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;

public class GUI extends Application {

    Controller Crontroller;

    @Override
    public void init() {
        Crontroller = new Controller(this);
    }

    @Override
    public void start(Stage stage){

        GridPane gridPane = new GridPane();
        Scene view = new Scene(gridPane, 400, 400);
        stage.setTitle("view.FPGUI");
        stage.setScene(view);
        stage.show();
    }
}
