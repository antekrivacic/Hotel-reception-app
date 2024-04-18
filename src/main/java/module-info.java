module com.reception.projektkrivacic {
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    requires java.sql;

    exports com.reception.projektkrivacic.controllers;
    opens com.reception.projektkrivacic.controllers to javafx.fxml;
    opens com.reception.projektkrivacic to javafx.fxml;
    exports com.reception.projektkrivacic;
}