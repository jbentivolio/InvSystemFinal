package controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import model.Inhouse;
import model.Inventory;
import model.Outsourced;
import model.Part;

import java.io.IOException;

/**
 * controller for the altering of part data
 */
public class ModifyPartsController {
    public Button modifyPartCancelButton;
    public RadioButton modifyPartInHouseRadio;
    public RadioButton modifyPartOutsourcedRadio;
    public TextField modifyPartIDText;
    public TextField modifyPartNameText;
    public TextField modifyPartInvText;
    public TextField modifyPartMaxText;
    public TextField modifyPartPriceCostText;
    public TextField modifyPartMachineIDText;
    public TextField modifyPartMinText;
    public Button modifyPartSaveButton;
    public Label modifyPartMachineOrCompanyLabel;

    /**
     * sets index to first value
     */
    private int currentIndex = 0;

    /**
     * cancels selection and redirects user back to MainForm.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionModifyCancelButton(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
    }

    /**
     * creates table update with user text and selection input
     * @param selectedIndex
     * @param selectedPart
     */
    public void sendPart(int selectedIndex, Part selectedPart) {
        currentIndex = selectedIndex;

        if (selectedPart instanceof Inhouse) {
            modifyPartInHouseRadio.setSelected(true);
            modifyPartMachineIDText.setText(String.valueOf(((Inhouse) selectedPart).getMachineID()));
        }
        else {
            modifyPartOutsourcedRadio.setSelected(true);
            modifyPartMachineIDText.setText(((Outsourced) selectedPart).getCompanyName());

        }
        modifyPartIDText.setText(String.valueOf(selectedPart.getId()));
        modifyPartNameText.setText(String.valueOf(selectedPart.getName()));
        modifyPartInvText.setText(String.valueOf(selectedPart.getStock()));
        modifyPartPriceCostText.setText(String.valueOf(selectedPart.getStock()));
        modifyPartMaxText.setText(String.valueOf(selectedPart.getMax()));
        modifyPartMinText.setText(String.valueOf(selectedPart.getMin()));
    }

    /**
     * changes value to Machine ID if Inhouse radio is selected
     * @param actionEvent
     */
    public void onRadioActionInhouse(ActionEvent actionEvent) {
        modifyPartMachineOrCompanyLabel.setText("Machine ID");
        /**
         * used to change between "Machine ID" and "Company" when selecting radio button options
         */
    }

    /**
     * changes value to Company if outsources radio is selected
     * @param actionEvent
     */
    public void onRadioActionOutsourced(ActionEvent actionEvent) {
        modifyPartMachineOrCompanyLabel.setText("Company");
    }

    /**
     * saves values of user submitted data, while checking min/max values to determine if values are valid
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionModifyPartSave(ActionEvent actionEvent) throws IOException {
        try {
            int uniqueID = Integer.parseInt(modifyPartIDText.getText());
            String name = modifyPartNameText.getText();
            int stock = Integer.parseInt(modifyPartInvText.getText());
            double price = Double.parseDouble(modifyPartPriceCostText.getText());
            int min = Integer.parseInt(modifyPartMinText.getText());
            int max = Integer.parseInt(modifyPartMaxText.getText());

            /**
             * min needs to be smaller than the max value
             */
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max needs to be larger than Min");
                alert.showAndWait();
                return;

                /**
                 * stock value needs to be in the range of min and max values
                 */
            } else if (min > stock || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be within range of Min and Max");
                alert.showAndWait();
                return;
            }
            /**
             * determines what type of part types are saved (inhouse vs outsourced) based on the user's selection of radio buttons
             */
            if (modifyPartInHouseRadio.isSelected()) {
                System.out.println("Inhouse Radio Selected");

                int machineID = Integer.parseInt(modifyPartMachineIDText.getText());
                Inhouse updatedPart = new Inhouse(uniqueID, name, price, stock, min, max, machineID);
                Inventory.updatePart(currentIndex, updatedPart);
            }

            /**
             * determines what type of part types are saved (inhouse vs outsourced) based on the user's selection of radio buttons
             */
            if (modifyPartOutsourcedRadio.isSelected()) {
                System.out.println("Outsourced Radio Selected");

                String companyName = modifyPartMachineIDText.getText();
                Outsourced updatedPart = new Outsourced(uniqueID, name, price, stock, min, max, companyName);
                Inventory.updatePart(currentIndex, updatedPart);
            }

            /**
             * sets stage and redirects user after saving to MainForm.fxml
             */
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

            /**
             * throws message when input parameters are not valid values
             */
        }catch (NumberFormatException e){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect Value");
            alert.showAndWait();
            return;
        }
    }
}

