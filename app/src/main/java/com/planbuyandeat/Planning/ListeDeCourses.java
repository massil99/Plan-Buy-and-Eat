package com.planbuyandeat.Planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class ListeDeCourses {
    private long id;
    private Date date;
    private List<String> items;

    public ListeDeCourses(Date date) {
        this.items = new ArrayList<>();
        this.date = date;
    }

    public void addItem(String ing){
        if(!this.items.contains(ing))
            this.items.add(ing);
    }

    public void removeItem(String ing){
        this.items.remove(ing);
    }

    public void replaceItem(String oldIng, String newIng){
        this.removeItem(oldIng);
        this.addItem(newIng);
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public List<String> getItems() {
        return items;
    }
}
