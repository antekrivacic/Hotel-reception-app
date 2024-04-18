package com.reception.projektkrivacic.Threads;

public class FindNextAddressIdThread extends DatabaseThread implements Runnable{

    private Long addressId;

    public Long getAddressId() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return addressId;
    }

    @Override
    public void run() {
        addressId = super.findNextAddressId();
    }
}
