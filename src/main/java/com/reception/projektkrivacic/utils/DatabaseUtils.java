package com.reception.projektkrivacic.utils;

import com.reception.projektkrivacic.exceptions.InvalidUsernameFormatException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.*;
import com.reception.projektkrivacic.model.records.Receipt;

import com.reception.projektkrivacic.model.classes.Reservation;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.math.BigDecimal;
import java.sql.*;

import java.sql.Date;
import java.time.LocalDate;
import java.util.*;

public class DatabaseUtils {

    private static final Logger logger = LoggerFactory.getLogger(DatabaseUtils.class);
    private static final String DATABASE_FILE = "conf/database.properties";

    public static Connection connectToDatabase() throws SQLException, IOException{
        Properties svojstva = new Properties();
        try {
            svojstva.load(new FileReader(DATABASE_FILE));
            String urlBazePodataka = svojstva.getProperty("databaseUrl");
            String username = svojstva.getProperty("username");
            String password = svojstva.getProperty("password");
            Connection veza = DriverManager.getConnection(urlBazePodataka, username, password);
            return veza;
        } catch (SQLException e) {
            logger.error("SQL Exception while connecting to database: " + e.getMessage(), e);
        } catch (IOException e) {
            logger.error("IO Exception while loading database properties: " + e.getMessage(), e);
        }
        return null;
    }
    public static void signUpUser(User user) {

        try (Connection connection = connectToDatabase() ) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO USER_TABLE " +
                    "(USERNAME, ISANEMPLOYEE, FIRST_NAME, LAST_NAME, ADDRESS_ID) VALUES (?, ?, ?, ?, ?)");
            preparedStatement.setString(1, user.getName());
            preparedStatement.setBoolean(2, user.getIsAnEmployee());
            preparedStatement.setString(3, user.getFirstName());
            preparedStatement.setString(4, user.getLastName());
            preparedStatement.setBigDecimal(5, new BigDecimal(user.getAddress().getId()));
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e){
            logger.error("Error while writing to database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Long findNextUserId() {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM USER_TABLE");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static Long findNextAccommodationId() {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM ACCOMMODATIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static void updateHotelFinances(HotelFinance hotel) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE HOTEL_FINANCES SET " +
                    "ANNUAL_INCOME = ?, ANNUAL_EXPENSES = ?, TOTAL_PROFIT = ? WHERE ID = ?");
            preparedStatement.setBigDecimal(1, hotel.getTotalIncomeYearly());
            preparedStatement.setBigDecimal(2, hotel.getTotalExpenseYearly());
            preparedStatement.setBigDecimal(3, hotel.getTotalProfitYearly());
            preparedStatement.setLong(4, hotel.getHotelFinanceId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while writing to database!" + e.getMessage());
        }
    }

    public static HotelFinance getHotelFinance() {
        try (Connection connection = connectToDatabase()) {
            Statement statement = connection.createStatement();
            statement.execute("SELECT * FROM HOTEL_FINANCES");
            ResultSet resultSet = statement.getResultSet();

            if(resultSet.next()) {
                Long id = resultSet.getLong("id");
                BigDecimal totalIncome = resultSet.getBigDecimal("ANNUAL_INCOME");
                BigDecimal totalExpenses = resultSet.getBigDecimal("ANNUAL_EXPENSES");
                return new HotelFinance(id, totalIncome, totalExpenses);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static void makeReceipt(Long reservationId, BigDecimal totalAmount, String paymentMethod) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RECEIPT(RESERVATION_ID," +
                    "TOTAL_AMOUNT, PAYMENT_METHOD, ISSUE_DATE_TIME) VALUES(?, ? ,? ,?);");

            preparedStatement.setLong(1, reservationId);
            preparedStatement.setBigDecimal(2, totalAmount);
            preparedStatement.setString(3, paymentMethod);
            preparedStatement.setTimestamp(4, new Timestamp(System.currentTimeMillis()));

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while writing to receipt database!" + e.getMessage());
            System.out.printf("Error while writing to receipt database!" + e.getMessage());
        }
    }
    public static List<Receipt> getReceiptsFromDatabase() {
        List<Receipt> receiptsList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM RECEIPT");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                Long reservationId = resultSet.getLong("RESERVATION_ID");
                BigDecimal totalAmount = resultSet.getBigDecimal("TOTAL_AMOUNT");
                String paymentMethod = resultSet.getString("PAYMENT_METHOD");
                Timestamp paymentDate = resultSet.getTimestamp("ISSUE_DATE_TIME");
                receiptsList.add(new Receipt(id, reservationId, totalAmount, paymentMethod, paymentDate));
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return receiptsList;
    }

    public static void makeReservation(Long accommodationId, Long userId, LocalDate checkInDate,
                                       LocalDate checkOutDate, String paymentMethod) {
        try(Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO RESERVATION(ACCOMMODATION_ID, " +
                    "USER_ID, CHECK_IN_DATE, CHECK_OUT_DATE, PAYMENT_METHOD) VALUES (?, ?, ?, ?, ?);");

            preparedStatement.setLong(1, accommodationId);
            preparedStatement.setLong(2, userId);
            preparedStatement.setDate(3, Date.valueOf(checkInDate));
            preparedStatement.setDate(4, Date.valueOf(checkOutDate));
            preparedStatement.setString(5, paymentMethod);

            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while writing to reservation database!" + e.getMessage());
            System.out.printf("Error while writing to reservation database!" + e.getMessage());
        }
    }
    public static List<Reservation> getReservationsFromDatabase() {
        List<Reservation> reservationsList = new ArrayList<>();
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM RESERVATION");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                Long id = resultSet.getLong("ID");
                Long accommodationId = resultSet.getLong("ACCOMMODATION_ID");
                Long userId = resultSet.getLong("USER_ID");
                LocalDate checkInDate = resultSet.getDate("CHECK_IN_DATE").toLocalDate();
                LocalDate checkOutDate = resultSet.getDate("CHECK_OUT_DATE").toLocalDate();

                Reservation newReservation = new Reservation(id, accommodationId, userId, checkInDate, checkOutDate);
                reservationsList.add(newReservation);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return reservationsList;
    }

    public static Long getNextReservationId() {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM RESERVATION");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static List<Accommodation> getAllAccommodationsFromDatabase() {
        List<Accommodation> accommodationsList = new ArrayList<>();
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOMMODATIONS");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long id = resultSet.getLong("ID");
                Integer roomNumber = resultSet.getInt("ROOM_NUMBER");
                String roomType = resultSet.getString("ROOM_TYPE");
                Integer floor = resultSet.getInt("FLOOR");
                Integer numberOfBeds = resultSet.getInt("NUMBER_OF_BEDS");
                BigDecimal price = resultSet.getBigDecimal("PRICE");
                Boolean isAvailable = resultSet.getBoolean("AVAILABLE");
                Accommodation accommodation = new Accommodation(id, roomNumber.toString(), roomType, floor,
                        numberOfBeds, price, isAvailable);
                accommodationsList.add(accommodation);
            }
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return accommodationsList;
    }

    public static boolean isAccommodationAvailable(Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate) {

        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM RESERVATION WHERE " +
                    "ACCOMMODATION_ID = ? AND CHECK_IN_DATE <= ? AND CHECK_OUT_DATE >= ?");
            preparedStatement.setLong(1, accommodation.getId());
            preparedStatement.setDate(2, Date.valueOf(checkOutDate));
            preparedStatement.setDate(3, Date.valueOf(checkInDate));
            ResultSet resultSet = preparedStatement.executeQuery();
            return !resultSet.next();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return false;
    }

    public static User getUser(String name) {

        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER_TABLE WHERE USERNAME = ?");
            preparedStatement.setString(1, name);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("ID");
                String username = resultSet.getString("USERNAME");
                Boolean isAnEmployee = resultSet.getBoolean("ISANEMPLOYEE");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                Long addressId = resultSet.getLong("ADDRESS_ID");

                Address address = getAddress(addressId);

                return new User(id, username, firstName, lastName, isAnEmployee, address);
            }
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return null;
    }
    public static Set<User> getUsersFromDatabase() {
        Set<User> usersList = new HashSet<>();
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM USER_TABLE");
            ResultSet resultSet = preparedStatement.executeQuery();
            while(resultSet.next()){
                Long id = resultSet.getLong("ID");
                String username = resultSet.getString("USERNAME");
                Boolean isAnEmployee = resultSet.getBoolean("ISANEMPLOYEE");
                String firstName = resultSet.getString("FIRST_NAME");
                String lastName = resultSet.getString("LAST_NAME");
                Long addressId = resultSet.getLong("ADDRESS_ID");

                Address address = getAddress(addressId);

                User user = new User(id, username, firstName, lastName, isAnEmployee, address);
                usersList.add(user);
            }
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return usersList;
    }
    public static Address getAddress(Long addressId) {

        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ADDRESS WHERE ID = ?");
            preparedStatement.setLong(1, addressId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                String street = resultSet.getString("STREET");
                String city = resultSet.getString("CITY");
                String country = resultSet.getString("COUNTRY");
                String postalCode = resultSet.getString("POSTAL_CODE");
                return new AddressBuilder()
                        .setStreet(street)
                        .setCity(city)
                        .setCountry(country)
                        .setPostalCode(postalCode)
                        .createAddress();
            }
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static void addAddressToDatabase(Address address) {

        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ADDRESS(STREET, CITY, COUNTRY, POSTAL_CODE) " +
                    "VALUES (?, ?, ?, ?)");
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getCountry());
            preparedStatement.setString(4, address.getPostalCode());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while writing to database!" + e.getMessage());
            System.out.printf("Error while writing to database!" + e.getMessage());
        }
    }


    public static void deleteAccommodationFromDatabase(Accommodation selectedAccommodation) {
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM ACCOMMODATIONS WHERE ID = ?");
            preparedStatement.setLong(1, selectedAccommodation.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while deleting from database!" + e.getMessage());
            System.out.printf("Error while deleting from database!" + e.getMessage());
        }
    }

    public static void updateAccommodation(Accommodation selectedAccommodation) {
        try (Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ACCOMMODATIONS SET ROOM_NUMBER = ?, " +
                    "ROOM_TYPE = ?, FLOOR = ?, NUMBER_OF_BEDS = ?, PRICE = ?, AVAILABLE = ? WHERE ID = ?");
            preparedStatement.setInt(1, Integer.parseInt(selectedAccommodation.getName()));
            preparedStatement.setString(2, selectedAccommodation.getRoomType());
            preparedStatement.setInt(3, selectedAccommodation.getAccommodationFloor());
            preparedStatement.setInt(4, selectedAccommodation.getNumberOfBeds());
            preparedStatement.setBigDecimal(5, selectedAccommodation.getAccommodationPrice());
            preparedStatement.setBoolean(6, selectedAccommodation.getAvailable());
            preparedStatement.setLong(7, selectedAccommodation.getId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while updating database!" + e.getMessage());
            System.out.printf("Error while updating database!" + e.getMessage());
        }
    }

    public static Long findNextAddressId() {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT MAX(id) FROM ADDRESS");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1) + 1;
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return null;
    }

    public static void addAccommodation(Accommodation newAccommodation) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO ACCOMMODATIONS " +
                    "(ROOM_NUMBER, ROOM_TYPE, FLOOR, NUMBER_OF_BEDS, PRICE, AVAILABLE) VALUES (?, ?, ?, ?, ?, ?)");
            preparedStatement.setInt(1, Integer.parseInt(newAccommodation.getName()));
            preparedStatement.setString(2, newAccommodation.getRoomType());
            preparedStatement.setInt(3, newAccommodation.getAccommodationFloor());
            preparedStatement.setInt(4, newAccommodation.getNumberOfBeds());
            preparedStatement.setBigDecimal(5, newAccommodation.getAccommodationPrice());
            preparedStatement.setBoolean(6, newAccommodation.getAvailable());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while writing to database!" + e.getMessage());
            System.out.println("Error while writing to database!" + e.getMessage());
            e.printStackTrace();
        }
    }


    public static void deleteReservationFromDatabase(Reservation selectedReservation) {
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM RESERVATION WHERE ID = ?");
            preparedStatement.setLong(1, selectedReservation.getReservationId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while deleting from database!" + e.getMessage());
            System.out.printf("Error while deleting from database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Accommodation getAccommodationFromDatabase(Long accommodationId) {
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT * FROM ACCOMMODATIONS WHERE ID = ?");
            preparedStatement.setLong(1, accommodationId);
            ResultSet resultSet = preparedStatement.executeQuery();
            if(resultSet.next()){
                Long id = resultSet.getLong("ID");
                Integer roomNumber = resultSet.getInt("ROOM_NUMBER");
                String roomType = resultSet.getString("ROOM_TYPE");
                Integer floor = resultSet.getInt("FLOOR");
                Integer numberOfBeds = resultSet.getInt("NUMBER_OF_BEDS");
                BigDecimal price = resultSet.getBigDecimal("PRICE");
                Boolean isAvailable = resultSet.getBoolean("AVAILABLE");
                return new Accommodation(id, roomNumber.toString(), roomType, floor, numberOfBeds, price, isAvailable);
            }
        }
        catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
            System.out.printf("Error while reading from database!" + e.getMessage());
            e.printStackTrace();
        }
        return null;
    }

    public static void updateReservationInDatabase(Reservation selectedReservation) {
        try(Connection connection = connectToDatabase()){
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE RESERVATION SET CHECK_IN_DATE = ?, " +
                    "CHECK_OUT_DATE = ? WHERE ID = ?");
            preparedStatement.setDate(1, Date.valueOf(selectedReservation.getCheckInDate()));
            preparedStatement.setDate(2, Date.valueOf(selectedReservation.getCheckOutDate()));
            preparedStatement.setLong(3, selectedReservation.getReservationId());
            preparedStatement.executeUpdate();
        }
        catch (SQLException | IOException e) {
            logger.error("Error while updating database!" + e.getMessage());
            System.out.printf("Error while updating database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteReceiptFromDatabase(Receipt selectedReceipt) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM RECEIPT WHERE ID = ?");
            preparedStatement.setLong(1, selectedReceipt.receiptId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting from database!" + e.getMessage());
            System.out.printf("Error while deleting from database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void deleteUserFromDatabase(User selectedUser) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("DELETE FROM USER_TABLE WHERE ID = ?");
            preparedStatement.setLong(1, selectedUser.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while deleting from database!" + e.getMessage());
            System.out.printf("Error while deleting from database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void editAddressInDatabase(Address address) {

        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE ADDRESS SET STREET = ?, " +
                    "CITY = ?, COUNTRY = ?, POSTAL_CODE = ? WHERE ID = ?");
            preparedStatement.setString(1, address.getStreet());
            preparedStatement.setString(2, address.getCity());
            preparedStatement.setString(3, address.getCountry());
            preparedStatement.setString(4, address.getPostalCode());
            preparedStatement.setLong(5, address.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating database!" + e.getMessage());
            System.out.printf("Error while updating database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static void editUserInDatabase(User selectedUser) {

        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("UPDATE USER_TABLE SET USERNAME = ?, " +
                    "ISANEMPLOYEE = ?, FIRST_NAME = ?, LAST_NAME = ?, ADDRESS_ID = ? WHERE ID = ?");
            preparedStatement.setString(1, selectedUser.getName());
            preparedStatement.setBoolean(2, selectedUser.getIsAnEmployee());
            preparedStatement.setString(3, selectedUser.getFirstName());
            preparedStatement.setString(4, selectedUser.getLastName());
            preparedStatement.setLong(5, selectedUser.getAddress().getId());
            preparedStatement.setLong(6, selectedUser.getId());
            preparedStatement.executeUpdate();
        } catch (SQLException | IOException e) {
            logger.error("Error while updating database!" + e.getMessage());
            System.out.printf("Error while updating database!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static Long findUserAddressId(User selectedUser) {
        try (Connection connection = connectToDatabase()) {
            PreparedStatement preparedStatement = connection.prepareStatement("SELECT ADDRESS_ID FROM USER_TABLE WHERE ID = ?");
            preparedStatement.setLong(1, selectedUser.getId());
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getLong(1);
            }
        } catch (SQLException | IOException e) {
            logger.error("Error while reading from database!" + e.getMessage());
        }
        return null;
    }
}
