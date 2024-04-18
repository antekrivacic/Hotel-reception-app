package com.reception.projektkrivacic.model.classes;

import com.reception.projektkrivacic.model.interfaces.Payment;

import java.math.BigDecimal;

public final class CashPayment implements Payment {

    @Override
    public void pay(BigDecimal amount, HotelFinance hotelFinance) {
        hotelFinance.addIncome(amount);
    }

}
