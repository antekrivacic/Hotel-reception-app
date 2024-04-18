package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Accommodation;

import java.util.List;

public class GetAllAccommodationsFromDatabaseThread extends DatabaseThread implements Runnable{

    private List<Accommodation> accommodations;

    public List<Accommodation> getAccommodations() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return accommodations;
    }


    @Override
    public void run() {
        accommodations = super.getAllAccommodationsFromDatabase();
    }
}
