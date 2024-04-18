package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.login.User;

public class DeleteUserFromDatabaseThread extends DatabaseThread implements Runnable{

    private User user;


    public DeleteUserFromDatabaseThread(User user) {
        this.user = user;
    }

    @Override
    public void run() {
        super.deleteUserFromDatabase(user);
    }
}
