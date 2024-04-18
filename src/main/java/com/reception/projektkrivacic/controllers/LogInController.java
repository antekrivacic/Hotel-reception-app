package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.GetUserThread;
import com.reception.projektkrivacic.exceptions.UserDoesntExistException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.fxml.FXML;

import javafx.scene.control.Alert;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;


public class LogInController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private PasswordField passwordTextField;


    public void userLogin() {
        List<User> users = FileUtils.loadUsers();
        String username = usernameTextField.getText();
        String password = passwordTextField.getText();


        if (isAnyInputEmpty()) {
            showAlert("All fields must be filled!");
            return;
        }
        try {
            FileUtils.findUser(username);
        }
        catch (UserDoesntExistException e){
            showAlert(e.getMessage());
            return;
        }
        for (User user : users) {
            if (user.getName().equals(username) && user.getPassword().equals(user.hashPassword(password)) ) {
                Executor executor = Executors.newSingleThreadExecutor();
                GetUserThread getUserThread = new GetUserThread(user.getName());
                executor.execute(getUserThread);

                User userDB = getUserThread.getUser();
                while (userDB == null) {
                    try {
                        Thread.sleep(100);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    userDB = getUserThread.getUser();
                }

                SceneUtils.changeToShowAccommodationsScreen(userDB);
                return;
            }
        }
        showAlert("Wrong username or password!");
    }

    private boolean isAnyInputEmpty() {
        return usernameTextField.getText().trim().isEmpty() ||
                passwordTextField.getText().trim().isEmpty();
    }

    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    public void goToSignup() {
        SceneUtils.loginSignupChangeScene("signUpScreen.fxml", "Sign Up Screen", 800, 400);
    }

}
