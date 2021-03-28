package com.planbuyandeat.SQLite.Models;

import java.util.Objects;

/**
 * Modéle d'un élément d'une liste des courses
 */
public class LDCItem {
    /**
     * Identifiant de l'élément de la liste
     */
    private long id;

    /**
     * Nom de l'élément de la liste
     */
    private String nom;

    /**
     * Identifiant de la liste à laquel appartient cet élément
     */
    private long ldc_id;

    /**
     * État de l'élément (coché/non coché)
     */
    private boolean checked;

    public LDCItem() {
    }


    /** Getter et setter */
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

    public long getLdc_id() {
        return ldc_id;
    }

    public void setLdc_id(long ldc_id) {
        this.ldc_id = ldc_id;
    }

    public boolean isChecked(){
        return checked;
    }

    public void uncheck(){
        checked = false;
    }

    public void check(){
        checked = true;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        LDCItem ldcItem = (LDCItem) o;
        return id == ldcItem.getId() &&
                nom.equals(ldcItem.getNom()) &&
                ldc_id == ldcItem.getLdc_id();
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, nom, ldc_id, checked);
    }
}
