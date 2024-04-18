package com.reception.projektkrivacic.Threads;

import java.math.BigDecimal;

public class MakeReceiptThread extends DatabaseThread implements Runnable{
    private Long reservationId;
    private BigDecimal totalAmount;
    private String paymentMethod;

    public MakeReceiptThread(Long reservationId, BigDecimal totalAmount, String paymentMethod) {
        this.reservationId = reservationId;
        this.totalAmount = totalAmount;
        this.paymentMethod = paymentMethod;
    }

    @Override
    public void run() {
        super.makeReceipt(reservationId, totalAmount, paymentMethod);
    }
}
