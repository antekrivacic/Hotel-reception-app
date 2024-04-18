package com.reception.projektkrivacic.model.generics;

import com.reception.projektkrivacic.model.interfaces.Payment;
import com.reception.projektkrivacic.model.records.Receipt;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class PayedForTracker<T extends Receipt> {

    Map<T, BigDecimal> unpaidReceipts;
    List<T> receipts;

    public PayedForTracker(List<T> receipts) {
        this.receipts = receipts;
        this.unpaidReceipts = new HashMap<>();

        for(Receipt receipt : receipts) {
            if(receipt.paymentMethod().contains("Pending")){
                unpaidReceipts.put((T) receipt, receipt.totalAmount());
            }
        }
    }
    public Map<T, BigDecimal> getUnpaidReceipts() {
        return unpaidReceipts;
    }

}
