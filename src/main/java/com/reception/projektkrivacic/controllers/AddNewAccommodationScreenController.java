package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.AddAccommodationThread;
import com.reception.projektkrivacic.Threads.FindNextAccommodationIdThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import javafx.collections.FXCollections;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class AddNewAccommodationScreenController {

    @FXML
    private TextField roomNumberTextField;
    @FXML
    private ChoiceBox<String> roomTypeChoiceBox;
    @FXML
    private ChoiceBox<String> floorChoiceBox;
    @FXML
    private ChoiceBox<String> numberOfBedsChoiceBox;
    @FXML
    private TextField priceTextField;

    private User currentUser;

    public void initialize(User currentUser) {
        this.currentUser = currentUser;

        roomTypeChoiceBox.setItems(FXCollections.observableArrayList("Single", "Double", "Suite"));
        floorChoiceBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6"));
        numberOfBedsChoiceBox.setItems(FXCollections.observableArrayList("1", "2", "3", "4", "5", "6"));
    }



    public void edit(){
        String roomNumber = roomNumberTextField.getText();
        String roomType = roomTypeChoiceBox.getValue();
        String floor = floorChoiceBox.getValue();
        String numberOfBeds = numberOfBedsChoiceBox.getValue();
        String price = priceTextField.getText();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to edit this entity?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if(isAnyFieldEmpty()){
                alert = new Alert(Alert.AlertType.ERROR, "All fields must be filled!", ButtonType.OK);
                alert.showAndWait();
                return;
            }

            Executor executor = Executors.newSingleThreadExecutor();

            FindNextAccommodationIdThread findNextAccommodationIdThread = new FindNextAccommodationIdThread();
            executor.execute(findNextAccommodationIdThread);
            Long newId = findNextAccommodationIdThread.getNextAccommodationId();

            Accommodation newAccommodation = new Accommodation(newId, roomNumber, roomType, Integer.parseInt(floor),
                    Integer.parseInt(numberOfBeds), new BigDecimal(price), true);

            AddAccommodationThread addAccommodationThread = new AddAccommodationThread(newAccommodation);
            executor.execute(addAccommodationThread);

            alert = new Alert(Alert.AlertType.INFORMATION, "Accommodation added successfully!", ButtonType.OK);
            alert.showAndWait();
        }
    }

    public Boolean isAnyFieldEmpty(){
        if(roomNumberTextField.getText().isEmpty() || roomTypeChoiceBox.getValue() == null || floorChoiceBox.getValue() == null
                || numberOfBedsChoiceBox.getValue() == null || priceTextField.getText().isEmpty()){
            return true;
        }
        return false;
    }
}
