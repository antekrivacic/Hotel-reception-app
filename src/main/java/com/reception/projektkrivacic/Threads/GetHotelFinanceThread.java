package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.HotelFinance;

public class GetHotelFinanceThread extends DatabaseThread implements Runnable{

    private HotelFinance hotelFinance;
    public HotelFinance getHotelFinance() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return hotelFinance;
    }

    @Override
    public void run() {
        hotelFinance = super.getHotelFinance();
    }
}
