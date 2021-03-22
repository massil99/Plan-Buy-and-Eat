package com.planbuyandeat.SQLite.Models;

import java.util.LinkedList;
import java.util.List;

/**
 * Modéle d'un plat
 */
public class Plat {
    /**
     * L'identifiant du plat dans la base de données
     */
    private long id;

    /**
     * L'identifiant de l'utilisateur qui l'a ajouté
     */
    private long adderid;

    /**
     * Nom du plat
     */
    private String nom;

    /**
     * La liste des intgrédient du plat
     */
    private List<String> ingredients;

    public Plat(){
    }

    /**
     * Création du plat et initilisation de la liste des igrédients
     * @param nom
     */
    public Plat(String nom) {
        this.nom = nom;
        this.ingredients = new LinkedList<>();
    }

    /**
     * Ajouter les ingrédient à la liste des ingrédients
     * @param ing Ingrédient à ajouter
     */
    public void addIngredient(String ing){
        this.ingredients.add(ing);
    }

    /**
     * Supprimer les ingrédient à la liste des ingrédients
     * @param ing Ingrédient à supprimer
     */
    public void removeIngredient(String ing){
        this.ingredients.remove(ing);
    }

    /** Getters et Setters **/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public long getAdderid() {
        return adderid;
    }

    public void setAdderid(long adderid) {
        this.adderid = adderid;
    }

    public List<String> getIngredients(){
        return this.ingredients;
    }

    public String getIngredient(int position){
        return this.ingredients.get(position);
    }

    public void setIngredients(int position, String ingredient){
        this.ingredients.set(position, ingredient);
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom;
    }
}
