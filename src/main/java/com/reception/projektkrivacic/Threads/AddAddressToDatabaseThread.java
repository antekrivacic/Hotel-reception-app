package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.Address;

public class AddAddressToDatabaseThread extends DatabaseThread implements Runnable{

    private Address address;

    public AddAddressToDatabaseThread(Address address) {
        this.address = address;
    }

    @Override
    public void run() {
        super.addAddressToDatabase(address);
    }
}
