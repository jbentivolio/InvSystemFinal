package controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

/**
 * controller for altering the product info and the part data associated with creating a product
 */
public class ModifyProductController implements Initializable {

    public Button modifyProductAddPartButton;
    public Button modifyProductRemoveAssociatedPart;
    public Button modifyProductSave;
    public Button modifyProductCancel;
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    public Pane modifyPartsTablePane;
    public TableView modifyProductTable;
    public TableView modifyAssociatedPartTable;
    public TextField modifyProductInvBox;
    public TextField modifyProductPriceBox;
    public TextField modifyProductMinBox;
    public TextField modifyProductMaxBox;
    public TextField modifyProductTableSearchBox;
    public TextField modifyProductNameBox;
    public TextField modifyProductIDDBox;
    public TableColumn modifyAssocistedPartsToProductIDFinal;
    public TableColumn modifyPartsToProductID;
    public TableColumn modifyPartToProductName;
    public TableColumn modifyPartToProductInv;
    public TableColumn modifyPartToProductPriceCost;
    public TableColumn modifyAssociatedPartToProductNameFinal;
    public TableColumn modifyAssociatedPartToProductInvFinal;
    public TableColumn modifyAssociatedPartToProductPriceCostFinal;

    /**
     * created value for current index
     */
    private int currentIndex = 0;

    /**
     * sets table with values for parts list and associated parts
     * @param url
     * @param resourceBundle
     */
    public void initialize(URL url, ResourceBundle resourceBundle) {
        modifyProductTable.setItems(Inventory.getAllParts());
        modifyPartsToProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyPartToProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyPartToProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyPartToProductPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        modifyAssociatedPartTable.setItems(associatedPartsList);
        modifyAssocistedPartsToProductIDFinal.setCellValueFactory(new PropertyValueFactory<>("id"));
        modifyAssociatedPartToProductNameFinal.setCellValueFactory(new PropertyValueFactory<>("name"));
        modifyAssociatedPartToProductInvFinal.setCellValueFactory(new PropertyValueFactory<>("stock"));
        modifyAssociatedPartToProductPriceCostFinal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * grabs table data of parts that makeup a product table
     * @param selectedIndex
     * @param product
     */
    public void sendProduct(int selectedIndex, Product product) {
        currentIndex = selectedIndex;
        modifyProductIDDBox.setText(String.valueOf(product.getId()));
        modifyProductNameBox.setText(String.valueOf(product.getName()));
        modifyProductInvBox.setText(String.valueOf(product.getStock()));
        modifyProductPriceBox.setText(String.valueOf(product.getPrice()));
        modifyProductMaxBox.setText(String.valueOf(product.getMax()));
        modifyProductMinBox.setText(String.valueOf(product.getMin()));

        for(Part part: product.getAllAssociatedParts()) {
            associatedPartsList.add(part);
        }
    }

    /**
     * Search box for looking up products by Part ID and Part Name
     *FUTURE ENHANCEMENT searching in the text box requires an action of pressing enter, would be nice if it searched key press by key press
     *FUTURE ENHANCEMENT searching requires exact CaseSensitive typing, a lowercase option that returned results would be a great enhancement
     */

    public void onActionModifyProductTableSearchBox(ActionEvent actionEvent) {
        String searchTableText = modifyProductTableSearchBox.getText();
        ObservableList<Part> result = Inventory.lookupPart(searchTableText);
        try {
            while (result.size() == 0) {
                int partID = Integer.parseInt(searchTableText);
                result.add((Part) Inventory.lookupPart(partID));
            }
            modifyProductTable.setItems(result);
        }catch (NumberFormatException e) {
            Alert failedPartSearch = new Alert(Alert.AlertType.ERROR);
            failedPartSearch.setHeaderText("Part Not Found!");
            failedPartSearch.show();
        }
    }

    /**
     * adds selected part from parts list and adds it the associated parts list when creating a product
     * @param actionEvent
     */
    public void onButtonActionModifyProductAddPartButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) modifyProductTable.getSelectionModel().getSelectedItem();
        associatedPartsList.add(selectedPart);
        modifyAssociatedPartTable.setItems(associatedPartsList);
    }

    /**
     * removes a selected part from parts list and adds it back to the associated parts list when altering a product makeup
     * @param actionEvent
     */
    public void onButtonActionModifyRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) modifyAssociatedPartTable.getSelectionModel().getSelectedItem();

        /**
         * throws error if values of inputs are not valid parameters
         */
        if (selectedPart == null) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Input Error");
            alert.setContentText("Select part from list");
            alert.showAndWait();
            return;
        }

        /**
         * prompts message for product removal
         */
        else if (associatedPartsList.contains(selectedPart)) {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle("ATTENTION!");
            alert.setHeaderText("Product will be removed. Are you sure?");
            Optional<ButtonType> result = alert.showAndWait();
            Product.deleteAPar(selectedPart);
            associatedPartsList.remove(selectedPart);
            modifyAssociatedPartTable.setItems(associatedPartsList);
        }
    }

    /**
     * saves current state of parts makeup for a product and submits it to products table
     */
    public void onButtonActionModifyProductSave(ActionEvent actionEvent) throws IOException {
        try {

            int uniqueID = Integer.parseInt(modifyProductIDDBox.getText());
            String name = modifyProductNameBox.getText();
            int stock = Integer.parseInt(modifyProductInvBox.getText());
            double price = Double.parseDouble(modifyProductPriceBox.getText());
            int max = Integer.parseInt(modifyProductMaxBox.getText());
            int min = Integer.parseInt(modifyProductMinBox.getText());

            /**
             * Min needs to be less than max
             */
            if (min > max) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Max needs to be larger than Min");
                alert.showAndWait();
                return;

                /**
                 * Stock needs to be in the range of Min and Max
                 */
            } else if (min > stock || max < stock) {
                Alert alert = new Alert(Alert.AlertType.ERROR, "Inventory must be within range of Min and Max");
                alert.showAndWait();
                return;
            }
            Product updatedProduct = new Product(uniqueID, name, price, stock, min, max);

            /**
             * updates table of products with proper values, no duplicates
             */
            if (updatedProduct != associatedPartsList) {
                Inventory.updateProduct(currentIndex, updatedProduct);
            }
            for (Part part : associatedPartsList) {
                if (part != associatedPartsList)
                    updatedProduct.addAssociatedParts(part);
            }

            /**
             * creates stage and redirects to MainForm.fxml
             */
            Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
            Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            stage.setScene(new Scene((Parent) scene));
            stage.show();

            /**
             * throws error when input is not valid
             */
        } catch (NumberFormatException e) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setContentText("Incorrect Value");
            alert.showAndWait();
            return;
        }
    }

    /**
     * cancels product modification and redirects user to MainForm.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionModifyProductCancel(ActionEvent actionEvent) throws IOException {
            Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Scene scene = new Scene(root);
            stage.setTitle("Home Screen");
            stage.setScene(scene);
            stage.show();
        }
}


