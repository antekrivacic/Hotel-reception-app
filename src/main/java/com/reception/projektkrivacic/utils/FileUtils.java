package com.reception.projektkrivacic.utils;

import com.reception.projektkrivacic.MainApplication;
import com.reception.projektkrivacic.exceptions.InvalidUsernameFormatException;
import com.reception.projektkrivacic.exceptions.UserDoesntExistException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.Accommodation;
import com.reception.projektkrivacic.model.generics.DataChange;
import com.reception.projektkrivacic.model.classes.Reservation;
import com.reception.projektkrivacic.model.records.Receipt;

import javafx.scene.chart.PieChart;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.util.*;

public class FileUtils {

    private static final Logger logger = LoggerFactory.getLogger(MainApplication.class);
    private static final File USERS_FILE = new File("dat/users.txt");
    private static final File SERIALIZED_USERS_FILE = new File("dat/serialized-users.txt");
    private static final File ADMIN_PASSWORD_FILE = new File("dat/adminPassword.txt");
    private static final File SERIALIZED_ACCOMMODATIONS_FILE = new File("dat/serialized-accommodations.txt");
    private static final File SERIALIZED_RECEIPTS_FILE = new File("dat/serialized-receipts.txt");
    private static final File SERIALIZED_RESERVATIONS_FILE = new File("dat/serialized-reservations.txt");
    private static final File SERIALIZED_DATACHANGES_FILE = new File("dat/serialized-datachanges.txt");

