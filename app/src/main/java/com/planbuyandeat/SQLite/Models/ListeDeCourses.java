package com.planbuyandeat.SQLite.Models;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modéle d'une liste de courses
 */
public class ListeDeCourses {
    /**
     * Date des courses
     */
    private CustomDate date;

    /**
     * La liste des éléments
     */
    private List<String> items;

    public ListeDeCourses() {
        this.items = new ArrayList<>();
        date = new CustomDate();
    }

    /**
     * Ajouter un élément à la liste
     * @param ing Élement à ajouter
     */
    public void addItem(String ing){
        if(!this.items.contains(ing))
            this.items.add(ing);
    }

    /**
     * Supprimer un élément à la liste
     * @param ing Élement à Supprimer
     */
    public void removeItem(String ing){
        this.items.remove(ing);
    }


    /**
     * Remplacer un élément de la liste
     * @param oldIng Élement à remplacer
     * @param newIng Élement remplaçant
     */
    public void replaceItem(String oldIng, String newIng){
        this.removeItem(oldIng);
        this.addItem(newIng);
    }

    /** Getters et Setters */
    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date.setTime(date.getTime());
    }

    public List<String> getItems() {
        return items;
    }
}
