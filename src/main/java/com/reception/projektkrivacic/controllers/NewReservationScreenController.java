package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.IsAccommodationAvailableThread;
import com.reception.projektkrivacic.exceptions.InvalidReservationDateException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.time.LocalDate;

public class NewReservationScreenController {
    @FXML
    private DatePicker checkInDatePicker;
    @FXML
    private DatePicker checkOutDatePicker;
    @FXML
    private ChoiceBox<String> paymentMethodChoiceBox;
    @FXML
    private Label welcomeText;

    private Stage reservationStage;
    private User currentUser;
    private Accommodation accommodation;

    public void initialize(User user, Accommodation accommodation, Stage reservationStage) {
        this.accommodation = accommodation;
        this.currentUser = user;
        this.reservationStage = reservationStage;
        welcomeText.setText("Reservation for " + accommodation.getRoomType());

        paymentMethodChoiceBox.setItems(FXCollections.observableArrayList("Cash", "Credit Card"));
    }

    public void procceedButton(){
        LocalDate checkInDate = checkInDatePicker.getValue();
        LocalDate checkOutDate = checkOutDatePicker.getValue();
        String paymentMethod = paymentMethodChoiceBox.getValue();

        if(isAnyValueEmpty()){
            showAlert("Please fill in all fields.");
            return;
        }
        try{
            checkDates(checkInDate, checkOutDate, accommodation);
        } catch (InvalidReservationDateException e){
            showAlert(e.getMessage());
            return;
        }

        SceneUtils.changeToPaymentScreen(accommodation, currentUser, checkInDate, checkOutDate, paymentMethod,
                reservationStage);

    }

    public Boolean isAnyValueEmpty(){
        return checkInDatePicker.getValue() == null || checkOutDatePicker.getValue() == null ||
                paymentMethodChoiceBox.getValue() == null;
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    public void checkDates(LocalDate checkInDate, LocalDate checkOutDate, Accommodation accommodation)
            throws InvalidReservationDateException {

        IsAccommodationAvailableThread isAccommodationAvailableThread = new IsAccommodationAvailableThread(accommodation,
                checkInDate, checkOutDate);
        isAccommodationAvailableThread.run();
        if(checkInDate.isAfter(checkOutDate) || checkInDate.isEqual(checkOutDate)){
            throw new InvalidReservationDateException("Check out date must be after check in date.");
        }
        else if(checkInDate.isBefore(LocalDate.now())){
            throw new InvalidReservationDateException("Check in date must be in the future.");
        }
        else if(!isAccommodationAvailableThread.getIsAccommodationAvailable())
            throw new InvalidReservationDateException("Accommodation is not available for selected dates.");
    }

}