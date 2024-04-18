package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.records.Receipt;

public class DeleteReceiptFromDatabaseThread extends DatabaseThread implements Runnable{

    private Receipt receipt;

    public DeleteReceiptFromDatabaseThread(Receipt receipt) {
        this.receipt = receipt;
    }

    @Override
    public void run() {
        super.deleteReceiptFromDatabase(receipt);
    }
}
