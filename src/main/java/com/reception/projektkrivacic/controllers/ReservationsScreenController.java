package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.DeleteReservationFromDatabaseThread;
import com.reception.projektkrivacic.Threads.GetNextReservationIdThread;
import com.reception.projektkrivacic.Threads.GetReservationsFromDatabaseThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.model.classes.Reservation;

import com.reception.projektkrivacic.sort.SortByDate;
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

import java.sql.Timestamp;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class ReservationsScreenController {

    @FXML
    private Label usernameLabel;
    @FXML
    private ComboBox<User> userComboBox;
    @FXML
    private TableView<Reservation> reservationsTableView;
    @FXML
    private TableColumn<Reservation, String> reservationIdColumn;
    @FXML
    private TableColumn<Reservation, String> accommodationColumn;
    @FXML
    private TableColumn<Reservation, String> userColumn;
    @FXML
    private TableColumn<Reservation, String> checkInDateColumn;
    @FXML
    private TableColumn<Reservation, String> checkOutDateColumn;

    private User currentUser;


    public void initialize(User user) {
        this.currentUser = user;
        usernameLabel.setText(currentUser.getName());

        userComboBox.setItems(FXCollections.observableArrayList(DatabaseUtils.getUsersFromDatabase()));

        reservationIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservationStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(reservationStringCellDataFeatures.getValue().getReservationId().toString());
            }
        });
        accommodationColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservationStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(reservationStringCellDataFeatures.getValue().getAccommodationId().toString());
            }
        });
        userColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservationStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(reservationStringCellDataFeatures.getValue().getUserId().toString());
            }
        });
        checkInDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservationStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(reservationStringCellDataFeatures.getValue().getCheckInDate().toString());
            }
        });
        checkOutDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> reservationStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(reservationStringCellDataFeatures.getValue().getCheckOutDate().toString());
            }
        });

        reservationsTableView.setRowFactory(tv -> {
            TableRow<Reservation> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete Reservation");
            MenuItem editItem = new MenuItem("Edit Reservation");

            deleteItem.setOnAction(event -> {
                if(currentUser.getIsAnEmployee()){
                    Reservation oldReservation = row.getItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete this reservation?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Reservation selectedReservation = row.getItem();

                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        DeleteReservationFromDatabaseThread deleteReservationFromDatabaseThread =
                                new DeleteReservationFromDatabaseThread(selectedReservation);
                        executor.execute(deleteReservationFromDatabaseThread);

                        executor.shutdown();
                        try {
                            executor.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) { e.printStackTrace(); }
                        searchReservations();

                        DataChange<Reservation, Reservation> dataChange = new DataChange<>("Reservation",
                                oldReservation, null, new Timestamp(System.currentTimeMillis()), currentUser);
                        FileUtils.serializeDataChange(dataChange);
                    }
                }
            });
            editItem.setOnAction(event ->{
                if(currentUser.getIsAnEmployee()) {
                    Reservation selectedReservation = row.getItem();
                    SceneUtils.changeToEditReservation(currentUser, selectedReservation);
                }
            });
            contextMenu.getItems().addAll(deleteItem, editItem);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null)
            );
            return row;
        });

    }


    public void searchReservations() {
        Executor executor = Executors.newSingleThreadExecutor();
        GetReservationsFromDatabaseThread getReservationsFromDatabaseThread = new GetReservationsFromDatabaseThread();
        executor.execute(getReservationsFromDatabaseThread);
        List<Reservation> reservations = getReservationsFromDatabaseThread.getReservations();

        FileUtils.serializeReservations(reservations);
        List<Reservation> filteredReservations = reservations.stream()
                .filter(r -> userComboBox.getValue() == null || r.getUserId().equals(userComboBox.getValue().getId()))
                .toList();

        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(filteredReservations);
        observableReservations.sort(new SortByDate());

        reservationsTableView.setItems(observableReservations);
    }

    public void resetButton() {
        userComboBox.setValue(null);
        searchReservations();
    }
    public void addNewReservation() {
        SceneUtils.changeToAddReservation(currentUser);
    }

    public void changeToShowAccommodationsScene() {
        SceneUtils.changeToShowAccommodationsScreen(currentUser);
    }
    public void changeToSearchReservationsScene() {
        if (currentUser.getIsAnEmployee())
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
