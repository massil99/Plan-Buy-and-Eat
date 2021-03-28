package com.planbuyandeat.SQLite.Models;

import java.util.Objects;

public class Ingredient {
    /**
     * Id dans la base de données de l'ingrdient
     */
    private long id;

    /**
     * Le nom de l'ingredient
     */
    private String nom;

    /**
     * L'id du plat auquel cette ingredient est associé
     */
    private long platId;

    public Ingredient() {
    }

    /** Getters and Setters **/
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getNom() {
        return nom;
    }

    public void setNom(String nom) {
        this.nom = nom.toLowerCase();
    }

    public long getPlatId() {
        return platId;
    }

    public void setPlatId(long platId) {
        this.platId = platId;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Ingredient that = (Ingredient) o;
        return id == that.getId() ||
                Objects.equals(nom, that.getNom());
    }
}
