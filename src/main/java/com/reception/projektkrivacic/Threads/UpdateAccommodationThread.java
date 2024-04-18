package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Accommodation;

public class UpdateAccommodationThread extends DatabaseThread implements Runnable{

    private final Accommodation accommodation;

    public UpdateAccommodationThread(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public void run() {
        super.updateAccommodation(accommodation);
    }
}
