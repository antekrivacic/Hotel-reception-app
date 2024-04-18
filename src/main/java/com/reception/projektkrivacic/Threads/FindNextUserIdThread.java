package com.reception.projektkrivacic.Threads;

public class FindNextUserIdThread extends DatabaseThread implements Runnable{

    private Long userId;

    public Long getUserId() {
        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return userId;
    }

    @Override
    public void run() {
        userId = super.findNextUserId();
    }
}
