package com.planbuyandeat.Repertoire;

import java.util.LinkedList;
import java.util.List;

public class Plat {
    private String nom;
    private List<String> ingredients;

    public Plat(String nom) {
        this.nom = nom;
        this.ingredients = new LinkedList<>();
    }

    public void addIngredient(String ing){
        this.ingredients.add(ing);
    }

    public void removeIngredient(String ing){
        this.ingredients.remove(ing);
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
