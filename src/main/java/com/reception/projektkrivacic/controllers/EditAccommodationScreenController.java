package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.sql.Timestamp;

public class EditAccommodationScreenController {

    @FXML
    private Label accommodationIdLabel;
    @FXML
    private TextField roomNumberTextField;
    @FXML
    private ChoiceBox<String> roomTypeChoiceBox;
    @FXML
    private ChoiceBox<Integer> floorChoiceBox;
    @FXML
    private ChoiceBox<Integer> numberOfBedsChoiceBox;
    @FXML
    private TextField priceTextField;
    private User currentUser;
    private Accommodation selectedAccommodation;


    public void initialize(User user, Accommodation accommodation) {
        currentUser = user;
        selectedAccommodation = accommodation;

        accommodationIdLabel.setText("Edit accommodation #" + accommodation.getId().toString());
        roomNumberTextField.setText(accommodation.getName());
        roomTypeChoiceBox.getItems().addAll("Single room", "Double room", "Suite");
        roomTypeChoiceBox.setValue(accommodation.getRoomType());
        floorChoiceBox.getItems().addAll(1, 2, 3, 4, 5, 6);
        floorChoiceBox.setValue(accommodation.getAccommodationFloor());
        numberOfBedsChoiceBox.getItems().addAll(1, 2, 3, 4);
        numberOfBedsChoiceBox.setValue(accommodation.getNumberOfBeds());
        priceTextField.setText(accommodation.getAccommodationPrice().toString());
    }

    public void edit() {
        Accommodation accommodationOld = selectedAccommodation;

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to edit this entity?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {

            selectedAccommodation.setName(roomNumberTextField.getText());
            selectedAccommodation.setRoomType(roomTypeChoiceBox.getValue());
            selectedAccommodation.setAccommodationFloor(floorChoiceBox.getValue());
            selectedAccommodation.setNumberOfBeds(numberOfBedsChoiceBox.getValue());
            selectedAccommodation.setAccommodationPrice(new BigDecimal(priceTextField.getText()));

            DatabaseUtils.updateAccommodation(selectedAccommodation);

            alert = new Alert(Alert.AlertType.INFORMATION, "Accommodation updated successfully!", ButtonType.OK);
            alert.showAndWait();

            DataChange<Accommodation, Accommodation> dataChange = new DataChange<>("Accommodation", accommodationOld,
                    selectedAccommodation, new Timestamp(System.currentTimeMillis()), currentUser);
            FileUtils.serializeDataChange(dataChange);
        }
    }

}
