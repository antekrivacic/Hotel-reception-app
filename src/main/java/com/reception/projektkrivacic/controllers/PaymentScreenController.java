package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.GetHotelFinanceThread;
import com.reception.projektkrivacic.exceptions.InvalidCreditCardException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.classes.CreditCardPayment;
import com.reception.projektkrivacic.model.classes.HotelFinance;
import javafx.fxml.FXML;
import javafx.scene.control.*;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class PaymentScreenController {

    @FXML
    private TextField cardNumberTextField;
    @FXML
    private DatePicker expirationDateDatePicker;
    @FXML
    private TextField cvvTextField;
    @FXML
    private Label instructionLabel;
    @FXML
    private Label creditCardNumberLabel;
    @FXML
    private Label expirationDateLabel;
    @FXML
    private Label cvvLabel;
    @FXML
    private Button payButton;
    private User currentUser;
    private Accommodation accommodation;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String cardHolder;
    private String paymentMethod;

    public void initialize(Accommodation accommodation, User user, LocalDate checkInDate, LocalDate checkOutDate,
                           String paymentMethod) {
        currentUser = user;
        this.accommodation = accommodation;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.paymentMethod = paymentMethod;
        cardHolder = currentUser.getFirstName().toUpperCase() + " " + currentUser.getLastName().toUpperCase();

        if (paymentMethod.equals("Cash")) {
            hideCreditCardFields();
        }
    }



    public void pay() {
        String cardNumber = cardNumberTextField.getText().replaceAll("\\s","");
        LocalDate expirationDate = expirationDateDatePicker.getValue();
        String cvv = cvvTextField.getText().trim();

        Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to pay?", ButtonType.YES, ButtonType.NO);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.YES) {
            if(paymentMethod.equals("Credit Card")){
                if (isAnyInputEmpty()) {
                    showAlert("Please fill in all the fields.");
                    return;
                }
                try {
                    checkCreditCardNumber(cardNumber);
                    checkCreditCardDate(expirationDate);
                    checkCreditCardCvv(cvv);
                } catch (InvalidCreditCardException e) {
                    showAlert(e.getMessage());
                    return;
                }
                BigDecimal amount = accommodation.getAccommodationPrice();
                CreditCardPayment creditCardPayment = new CreditCardPayment(cardNumber, cardHolder, expirationDate, cvv);

                GetHotelFinanceThread getHotelFinanceThread = new GetHotelFinanceThread();
                Executor executor = Executors.newSingleThreadExecutor();
                executor.execute(getHotelFinanceThread);

                HotelFinance hotelFinance = getHotelFinanceThread.getHotelFinance();

                creditCardPayment.pay(amount, hotelFinance);
            }

            accommodation.makeReservation(currentUser, checkInDate, checkOutDate, paymentMethod);

            Alert infoAlert = new Alert(Alert.AlertType.INFORMATION, "Payment successful!", ButtonType.OK);
            infoAlert.showAndWait();
        }
    }

    public Boolean isAnyInputEmpty() {
        return cardNumberTextField.getText().isEmpty() || expirationDateDatePicker.getValue() == null ||
                cvvTextField.getText().isEmpty();
    }

    public void checkCreditCardNumber(String cardNumber) {
        if (cardNumber.length() != 16 || !cardNumber.matches("[0-9]+")) {
            throw new InvalidCreditCardException("Invalid credit card number.");
        }
    }
    public void checkCreditCardDate(LocalDate expirationDate) {
        if (expirationDate.isBefore(LocalDate.now())) {
            throw new InvalidCreditCardException("The credit card you inserted has expired.");
        }
    }
    public void checkCreditCardCvv(String cvv) {
        if (cvv.length() != 3 || !cvv.matches("[0-9]+")) {
            throw new InvalidCreditCardException("Invalid CVV.");
        }
    }
    private void showAlert(String message) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
    private void hideCreditCardFields() {
        creditCardNumberLabel.setVisible(false);
        expirationDateLabel.setVisible(false);
        cvvLabel.setVisible(false);
        cardNumberTextField.setVisible(false);
        expirationDateDatePicker.setVisible(false);
        cvvTextField.setVisible(false);
        instructionLabel.setText("Please proceed to the reception to pay in cash.");
        payButton.setText("Done");
    }
}
