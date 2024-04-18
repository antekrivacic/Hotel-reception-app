package com.reception.projektkrivacic.controllers;

import com.reception.projektkrivacic.Threads.GetHotelFinanceThread;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.HotelFinance;
import com.reception.projektkrivacic.utils.DatabaseUtils;
import com.reception.projektkrivacic.utils.SceneUtils;
import javafx.beans.property.ReadOnlyStringWrapper;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.scene.chart.PieChart;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public class HotelFinancesScreenController {

    @FXML
    private TableView<HotelFinance> hotelFinancesTableView;
    @FXML
    private TableColumn<HotelFinance, String> currentIncomeColumn;
    @FXML
    private TableColumn<HotelFinance, String> currentExpensesColumn;
    @FXML
    private TableColumn<HotelFinance, String> totalProfitColumn;

    @FXML
    private Label usernameLabel;
    private User currentUser;

    public void initialize(User user) {
        currentUser = user;
        usernameLabel.setText(user.getName());

        currentIncomeColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HotelFinance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HotelFinance, String> hotelFinanceStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(hotelFinanceStringCellDataFeatures.getValue().getTotalIncomeYearly().toString());
            }
        });
        currentExpensesColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HotelFinance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HotelFinance, String> hotelFinanceStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(hotelFinanceStringCellDataFeatures.getValue().getTotalExpenseYearly().toString());
            }
        });
        totalProfitColumn.setCellValueFactory(new Callback<TableColumn.CellDataFeatures<HotelFinance, String>, ObservableValue<String>>() {
            @Override
            public ObservableValue<String> call(TableColumn.CellDataFeatures<HotelFinance, String> hotelFinanceStringCellDataFeatures) {
                return new ReadOnlyStringWrapper(hotelFinanceStringCellDataFeatures.getValue().getTotalProfitYearly().toString());
            }
        });
    }

    public void showThisHotelFinances() {
        Executor executor = Executors.newSingleThreadExecutor();
        GetHotelFinanceThread getHotelFinanceThread = new GetHotelFinanceThread();
        executor.execute(getHotelFinanceThread);

        HotelFinance hotelFinance = getHotelFinanceThread.getHotelFinance();

        hotelFinancesTableView.getItems().add(hotelFinance);
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
