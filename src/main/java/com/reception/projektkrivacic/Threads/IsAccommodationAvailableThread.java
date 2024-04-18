package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Accommodation;

import java.time.LocalDate;

public class IsAccommodationAvailableThread extends DatabaseThread implements Runnable{

    public Boolean isAvailable;
    public Accommodation accommodation;
    public LocalDate checkInDate;
    public LocalDate checkOutDate;

    public IsAccommodationAvailableThread(Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate) {
        this.accommodation = accommodation;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }

    public Boolean getIsAccommodationAvailable() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return isAvailable;
    }

    @Override
    public void run() {
        isAvailable = super.isAccommodationAvailable(accommodation, checkInDate, checkOutDate);
    }
}
