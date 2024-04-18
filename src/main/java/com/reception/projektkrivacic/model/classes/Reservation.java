package com.reception.projektkrivacic.model.classes;

import java.io.Serializable;
import java.time.LocalDate;

public class Reservation implements Serializable {

    private Long reservationId;
    private Long accommodationId;
    private Long userId;
    private LocalDate checkInDate;
    private LocalDate checkOutDate;

    public Reservation(Long reservationId, Long accommodationId, Long userId, LocalDate checkInDate, LocalDate checkOutDate) {
        this.reservationId = reservationId;
        this.accommodationId = accommodationId;
        this.userId = userId;
        this.checkInDate = checkInDate;
        this.checkOutDate = checkOutDate;
    }


    public Long getReservationId() {
        return reservationId;
    }

    public void setReservationId(Long reservationId) {
        this.reservationId = reservationId;
    }

    public Long getAccommodationId() {
        return accommodationId;
    }

    public void setAccommodationId(Long accommodationId) {
        this.accommodationId = accommodationId;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public LocalDate getCheckInDate() {
        return checkInDate;
    }

    public void setCheckInDate(LocalDate checkInDate) {
        this.checkInDate = checkInDate;
    }

    public LocalDate getCheckOutDate() {
        return checkOutDate;
    }

    public void setCheckOutDate(LocalDate checkOutDate) {
        this.checkOutDate = checkOutDate;
    }


    @Override
    public String toString() {
        return "Id=" + reservationId +
                ", AccommodationId=" + accommodationId +
                ", UserId=" + userId +
                ", CheckInDate=" + checkInDate +
                ", CheckOutDate=" + checkOutDate +
                '}';
    }
}
