package com.planbuyandeat.Planning;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modéle d'une liste de courses
 */
public class ListeDeCourses {
    /**
     * Identifiatn de la liste
     */
    private long id;

    /**
     * Date des courses
     */
    private Date date;

    /**
     * La liste des éléments
     */
    private List<String> items;

    public ListeDeCourses(Date date) {
        this.items = new ArrayList<>();
        this.date = date;
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
