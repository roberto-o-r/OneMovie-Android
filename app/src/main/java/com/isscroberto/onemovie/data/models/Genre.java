package com.isscroberto.onemovie.data.models;

/**
 * Created by roberto.orozco on 15/11/2017.
 */

public class Genre {

    private int id;
    private String name;

    public Genre() {
    }

    public Genre(int id, String name) {
        setId(id);
        setName(name);
    }

    public String toString() {
        return name;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
