package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;

public class EditUserInDatabaseThread extends DatabaseThread implements Runnable{

    private User user;

    public EditUserInDatabaseThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        super.editUserInDatabase(user);
    }
}
