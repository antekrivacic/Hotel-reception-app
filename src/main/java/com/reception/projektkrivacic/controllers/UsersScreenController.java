package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.DeleteUserFromDatabaseThread;
import com.reception.projektkrivacic.Threads.GetUsersFromDatabaseThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Address;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.sort.SortByID;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.FileUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.beans.binding.Bindings;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.util.Callback;

import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;
import java.util.concurrent.Executor;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;
import java.util.stream.Collectors;

public class UsersScreenController {

    @FXML
    private TextField usernameTextField;
    @FXML
    private CheckBox employeesOnlyCheckBox;
    @FXML
    private TableView<User> usersTableView;
    @FXML
    private TableColumn<User, String> userIdColumn;
    @FXML
    private TableColumn<User, String> usernameColumn;
    @FXML
    private TableColumn<User, String> firstNameColumn;
    @FXML
    private TableColumn<User, String> lastNameColumn;
    @FXML
    private TableColumn<User, String> addressColumn;
    @FXML
    private TableColumn<User, String> roleTableColumn;

    @FXML
    private Label usernameLabel;
    private User currentUser;

    public void initialize(User user) {
        currentUser = user;
        usernameLabel.setText(user.getName());

        userIdColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(userStringCellDataFeatures.getValue().getId().toString());
            }
        });
        usernameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(userStringCellDataFeatures.getValue().getName());
            }
        });
        firstNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(userStringCellDataFeatures.getValue().getFirstName());
            }
        });
        lastNameColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(userStringCellDataFeatures.getValue().getLastName());
            }
        });
        addressColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                Address address = userStringCellDataFeatures.getValue().getAddress();
                return new ReadOnlyStringWrapper(address != null ? address.toString() : "");
            }
        });
        roleTableColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<User, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<User, String> userStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(userStringCellDataFeatures.getValue().getRoleToString());
            }
        });

        usersTableView.setRowFactory(tv -> {
            TableRow<User> row = new TableRow<>();
            ContextMenu contextMenu = new ContextMenu();
            MenuItem deleteItem = new MenuItem("Delete User");
            MenuItem editItem = new MenuItem("Edit User");

            deleteItem.setOnAction(event -> {

                    Alert alert = new Alert(Alert.AlertType.CONFIRMATION,
                            "Are you sure you want to delete this user?", ButtonType.YES, ButtonType.NO);
                    alert.showAndWait();
                    if (alert.getResult() == ButtonType.YES) {
                        User selectedUser = row.getItem();
                        if(!selectedUser.equals(currentUser)){
                            User oldUser = selectedUser;

                            ExecutorService executor = Executors.newSingleThreadExecutor();
                            DeleteUserFromDatabaseThread deleteUserFromDatabaseThread = new DeleteUserFromDatabaseThread(selectedUser);
                            executor.execute(deleteUserFromDatabaseThread);

                            try {
                                executor.awaitTermination(300, TimeUnit.MILLISECONDS);
                            } catch (InterruptedException e) {
                                e.printStackTrace();
                            }
                            searchUsers();

                            DataChange<User, User> dataChange = new DataChange<>("User",
                                    oldUser, null, new Timestamp(System.currentTimeMillis()), currentUser);
                            FileUtils.serializeDataChange(dataChange);
                        }
                        else{
                            Alert alert2 = new Alert(Alert.AlertType.ERROR, "You can't delete yourself!", ButtonType.OK);
                            alert2.showAndWait();
                        }
                    }
            });
            editItem.setOnAction(event ->{
                User selectedUser = row.getItem();
                SceneUtils.changeToEditUserScreen(currentUser, selectedUser);
            });
            contextMenu.getItems().addAll(deleteItem, editItem);

            row.contextMenuProperty().bind(
                    Bindings.when(Bindings.isNotNull(row.itemProperty()))
                            .then(contextMenu)
                            .otherwise((ContextMenu)null)
            );
            return row;
        });
    }

    public void searchUsers(){
        String usernameString = usernameTextField.getText();
        Boolean employeesOnly = employeesOnlyCheckBox.isSelected();

        Executor executor = Executors.newSingleThreadExecutor();
        GetUsersFromDatabaseThread getUsersFromDatabaseThread = new GetUsersFromDatabaseThread();
        executor.execute(getUsersFromDatabaseThread);

        List<User> users = getUsersFromDatabaseThread.getUsers().stream().toList();
        ObservableList<User> observableList = FXCollections.observableList(users);

        List<User> filteredUserList = observableList.stream()
                .filter(user -> usernameString == null || user.getName().contains(usernameString))
                .filter(user -> !employeesOnly || user.getIsAnEmployee())
                .collect(Collectors.toList());

        filteredUserList.sort(new SortByID());

        usersTableView.setItems(FXCollections.observableList(filteredUserList));
    }

    public void changeToShowAccommodationsScene() {
        SceneUtils.changeToShowAccommodationsScreen(currentUser);
    }
    public void changeToSearchReservationsScene() {
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
    public void changeToChangesScreen() {
        if (currentUser.getIsAnEmployee())
            SceneUtils.changeToChangesScreen(currentUser);
    }
    public void changeToLoginScene() {
        SceneUtils.loginSignupChangeScene("loginScreen.fxml", "Login", 600, 400);
    }

}
