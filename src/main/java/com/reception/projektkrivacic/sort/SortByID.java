package com.reception.projektkrivacic.sort;

import com.reception.projektkrivacic.model.classes.NamedEntity;

import java.util.Comparator;

public class SortByID implements Comparator<NamedEntity> {

    @Override
    public int compare(NamedEntity o1, NamedEntity o2) {
        if(o1.getId()<o2.getId()) return -1;
        else if(o1.getId()>o2.getId()) return 1;
        else return 0;
    }
}
