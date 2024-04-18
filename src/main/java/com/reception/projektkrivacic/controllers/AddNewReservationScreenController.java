package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.GetAllAccommodationsFromDatabaseThread;
import com.reception.projektkrivacic.Threads.GetNextReservationIdThread;
import com.reception.projektkrivacic.exceptions.InvalidReservationDateException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class AddNewReservationScreenController {

    @FXML
    private ComboBox<Accommodation> accommodationComboeBox;
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;

    private User currentUser;

    public void initialize(User user){
        this.currentUser = user;

        Executor executor = Executors.newSingleThreadExecutor();
        GetAllAccommodationsFromDatabaseThread thread = new GetAllAccommodationsFromDatabaseThread();
        executor.execute(thread);
        List<Accommodation> accommodationList = thread.getAccommodations();

        accommodationComboeBox.setItems(FXCollections.observableArrayList(accommodationList));
    }


    public void add() {
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();
        Accommodation selectedAccommodation = accommodationComboeBox.getSelectionModel().getSelectedItem();

        if(isAnyFieldEmpty()){
            Alert("All fields must be filled!", Alert.AlertType.ERROR);
            return;
        }
        else{
            try{
                checkDates(checkInDate, checkOutDate, selectedAccommodation);

                Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to add this reservation?",
                        ButtonType.YES, ButtonType.NO);
                alert.showAndWait();
                if (alert.getResult() == ButtonType.YES) {
                    ExecutorService executor = Executors.newSingleThreadExecutor();
                    GetNextReservationIdThread thread = new GetNextReservationIdThread();
                    executor.execute(thread);

                    Long reservationId = thread.getNextReservationId();
                    try {
                        executor.awaitTermination(300, TimeUnit.MILLISECONDS);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }

                    selectedAccommodation.makeReservation(currentUser, checkInDate, checkOutDate, "Pending");
                    Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Reservation added successfully.");
                    infoAlert.showAndWait();

                    Reservation newReservation = new Reservation(reservationId, selectedAccommodation.getId(),
                            currentUser.getId(), checkInDate, checkOutDate);
                    DataChange<Reservation, Reservation> dataChange = new DataChange<>("Reservation", null,
                            newReservation, new Timestamp(System.currentTimeMillis()), currentUser);
                    FileUtils.serializeDataChange(dataChange);
                }
            } catch (InvalidReservationDateException e){
                Alert(e.getMessage(), Alert.AlertType.ERROR);
            }
        }

    }

    public void checkDates(LocalDate checkInDate, LocalDate checkOutDate, Accommodation accommodation)
            throws InvalidReservationDateException {

        if(checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)){
            throw new InvalidReservationDateException("Check out date must be after check in date.");
        }
        else if(checkInDate.isBefore(LocalDate.now())){
            throw new InvalidReservationDateException("Check in date must be in the future.");
        }
        else if(!DatabaseUtils.isAccommodationAvailable(accommodation, checkInDate, checkOutDate)){
            throw new InvalidReservationDateException("Accommodation is not available for selected dates.");
        }
    }
    public void Alert(String message, Alert.AlertType alertType) {
        Alert alert = new Alert(alertType);
        alert.setTitle("Error");
        alert.setHeaderText("Error");
        alert.setContentText(message);
        alert.showAndWait();
    }
    public Boolean isAnyFieldEmpty(){
        return checkInDatePicker.getValue() == null
                || checkOutDatePicker.getValue() == null
                || accommodationComboeBox.getSelectionModel().isEmpty();
    }
}
