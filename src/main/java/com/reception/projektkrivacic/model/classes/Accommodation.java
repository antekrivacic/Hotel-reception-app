package com.reception.projektkrivacic.model.classes;

import com.reception.projektkrivacic.Threads.GetNextReservationIdThread;
import com.reception.projektkrivacic.Threads.MakeReceiptThread;
import com.reception.projektkrivacic.Threads.MakeReservationThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.interfaces.Payment;
import com.reception.projektkrivacic.model.interfaces.Reservable;
import com.reception.projektkrivacic.utils.DatabaseUtils;

import java.io.Serializable;
import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.concurrent.*;

public class Accommodation extends NamedEntity implements Reservable, Serializable {
    private static final long serialVersionUID = -3419558179225874861L;

    private String roomType;
    private Integer accommodationFloor;
    private Integer numberOfBeds;
    private BigDecimal accommodationPrice;
    private Boolean isAvailable;

    public Accommodation(Long accommodationId, String roomNumber, String roomType, Integer accommodationFloor,
                         Integer numberOfBeds, BigDecimal accommodationPrice, Boolean isAvailable) {
        super(accommodationId, roomNumber);
        this.roomType = roomType;
        this.accommodationFloor = accommodationFloor;
        this.numberOfBeds = numberOfBeds;
        this.accommodationPrice = accommodationPrice;
        this.isAvailable = isAvailable;
    }
    public void setRoomType(String roomType) {
        this.roomType = roomType;
    }
    public String getRoomType() {
        return roomType;
    }

    public Boolean getAvailable() {
        return isAvailable;
    }
    public void setAvailable(Boolean available) {
        isAvailable = available;
    }
    public Integer getAccommodationFloor() {
        return accommodationFloor;
    }

    public void setAccommodationFloor(Integer accommodationFloor) {
        this.accommodationFloor = accommodationFloor;
    }

    public Integer getNumberOfBeds() {
        return numberOfBeds;
    }

    public void setNumberOfBeds(Integer numberOfBeds) {
        this.numberOfBeds = numberOfBeds;
    }

    public BigDecimal getAccommodationPrice() {
        return accommodationPrice;
    }

    public void setAccommodationPrice(BigDecimal accommodationPrice) {
        this.accommodationPrice = accommodationPrice;
    }

    @Override
    public void makeReservation(User user, LocalDate checkInDate, LocalDate checkOutDate, String paymentMethod) {
        ExecutorService executorService = Executors.newSingleThreadExecutor();

        MakeReservationThread makeReservationThread = new MakeReservationThread(getId(), user.getId(), checkInDate,
                checkOutDate, paymentMethod);
        executorService.execute(makeReservationThread);

        GetNextReservationIdThread getNextReservationIdThread = new GetNextReservationIdThread();
        executorService.execute(getNextReservationIdThread);
        Long reservationId = getNextReservationIdThread.getReservationId();

        MakeReceiptThread makeReceiptThread = new MakeReceiptThread(reservationId, getAccommodationPrice(), paymentMethod);
        executorService.execute(makeReceiptThread);

        //executorService.shutdown();
//        DatabaseUtils.makeReservation(getId(), user.getId(), checkInDate, checkOutDate, paymentMethod);
//        Long newReservationId = DatabaseUtils.getNextReservationId();
//        DatabaseUtils.makeReceipt(newReservationId, getAccommodationPrice(), paymentMethod);
    }
    @Override
    public void cancelReservation() {
        isAvailable = true;
    }

    @Override
    public String toString() {
        return  "Type='" + roomType + '\'' +
                ", Floor=" + accommodationFloor +
                ", NumOfBeds=" + numberOfBeds +
                ", Price=" + accommodationPrice +
                '}';
    }


}
