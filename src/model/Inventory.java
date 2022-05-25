package model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

import java.util.Collection;

/**
 * Inventory class stores the parts and products in observable lists
 */
public class Inventory {

    /**
     * observable list for parts
     */
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();

    /**
     * observable list for products
     */
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();

    /**
     * adds new part the allParts list
     * @param newPart
     */
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }

    /**
     * Add updates to the allParts list
     * @param index
     * @param selectedPart
     */
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
    }

    /**
     * returns all parts in the allParts observable list
     * @return
     */
    public static ObservableList<Part> getAllParts(){
        return allParts;
    }

    /**
     * deletes parts in the allParts observable list
     * @param selectedPart
     * @return
     */
    public static boolean deletePart(Part selectedPart) {
        if(allParts.contains(selectedPart)) {
            allParts.remove(selectedPart);
        }
        return false;
    }

    /**
     * returns part if found and null if not found
     * @param partID
     * @return
     */
    public static Part lookupPart(int partID) {
        for(Part part : Inventory.getAllParts()){
            while (part.getId() == partID)
                return part;
        }
        return null;
    }

    /**
     * creates observable list of products that have specific parameters, returns if products if found
     * @param productName
     * @return
     */
    public static ObservableList<Product> lookupProduct(String productName) {
        ObservableList<Product> ProductName = FXCollections.observableArrayList();

        for (Product product : allProducts) {
            if (product.getName().contains(productName)) {
                ProductName.add(product);
            }
        }
        return ProductName;
    }

    /**
     * creates observable list of parts that have specific parameters, returns if part if found
     * @param searchTableText
     * @return
     */
    public static ObservableList<Part> lookupPart(String searchTableText) {
        ObservableList<Part> PartName = FXCollections.observableArrayList();

        for (Part part: allParts) {
            if (part.getName().contains(searchTableText)) {
                PartName.add(part);
            }
        }
        return PartName;
    }

    /**
     * returns product if found and null if not found
     * @param productID
     * @return
     */
    public static Product lookupProduct(int productID) {
        for(Product product : Inventory.getAllProducts()){
            while (product.getId() == productID)
                return product;
        }
        return null;
    }

    /**
     * returns all products in tha products observable list
     */
    public static Collection<Product> getAllProducts() {
        return allProducts;
    }

    /**
     * deletes a product in the all product observable list
     * @param selectedProduct
     * @return
     */
    public static boolean deleteProduct(Product selectedProduct) {
        if(allProducts.contains(selectedProduct)) {
            allProducts.remove(selectedProduct);
            return true;
        }
        else {
            return false;
        }
    }
    /**
     * updates the product in the observable product list
     * @param currentIndex
     * @param updatedProduct
     */
    public static void updateProduct(int currentIndex, Product updatedProduct) {
        allProducts.set(currentIndex, updatedProduct);
    }
}

