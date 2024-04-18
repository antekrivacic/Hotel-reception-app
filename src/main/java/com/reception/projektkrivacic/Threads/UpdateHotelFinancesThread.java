package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.classes.HotelFinance;

public class UpdateHotelFinancesThread extends DatabaseThread implements Runnable{

    private HotelFinance hotelFinance;

    public UpdateHotelFinancesThread(HotelFinance hotelFinance) {
        this.hotelFinance = hotelFinance;
    }

    @Override
    public void run() {
        super.updateHotelFinances(hotelFinance);
    }
}
