package com.isscroberto.onemovie.data.models;

/**
 * Created by roberto.orozco on 24/10/2017.
 */

public class Filter {

    private int MaxPage;
    private boolean InTheatres;
    private Language Language;
    private String Year;
    private boolean Popular;
    private int Vote;
    private Genre Genre;

    public boolean isInTheatres() {
        return InTheatres;
    }

    public void setInTheatres(boolean inTheatres) {
        InTheatres = inTheatres;
    }

    public int getMaxPage() {
        return MaxPage;
    }

    public void setMaxPage(int maxPage) {
        MaxPage = maxPage;
    }

    public com.isscroberto.onemovie.data.models.Language getLanguage() {
        return Language;
    }

    public void setLanguage(com.isscroberto.onemovie.data.models.Language language) {
        Language = language;
    }

    public String getYear() {
        return Year;
    }

    public void setYear(String year) {
        Year = year;
    }

    public boolean isPopular() {
        return Popular;
    }

    public void setPopular(boolean popular) {
        Popular = popular;
    }

    public int getVote() {
        return Vote;
    }

    public void setVote(int vote) {
        Vote = vote;
    }

    public com.isscroberto.onemovie.data.models.Genre getGenre() {
        return Genre;
    }

    public void setGenre(com.isscroberto.onemovie.data.models.Genre genre) {
        Genre = genre;
    }
}
