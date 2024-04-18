package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.TextArea;

import java.io.File;
import java.sql.Timestamp;
import java.time.LocalDate;

public class EditReservationScreenController {

    @FXML
    private Label reservationIdLabel;
    @FXML
    private TextArea accommodationTextArea;
    @FXML
    private TextArea userTextArea;
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;


    private User currentUser;
    private Reservation selectedReservation;


    public void initialize(User user, Reservation reservation) {
        this.currentUser = user;
        this.selectedReservation = reservation;
        reservationIdLabel.setText("Edit reservation" + selectedReservation.getReservationId().toString());

        Accommodation accommodation = DatabaseUtils.getAccommodationFromDatabase(selectedReservation.getAccommodationId());
        accommodationTextArea.setText(accommodation.toString());
        accommodationTextArea.setEditable(false);
        accommodationTextArea.setWrapText(true);

        userTextArea.setText(currentUser.toString());
        userTextArea.setEditable(false);
        userTextArea.setWrapText(true);

        checkInDatePicker.setValue(selectedReservation.getCheckInDate());
        checkOutDatePicker.setValue(selectedReservation.getCheckOutDate());
    }


    public void edit() {
        selectedReservation.setCheckInDate(checkInDatePicker.getValue());
        selectedReservation.setCheckOutDate(checkOutDatePicker.getValue());

        Alert confirmationAlert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to edit this reservation?");
        confirmationAlert.showAndWait();
        if (confirmationAlert.getResult().getText().equals("OK")) {
            Reservation oldReservation = selectedReservation;

            selectedReservation.setCheckInDate(checkInDatePicker.getValue());
            selectedReservation.setCheckOutDate(checkOutDatePicker.getValue());
            DatabaseUtils.updateReservationInDatabase(selectedReservation);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION, "Reservation edited successfully!");
            alert2.show();

            DataChange<Reservation, Reservation> dataChange = new DataChange<>("Reservation", oldReservation,
                    selectedReservation, new Timestamp(System.currentTimeMillis()), currentUser);
            FileUtils.serializeDataChange(dataChange);
        }

    }
}
