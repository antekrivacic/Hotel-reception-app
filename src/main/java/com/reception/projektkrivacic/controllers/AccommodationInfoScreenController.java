package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.GetNextReservationIdThread;
import com.reception.projektkrivacic.Threads.GetReservationsFromDatabaseThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.classes.Reservation;

import com.reception.projektkrivacic.utils.DatabaseUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.util.Callback;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;
import java.util.stream.Collectors;

public class AccommodationInfoScreenController {

    @FXML
    private ImageView accommodationImageView1;
    @FXML
    private ImageView accommodationImageView2;
    @FXML
    private Label accommodationTypeLabel;

    @FXML
    private TableView<Reservation> reservationTableView;
    @FXML
    private TableColumn<Reservation, String> checkInDateColumn;
    @FXML
    private TableColumn<Reservation, String> checkOutDateColumn;

    private User currentUser;
    private Accommodation selectedAccommodation;

    public void initialize(User user, Accommodation accommodation) {
        currentUser = user;
        selectedAccommodation = accommodation;

        switch (selectedAccommodation.getRoomType()) {
            case "Single room":
                accommodationTypeLabel.setText("Single room");
                accommodationImageView1.setImage(new Image(getClass().getResource("/images/single1.jpg").toExternalForm()));
                accommodationImageView2.setImage(new Image(getClass().getResource("/images/single2.jpg").toExternalForm()));
                break;
            case "Double room":
                accommodationTypeLabel.setText("Double room");
                accommodationImageView1.setImage(new Image(getClass().getResource("/images/double1.jpg").toExternalForm()));
                accommodationImageView2.setImage(new Image(getClass().getResource("/images/double2.png").toExternalForm()));
                break;
            case "Suite":
                accommodationTypeLabel.setText("Suite");
                break;
        }

        checkInDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getCheckInDate().toString());
            }
        });
        checkOutDateColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Reservation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Reservation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getCheckOutDate().toString());
            }
        });

        seeAvailability();
    }


    public void seeAvailability() {
        Executor executor = Executors.newSingleThreadExecutor();
        GetReservationsFromDatabaseThread getReservationsFromDatabaseThread = new GetReservationsFromDatabaseThread();
        executor.execute(getReservationsFromDatabaseThread);

        List<Reservation> reservations = getReservationsFromDatabaseThread.getReservations();

        List<Reservation> filteredReservations = reservations.stream()
                .filter(reservation -> reservation.getAccommodationId().equals(selectedAccommodation.getId()))
                .collect(Collectors.toList());

        ObservableList<Reservation> observableReservations = FXCollections.observableArrayList(filteredReservations);
        reservationTableView.setItems(observableReservations);
    }

}
