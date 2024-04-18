package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.AddressBuilder;

public class FindUserAddressIdThread extends DatabaseThread implements Runnable{

    private User user;
    private Long addressId;

    public FindUserAddressIdThread(User user) {
        this.user = user;
    }

    public Long findAddressId() {
        return addressId;
    }

    @Override
    public void run() {
        addressId = super.findUserAddressId(user);
    }
}
