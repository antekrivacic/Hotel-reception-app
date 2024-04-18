package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.EditAddressInDatabaseThread;
import com.reception.projektkrivacic.Threads.EditUserInDatabaseThread;
import com.reception.projektkrivacic.Threads.FindUserAddressIdThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Address;
import com.reception.projektkrivacic.model.classes.AddressBuilder;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.sql.Timestamp;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class EditUserScreenController {

    @FXML
    private Label topLabel;
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private ComboBox<String> roleComboBox;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField postalCodeTextField;

    private User currentUser;
    private User selectedUser;

    public void initialize(User currentUser, User selectedUser) {
        this.currentUser = currentUser;
        this.selectedUser = selectedUser;

        topLabel.setText("Edit user: " + selectedUser.getName());
        usernameTextField.setDisable(true);
        roleComboBox.getItems().addAll("Employee", "Guest");

        usernameTextField.setText(selectedUser.getName());
        firstNameTextField.setText(selectedUser.getFirstName());
        lastNameTextField.setText(selectedUser.getLastName());
        roleComboBox.setValue(selectedUser.getRoleToString());
        streetTextField.setText(selectedUser.getAddress().getStreet());
        cityTextField.setText(selectedUser.getAddress().getCity());
        countryTextField.setText(selectedUser.getAddress().getCountry());
        postalCodeTextField.setText(selectedUser.getAddress().getPostalCode());

    }


    public void edit() {
        String username = usernameTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        String role = roleComboBox.getValue();
        Boolean isAdmin = role.equals("Employee");

        if (isAnyInputEmpty()) { showAlert("Please fill in all required fields: "); return; }

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to edit this user?",
                ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES){
            User oldUser = selectedUser;
            ExecutorService executorService = Executors.newSingleThreadExecutor();

            FindUserAddressIdThread findUserAddressIdThread = new FindUserAddressIdThread(selectedUser);
            executorService.execute(findUserAddressIdThread);
            Long selectedUserAddressId = findUserAddressIdThread.findAddressId();

            while (selectedUserAddressId == null) {
                try {
                    Thread.sleep(100);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                selectedUserAddressId = findUserAddressIdThread.findAddressId();
            }

            Address address = new AddressBuilder()
                    .setId(selectedUserAddressId)
                    .setStreet(streetTextField.getText())
                    .setCity(cityTextField.getText())
                    .setCountry(countryTextField.getText())
                    .setPostalCode(postalCodeTextField.getText())
                    .createAddress();

            selectedUser.setName(username);
            selectedUser.setFirstName(firstName);
            selectedUser.setLastName(lastName);
            selectedUser.setAnEmployee(isAdmin);
            selectedUser.setAddress(address);

            EditAddressInDatabaseThread editAddressInDatabaseThread = new EditAddressInDatabaseThread(selectedUser.getAddress());
            executorService.execute(editAddressInDatabaseThread);

            EditUserInDatabaseThread editUserInDatabaseThread = new EditUserInDatabaseThread(selectedUser);
            executorService.execute(editUserInDatabaseThread);

            Alert alert2 = new Alert(Alert.AlertType.INFORMATION);
            alert2.setTitle("Success");
            alert2.setHeaderText("User successfully edited");
            alert2.showAndWait();

            DataChange<User, User> dataChange = new DataChange<>("User",
                    oldUser, selectedUser, new Timestamp(System.currentTimeMillis()), currentUser);
            FileUtils.serializeDataChange(dataChange);
        }

    }


    private boolean isAnyInputEmpty() {
        return usernameTextField.getText().trim().isEmpty() ||
                firstNameTextField.getText().trim().isEmpty() ||
                lastNameTextField.getText().trim().isEmpty() ||
                roleComboBox.getValue().trim().isEmpty();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}
