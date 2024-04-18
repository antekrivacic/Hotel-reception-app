package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Address;

public class EditAddressInDatabaseThread extends DatabaseThread implements Runnable{

    private Address address;

    public EditAddressInDatabaseThread(Address address) {
        this.address = address;
    }

    @Override
    public void run() {
        super.editAddressInDatabase(address);
    }
}
