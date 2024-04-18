package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;

import java.util.List;
import java.util.Set;

public class GetUsersFromDatabaseThread extends DatabaseThread implements Runnable{

    private Set<User> users;

    public Set<User> getUsers() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return users;
    }

    @Override
    public void run() {
        users = super.getUsersFromDatabase();
    }
}
