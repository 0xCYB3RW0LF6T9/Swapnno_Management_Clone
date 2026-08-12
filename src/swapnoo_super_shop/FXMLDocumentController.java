package swapnoo_super_shop;

import java.net.URL;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ResourceBundle;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.print.PageLayout;
import javafx.print.PageOrientation;
import javafx.print.Paper;
import javafx.print.PrinterJob;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyCode;
import javafx.scene.layout.VBox;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

public class FXMLDocumentController implements Initializable {

    // =========================================================
    // TOP INFORMATION
    // =========================================================
    @FXML
    private Label lastInvoiceField;

    @FXML
    private Label terminalField;

    @FXML
    private Label userField;

    // =========================================================
    // ITEM / CUSTOMER INFORMATION
    // =========================================================
    @FXML
    private TextField itemScanField;

    @FXML
    private TextField customerField;

    @FXML
    private TextField numberField;

    @FXML
    private TextField numberField1;

    @FXML
    private TextField numberField11;

    @FXML
    private Label itemNoField;

    @FXML
    private TextField recallInvoiceField;

    // =========================================================
    // TOTAL INFORMATION
    // =========================================================
    @FXML
    private Label mrpField;

    @FXML
    private Label sdField;

    @FXML
    private Label discountField;

    @FXML
    private Label totalSield;

    // =========================================================
    // PAYMENT FIELDS
    // =========================================================
    @FXML
    private TextField bracBankField;

    @FXML
    private TextField easternBankField;

    @FXML
    private TextField ecomCashBankfield;

    @FXML
    private TextField ecomonlineBAnkField;

    @FXML
    private TextField nagadField;

    @FXML
    private TextField mtbCardField;

    @FXML
    private TextField mtbQRField;

    @FXML
    private TextField pblBankField;

    @FXML
    private Label roundOffField;

    @FXML
    private Label paidableField;

    @FXML
    private TextField cashRecieveField;

    // =========================================================
    // TABLEVIEW
    // =========================================================
    @FXML
    private TableView<product> taable;

    @FXML
    private TableColumn<product, String> codeCol;

    @FXML
    private TableColumn<product, String> descriptionCol;

    @FXML
    private TableColumn<product, Integer> priceCol;

    @FXML
    private TableColumn<product, Integer> quantityCol;

    // =========================================================
    // PRODUCT INPUT FIELDS
    // =========================================================
    @FXML
    private TextField codeField;

    @FXML
    private TextField desscriptionField;

    @FXML
    private TextField priceField;

    @FXML
    private TextField quantityField;

    // =========================================================
    // PRODUCT LIST
    // =========================================================
    private final ObservableList<product> productList
            = FXCollections.observableArrayList();

    // =========================================================
    // INVOICE NUMBER
    // =========================================================
    private int invoiceNumber = 1000;

    private String currentInvoiceNumber = "";

    // =========================================================
    // INITIALIZE
    // =========================================================
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        setupTableView();

        taable.setItems(productList);

        initializeFields();

        setupBarcodeScanner();

        setupPaymentListeners();
    }

    // =========================================================
    // INITIALIZE FIELDS
    // =========================================================
    private void initializeFields() {

        itemNoField.setText("0");

        mrpField.setText("0.00");

        sdField.setText("0.00");

        discountField.setText("0.00");

        totalSield.setText("0.00");

        paidableField.setText("0.00");

        roundOffField.setText("0.00");

        cashRecieveField.setText("0");

        bracBankField.setText("0");

        easternBankField.setText("0");

        ecomCashBankfield.setText("0");

        ecomonlineBAnkField.setText("0");

        nagadField.setText("0");

        mtbCardField.setText("0");

        mtbQRField.setText("0");

        pblBankField.setText("0");
    }

    // =========================================================
    // BARCODE SCANNER
    // =========================================================
    private void setupBarcodeScanner() {

        itemScanField.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {

                scanBarcode();

                event.consume();
            }
        });
    }

    // =========================================================
// PAYMENT LISTENERS
// =========================================================
    private void setupPaymentListeners() {

        setupPaymentField(cashRecieveField);
        setupPaymentField(bracBankField);
        setupPaymentField(easternBankField);
        setupPaymentField(ecomCashBankfield);
        setupPaymentField(ecomonlineBAnkField);
        setupPaymentField(nagadField);
        setupPaymentField(mtbCardField);
        setupPaymentField(mtbQRField);
        setupPaymentField(pblBankField);
    }

