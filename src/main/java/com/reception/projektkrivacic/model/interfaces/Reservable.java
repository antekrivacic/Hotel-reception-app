package com.reception.projektkrivacic.model.interfaces;

import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.*;

import java.time.LocalDate;

public interface Reservable {

    void makeReservation(User user, LocalDate checkInDate, LocalDate checkOutDate, String paymentMethod);
    void cancelReservation();
}
