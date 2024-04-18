package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.model.generics.PayedForTracker;
import com.reception.projektkrivacic.model.records.Receipt;
import com.reception.projektkrivacic.utils.FileUtils;
import javafx.animation.KeyFrame;
import javafx.animation.Timeline;
import javafx.application.Platform;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.util.Duration;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class BillsReminderThread extends DatabaseThread implements Runnable{
    private Timeline timeline;

    public BillsReminderThread() {

        timeline = new Timeline(new KeyFrame(Duration.seconds(12), new EventHandler<ActionEvent>() {
            @Override
            public void handle(ActionEvent event) {
                run();
            }
        }));

        timeline.setCycleCount(Timeline.INDEFINITE);
        timeline.play();
    }
    public void run() {
        List<Receipt> receipts = super.getReceiptsFromDatabase();

        PayedForTracker<Receipt> payedForTracker = new PayedForTracker<>(receipts);
        Map<Receipt, BigDecimal> unpaidReceipts = payedForTracker.getUnpaidReceipts();

        String message = "Unpaid receipts: \n";
        for (Map.Entry<Receipt, BigDecimal> entry : unpaidReceipts.entrySet()) {
            message += "ID= " + entry.getKey().receiptId() + ", Amount= " + entry.getValue() + "\n";
        }
        String alertMessage = message;

        ButtonType ok = new ButtonType("ALREADY DID");
        ButtonType close = new ButtonType("REMIND ME LATER");
        Alert alert = new Alert(Alert.AlertType.WARNING, alertMessage, ok, close);

        alert.setTitle("Unpaid Receipts");
        alert.setHeaderText("Please hand out these unpaid receipts to guests!");

        if (!unpaidReceipts.isEmpty()) {
            Platform.runLater(() -> {
                Optional<ButtonType> result = alert.showAndWait();
                if (result.isPresent() && result.get() == ok) {
                    stop();
                }
            });
        }
    }
    public void stop() {
        if (timeline != null) {
            timeline.stop();
        }
    }
}
