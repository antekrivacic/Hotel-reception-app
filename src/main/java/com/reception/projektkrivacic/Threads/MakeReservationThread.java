package com.reception.projektkrivacic.Threads;

import java.time.LocalDate;

public class MakeReservationThread extends DatabaseThread implements Runnable{

    private Long accommodationId;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;
    private String paymentMethod;

    public MakeReservationThread(Long accommodationId, Long userId, LocalDate checkInDate,
                                 LocalDate checkOutDate, String paymentMethod) {
        this.accommodationId = accommodationId;
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public void run() {
        super.makeReservation(accommodationId, userId, checkInDate, checkOutDate, paymentMethod);
    }
}