// =========================================================
// SETUP ONE PAYMENT FIELD
// =========================================================
    private void setupPaymentField(TextField field) {

        field.setOnMouseClicked(event -> {

            // If the field contains only 0,
            // select it so the next number replaces 0.
            if (field.getText().equals("0")) {
                field.selectAll();
            }
        });

        field.setOnKeyPressed(event -> {

            if (event.getCode() == KeyCode.ENTER) {

                updatePaymentSummary();

                event.consume();
            }
        });

        field.textProperty().addListener((observable, oldValue, newValue) -> {

            updatePaymentSummary();
        });
    }

    // =========================================================
    // TABLEVIEW SETUP
    // =========================================================
    private void setupTableView() {

        codeCol.setCellValueFactory(
                new PropertyValueFactory<>("code")
        );

        descriptionCol.setCellValueFactory(
                new PropertyValueFactory<>("description")
        );

        priceCol.setCellValueFactory(
                new PropertyValueFactory<>("price")
        );

        quantityCol.setCellValueFactory(
                new PropertyValueFactory<>("quantity")
        );
    }

    // =========================================================
    // USER + TERMINAL
    // =========================================================
    public void setUserAndTerminal(String user, String terminal) {

        if (userField != null) {
            userField.setText(user);
        }

        if (terminalField != null) {
            terminalField.setText(terminal);
        }
    }

    // =========================================================
    // ADD PRODUCT
    // =========================================================
    @FXML
    private void submitOnAction(ActionEvent event) {

        String code = codeField.getText().trim();

        String description
                = desscriptionField.getText().trim();

        String priceText
                = priceField.getText().trim();

        String quantityText
                = quantityField.getText().trim();

        if (code.isEmpty()
                || description.isEmpty()
                || priceText.isEmpty()
                || quantityText.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Missing Information",
                    "Please fill in all product information."
            );

            return;
        }

        int price;

        int quantity;

        try {

            price = Integer.parseInt(priceText);

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Price",
                    "Please enter a valid price."
            );

            return;
        }

        try {

            quantity = Integer.parseInt(quantityText);

        } catch (NumberFormatException e) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Invalid Quantity",
                    "Please enter a valid quantity."
            );

            return;
        }

        if (price <= 0) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Price",
                    "Price must be greater than zero."
            );

            return;
        }

        if (quantity <= 0) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Invalid Quantity",
                    "Quantity must be greater than zero."
            );

            return;
        }

        product existingProduct
                = findProductByBarcode(code);

        if (existingProduct != null) {

            existingProduct.setQuantity(
                    existingProduct.getQuantity() + quantity
            );

        } else {

            product newProduct
                    = new product(
                            code,
                            description,
                            price,
                            quantity
                    );

            productList.add(newProduct);
        }

        taable.refresh();

        updateItemCount();

        updateTotal();

        clearProductFields();

        codeField.requestFocus();
    }

    // =========================================================
    // ITEM COUNT
    // =========================================================
    private void updateItemCount() {

        int totalItems = 0;

        for (product p : productList) {

            totalItems += p.getQuantity();
        }

        itemNoField.setText(
                String.valueOf(totalItems)
        );
    }

    // =========================================================
    // CALCULATE TOTAL
    // =========================================================
    private double calculateTotal() {

        double total = 0.0;

        for (product p : productList) {

            total
                    += (double) p.getPrice()
                    * p.getQuantity();
        }

        return total;
    }

    // =========================================================
    // UPDATE TOTAL
    // =========================================================
    private void updateTotal() {

        double total = calculateTotal();

        mrpField.setText(
                String.format("%.2f", total)
        );

        sdField.setText("0.00");

        discountField.setText("0.00");

        totalSield.setText(
                String.format("%.2f", total)
        );

        paidableField.setText(
                String.format("%.2f", total)
        );

        updatePaymentSummary();
    }

    // =========================================================
    // CLEAR PRODUCT FIELDS
    // =========================================================
    private void clearProductFields() {

        codeField.clear();

        desscriptionField.clear();

        priceField.clear();

        quantityField.clear();
    }

    // =========================================================
    // ALERT
    // =========================================================
    private void showAlert(
            Alert.AlertType type,
            String title,
            String message) {

        Alert alert = new Alert(type);

        alert.setTitle(title);

        alert.setHeaderText(null);

        alert.setContentText(message);

        alert.showAndWait();
    }

    // =========================================================
    // FIND PRODUCT
    // =========================================================
    private product findProductByBarcode(String barcode) {

        for (product p : productList) {

            if (p.getCode().equals(barcode)) {

                return p;
            }
        }

        return null;
    }

    // =========================================================
    // BARCODE SCANNING
    // =========================================================
    private void scanBarcode() {

        String barcode
                = itemScanField.getText().trim();

        if (barcode.isEmpty()) {

            return;
        }

        product scannedProduct
                = findProductByBarcode(barcode);

        if (scannedProduct == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Product Not Found",
                    "No product was found with barcode: "
                    + barcode
            );

            itemScanField.clear();

            itemScanField.requestFocus();

            return;
        }

        scannedProduct.setQuantity(
                scannedProduct.getQuantity() + 1
        );

        taable.refresh();

        updateItemCount();

        updateTotal();

        itemScanField.clear();

        itemScanField.requestFocus();
    }

    // =========================================================
