package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;

public class SignUpUserThread extends DatabaseThread implements Runnable{

    private User user;

    public SignUpUserThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        super.signUpUser(user);
    }
}
