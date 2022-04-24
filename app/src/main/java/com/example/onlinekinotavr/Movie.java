package com.example.onlinekinotavr;

public class Movie {

    private String Title;
    private String Countries;
    private String Poster;
    private Integer kino_id;

    public String getTitle() {
        return Title;
    }

    public void setTitle(String title) {
        Title = title;
    }

    public String getCountries() {
        return Countries;
    }

    public void setCountries(String countries) {
        Countries = countries;
    }

    public String getPoster() {
        return Poster;
    }

    public void setPoster(String poster) {
        Poster = poster;
    }

    public Integer getKino_id() {
        return kino_id;
    }

    public void setKino_id(Integer kino_id) {
        this.kino_id = kino_id;
    }

    public Movie(String title, String countries, String poster, Integer kino_id) {
        Title = title;
        Countries = countries;
        Poster = poster;
        this.kino_id = kino_id;
    }
}
