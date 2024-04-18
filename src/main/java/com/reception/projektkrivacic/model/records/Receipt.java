package com.reception.projektkrivacic.model.records;

import com.reception.projektkrivacic.model.interfaces.Payment;

import java.io.Serializable;
import java.math.BigDecimal;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.util.Objects;

public record Receipt(
        Long receiptId,
        Long reservationId,
        BigDecimal totalAmount,
        String paymentMethod,
        Timestamp paymentTime
) implements Serializable{
    @Override
    public Long receiptId() {
        return receiptId;
    }

    @Override
    public Long reservationId() {
        return reservationId;
    }

    @Override
    public BigDecimal totalAmount() {
        return totalAmount;
    }

    @Override
    public String paymentMethod() {
        return paymentMethod;
    }

    @Override
    public Timestamp paymentTime() {
        return paymentTime;
    }


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Receipt receipt = (Receipt) o;
        return Objects.equals(receiptId, receipt.receiptId) && Objects.equals(reservationId, receipt.reservationId) && Objects.equals(totalAmount, receipt.totalAmount) && Objects.equals(paymentMethod, receipt.paymentMethod) && Objects.equals(paymentTime, receipt.paymentTime);
    }

    @Override
    public int hashCode() {
        return Objects.hash(receiptId, reservationId, totalAmount, paymentMethod, paymentTime);
    }


    @Override
    public String toString() {
        return "Id=" + receiptId +
                ", ReservationId=" + reservationId +
                ", TotalAmount=" + totalAmount +
                ", PaymentMethod='" + paymentMethod + '\'' +
                ", PaymentTime=" + paymentTime +
                '}';
    }
}
