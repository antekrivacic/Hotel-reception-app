package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.DeleteAccommodationFromDatabaseThread;
import com.reception.projektkrivacic.Threads.GetAllAccommodationsFromDatabaseThread;
import com.reception.projektkrivacic.Threads.UpdateAccommodationThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class AccommodationsScreenController {

    @FXML
    private Label usernameLabel;
    @FXML
    private ComboBox<String> roomTypeComboBox;
    @FXML
    private CheckBox showOnlyAvailableRoomsCheckBox;
    @FXML
    private Button showReservationsButton;
    @FXML
    private TableView<Accommodation> roomsTableView;
    @FXML
    private TableColumn<Accommodation, String> accommodationIdTableColumn;
    @FXML
    private TableColumn<Accommodation, String> accommodationTypeTableColumn;
    @FXML
    private TableColumn<Accommodation, String> roomNumberTableColumn;
    @FXML
    private TableColumn<Accommodation, String> accommodationFloorTableColumn;
    @FXML
    private TableColumn<Accommodation, String> numberOfBedsTableColumn;
    @FXML
    private TableColumn<Accommodation, String> accommodationPriceTableColumn;
    @FXML
    private TableColumn<Accommodation, String> isAvailableTableColumn;

    private User currentUser;

    public void initialize(User user) {
        currentUser = user;
        usernameLabel.setText(user.getName());
        roomTypeComboBox.getItems().addAll("Single room", "Double room", "Suite");

        if (!currentUser.getIsAnEmployee())
            showReservationsButton.setVisible(false);

        accommodationIdTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getId().toString());
            }
        });
        accommodationTypeTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getRoomType());
            }
        });
        roomNumberTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getName());
            }
        });
        accommodationFloorTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAccommodationFloor().toString());
            }
        });
        numberOfBedsTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getNumberOfBeds().toString());
            }
        });
        accommodationPriceTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAccommodationPrice().toString());
            }
        });
        isAvailableTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<Accommodation, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<Accommodation, String> param) {
                return new ReadOnlyStringWrapper(param.getValue().getAvailable().toString());
            }
        });

        roomsTableView.setRowFactory(tv -> {
            TableRow<Accommodation> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem reserveItem = new MenuItem("Reserve a Room");
            MenuItem infoItem = new MenuItem("Room Info");
            MenuItem deleteItem = new MenuItem("Delete Room");
            MenuItem editItem = new MenuItem("Edit Room");
            MenuItem suspendItem = new MenuItem("Change availability");

            suspendItem.setOnAction(event -> {
                Accommodation selectedAccommodation = row.getItem();
                selectedAccommodation.setAvailable(!selectedAccommodation.getAvailable());

                UpdateAccommodationThread updateAccommodationThread = new UpdateAccommodationThread(selectedAccommodation);
                ExecutorService executor = Executors.newSingleThreadExecutor();
                executor.execute(updateAccommodationThread);
                executor.shutdown();
                try {
                    executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                showReservations();
            });
            reserveItem.setOnAction(event -> {
                Accommodation selectedAccommodation = row.getItem();
                if(selectedAccommodation.getAvailable())
                    SceneUtils.changeToNewReservationScene(currentUser, selectedAccommodation);
                else{
                    Alert alert = new Alert(Alert.AlertType.ERROR, "This room is not available for reservation",
                            ButtonType.OK);
                    alert.showAndWait();
                }
            });
            infoItem.setOnAction(event -> {
                Accommodation selectedAccommodation = row.getItem();
                SceneUtils.changeToAccommodationInfoScene(currentUser, selectedAccommodation);
            });
            deleteItem.setOnAction(event -> {
                if(currentUser.getIsAnEmployee()){
                    Accommodation oldAccommodation = row.getItem();

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION, "Are you sure you want to delete this entity?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        Accommodation selectedAccommodation = row.getItem();

                        DeleteAccommodationFromDatabaseThread deleteAccommodationFromDatabaseThread =
                                new DeleteAccommodationFromDatabaseThread(selectedAccommodation);
                        ExecutorService executor = Executors.newSingleThreadExecutor();
                        executor.execute(deleteAccommodationFromDatabaseThread);

                        executor.shutdown();
                        try {
                            executor.awaitTermination(Long.MAX_VALUE, java.util.concurrent.TimeUnit.NANOSECONDS);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        showReservations();

                        DataChange<Accommodation, Accommodation> dataChange = new DataChange<>("Accommodation",
                                oldAccommodation, null, new Timestamp(System.currentTimeMillis()), currentUser);
                        FileUtils.serializeDataChange(dataChange);
                    }
                }
            });
            editItem.setOnAction(event ->{
                if(currentUser.getIsAnEmployee()) {
                    Accommodation selectedAccommodation = row.getItem();
                    SceneUtils.changeToEditAccommodationScene(currentUser, selectedAccommodation);
                }
            });
            if(currentUser.getIsAnEmployee()) {
                contextMenu.getItems().addAll(reserveItem, infoItem, deleteItem, editItem, suspendItem);
            }else
                contextMenu.getItems().addAll(reserveItem, infoItem);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null)
            );
            return row;
        });


    }

    public void showReservations() {
        GetAllAccommodationsFromDatabaseThread getAllAccommodationsFromDatabaseThread = new GetAllAccommodationsFromDatabaseThread();
        ExecutorService executor = Executors.newSingleThreadExecutor();
        executor.execute(getAllAccommodationsFromDatabaseThread);

        List<Accommodation> accommodations = getAllAccommodationsFromDatabaseThread.getAccommodations();

        FileUtils.serializeAccommodations(accommodations);

        Optional<String> selectedRoomType = Optional.ofNullable(roomTypeComboBox.getValue());
        Boolean showOnlyAvailableRoomsCheckBoxSelected = showOnlyAvailableRoomsCheckBox.isSelected();

        ObservableList<Accommodation> observableList = FXCollections.observableArrayList(accommodations);
        List<Accommodation> filteredAccommodations = accommodations;

        if(selectedRoomType.isPresent() && showOnlyAvailableRoomsCheckBoxSelected){
            filteredAccommodations = accommodations.stream()
                    .filter(a -> a.getRoomType().contains(selectedRoomType.get()))
                    .filter(Accommodation::getAvailable)
                    .toList();
            observableList = FXCollections.observableArrayList(filteredAccommodations);
        }
        else if(selectedRoomType.isPresent()){
            filteredAccommodations = accommodations.stream()
                    .filter(a -> a.getRoomType().contains(selectedRoomType.get()))
                    .toList();
            observableList = FXCollections.observableArrayList(filteredAccommodations);
        }
        else if(showOnlyAvailableRoomsCheckBoxSelected){
            filteredAccommodations = accommodations.stream()
                    .filter(Accommodation::getAvailable)
                    .toList();
            observableList = FXCollections.observableArrayList(filteredAccommodations);
        }
        roomsTableView.setItems(observableList);
    }

    public void addNewAccommodation() {
        if(currentUser.getIsAnEmployee())
            SceneUtils.changeToAddNewAccommodationScreen(currentUser);
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
        if (currentUser.getIsAnEmployee())
            SceneUtils.changeToChangesScreen(currentUser);
    }
    public void changeToLoginScene() {
        SceneUtils.loginSignupChangeScene("loginScreen.fxml", "Login", 600, 400);
    }



}
