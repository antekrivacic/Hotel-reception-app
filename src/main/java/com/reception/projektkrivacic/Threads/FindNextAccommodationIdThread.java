package com.reception.projektkrivacic.Threads;

public class FindNextAccommodationIdThread extends DatabaseThread implements Runnable{

    private Long nextAccommodationId;

    public Long getNextAccommodationId() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return nextAccommodationId;
    }


    @Override
    public void run() {
        nextAccommodationId = super.findNextAccommodationId();
    }
}
