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
import javafx.stage.Stage;
import model.Inventory;
import model.Part;
import model.Product;

import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 *AddProductController changes the product inventory
 */
public class AddProductController implements Initializable {

    public TextField addProductIDBox;
    public TextField addProductInvBox;
    public TextField addProductPriceBox;
    public TextField addProductMaxBox;
    public TextField addProductMinBox;
    private ObservableList<Part> associatedPartsList = FXCollections.observableArrayList();
    public TextField addProductTableSearchBox;
    public Button addProductAddPartButton;
    public Button addProductCancel;
    public Button addProductSave;
    public Button addProductRemoveAssociatedPart;
    public TableColumn addPartsToProductID;
    public TableColumn addPartToProductName;
    public TableColumn addPartToProductInv;
    public TableColumn addPartToProductPriceCost;
    public TableView addProductTable;
    public TableView addAssociatedPartTable;
    public TableColumn addAssocistedPartsToProductIDFinal;
    public TableColumn addAssociatedPartToProductNameFinal;
    public TableColumn addAssociatedPartToProductInvFinal;
    public TableColumn addAssociatedPartToProductPriceCostFinal;


    /**
     * Initializes product and part info to associated tables for viewer
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        addProductTable.setItems(Inventory.getAllParts());
        addPartsToProductID.setCellValueFactory(new PropertyValueFactory<>("id"));
        addPartToProductName.setCellValueFactory(new PropertyValueFactory<>("name"));
        addPartToProductInv.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addPartToProductPriceCost.setCellValueFactory(new PropertyValueFactory<>("price"));

        addAssociatedPartTable.setItems(associatedPartsList);
        addAssocistedPartsToProductIDFinal.setCellValueFactory(new PropertyValueFactory<>("id"));
        addAssociatedPartToProductNameFinal.setCellValueFactory(new PropertyValueFactory<>("name"));
        addAssociatedPartToProductInvFinal.setCellValueFactory(new PropertyValueFactory<>("stock"));
        addAssociatedPartToProductPriceCostFinal.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    /**
     * Search box for looking up products by Product ID and Product Name
     * @param actionEvent
     */
    public void onActionAddProductTableSearchBox(ActionEvent actionEvent) {

        String searchTableText = addProductTableSearchBox.getText();
        ObservableList<Part> result = Inventory.lookupPart(searchTableText);
        try{
            while (result.size() == 0) {
                int partID = Integer.parseInt(searchTableText);
                result.add((Part) Inventory.lookupPart(partID));
            }
            addProductTable.setItems(result);
            }catch (NumberFormatException e) {
                Alert failedProductSearch = new Alert(Alert.AlertType.ERROR);
                failedProductSearch.setHeaderText("Part Not Found!");
                failedProductSearch.show();
            }
    }

    /**
     * selects a part and adds it to the associated table
     * @param actionEvent
     */
    public void onButtonActionAddProductAddPartButton(ActionEvent actionEvent) {
        Part selectedPart = (Part) addProductTable.getSelectionModel().getSelectedItem();
        associatedPartsList.add(selectedPart);
        addAssociatedPartTable.setItems(associatedPartsList);

    }

    /**
     * cancels the addition of a product and redirects to the MainForm.fxml
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAddProductCancel(ActionEvent actionEvent) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Scene scene = new Scene(root);
        stage.setTitle("Home Screen");
        stage.setScene(scene);
        stage.show();
        }

    /**
     * submits the product into the product table for viewing and modifying
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAddProductSave(ActionEvent actionEvent) throws IOException{

        /**
         * creates random number for sorting and unigueIDs
         */
        int uniqueID = (int) (Math.random() * 1000);

        String name = addProductIDBox.getText();
        int stock = Integer.parseInt(addProductInvBox.getText());
        double price = Double.parseDouble(addProductPriceBox.getText());
        int max = Integer.parseInt(addProductMaxBox.getText());
        int min = Integer.parseInt(addProductMinBox.getText());


        Product product = new Product(uniqueID, name, price, stock, min, max);

        for (Part part: associatedPartsList) {
            if (part != associatedPartsList)
                product.addAssociatedParts(part);
        }

        Inventory.getAllProducts().add(product);
        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Object scene = FXMLLoader.load(getClass().getResource("/view/MainForm.fxml"));
        stage.setScene(new Scene((Parent) scene));
        stage.show();
    }

    /**
     * asks user if associated part is to be removed
     * @param actionEvent
     */
    public void onButtonActionRemoveAssociatedPart(ActionEvent actionEvent) {
        Part selectedPart = (Part) addAssociatedPartTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION!");
        alert.setHeaderText("Product will be deleted. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            associatedPartsList.remove(selectedPart);
            addAssociatedPartTable.setItems(associatedPartsList);
        }
    }
}
