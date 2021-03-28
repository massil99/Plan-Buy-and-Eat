package com.planbuyandeat.SQLite.Models;

import com.planbuyandeat.Repertoire.Ingredients.Ingredients;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Modéle d'une liste de courses
 */
public class ListeDesCourses {

    /**
     * Identifiant de la liste dans la base de données
     */
    private long id;

    /**
     * Date des courses
     */
    private CustomDate date;

    /**
     * L'id de l'utilisateur à qui apparitent cette liste
     */
    private long userid;

    /**
     * La liste des éléments
     */
    private List<LDCItem> items;

    public ListeDesCourses() {
        this.items = new ArrayList<>();
        date = new CustomDate();
    }

    /**
     * Ajouter un élément à la liste
     * @param ing Élement à ajouter
     */
    public void addItem(LDCItem ing){
        if(!this.items.contains(ing))
            this.items.add(ing);
    }

    /**
     * Supprimer un élément à la liste
     * @param ing Élement à Supprimer
     */
    public void removeItem(LDCItem ing){
        this.items.remove(ing);
    }


    /**
     * Remplacer un élément de la liste
     * @param oldIng Élement à remplacer
     * @param newIng Élement remplaçant
     */
    public void replaceItem(LDCItem oldIng, LDCItem newIng){
        this.removeItem(oldIng);
        this.addItem(newIng);
    }


    /** Getters et Setters */
    public List<LDCItem> getItems() {
        return items;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public CustomDate getDate() {
        return date;
    }

    public void setDate(CustomDate date) {
        this.date.setTime(date.getTime());
    }

    public long getUserid() {
        return userid;
    }

    public void setUserid(long userid) {
        this.userid = userid;
    }
}
