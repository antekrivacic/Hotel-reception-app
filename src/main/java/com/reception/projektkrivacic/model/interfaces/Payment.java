package com.reception.projektkrivacic.model.interfaces;

import com.reception.projektkrivacic.model.classes.CashPayment;
import com.reception.projektkrivacic.model.classes.CreditCardPayment;
import com.reception.projektkrivacic.model.classes.HotelFinance;

import java.math.BigDecimal;

public sealed interface Payment permits CashPayment, CreditCardPayment{

    void pay(BigDecimal amount, HotelFinance hotelFinance);
}
