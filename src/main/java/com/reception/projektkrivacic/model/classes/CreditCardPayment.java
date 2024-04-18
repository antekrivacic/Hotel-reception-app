package com.reception.projektkrivacic.model.classes;

import com.reception.projektkrivacic.Threads.UpdateHotelFinancesThread;
import com.reception.projektkrivacic.model.interfaces.Payment;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import javafx.scene.chart.PieChart;

import java.math.BigDecimal;
import java.time.LocalDate;

public final class CreditCardPayment implements Payment {

    private String cardNumber;
    private String cardHolder;
    private LocalDate expiryDate;
    private String cvv;

    public CreditCardPayment(String cardNumber, String cardHolder, LocalDate expiryDate, String cvv) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.expiryDate = expiryDate;
        this.cvv = cvv;
    }

    @Override
    public void pay(BigDecimal amount, HotelFinance hotelFinance) {
        hotelFinance.addIncome(amount);

        UpdateHotelFinancesThread updateHotelFinancesThread = new UpdateHotelFinancesThread(hotelFinance);
        Thread thread = new Thread(updateHotelFinancesThread);
        thread.start();

        DatabaseUtils.updateHotelFinances(hotelFinance);
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public LocalDate getExpiryDate() {
        return expiryDate;
    }

    public void setExpiryDate(LocalDate expiryDate) {
        this.expiryDate = expiryDate;
    }

    public String getCvv() {
        return cvv;
    }

    public void setCvv(String cvv) {
        this.cvv = cvv;
    }
}


