package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Reservation;

public class DeleteReservationFromDatabaseThread extends DatabaseThread implements Runnable{

    private Reservation reservation;

    public DeleteReservationFromDatabaseThread(Reservation reservation) {
        this.reservation = reservation;
    }


    @Override
    public void run() {
        super.deleteReservationFromDatabase(reservation);
    }
}