    public static void signUpUser(User user) throws InvalidUsernameFormatException {
        if (user.getName().contains(" ")) {
            throw new InvalidUsernameFormatException("Username should be one word!");
        }
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(USERS_FILE, true))) {
            writer.write(user.getName() + " " + user.getHashedPassword());
            writer.newLine();
        } catch (IOException e) {
            logger.error("Error while writing to file: " + USERS_FILE.getAbsolutePath());
            e.printStackTrace();
        }
    }

    public static List<User> loadUsers() {
        List<User> users = new ArrayList<>();

        try (BufferedReader reader = new BufferedReader(new BufferedReader(new FileReader(USERS_FILE)))) {
            String line;
            while ((line = reader.readLine()) != null) {
                String[] parts = line.split(" ");
                users.add(new User(parts[0], parts[1]));
            }
        } catch (IOException e) {
            logger.error("Error while reading from file: " + USERS_FILE.getAbsolutePath());
            e.printStackTrace();
        }
        return users;
    }

    public static Boolean doesUserExist(String username) {
        List<User> users = loadUsers();
        for (User u : users) {
            if (u.getName().equals(username)) {
                return true;
            }
        }
        return false;
    }

    public static void findUser(String username) throws UserDoesntExistException {
        List<User> users = loadUsers();
        Optional<User> userOptional = Optional.empty();
        for (User u : users) {
            if (u.getName().equals(username)) {
                userOptional = Optional.of(u);
            }
        }
        if (userOptional.isPresent()) {
            userOptional.get();
        } else {
            throw new UserDoesntExistException("User with username " + username + " does not exist!");
        }
    }

    public static String getAdminPassword() {
        try (BufferedReader reader = new BufferedReader(new FileReader(ADMIN_PASSWORD_FILE))) {
            return reader.readLine();
        } catch (IOException e) {
            logger.error("Error while reading from file: " + ADMIN_PASSWORD_FILE.getAbsolutePath());
            e.printStackTrace();
        }
        return null;
    }

    public static void serializeUsers(Set<User> users) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_USERS_FILE))) {
            for (User user : users) {
                oos.writeObject(user);
            }
        } catch (IOException e) {
            logger.error("User serialization failed!");
            e.printStackTrace();
        }
    }
    public static Set<User> deserializeUsers() {
        Set<User> users = new HashSet<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_USERS_FILE))) {
            while (true) {
                try {
                    User user = (User) ois.readObject();
                    users.add(user);
                } catch (EOFException e) {
                    break;
                }
            }
        }
        catch (IOException ex){
            System.out.println("Error while reading from file: " + SERIALIZED_USERS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_USERS_FILE.getAbsolutePath());
            ex.printStackTrace();
        }
        catch (ClassNotFoundException ex){
            System.out.println("Error while reading from file: " + SERIALIZED_USERS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_USERS_FILE.getAbsolutePath());
        }
        return users;
    }

    public static void serializeAccommodations(List<Accommodation> accommodations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_ACCOMMODATIONS_FILE))) {
            for (Accommodation accommodation : accommodations) {
                oos.writeObject(accommodation);
            }
        } catch (IOException e) {
            logger.error("Accommodation serialization failed!" + e.getMessage());
            System.out.println("Accommodation serialization failed!" + e.getMessage());
        }
    }

    public static List<Accommodation> deserializeAccommodations() {
        List<Accommodation> accommodations = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_ACCOMMODATIONS_FILE))) {
            while (true) {
                try {
                    Accommodation accommodation = (Accommodation) ois.readObject();
                    accommodations.add(accommodation);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_ACCOMMODATIONS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_ACCOMMODATIONS_FILE.getAbsolutePath());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_ACCOMMODATIONS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_ACCOMMODATIONS_FILE.getAbsolutePath());
        }
        return accommodations;
    }

    public static void serializeReceipts(List<Receipt> receipts) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_RECEIPTS_FILE))) {
            for (Receipt receipt : receipts) {
                oos.writeObject(receipt);
            }
        } catch (IOException e) {
            logger.error("Receipt serialization failed!" + e.getMessage());
            System.out.println("Receipt serialization failed!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Receipt> deserializeReceipts() {
        List<Receipt> receipts = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_RECEIPTS_FILE))) {
            while (true) {
                try {
                    Receipt receipt = (Receipt) ois.readObject();
                    receipts.add(receipt);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_RECEIPTS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_RECEIPTS_FILE.getAbsolutePath());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_RECEIPTS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_RECEIPTS_FILE.getAbsolutePath());
        }
        return receipts;
    }

    public static void serializeReservations(List<Reservation> reservations) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_RESERVATIONS_FILE))) {
            for (Reservation reservation : reservations) {
                oos.writeObject(reservation);
            }
        } catch (IOException e) {
            logger.error("Reservation serialization failed!" + e.getMessage());
            System.out.println("Reservation serialization failed!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<Reservation> deserializeReservations() {
        List<Reservation> reservations = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_RESERVATIONS_FILE))) {
            while (true) {
                try {
                    Reservation reservation = (Reservation) ois.readObject();
                    reservations.add(reservation);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_RESERVATIONS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_RESERVATIONS_FILE.getAbsolutePath());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_RESERVATIONS_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_RESERVATIONS_FILE.getAbsolutePath());
        }
        return reservations;
    }

    public static void serializeDataChange(DataChange newDataChange) {
        List<DataChange> dataChanges = deserializeDataChanges();
        dataChanges.add(newDataChange);

        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(SERIALIZED_DATACHANGES_FILE))) {
            for (DataChange dataChange : dataChanges) {
                oos.writeObject(dataChange);
            }
        } catch (IOException e) {
            logger.error("DataChange serialization failed!" + e.getMessage());
            System.out.println("DataChange serialization failed!" + e.getMessage());
            e.printStackTrace();
        }
    }

    public static List<DataChange> deserializeDataChanges() {
        List<DataChange> dataChanges = new ArrayList<>();
        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(SERIALIZED_DATACHANGES_FILE))) {
            while (true) {
                try {
                    DataChange dataChange = (DataChange) ois.readObject();
                    dataChanges.add(dataChange);
                } catch (EOFException e) {
                    break;
                }
            }
        } catch (IOException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_DATACHANGES_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_DATACHANGES_FILE.getAbsolutePath());
            ex.printStackTrace();
        } catch (ClassNotFoundException ex) {
            System.out.println("Error while reading from file: " + SERIALIZED_DATACHANGES_FILE.getAbsolutePath());
            logger.error("Error while reading from file: " + SERIALIZED_DATACHANGES_FILE.getAbsolutePath());
        }
        return dataChanges;
    }
}