package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.util.ArrayList;
import java.util.List;

/**
 * Intremédiaire enter l'application et la base de de données pour la gestion des
 * objets Utilisateur
 */
public class UsersSQLiteDAO implements DAO<Utilisateur> {

    // Champs de la base de données
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_USERS_ID,
            DBHelper.COLUMN_USERS_NOM,
            DBHelper.COLUMN_USERS_PRENOM,
            DBHelper.COLUMN_USERS_USERNAME,
            DBHelper.COLUMN_USERS_MDP,
            DBHelper.COLUMN_USERS_NBPlatJours,
            DBHelper.COLUMN_USERS_PERIODE,
            DBHelper.COLUMN_USERS_DateDebut,
    };

    public UsersSQLiteDAO(Context context) {
        dbHelper = new DBHelper(context);
    }


    /**
     * Ouvrire une instatnce de la  base de données
     */
    @Override
    public void open() {
        database = dbHelper.getWritableDatabase();
    }

    /**
     * Fermer l'instance de base de données
     */
    @Override
    public void close() {
        dbHelper.close();
    }

    /**
     * Creser un tilisateur dans la base de de données
     * @param o objet utilsiateur contenant les infomation à stocker
     * @return l'utilisteur avec son id dans la base de de données
     */
    @Override
    public Utilisateur create(Utilisateur o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_USERS_PRENOM, o.getPrenom());
        values.put(DBHelper.COLUMN_USERS_USERNAME, o.getUsername());
        values.put(DBHelper.COLUMN_USERS_MDP, o.getMdp());
        values.put(DBHelper.COLUMN_USERS_NBPlatJours, o.getNbPlatjour());
        values.put(DBHelper.COLUMN_USERS_PERIODE, o.getPeriod());
        values.put(DBHelper.COLUMN_USERS_DateDebut, o.getDateDebut());

        long insertId = database.insert(DBHelper.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(DBHelper.TABLE_USERS,
                allColumns, DBHelper.COLUMN_USERS_ID + " = " + insertId, null,
                null, null, null);
        cursor.moveToFirst();
        Utilisateur newUser = cursorToUser(cursor);
        cursor.close();
        return newUser;
    }

    /**
     * Supprimer un tilisateur de la  base de données
     * @param o l'utilisateur à supprimer
     */
    @Override
    public void delete(Utilisateur o) {
        long id = o.getId();
        System.out.println("Comment deleted with id: " + id);
        database.delete(DBHelper.TABLE_USERS, DBHelper.COLUMN_USERS_ID
                + " = " + id, null);
    }

    /**
     * Récuperer tous les utilisateur de la base de données
     * @return la liste des utilisateurs
     */
    @Override
    public List<Utilisateur> getALL() {
        List<Utilisateur> users = new ArrayList<Utilisateur>();

        Cursor cursor = database.query(DBHelper.TABLE_USERS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()) {
            Utilisateur user = cursorToUser(cursor);
            users.add(user);
            cursor.moveToNext();
        }
        // assurez-vous de la fermeture du curseur
        cursor.close();

        return users;
    }

    /**
     * Authentificaiton de l'utilisateur à parti de la base de données
     * @param user
     * @return les information de l'uilistaer si il authentifié, null sinon
     */
    public Utilisateur checkCredentials(Utilisateur user){
        Cursor cursor = database.query(DBHelper.TABLE_USERS,
                allColumns, DBHelper.COLUMN_USERS_USERNAME +" = '"+user.getUsername() +"' "+
                "AND "+DBHelper.COLUMN_USERS_MDP + " = '"+ user.getMdp()+"'",
                null,null, null, null);
        if(cursor != null && cursor.getCount() >0)
            return cursorToUser(cursor);
        else
            return null;
    }

    /**
     * Parse les information de la DB en objet utilisateur
     * @param cursor
     * @return
     */
    private Utilisateur cursorToUser(Cursor cursor){
        Utilisateur user = new Utilisateur();
        user.setId(cursor.getLong(0));
        user.setNom(cursor.getString(1));
        user.setPrenom(cursor.getString(2));
        user.setUsername(cursor.getString(3));
        user.hasAndSetMdp(cursor.getString(4));
        user.setNbPlatjour(cursor.getInt(5));
        user.setPeriod(cursor.getInt(6));
        user.setDateDebut(cursor.getString(7));
        return user;
    }

}
