package controller;

/**
 * Jacob Bentivolio Student ID:001252668 WGU-C482
  */


import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.control.*;
import javafx.stage.Stage;
import javafx.scene.Scene;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;


/**
 * AddPartController can change the inventory
 */
public class AddPartController implements Initializable {

    public TextField AddPartIDText;
    public TextField AddPartNameText;
    public TextField AddPartInvText;
    public TextField AddPartPriceCostText;
    public TextField AddPartMaxText;
    public TextField AddPartMachineIDText;
    public TextField AddPartMinText;
    public Button AddPartSaveButton;
    public RadioButton AddPartInHouseRadio;
    public RadioButton AddPartOutsourcedRadio;
    public Label machineOrCompany;


    /**
     * creates parts list
     */
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();

    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
    }


    /**
     * Saves part to part's table
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAddPartSave(ActionEvent actionEvent) throws IOException{
        /**
         * RUNTIME ERROR - java: unreported exception java.io.IOException; must be caught or declared to be thrown
         * add throws IOException
         */
        try {
            /**
             * creates the uniqueID for part organization
             */
            int uniqueID = (int) (Math.random() * 1000);
            /**
             * gets input from textarea
             */
            String name = AddPartNameText.getText();
            double price = Double.parseDouble(AddPartPriceCostText.getText());
            int stock = Integer.parseInt(AddPartInvText.getText());
            int min = Integer.parseInt(AddPartMinText.getText());
            int max = Integer.parseInt(AddPartMaxText.getText());

            /**
             * Min needs to be less than max
             */
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max needs to be larger than Min");
                alert.showAndWait();
                return;
            }
            /**
             * Stock needs to be in the range of Min and Max
             */
            else if (min > stock || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be within range of Min and Max");
                alert.showAndWait();
                return;
            }

            /**
             * Determines what kind of part this is, inhouse or outsourced
             */
            if (AddPartInHouseRadio.isSelected()){
                System.out.println("Inhouse Radio Selected");

                int machineID = Integer.parseInt(AddPartMachineIDText.getText());
                Inhouse addPart = new Inhouse(uniqueID, name, price, stock, min, max, machineID);
                Inventory.addPart(addPart);

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

            /**
             * Determines what kind of part this is, inhouse or outsourced
             */
            else if(AddPartOutsourcedRadio.isSelected()){
                System.out.println("Outsourced Radio Selected");

                String companyName = AddPartMachineIDText.getText();
                Outsourced addPart = new Outsourced(uniqueID, name, price, stock, min, max, companyName);
                Inventory.addPart(addPart);

                Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
                Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
                stage.setScene(new Scene((Parent) scene));
                stage.show();
            }

            /**
             *throws warning when input parameters are invalid
             */
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect Value");
            alert.showAndWait();
            return;
        }
    }

    /**
     * changes the text for selection of radio buttons
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionInhouse(ActionEvent actionEvent) throws IOException {
        machineOrCompany.setText("Machine ID");
    }

    /**
     * changes the text for selection of radio buttons
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionOutsourced(ActionEvent actionEvent) throws IOException{
        machineOrCompany.setText("Company");
    }

    /**
     * resets back to main page when cancel button is pressed
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionCancel(ActionEvent actionEvent) throws IOException{
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
    }
}

