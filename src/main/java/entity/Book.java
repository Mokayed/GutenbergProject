package entity;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Book {

    private String id;
    private String title;
    private List<String> authors;
    private HashMap<String, Integer> cities;
    
    public Book(String id, String title, List<String> authors, HashMap<String, Integer> cities) {
        this.id = id;
        this.title = title;
        this.authors = authors;
        this.cities = cities;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public List<String> getAuthors() {
        return authors;
    }

    public void setAuthors(List<String> authors) {
        this.authors = authors;
    }

    public HashMap<String, Integer> getCities() {
        return cities;
    }

    public void setCities(HashMap<String, Integer> cities) {
        this.cities = cities;
    }

    @Override
    public String toString() {
        return "Book{" + "id=" + id + ", title=" + title + ", authors=" + authors + ", cities=" + cities + '}';
    }



}
