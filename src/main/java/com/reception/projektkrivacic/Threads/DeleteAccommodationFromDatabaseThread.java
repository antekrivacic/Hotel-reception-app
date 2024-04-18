package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Accommodation;

public class DeleteAccommodationFromDatabaseThread extends DatabaseThread implements Runnable{
    private final Accommodation accommodation;

    public DeleteAccommodationFromDatabaseThread(Accommodation accommodation){
        this.accommodation = accommodation;
    }

    @Override
    public void run() {
        super.deleteAccommodationFromDatabase(accommodation);
    }
}
