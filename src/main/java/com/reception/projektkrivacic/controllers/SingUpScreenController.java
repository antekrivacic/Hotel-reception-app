package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.*;
import com.reception.projektkrivacic.exceptions.InvalidUsernameFormatException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Address;
import com.reception.projektkrivacic.model.classes.AddressBuilder;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;

import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class SingUpScreenController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;
    @FXML
    private PasswordField reapetedPasswordTextField;
    @FXML
    private RadioButton yesRadioButton;
    @FXML
    private RadioButton noRadioButton;
    @FXML
    private PasswordField adminPasswordPasswordField;
    @FXML
    private TextField firstNameTextField;
    @FXML
    private TextField lastNameTextField;
    @FXML
    private TextField streetTextField;
    @FXML
    private TextField cityTextField;
    @FXML
    private TextField countryTextField;
    @FXML
    private TextField postalCodeTextField;

    public void initialize() {
        ToggleGroup adminToggleGroup = new ToggleGroup();
        yesRadioButton.setToggleGroup(adminToggleGroup);
        noRadioButton.setToggleGroup(adminToggleGroup);
        noRadioButton.setSelected(true);
    }
    public void userSignup() {
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();
        String repeatedPassword = reapetedPasswordTextField.getText();
        String firstName = firstNameTextField.getText();
        String lastName = lastNameTextField.getText();
        Boolean isAdmin = false;

        if(!password.equals(repeatedPassword)){ showAlert("Passwords do not match!"); return;}
        else if(password.length() < 6){ showAlert("Password must be at least 6 characters long!"); return;}
        else if(yesRadioButton.isSelected()){
            String adminPassword = adminPasswordPasswordField.getText();
            String adminPasswordFromFile = FileUtils.getAdminPassword();
            if(!adminPassword.equals(adminPasswordFromFile)){
                showAlert("Wrong admin password!");
                return;
            }
            isAdmin = true;
        }
        else if (isAnyInputEmpty()) { showAlert("Please fill in all required fields: "); return; }
        try{
            usernameCheck(username);
        }catch (InvalidUsernameFormatException e){
            showAlert(e.getMessage());
            return;
        }

        Address address = new AddressBuilder()
                .setStreet(streetTextField.getText())
                .setCity(cityTextField.getText())
                .setCountry(countryTextField.getText())
                .setPostalCode(postalCodeTextField.getText())
                .createAddress();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        FindNextAddressIdThread findNextAddressIdThread = new FindNextAddressIdThread();
        executor.execute(findNextAddressIdThread);

        while (findNextAddressIdThread.getAddressId() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }
        address.setId(findNextAddressIdThread.getAddressId());

        AddAddressToDatabaseThread addAddressToDatabaseThread = new AddAddressToDatabaseThread(address);
        executor.execute(addAddressToDatabaseThread);

        FindNextUserIdThread findNextUserIdThread = new FindNextUserIdThread();
        executor.execute(findNextUserIdThread);

        while (findNextUserIdThread.getUserId() == null) {
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        Long id = findNextUserIdThread.getUserId();
        User userDB = new User(id, username, firstName, lastName, isAdmin, address);
        User userFile = new User(username, password);

        try {
            FileUtils.signUpUser(userFile);

            SignUpUserThread signUpUserThread = new SignUpUserThread(userDB);
            executor.execute(signUpUserThread);

        }
        catch (InvalidUsernameFormatException e){
            showAlert(e.getMessage());
            return;
        }

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText("User successfully created");
        alert.setContentText("You can now log in");
        alert.showAndWait();

        try {
            executor.awaitTermination(300, java.util.concurrent.TimeUnit.MILLISECONDS);
        }
        catch (InterruptedException e) {
            throw new RuntimeException(e);
        }

        GetUsersFromDatabaseThread getUsersFromDatabaseThread = new GetUsersFromDatabaseThread();
        executor.execute(getUsersFromDatabaseThread);

        Set<User> users = getUsersFromDatabaseThread.getUsers();
        FileUtils.serializeUsers(users);

        SceneUtils.loginSignupChangeScene("loginScreen.fxml", "Log in Screen", 600, 400);
    }



    private boolean isAnyInputEmpty() {
        return usernameTextField.getText().trim().isEmpty() ||
                passwordTextField.getText().trim().isEmpty() ||
                reapetedPasswordTextField.getText().trim().isEmpty() ||
                firstNameTextField.getText().trim().isEmpty() ||
                lastNameTextField.getText().trim().isEmpty();
    }
    private static void usernameCheck(String username) throws InvalidUsernameFormatException {
        if(username.contains(" ")){
            throw new InvalidUsernameFormatException("Username should be one word!");
        }
        if(FileUtils.doesUserExist(username)){
            throw new InvalidUsernameFormatException("Username already exists!");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToLogin() {
        SceneUtils.loginSignupChangeScene ("loginScreen.fxml", "Log in Screen", 600, 400);
    }

}
