package com.planbuyandeat.Models;

/**
 * Model d'un utilisateur de l'application
 */
public class Utilisateur {
    /**
     * ID de l'utilisateur dans la  base de données
     */
    private long id;

    /**
     * Nom de l'utilistateur
     */
    private String nom;

    /**
     * Prénom de l'utilisateur
     */
    private String prenom;

    /**
     * Username de l'utilisateur
     */
    private String username;

    /**
     * Mot de passe d'utilisateur
     */
    private String mdp;

    public Utilisateur(String nom, String prenom, String username, String mdp) {
        this.nom = nom;
        this.prenom = prenom;
        this.username = username;
        this.mdp = mdp;
    }

    /** Getters et Setters **/
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
        this.nom = nom;
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }
}
