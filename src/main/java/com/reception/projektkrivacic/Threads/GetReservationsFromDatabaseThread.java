package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Reservation;

import java.util.List;

public class GetReservationsFromDatabaseThread extends DatabaseThread implements Runnable{

    List<Reservation> reservations;

    public List<Reservation> getReservations() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return reservations;
    }

    @Override
    public void run() {
        reservations = super.getReservationsFromDatabase();
    }
}