// PARSE PAYMENT
// =========================================================
    private double parseAmount(String value) {

        if (value == null
                || value.trim().isEmpty()) {

            return 0.0;
        }

        try {

            double amount
                    = Double.parseDouble(value.trim());

            if (amount < 0) {

                return 0.0;
            }

            return amount;

        } catch (NumberFormatException e) {

            return 0.0;
        }
    }

    // =========================================================
// GET TOTAL PAYMENT
// =========================================================
    private double getTotalPayment() {

        double totalPayment = 0.0;

        totalPayment += parseAmount(
                cashRecieveField.getText()
        );

        totalPayment += parseAmount(
                bracBankField.getText()
        );

        totalPayment += parseAmount(
                easternBankField.getText()
        );

        totalPayment += parseAmount(
                ecomCashBankfield.getText()
        );

        totalPayment += parseAmount(
                ecomonlineBAnkField.getText()
        );

        totalPayment += parseAmount(
                nagadField.getText()
        );

        totalPayment += parseAmount(
                mtbCardField.getText()
        );

        totalPayment += parseAmount(
                mtbQRField.getText()
        );

        totalPayment += parseAmount(
                pblBankField.getText()
        );

        return totalPayment;
    }

    // =========================================================
    // PAYMENT SUMMARY
    // =========================================================
    // =========================================================
