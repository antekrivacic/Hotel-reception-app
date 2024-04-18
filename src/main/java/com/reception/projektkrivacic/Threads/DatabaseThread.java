package com.reception.projektkrivacic.Threads;

import com.reception.projektkrivacic.MainApplication;
import com.reception.projektkrivacic.exceptions.InvalidUsernameFormatException;
import com.reception.projektkrivacic.login.User;
import com.reception.projektkrivacic.model.classes.*;
import com.reception.projektkrivacic.model.generics.PayedForTracker;
import com.reception.projektkrivacic.model.records.Receipt;
import com.reception.projektkrivacic.utils.DatabaseUtils;
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
import java.time.LocalDate;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.logging.Logger;

public abstract class DatabaseThread {

    public static Boolean isDatabaseOperationInProgress = false;
    private static final Logger logger = Logger.getLogger(MainApplication.class.getName());

    public synchronized void signUpUser(User user) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.signUpUser(user);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized Long findNextUserId() {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        Long userId = DatabaseUtils.findNextUserId();
        isDatabaseOperationInProgress = false;
        notifyAll();

        return userId;
    }

    public synchronized Long findNextAccommodationId() {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        Long accommodationId = DatabaseUtils.findNextAccommodationId();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return accommodationId;
    }

    public synchronized void updateHotelFinances(HotelFinance hotelFinance) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateHotelFinances(hotelFinance);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized HotelFinance getHotelFinance() {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        HotelFinance hotelFinance = DatabaseUtils.getHotelFinance();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return hotelFinance;
    }

    public synchronized void makeReceipt(Long reservationId, BigDecimal totalAmount,
                                         String paymentMethod) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.makeReceipt(reservationId, totalAmount, paymentMethod);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized List<Receipt> getReceiptsFromDatabase() {
        List<Receipt> receipts;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        receipts = DatabaseUtils.getReceiptsFromDatabase();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return receipts;
    }

    public synchronized void makeReservation(Long accommodationId, Long userId, LocalDate checkInDate,
                                             LocalDate checkOutDate, String paymentMethod) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.makeReservation(accommodationId, userId, checkInDate, checkOutDate, paymentMethod);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized List<Reservation> getReservationsFromDatabase() {
        List<Reservation> reservations;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        reservations = DatabaseUtils.getReservationsFromDatabase();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return reservations;
    }

    public synchronized Long getNextReservationId() {
        Long nextReservationId;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        nextReservationId = DatabaseUtils.getNextReservationId();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return nextReservationId;
    }

    public synchronized List<Accommodation> getAllAccommodationsFromDatabase() {
        List<Accommodation> accommodations;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        accommodations = DatabaseUtils.getAllAccommodationsFromDatabase();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return accommodations;
    }

    public synchronized boolean isAccommodationAvailable(Accommodation accommodation, LocalDate checkInDate, LocalDate checkOutDate) {
        boolean isAvailable;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        isAvailable = DatabaseUtils.isAccommodationAvailable(accommodation, checkInDate, checkOutDate);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return isAvailable;
    }

    public synchronized User getUser(String name) {
        User user;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        user = DatabaseUtils.getUser(name);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return user;
    }

    public synchronized Set<User> getUsersFromDatabase() {
        Set<User> users;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        users = DatabaseUtils.getUsersFromDatabase();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return users;
    }

    public synchronized Address getAddress(Long addressId) {
        Address address;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        address = DatabaseUtils.getAddress(addressId);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return address;
    }

    public synchronized void addAddressToDatabase(Address address) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addAddressToDatabase(address);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteAccommodationFromDatabase(Accommodation selectedAccommodation) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteAccommodationFromDatabase(selectedAccommodation);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void updateAccommodation(Accommodation selectedAccommodation) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateAccommodation(selectedAccommodation);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized Long findNextAddressId() {
        Long nextAddressId;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        nextAddressId = DatabaseUtils.findNextAddressId();
        isDatabaseOperationInProgress = false;
        notifyAll();
        return nextAddressId;
    }

    public synchronized void addAccommodation(Accommodation newAccommodation) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.addAccommodation(newAccommodation);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteReservationFromDatabase(Reservation selectedReservation) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteReservationFromDatabase(selectedReservation);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized Accommodation getAccommodationFromDatabase(Long accommodationId) {
        Accommodation accommodation;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        accommodation = DatabaseUtils.getAccommodationFromDatabase(accommodationId);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return accommodation;
    }

    public synchronized void updateReservationInDatabase(Reservation selectedReservation) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.updateReservationInDatabase(selectedReservation);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteReceiptFromDatabase(Receipt selectedReceipt) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteReceiptFromDatabase(selectedReceipt);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void deleteUserFromDatabase(User selectedUser) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.deleteUserFromDatabase(selectedUser);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void editAddressInDatabase(Address address) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.editAddressInDatabase(address);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized void editUserInDatabase(User user) {
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        DatabaseUtils.editUserInDatabase(user);
        isDatabaseOperationInProgress = false;
        notifyAll();
    }

    public synchronized Long findUserAddressId(User selectedUser) {
        Long userAddressId;
        while (isDatabaseOperationInProgress) {
            try {
                wait();
            } catch (InterruptedException e) {
                throw new RuntimeException(e);
            }
        }

        isDatabaseOperationInProgress = true;
        userAddressId = DatabaseUtils.findUserAddressId(selectedUser);
        isDatabaseOperationInProgress = false;
        notifyAll();
        return userAddressId;
    }


}
