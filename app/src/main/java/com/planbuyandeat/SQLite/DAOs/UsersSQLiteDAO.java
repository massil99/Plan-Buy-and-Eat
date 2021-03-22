package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

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
     * Créer un utilisateur dans la base de de données
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

        /**
         * Récuperation de l'id du tuple inseré
         */
        long insertId = database.insert(DBHelper.TABLE_USERS, null,
                values);
        Cursor cursor = database.query(DBHelper.TABLE_USERS,
                allColumns, DBHelper.COLUMN_USERS_ID + " = " + insertId, null,
                null, null, null);
        if(cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            Utilisateur newUser = cursorToUser(cursor);
            cursor.close();
            return newUser;
        }else
            return null;
    }

    /**
     * Supprimer un tilisateur de la  base de données
     * @param o l'utilisateur à supprimer
     */
    @Override
    public void delete(Utilisateur o) {
        long id = o.getId();
        database.delete(DBHelper.TABLE_USERS, DBHelper.COLUMN_USERS_ID
                + " = " + id, null);
    }

    /**
     * Mettre à jour les information de l'utilisateur
     */

    @Override
    public void update(Utilisateur o) {
        long id = o.getId();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_USERS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_USERS_PRENOM, o.getPrenom());
        values.put(DBHelper.COLUMN_USERS_USERNAME, o.getUsername());
        values.put(DBHelper.COLUMN_USERS_MDP, o.getMdp());
        values.put(DBHelper.COLUMN_USERS_NBPlatJours, o.getNbPlatjour());
        values.put(DBHelper.COLUMN_USERS_PERIODE, o.getPeriod());
        values.put(DBHelper.COLUMN_USERS_DateDebut, o.getDateDebut());

        database.update(DBHelper.TABLE_USERS, values, DBHelper.COLUMN_USERS_ID + " = " + id, null);
    }

    /**
     * Récuperer un utilisateur de la base de données
     * @return l'utilisateur
     */
    public Utilisateur get(long id) {
        Cursor cursor = database.query(DBHelper.TABLE_USERS,
                allColumns, DBHelper.COLUMN_USERS_ID +" = "+ id, null, null, null, null);

        cursor.moveToFirst();
        Utilisateur user = cursorToUser(cursor);
        cursor.close();

        return user;
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

        if(cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            Utilisateur u = cursorToUser(cursor);
            cursor.close();
            return u;
        } else
            return null;
    }

    /**
     * Parse les information de la DB en objet utilisateur
     * @param cursor
     * @return
     */
    private Utilisateur cursorToUser(Cursor cursor){
        Utilisateur user = new Utilisateur();
        user.setId(cursor.getLong(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_ID)));
        user.setNom(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_NOM)));
        user.setPrenom(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_PRENOM)));
        user.setUsername(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_USERNAME)));
        user.setMdp(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_MDP)));
        user.setNbPlatjour(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_NBPlatJours)));
        user.setPeriod(cursor.getInt(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_PERIODE)));
        user.setDateDebut(cursor.getString(cursor.getColumnIndexOrThrow(DBHelper.COLUMN_USERS_DateDebut)));
        return user;
    }

}