// PAYMENT SUMMARY
// =========================================================
    private void updatePaymentSummary() {

        double total = calculateTotal();

        double totalPayment = getTotalPayment();

        double remaining = total - totalPayment;

        // ---------------------------------------------
        // Update total fields
        // ---------------------------------------------
        mrpField.setText(
                String.format("%.2f", total)
        );

        sdField.setText("0.00");

        discountField.setText("0.00");

        totalSield.setText(
                String.format("%.2f", total)
        );

        paidableField.setText(
                String.format("%.2f", total)
        );

        // ---------------------------------------------
        // Payment status
        // ---------------------------------------------
        if (total <= 0) {

            roundOffField.setText("0.00");

            return;
        }

        // ---------------------------------------------
        // Payment is not complete
        // ---------------------------------------------
        if (remaining > 0) {

            roundOffField.setText(
                    "DUE "
                    + String.format(
                            "%.2f",
                            remaining
                    )
            );

        } // ---------------------------------------------
        // Payment exactly matches bill
        // ---------------------------------------------
        else if (remaining == 0) {

            roundOffField.setText(
                    "CHANGE 0.00"
            );

        } // ---------------------------------------------
        // Customer paid extra
        // ---------------------------------------------
        else {

            double change = Math.abs(remaining);

            roundOffField.setText(
                    "CHANGE "
                    + String.format(
                            "%.2f",
                            change
                    )
            );
        }
    }

    // =========================================================
    // PAYMENT VALIDATION
    // =========================================================
    private boolean isPaymentValid() {

        double total = calculateTotal();

        double totalPayment
                = getTotalPayment();

        if (total <= 0) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Items",
                    "Please add at least one product before payment."
            );

            return false;
        }

        if (totalPayment < total) {

            double due
                    = total - totalPayment;

            showAlert(
                    Alert.AlertType.WARNING,
                    "Insufficient Payment",
                    "Payment is not enough.\n\n"
                    + "Payable Amount: "
                    + String.format("%.2f", total)
                    + "\nReceived: "
                    + String.format("%.2f", totalPayment)
                    + "\nDue: "
                    + String.format("%.2f", due)
            );

            return false;
        }

        return true;
    }

    // =========================================================
    // GENERATE INVOICE NUMBER
    // =========================================================
    private void generateInvoiceNumber() {

        invoiceNumber++;

        currentInvoiceNumber
                = "INV-" + invoiceNumber;

        if (lastInvoiceField != null) {

            lastInvoiceField.setText(
                    currentInvoiceNumber
            );
        }
    }

    // =========================================================
    // PRINT BUTTON
    // =========================================================
    @FXML
    private void printOnAction(ActionEvent event) {

        if (productList.isEmpty()) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "Empty Invoice",
                    "Please add products before printing."
            );

            return;
        }

        if (!isPaymentValid()) {

            return;
        }

        generateInvoiceNumber();

        boolean printed
                = printInvoice();

        if (printed) {

            showAlert(
                    Alert.AlertType.INFORMATION,
                    "Print Successful",
                    "Invoice "
                    + currentInvoiceNumber
                    + " has been printed successfully."
            );

            clearAfterInvoice();
        }
    }

    // =========================================================
    // PRINT INVOICE
    // =========================================================
    private boolean printInvoice() {

        PrinterJob job = PrinterJob.createPrinterJob();

        if (job == null) {

            showAlert(
                    Alert.AlertType.ERROR,
                    "Printer Error",
                    "No printer was found on this computer."
            );

            return false;
        }

        boolean proceed
                = job.showPrintDialog(
                        taable.getScene().getWindow()
                );

        if (!proceed) {
            return false;
        }

        VBox invoice = createPrintableInvoice();

        PageLayout pageLayout
                = job.getPrinter().getDefaultPageLayout();

        boolean success
                = job.printPage(
                        pageLayout,
                        invoice
                );

        if (success) {

            job.endJob();

            return true;
        }

        return false;
    }

    // =========================================================
    // CREATE PRINTABLE INVOICE
    // =========================================================
    private VBox createPrintableInvoice() {

        VBox invoice
                = new VBox(8);

        invoice.setPadding(
                new Insets(20)
        );

        invoice.setPrefWidth(500);

        Text shopName
                = new Text(
                        "SWAPNOO SUPER SHOP"
                );

        shopName.setFont(
                Font.font(22)
        );

        Text invoiceTitle
                = new Text(
                        "SALES INVOICE"
                );

        invoiceTitle.setFont(
                Font.font(16)
        );

        String dateTime
                = LocalDateTime.now()
                        .format(
                                DateTimeFormatter.ofPattern(
                                        "dd-MM-yyyy HH:mm:ss"
                                )
                        );

        String user
                = userField.getText();

        String terminal
                = terminalField.getText();

        Text invoiceInfo
                = new Text(
                        "Invoice No : "
                        + currentInvoiceNumber
                        + "\nDate       : "
                        + dateTime
                        + "\nUser       : "
                        + user
                        + "\nTerminal   : "
                        + terminal
                );

        String customer
                = customerField.getText().trim();

        if (customer.isEmpty()) {

            customer
                    = "Walk-in Customer";
        }

        Text customerInfo
                = new Text(
                        "Customer   : "
                        + customer
                );

        VBox productBox
                = new VBox(5);

        Text header
                = new Text(
                        String.format(
                                "%-15s %-20s %8s %8s",
                                "CODE",
                                "DESCRIPTION",
                                "PRICE",
                                "QTY"
                        )
                );

        productBox.getChildren().add(header);

        for (product p : productList) {

            String description
                    = p.getDescription();

            if (description.length() > 20) {

                description
                        = description.substring(0, 20);
            }

            Text productText
                    = new Text(
                            String.format(
                                    "%-15s %-20s %8.2f %8d",
                                    p.getCode(),
                                    description,
                                    (double) p.getPrice(),
                                    p.getQuantity()
                            )
                    );

            productBox.getChildren().add(
                    productText
            );
        }

        double total
                = calculateTotal();

        double totalPayment
                = getTotalPayment();

        double change
                = totalPayment - total;

        StringBuilder summaryBuilder
                = new StringBuilder();

        summaryBuilder.append(
                "\n----------------------------------------"
        );

        summaryBuilder.append(
                "\nMRP          : "
        ).append(
                String.format("%.2f", total)
        );

        summaryBuilder.append(
                "\nSD           : 0.00"
        );

        summaryBuilder.append(
                "\nDiscount     : 0.00"
        );

        summaryBuilder.append(
                "\nTOTAL        : "
        ).append(
                String.format("%.2f", total)
        );

        summaryBuilder.append(
                "\nPAYABLE      : "
        ).append(
                String.format("%.2f", total)
        );

        summaryBuilder.append(
                "\n----------------------------------------"
        );

        summaryBuilder.append(
                "\nCASH         : "
        ).append(
                formatPayment(cashRecieveField)
        );

        summaryBuilder.append(
                "\nBRAC BANK    : "
        ).append(
                formatPayment(bracBankField)
        );

        summaryBuilder.append(
                "\nEASTERN BANK : "
        ).append(
                formatPayment(easternBankField)
        );

        summaryBuilder.append(
                "\nECOM CASH    : "
        ).append(
                formatPayment(ecomCashBankfield)
        );

        summaryBuilder.append(
                "\nECOM ONLINE  : "
        ).append(
                formatPayment(ecomonlineBAnkField)
        );

        summaryBuilder.append(
                "\nNAGAD        : "
        ).append(
                formatPayment(nagadField)
        );

        summaryBuilder.append(
                "\nMTB CARD     : "
        ).append(
                formatPayment(mtbCardField)
        );

        summaryBuilder.append(
                "\nMTB QR       : "
        ).append(
                formatPayment(mtbQRField)
        );

        summaryBuilder.append(
                "\nPBL BANK     : "
        ).append(
                formatPayment(pblBankField)
        );

        summaryBuilder.append(
                "\n----------------------------------------"
        );

        summaryBuilder.append(
                "\nTOTAL PAID   : "
        ).append(
                String.format("%.2f", totalPayment)
        );

        if (change >= 0) {

            summaryBuilder.append(
                    "\nCHANGE       : "
            ).append(
                    String.format("%.2f", change)
            );

        } else {

            summaryBuilder.append(
                    "\nDUE          : "
            ).append(
                    String.format(
                            "%.2f",
                            Math.abs(change)
                    )
            );
        }

        Text summary
                = new Text(
                        summaryBuilder.toString()
                );

        Text footer
                = new Text(
                        "\n\nThank you for shopping with us!"
                        + "\nPlease visit again."
                );

        invoice.getChildren().addAll(
                shopName,
                invoiceTitle,
                invoiceInfo,
                customerInfo,
                productBox,
                summary,
                footer
        );

        return invoice;
    }

    // =========================================================
    // FORMAT PAYMENT
    // =========================================================
    private String formatPayment(TextField field) {

        return String.format(
                "%.2f",
                parseAmount(field.getText())
        );
    }

    // =========================================================
    // CLEAR AFTER INVOICE
    // =========================================================
    private void clearAfterInvoice() {

        productList.clear();

        taable.refresh();

        itemNoField.setText("0");

        mrpField.setText("0.00");

        sdField.setText("0.00");

        discountField.setText("0.00");

        totalSield.setText("0.00");

        paidableField.setText("0.00");

        roundOffField.setText("0.00");

        cashRecieveField.setText("0");

        bracBankField.setText("0");

        easternBankField.setText("0");

        ecomCashBankfield.setText("0");

        ecomonlineBAnkField.setText("0");

        nagadField.setText("0");

        mtbCardField.setText("0");

        mtbQRField.setText("0");

        pblBankField.setText("0");

        customerField.clear();

        itemScanField.clear();

        currentInvoiceNumber = "";

        itemScanField.requestFocus();
    }

    // =========================================================
    // HOLD INVOICE
    // =========================================================
    @FXML
    private void holdInvoiceOnAction(ActionEvent event) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Hold Invoice",
                "Hold invoice functionality will be implemented in the next part."
        );
    }

    // =========================================================
    // RECALL INVOICE
    // =========================================================
    @FXML
    private void recallInvoiceOnAction(ActionEvent event) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Recall Invoice",
                "Recall invoice functionality will be implemented in the next part."
        );
    }

    // =========================================================
    // REPRINT INVOICE
    // =========================================================
    @FXML
    private void reprintInvoiceOnAction(ActionEvent event) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Reprint Invoice",
                "Reprint invoice functionality will be implemented in the next part."
        );
    }

    // =========================================================
    // REPRINT
    // =========================================================
    private void reprintOnAction(ActionEvent event) {

        showAlert(
                Alert.AlertType.INFORMATION,
                "Reprint",
                "Reprint functionality will be implemented in the next part."
        );
    }

    @FXML
    private void delItemOnAction(ActionEvent event) {

        // Get the currently selected product
        product selectedProduct = taable.getSelectionModel().getSelectedItem();

        // Check if an item is selected
        if (selectedProduct == null) {

            showAlert(
                    Alert.AlertType.WARNING,
                    "No Item Selected",
                    "Please select an item from the table first."
            );

            return;
        }

        // Remove the selected product
        productList.remove(selectedProduct);

        // Refresh the table
        taable.refresh();

        // Update item count and total
        updateItemCount();
        updateTotal();

        // Select the first item if any items remain
        if (!productList.isEmpty()) {
            taable.getSelectionModel().selectFirst();
        }
    }

    @FXML
    private void exitOnAction(ActionEvent event) {
        Stage stage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        stage.close();
    }

    @FXML
    private void clearOnAction(ActionEvent event) {
        clearAfterInvoice();
    }

}
