package com.isscroberto.onemovie.data.models;

/**
 * Created by roberto.orozco on 31/10/2017.
 */

public class Language {
    private String Name;
    private String Code;

    public Language(String name, String code) {
        setName(name);
        setCode(code);
    }

    public String toString(){
        return Name;
    }

    public String getName() {
        return Name;
    }

    public void setName(String name) {
        Name = name;
    }

    public String getCode() {
        return Code;
    }

    public void setCode(String code) {
        Code = code;
    }
}
