package com.reception.projektkrivacic.utils;

import com.reception.projektkrivacic.MainApplication;
import com.reception.projektkrivacic.controllers.*;
import com.reception.projektkrivacic.login.User;

import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.records.Receipt;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.stage.Modality;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.IOException;
import java.time.LocalDate;

public class SceneUtils {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);

    public static void loginSignupChangeScene(String fxmlFile, String title, Integer width, Integer height) {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource(fxmlFile));
        try{
            Scene scene = new Scene(fxmlLoader.load(), width, height);
            MainApplication.getMainStage().setTitle(title);
            MainApplication.getMainStage().setScene(scene);
            MainApplication.getMainStage().show();

        } catch (IOException e){
            e.printStackTrace();
            logger.error(e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToNewReservationScene(User user, Accommodation accommodation) {

        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("newReservationScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage newReservationStage = new Stage();
            newReservationStage.setTitle("Reservation");
            newReservationStage.setScene(scene);
            NewReservationScreenController newReservationScreenController = fxmlLoader.getController();
            newReservationScreenController.initialize(user, accommodation, newReservationStage);

            newReservationStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading new reservation screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToPaymentScreen(Accommodation accommodation, User user, LocalDate checkInDate,
                                             LocalDate checkOutDate, String paymentMethod, Stage reservationStage ) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("paymentScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage paymentStage = new Stage();
            paymentStage.setTitle("Payment");
            paymentStage.setScene(scene);
            paymentStage.initModality(Modality.APPLICATION_MODAL);

            PaymentScreenController paymentScreenController = fxmlLoader.getController();
            paymentScreenController.initialize(accommodation, user, checkInDate, checkOutDate, paymentMethod);

            reservationStage.close();
            paymentStage.show();

        } catch (IOException e) {
            logger.error("Error while loading payment screen: " + e.getMessage());
        }
    }

    public static void changeToShowAccommodationsScreen(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("accommodationsScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Reservations");
            MainApplication.getMainStage().setScene(scene);
            AccommodationsScreenController controller = fxmlLoader.getController();
            controller.initialize(user);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading reservations screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }
    public static void changeToShowReservationsScreen(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("reservationsScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Reservations");
            MainApplication.getMainStage().setScene(scene);

            ReservationsScreenController controller = fxmlLoader.getController();
            controller.initialize(user);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading reservations screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToReceiptsScreen(User user) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("receiptsScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Receipts");
            MainApplication.getMainStage().setScene(scene);

            ReceiptsScreenController controller = fxmlLoader.getController();
            controller.initialize(user);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading receipts screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    public static void changeToShowUsersScreen(User currentUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("usersScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Users");
            MainApplication.getMainStage().setScene(scene);

            UsersScreenController controller = fxmlLoader.getController();
            controller.initialize(currentUser);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading users screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToHotelFinancesScreen(User currentUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("hotelFinancesScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Hotel Finances");
            MainApplication.getMainStage().setScene(scene);

            HotelFinancesScreenController controller = fxmlLoader.getController();
            controller.initialize(currentUser);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading hotel finances screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToChangesScreen(User currentUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("changesScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 1000, 600);
            MainApplication.getMainStage().setTitle("Changes");
            MainApplication.getMainStage().setScene(scene);

            ChangesScreenController controller = fxmlLoader.getController();
            controller.initialize(currentUser);

            MainApplication.getMainStage().show();
        } catch (IOException e){
            logger.error("Error while loading changes screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToAccommodationInfoScene(User currentUser, Accommodation selectedAccommodation) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("accommodationInfoScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage accommodationInfoStage = new Stage();
            accommodationInfoStage.setTitle("Accommodation Info");
            accommodationInfoStage.initModality(Modality.APPLICATION_MODAL);
            accommodationInfoStage.setScene(scene);
            AccommodationInfoScreenController accommodationInfoScreenController = fxmlLoader.getController();
            accommodationInfoScreenController.initialize(currentUser, selectedAccommodation);

            accommodationInfoStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading accommodation info screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToEditAccommodationScene(User currentUser, Accommodation selectedAccommodation) {
    FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("editAccommodationScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 250);
            Stage editAccommodationStage = new Stage();
            editAccommodationStage.setTitle("Edit Accommodation");
            editAccommodationStage.initModality(Modality.APPLICATION_MODAL);
            editAccommodationStage.setScene(scene);

            EditAccommodationScreenController editAccommodationScreenController = fxmlLoader.getController();
            editAccommodationScreenController.initialize(currentUser, selectedAccommodation);

            editAccommodationStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading edit accommodation screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }

    }

    public static void changeToAddNewAccommodationScreen(User currentUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addNewAccommodationScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 250);
            Stage addNewAccommodationStage = new Stage();
            addNewAccommodationStage.setTitle("Add New Accommodation");
            addNewAccommodationStage.initModality(Modality.APPLICATION_MODAL);
            addNewAccommodationStage.setScene(scene);

            AddNewAccommodationScreenController addNewAccommodationScreenController = fxmlLoader.getController();
            addNewAccommodationScreenController.initialize(currentUser);

            addNewAccommodationStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading add new accommodation screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToEditReservation(User currentUser, Reservation selectedReservation) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("editReservationScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage editReservationStage = new Stage();
            editReservationStage.setTitle("Edit Reservation");
            editReservationStage.initModality(Modality.APPLICATION_MODAL);
            editReservationStage.setScene(scene);

            EditReservationScreenController editReservationScreenController = fxmlLoader.getController();
            editReservationScreenController.initialize(currentUser, selectedReservation);

            editReservationStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading edit reservation screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToAddReservation(User currentUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("addNewReservationScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 250);
            Stage newReservationStage = new Stage();
            newReservationStage.setTitle("Reservation");
            newReservationStage.initModality(Modality.APPLICATION_MODAL);
            newReservationStage.setScene(scene);

            AddNewReservationScreenController addNewReservationScreenController = fxmlLoader.getController();
            addNewReservationScreenController.initialize(currentUser);

            newReservationStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading new reservation screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

    public static void changeToEditUserScreen(User currentUser, User selectedUser) {
        FXMLLoader fxmlLoader = new FXMLLoader(MainApplication.class.getResource("editUserScreen.fxml"));
        try{
            Scene scene = new Scene(fxmlLoader.load(), 600, 400);
            Stage editUserStage = new Stage();
            editUserStage.setTitle("Edit User");
            editUserStage.initModality(Modality.APPLICATION_MODAL);
            editUserStage.setScene(scene);

            EditUserScreenController editUserScreenController = fxmlLoader.getController();
            editUserScreenController.initialize(currentUser, selectedUser);

            editUserStage.show();
        } catch (IOException e){
            e.printStackTrace();
            logger.error("Error while loading edit user screen: " + e.getMessage());
            System.out.println(e.getMessage());
        }
    }

}
