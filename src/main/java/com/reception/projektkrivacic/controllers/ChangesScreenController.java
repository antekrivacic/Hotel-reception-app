package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.*;
import javafx.scene.text.Text;
import javafx.util.Callback;

import java.io.ObjectInputFilter;
import java.util.List;

public class ChangesScreenController {

    @FXML
    private Label usernameLabel;
    @FXML
    private TableView<DataChange> changesTableView;
    @FXML
    private TableColumn<DataChange, String> changedEntityTableColumn;
    @FXML
    private TableColumn<DataChange, String> oldValueTableColumn;
    @FXML
    private TableColumn<DataChange, String> newValueTableColumn;
    @FXML
    private TableColumn<DataChange, String> roleTableColumn;
    @FXML
    private TableColumn<DataChange, String> changeTimeTableColumn;



    private User currentUser;

    public void initialize(User user) {
        currentUser = user;
        usernameLabel.setText(user.getName());

        changedEntityTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataChange, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataChange, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getObjectName());
            }
        });
        oldValueTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataChange, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataChange, String> param) {
                if (param.getValue().getOldValue() == null)
                    return new ReadOnlyStringWrapper("created");
                return new ReadOnlyStringWrapper(param.getValue().getOldValue().toString());
            }
        });
        newValueTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataChange, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataChange, String> param) {
                if (param.getValue().getNewValue() == null)
                    return new ReadOnlyStringWrapper("deleted");
                return new ReadOnlyStringWrapper(param.getValue().getNewValue().toString());
            }
        });
        roleTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataChange, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataChange, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getUser().getRoleToString());
            }
        });
        changeTimeTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<DataChange, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<DataChange, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getTimestamp().toString());
            }
        });

        oldValueTableColumn.setCellFactory(tc -> {
            TableCell<DataChange, String> cell = new TableCell<>();
            Text text = new javafx.scene.text.Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(oldValueTableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });
        newValueTableColumn.setCellFactory(tc -> {
            TableCell<DataChange, String> cell = new TableCell<>();
            Text text = new javafx.scene.text.Text();
            cell.setGraphic(text);
            cell.setPrefHeight(Control.USE_COMPUTED_SIZE);
            text.wrappingWidthProperty().bind(newValueTableColumn.widthProperty());
            text.textProperty().bind(cell.itemProperty());
            return cell ;
        });

    }


    public void showChanges() {


        List<DataChange> listOfChanges = FileUtils.deserializeDataChanges();
        ObservableList<DataChange> observableListOfChanges = FXCollections.observableArrayList(listOfChanges);

        changesTableView.setItems(observableListOfChanges);

    }


    public void changeToShowAccommodationsScene() {
        SceneUtils.changeToShowAccommodationsScreen(currentUser);
    }
    public void changeToSearchReservationsScene() {
        if (currentUser.getIsAnEmployee())
            SceneUtils.changeToShowReservationsScreen(currentUser);
    }
    public void changeToReceiptScene() {
        if(currentUser.getIsAnEmployee())
            SceneUtils.changeToReceiptsScreen(currentUser);
    }
    public void changeToShowUsersScene() {
        if(currentUser.getIsAnEmployee())
            SceneUtils.changeToShowUsersScreen(currentUser);
    }
    public void changeToHotelFinancesScene() {
        if (currentUser.getIsAnEmployee())
            SceneUtils.changeToHotelFinancesScreen(currentUser);
    }
    public void changeToChangesScene() {
        SceneUtils.changeToChangesScreen(currentUser);
    }
    public void changeToLoginScene() {
        SceneUtils.loginSignupChangeScene("loginScreen.fxml", "Login", 600, 400);
    }


}
