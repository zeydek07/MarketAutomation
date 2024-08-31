package sample;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.io.PrintWriter;

public class Controller {
    public TextField barcodeText;
    public TableView<Product> productTable;
    public TableColumn<Product, String> pNameColumn;
    public TableColumn<Product, String> pBarcodeColumn;
    public TableColumn<Product, Integer> pPriceColumn;
    public Button sellButton;
    public Button deleteButton;
    public Button addButton;
    public Label totalLabel;
    public TextField priceControlText;
    public Button seePriceButton;
    public TextField returnProductText;
    public Button refundButton;
    public TableView<Product> stockTable;
    public TableColumn<Product, String> pStockNameColumn;
    public TableColumn<Product, String> pStockBarcodeColumn;
    public TableColumn<Product, Integer> pStockPriceColumn;
    public TableColumn<Product, Integer> pStockAmountColumn;
    public ObservableList<Product> productList = FXCollections.observableArrayList();
    public ObservableList<Product> sellableList = FXCollections.observableArrayList();


    public int totalPrice = 0;

    @FXML
    public void setAddButton() {
        for (Product product : productList) {
            if (product.getP_barcode().equals(barcodeText.getText())) {
                sellableList.add(product);
                productTable.getItems().add(product);
                totalPrice += product.getP_price();
                totalLabel.setText("" + totalPrice);
            }
        }
        barcodeText.setText("");

    }

    @FXML
    public void setSellButton() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText("Selled Products Count and Price");
        alert.setContentText("Count : " + sellableList.size() + " Price : " + totalPrice);
        alert.showAndWait();

        for (Product sellableProduct : sellableList) {
            for (Product mainProduct : productList) {
                if (mainProduct.getP_barcode().equals(sellableProduct.getP_barcode())) {
                    mainProduct.setP_amount(mainProduct.getP_amount() - 1);
                    break;
                }
            }
        }
        totalPrice=0;
        totalLabel.setText("0");
        updateStockTable();
        sellableList.clear();
        productTable.getItems().clear();
        updateStockFile();
    }

    @FXML
    public void setDeleteButton()  {
        try {
            Product deletedProduct = productTable.getSelectionModel().getSelectedItem();
            System.out.println(deletedProduct.getP_name());
            productTable.getItems().remove(deletedProduct);
            sellableList.remove(deletedProduct);
            totalPrice=totalPrice-deletedProduct.getP_price();
        }catch (Exception e){
            showErrorAlert("There is no selected product!!!");;
        }

        totalLabel.setText(""+totalPrice);
    }

    @FXML
    public void setSeePriceButton() {
        Product tempProduct = getProductByBarcode(priceControlText.getText());
        if (tempProduct != null) {
            showAlert("Price for /" + tempProduct.getP_name() + "/", "----" + tempProduct.getP_price() + "$----");
        } else {
            showErrorAlert("Product can't found in stock!!");
        }
        priceControlText.setText("");
    }

    @FXML
    public void setRefundButton() {
        int index = productList.indexOf(getProductByBarcode(returnProductText.getText()));
        if (index != -1) {
            productList.get(index).setP_amount(productList.get(index).getP_amount()+1);
            showAlert("Refund for /" + productList.get(index).getP_name() + "/", "----" + productList.get(index).getP_price() + "$----");
        } else {
            showErrorAlert("Product can't found in stock!!");
        }
        returnProductText.setText("");
        updateStockFile();
        updateStockTable();
    }

    @FXML
    public void initialize() {
        loadProductsFromStockFile();
        configureStockTable();
        configureProductTable();
    }

    private void configureStockTable() {
        pStockNameColumn.setCellValueFactory(new PropertyValueFactory<>("p_name"));
        pStockBarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("p_barcode"));
        pStockPriceColumn.setCellValueFactory(new PropertyValueFactory<>("p_price"));
        pStockAmountColumn.setCellValueFactory(new PropertyValueFactory<>("p_amount"));
        stockTable.setItems(productList);
    }

    private void configureProductTable() {
        pNameColumn.setCellValueFactory(new PropertyValueFactory<>("p_name"));
        pBarcodeColumn.setCellValueFactory(new PropertyValueFactory<>("p_barcode"));
        pPriceColumn.setCellValueFactory(new PropertyValueFactory<>("p_price"));
    }

    private void updateStockTable() {
        stockTable.refresh();
    }

    private void updateStockFile() {
        try (PrintWriter writer = new PrintWriter("src/resources/stock.txt")) {
            for (Product product : productList) {
                writer.println(product.getP_name() + "," + product.getP_barcode() + "," +
                        product.getP_price() + "," + product.getP_amount());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadProductsFromStockFile() {
        try (BufferedReader br = new BufferedReader(new FileReader("src/resources/stock.txt"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] tempObject = line.split(",");
                Product tempProduct = new Product(tempObject[0], tempObject[1], Integer.parseInt(tempObject[2]), Integer.parseInt(tempObject[3]));
                productList.add(tempProduct);
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private Product getProductByBarcode(String barcode) {
        for (Product product : productList) {
            if (product.getP_barcode().equals(barcode)) {
                return product;
            }
        }
        return null;
    }

    private void showAlert(String header, String content) {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setHeaderText(header);
        alert.setContentText(content);
        alert.showAndWait();
    }

    private void showErrorAlert(String content) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setHeaderText("Error");
        alert.setContentText(content);
        alert.showAndWait();
    }
}
