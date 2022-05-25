package controller;

import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import model.*;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;


/**
 * "/controller/MainFormController" contains logic and interaction for the main start screen
 */
public class MainFormController implements Initializable {
    public Button ExitButton;
    public TableView<Part> partsTable;
    public TableColumn<Part, Integer> partsTablePartID;
    public TableColumn<Part, String> partsTablePartName;
    public TableColumn<Part, Integer> partsTableInventoryLevel;
    public TableColumn<Part, Integer> partsTablePriceCostUnit;
    public Pane PartsTablePane;
    public Button partsTablePaneAddPartsButton;
    public Button partsTablePaneModifyPartsButton;
    public Button partsTablePaneDeletePartsButton;
    public TableView productTable;
    public TableColumn productsTablePartID;
    public TableColumn productsTablePartName;
    public TableColumn productsTableInventoryLevel;
    public TableColumn productsTablePriceCostUnit;
    public Pane ProductsTablePane;
    public TextField partsTableSearch;
    public Button AddProductsButton;
    public TextField productsTableSearch;
    public Button ModifyProductsButton;
    public Button DeleteProductsButton;


    /**
     * Initialize the table, populate
     * @param url
     * @param resourceBundle
     */
    @Override
    public void initialize(URL url, ResourceBundle resourceBundle) {
        partsTable.setItems(Inventory.getAllParts());
        partsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        partsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        partsTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partsTablePriceCostUnit.setCellValueFactory(new PropertyValueFactory<>("price"));

        productTable.setItems((ObservableList) Inventory.getAllProducts());
        productsTablePartID.setCellValueFactory(new PropertyValueFactory<>("id"));
        productsTablePartName.setCellValueFactory(new PropertyValueFactory<>("name"));
        productsTableInventoryLevel.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productsTablePriceCostUnit.setCellValueFactory(new PropertyValueFactory<>("price"));
    }

    public void onButtonActionExit(ActionEvent actionEvent) {
        System.exit(0);
    }

    /**
     * Exits the application
     * @param actionEvent
     * @throws IOException
     */

    public void onButtonActionPartsTablePaneAddPartsButton(ActionEvent actionEvent) throws IOException {
        /**
         * System.out.println("Add check");
         * load widget hierarchy of next screen
         */
        Parent root = FXMLLoader.load(getClass().getResource("/view/AddPartForm.fxml"));
        /**
         * get the stage from an event's source widget
         */
        Stage stage = (Stage)((Node)actionEvent.getSource()).getScene().getWindow();
        /**
         * create new scene
         */
        Scene scene = new Scene(root);
        /**
         * RUNTIME ERROR - java: ';' expected
         * stage.setTitle("Add Parts"); used for testing
         * set the scene on the stage
         */
        stage.setScene(scene);
        stage.show();
    }

    /**
     * redirects to the ModifyPartform.fxml and populates table data
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionPartsTablePaneModifyParts(ActionEvent actionEvent) throws IOException{
        /**
         * System.out.println("Modify check"); - used for testing
         */
        FXMLLoader loader = new FXMLLoader();
            loader.setLocation(getClass().getResource("/view/ModifyPartForm.fxml"));
            loader.load();

