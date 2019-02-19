package com.katalog.kozmetik.catalog.registiration;

import com.katalog.kozmetik.catalog.brands.Page;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by user on 15.03.2018.
 */

public class User {
    private String name;
    private String surname;
    private String email;
    private String password;
    private String id;
    private String firebaseId;
    private List<Page> favouriteList;

    public User() {
        this.favouriteList = new ArrayList<>();
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getFirebaseId() {
        return firebaseId;
    }

    public void setFirebaseId(String firebaseId) {
        this.firebaseId = firebaseId;
    }

    public List<Page> getFavouriteList() {
        return favouriteList;
    }

    public void setFavouriteList(List<Page> favouriteList) {
        this.favouriteList = favouriteList;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getSurname() {
        return surname;
    }

    public void setSurname(String surname) {
        this.surname = surname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
