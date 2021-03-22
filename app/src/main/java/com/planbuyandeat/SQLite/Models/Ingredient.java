package com.planbuyandeat.SQLite.Models;

public class Ingrdient {
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

    public Ingrdient() {
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
}