            ModifyPartsController ModifyController = loader.getController();
            ModifyController.sendPart(partsTable.getSelectionModel().getSelectedIndex(),partsTable.getSelectionModel().getSelectedItem());

            Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
            Parent scene = loader.getRoot();
            stage.setScene(new Scene(scene));
            stage.show();
    }

    /**
     * deletes selected parts from parts table, with prompt
     * @param actionEvent
     */
    public void onButtonActionPartsTablePaneDeleteParts(ActionEvent actionEvent) {
        Part selectedPart = partsTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION!");
        alert.setHeaderText("Part will be deleted. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Inventory.deletePart(selectedPart);
        }
    }

    /**
     * Search box for looking up products by Part ID and Part Name
     * FUTURE ENHANCEMENT - searching in the text box requires an action of pressing enter, would be nice if it searched key press by key press
     * FUTURE ENHANCEMENT - searching requires exact CaseSensitive typing, a lowercase option that returned results would be a great enhancement
     */
    public void onActionPartsTableSearch(ActionEvent actionEvent) {
        String searchTableText = partsTableSearch.getText();
        ObservableList<Part> result = Inventory.lookupPart(searchTableText);
        try {
            while (result.size() == 0) {
                int partID = Integer.parseInt(searchTableText);
                result.add(Inventory.lookupPart(partID));
            }
            partsTable.setItems(result);
        }catch (NumberFormatException e) {
            /**
             * RUNTIME ERROR - java: cannot find symbol symbol: - class numberFormatException - location: class controller.MainFormController
             * fixed by changing NumberFormatExpression
             */
            Alert failedPartSearch = new Alert(Alert.AlertType.ERROR);
            failedPartSearch.setHeaderText("Part Not Found!");
            failedPartSearch.show();
        }
    }

    /**
     *Search box for looking up products by Product ID and Product Name
     *FUTURE ENHANCEMENT searching in the text box requires an action of pressing enter, would be nice if it searched key press by key press
     *FUTURE ENHANCEMENT searching requires exact CaseSensitive typing, a lowercase option that returned results would be a great enhancement
     * @param actionEvent
     */
    public void onActionProductTableSearch(ActionEvent actionEvent) {
        /**
         * System.out.println("Searching Test"); - used for testing
         */
        String searchText = productsTableSearch.getText();
        ObservableList<Product> results = Inventory.lookupProduct(searchText);
        try {
            while (results.size() == 0) {
                int productID = Integer.parseInt(searchText);
                results.add(Inventory.lookupProduct(productID));
            }
            productTable.setItems(results);
        }catch (NumberFormatException e) {
            Alert failedProductSearch = new Alert(Alert.AlertType.ERROR);
            failedProductSearch.setHeaderText("Product Not Found!");
            failedProductSearch.show();
        }
    }

    /**
     * /redirects to the AddProductForm.fxml view
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionAddProductMainAdd(ActionEvent actionEvent) throws IOException{
        /**
         * /System.out.println("Add Product Check");
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/AddProductForm.fxml"));
        loader.load();

        Stage stage = (Stage) ((Button)actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();
    }

    /**
     * redirects to the ModifyProductForm.fxml after populating data for table from product list
     * @param actionEvent
     * @throws IOException
     */
    public void onButtonActionModifyMain(ActionEvent actionEvent) throws IOException {
        /**
         * /System.out.println("Test for modify"); - used for testing
         */
        FXMLLoader loader = new FXMLLoader();
        loader.setLocation(getClass().getResource("/view/ModifyProductForm.fxml"));
        loader.load();

        ModifyProductController modifyProductController = loader.getController();
        modifyProductController.sendProduct(productTable.getSelectionModel().getSelectedIndex(), (Product) productTable.getSelectionModel().getSelectedItem());


        Stage stage = (Stage) ((Button) actionEvent.getSource()).getScene().getWindow();
        Parent scene = loader.getRoot();
        stage.setScene(new Scene(scene));
        stage.show();

    }

    /**
     * deletes product from the product table, with prompt, then determines if products have associated parts and denies deletion if parts are associated
     * @param actionEvent
     */
    public void onButtonActionMainDelete(ActionEvent actionEvent) {
        Product selectedProduct = (Product) productTable.getSelectionModel().getSelectedItem();
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle("ATTENTION!");
        alert.setHeaderText("Product will be deleted. Are you sure?");
        Optional<ButtonType> result = alert.showAndWait();

        if (result.isPresent() && result.get() == ButtonType.OK) {
            Product selectedDeleteProduct = (Product) productTable.getSelectionModel().getSelectedItem();
            if (selectedDeleteProduct.getAllAssociatedParts().size() > 0) {
                Alert cantDelete = new Alert(Alert.AlertType.ERROR);
                cantDelete.setTitle("Error Message");
                cantDelete.setContentText("Remove associated parts before you delete the product.");
                cantDelete.showAndWait();
                return;
            }
            Inventory.deleteProduct(selectedProduct);
        }
    }
}


