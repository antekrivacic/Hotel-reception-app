package com.reception.projektkrivacic.sort;

import com.reception.projektkrivacic.model.classes.Reservation;

import java.util.Comparator;

public class SortByDate implements Comparator<Reservation> {

    @Override
    public int compare(Reservation o1, Reservation o2) {
        return o1.getCheckInDate().compareTo(o2.getCheckInDate());
    }
}
