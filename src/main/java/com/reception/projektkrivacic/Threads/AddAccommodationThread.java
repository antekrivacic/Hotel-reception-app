package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Accommodation;

public class AddAccommodationThread extends DatabaseThread implements Runnable{

    private Accommodation accommodation;

    public AddAccommodationThread(Accommodation accommodation) {
        this.accommodation = accommodation;
    }

    @Override
    public void run() {
        super.addAccommodation(accommodation);
    }
}
