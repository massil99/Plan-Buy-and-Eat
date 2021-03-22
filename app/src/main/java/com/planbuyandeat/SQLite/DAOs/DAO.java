package com.planbuyandeat.SQLite.DAOs;


import java.util.List;

/**
 * les classe qui implementrerons cett interfaces serviront d'intermédiaire entre le SGBD
 * et les objet manipulés par l'application
 * @param <T> La class model stockant les information de la base de données et qui est manipulée
 *           dans l'application
 */
public interface DAO <T> {
    /**
     * Ouvrire un instance de la  base de données
     */
    void open();

    /**
     * Fermer l'instance de la base de données
     */
    void close();

    /**
     * Créer un tuple dans la base de données
     * @param o objet contenant les infomation à stocker
     * @return revoie d'infomation supplémentaire sur le tuple
     */
    T create(T o);
    /**
     * Supprimer un tuple de la base de donées
     */

    void update(T o);

    void delete(T o);
    /**
     * Recupere tous les tuple de la table
     * @return la lites des tupes
     */
    List<T> getALL();
}
