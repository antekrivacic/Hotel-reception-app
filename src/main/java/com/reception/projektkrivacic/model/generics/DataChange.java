package com.reception.projektkrivacic.model.generics;

import com.reception.projektkrivacic.login.User;

import java.io.Serializable;
import java.sql.Timestamp;

public class DataChange <T extends Serializable, U extends Serializable> implements Serializable {
    private static final long serialVersionUID = -3594910925704557207L;


    private String objectName;
    private T oldValue;
    private U newValue;
    private Timestamp timestamp;
    private User user;
    public DataChange() {}

    public DataChange(String objectName, T oldValue, U newValue, Timestamp timestamp, User user) {
        this.objectName = objectName;
        this.oldValue = oldValue;
        this.newValue = newValue;
        this.timestamp = timestamp;
        this.user = user;
    }


    public String getObjectName() {
        return objectName;
    }

    public void setObjectName(String objectName) {
        this.objectName = objectName;
    }

    public T getOldValue() {
        return oldValue;
    }

    public void setOldValue(T oldValue) {
        this.oldValue = oldValue;
    }

    public U getNewValue() {
        return newValue;
    }

    public void setNewValue(U newValue) {
        this.newValue = newValue;
    }

    public Timestamp getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(Timestamp timestamp) {
        this.timestamp = timestamp;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public String toString() {

        return "Change{" +
                "objectName='" + objectName + '\'' +
                ", oldValue=" + oldValue +
                ", newValue=" + newValue +
                ", timestamp=" + timestamp +
                ", user='" + user.getName() + "'" +
                ", role='" + user.getRoleToString() + '\'' +
                '}';
    }
}
