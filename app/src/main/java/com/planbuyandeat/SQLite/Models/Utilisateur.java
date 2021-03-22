package com.planbuyandeat.SQLite.Models;

import com.planbuyandeat.utils.MD5HashFunction;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

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

    /**
     * le nombre de plat que souhait avoir l'utillsateur par jour
     */
    private int nbPlatjour;

    /**
     * la date de début de generation du planning
     */
    private String dateDebut;

    /**
     * la frequence à laquelle l'utilisateur souhait faire ses courses
     */
    private int period;

    public Utilisateur() {
        nbPlatjour = 2;
        period = 7;
        dateDebut = new SimpleDateFormat("dd-MM-yyyy", Locale.getDefault()).format(new Date());
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
        this.nom = nom.toLowerCase();
    }

    public String getPrenom() {
        return prenom;
    }

    public void setPrenom(String prenom) {
        this.prenom = prenom.toLowerCase();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username.toLowerCase();
    }

    public String getMdp() {
        return mdp;
    }

    public void setMdp(String mdp) {
        this.mdp = mdp;
    }

    public void hasAndSetMdp(String mdp) {
        this.mdp = MD5HashFunction.hash(mdp);
    }

    public int getNbPlatjour() {
        return nbPlatjour;
    }

    public void setNbPlatjour(int nbPlatjour) {
        if(nbPlatjour >0 && nbPlatjour <= 5)
            this.nbPlatjour = nbPlatjour;
    }

    public String getDateDebut() {
        return dateDebut;
    }

    public void setDateDebut(String dateDebut) {
        this.dateDebut = dateDebut.toLowerCase();
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        if(period > 0)
            this.period = period;
    }
}
