package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.*;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.model.generics.PayedForTracker;
import com.reception.projektkrivacic.model.records.Receipt;

import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.math.BigDecimal;
import java.sql.Timestamp;
import java.util.Comparator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.*;

public class ReceiptsScreenController {

    @FXML
    private Label usernameLabel;
    @FXML
    private ComboBox<String> paymentMethodComboBox;

    @FXML
    private TableView<Receipt> receiptsTable;
    @FXML
    private TableColumn<Receipt, String> receiptIdColumn;
    @FXML
    private TableColumn<Receipt, String> reservationIdColumn;
    @FXML
    private TableColumn<Receipt, String> totalPriceColumn;
    @FXML
    private TableColumn<Receipt, String> paymentMethodColumn;
    @FXML
    private TableColumn<Receipt, String> paymentTimeColumn;

    private User currentUser;

    public void initialize(User user) {
        currentUser = user;
        usernameLabel.setText(user.getName());

        paymentMethodComboBox.getItems().addAll("Cash", "Credit Card", "Pending");

        receiptIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Receipt, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().receiptId().toString());
            }
        });
        reservationIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Receipt, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().reservationId().toString());
            }
        });
        totalPriceColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Receipt, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().totalAmount().toString());
            }
        });
        paymentMethodColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Receipt, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().paymentMethod());
            }
        });
        paymentTimeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Receipt, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Receipt, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().paymentTime().toString());
            }
        });

        receiptsTable.setRowFactory(tv -> {
            TableRow<Receipt> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete Receipt");

            deleteItem.setOnAction(event -> {
                if(currentUser.getIsAnEmployee()){
                    Receipt oldReservation = row.getItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete this receipt?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Receipt selectedReceipt = row.getItem();

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        DeleteReceiptFromDatabaseThread deleteReceiptThread = new DeleteReceiptFromDatabaseThread(selectedReceipt);
                        executor.execute(deleteReceiptThread);

                        try{
                            executor.awaitTermination(300, TimeUnit.MILLISECONDS);
                        }
                        catch (InterruptedException e){
                            e.printStackTrace();
                        }
                        searchReceipts();

                        DataChange<Receipt, Receipt> dataChange = new DataChange<>("Reservation",
                                oldReservation, null, new Timestamp(System.currentTimeMillis()), currentUser);
                        FileUtils.serializeDataChange(dataChange);
                    }
                }
            });
            contextMenu.getItems().addAll(deleteItem);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null)
            );
            return row;
        });

        BillsReminderThread billsReminderThread = new BillsReminderThread();
        billsReminderThread.run();

        searchReceipts();
    }

    public void searchReceipts() {
        Executor executor = Executors.newSingleThreadExecutor();
        GetReceiptsFromDatabaseThread getReceiptsThread = new GetReceiptsFromDatabaseThread();
        executor.execute(getReceiptsThread);

        List<Receipt> receipts = getReceiptsThread.getReceipts();
        FileUtils.serializeReceipts(receipts);

        String paymentMethod = paymentMethodComboBox.getValue();

        List<Receipt> filteredReceipts = receipts.stream()
                .filter(receipt -> paymentMethod == null || receipt.paymentMethod().equals(paymentMethod))
                .sorted(Comparator.comparing(Receipt::paymentTime).reversed())
                .toList();

        ObservableList<Receipt> receiptData = FXCollections.observableArrayList();
        receiptData.addAll(filteredReceipts);

        receiptsTable.setItems(receiptData);
    }

    public void resetButton() {
        paymentMethodComboBox.setValue(null);
        searchReceipts();
    }


    public void changeToShowAccommodationsScene() {
        SceneUtils.changeToShowAccommodationsScreen(currentUser);
    }
    public void changeToSearchReservationsScene() {
        SceneUtils.changeToShowReservationsScreen(currentUser);
    }
    public void changeToReceiptScene() {
        if(currentUser.getIsAnEmployee())
            SceneUtils.changeToReceiptsScreen(currentUser);
    }
    public void changeToShowUsersScene() {
        if(currentUser.getIsAnEmployee())
            SceneUtils.changeToShowUsersScreen(currentUser);
    }
    public void changeToHotelFinancesScene() {
        if (currentUser.getIsAnEmployee())
            SceneUtils.changeToHotelFinancesScreen(currentUser);
    }
    public void changeToChangesScene() {
        SceneUtils.changeToChangesScreen(currentUser);
    }
    public void changeToLoginScene() {
        SceneUtils.loginSignupChangeScene("loginScreen.fxml", "Login", 600, 400);
    }
}
