package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.records.Receipt;

import java.util.List;

public class GetReceiptsFromDatabaseThread extends DatabaseThread implements Runnable{

    private List<Receipt> receipts;

    public List<Receipt> getReceipts() {
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        return receipts;
    }

    @Override
    public void run() {
        receipts = super.getReceiptsFromDatabase();
    }
}
