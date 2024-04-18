package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;

public class GetUserThread extends DatabaseThread implements Runnable{

    private String username;
    private User user;

    public GetUserThread(String username) {
        this.username = username;
    }

    public User getUser() {
        return user;
    }

    @Override
    public void run() {
        user = super.getUser(username);
    }
}
