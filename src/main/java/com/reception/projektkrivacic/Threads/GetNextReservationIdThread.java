package com.reception.projektkrivacic.Threads;

public class GetNextReservationIdThread extends DatabaseThread implements Runnable{

    private Long reservationId;

    public Long getReservationId() {
        try{
            Thread.sleep(2000);
        }
        catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return reservationId;
    }

    @Override
    public void run() {
        reservationId = super.getNextReservationId();
    }
}
