package main;

/**
 * Jacob Bentivolio Student ID:001252668 WGU-C482
 *
 * Javadoc = /BentivolioWGUC482/Javadocs
 */

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 * Main class creates application with no sample data
 */
public class Main extends Application {

    /**
     * start method initiates FXML file MainForm.fxml
     * @param stage
     * @throws Exception
     */
    @Override
    public void start(Stage stage) throws Exception {

        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setTitle("Home Screen");
        stage.setScene(new Scene(root));
        stage.show();
    }

    /**
     *main method starts application
     * @param args
     */
    public static void main(String[] args){
        launch();
    }
}
