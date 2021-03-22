package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.Plat;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.util.ArrayList;
import java.util.List;

public class PlatsSQLiteDAO implements DAO<Plat> {
    // Champs de la base de données
    private IngredientsSQLiteDAO ingdao;

    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_PLATS_ID,
            DBHelper.COLUMN_PLATS_NOM,
            DBHelper.COLUMN_PLATS_ADDERID
    };

    public PlatsSQLiteDAO(Context context) {
        dbHelper = new DBHelper(context);
        ingdao = new IngredientsSQLiteDAO(context);
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
     * Créer un plat dans la base de de données
     * @param o objet plat contenant les infomation à stocker
     * @return le plat avec son id dans la base de de données
     */
    @Override
    public Plat create(Plat o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLATS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_PLATS_ADDERID, o.getAdderid());

        /**
         * Récuperation de l'id du tuple inseré
         */
        long insertId = database.insert(DBHelper.TABLE_PLATS, null,
                values);
        Cursor cursor = database.query(DBHelper.TABLE_PLATS,
                allColumns, DBHelper.COLUMN_USERS_ID + " = " + insertId, null,
                null, null, null);
        if(cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            Plat newPlat = cursorToPlat(cursor);
            cursor.close();
            return newPlat;
        }else
            return null;
    }

    @Override
    public void delete(Plat o) {
        long id = o.getId();
        database.delete(DBHelper.TABLE_PLATS, DBHelper.COLUMN_PLATS_ID
                + " = " + id, null);
    }

    @Override
    public void update(Plat o) {
        long id = o.getId();

        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLATS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_PLATS_ADDERID, o.getAdderid());

        database.update(DBHelper.TABLE_PLATS, values, DBHelper.COLUMN_PLATS_ID + " = " + id, null);
    }

    /**
     * Récuperer un utilisateur de la base de données
     * @return l'utilisateur
     */
    public Plat get(long id) {
        Cursor cursor = database.query(DBHelper.TABLE_PLATS,
                allColumns, DBHelper.COLUMN_PLATS_ID +" = "+ id, null, null, null, null);

        cursor.moveToFirst();
        Plat plat = cursorToPlat(cursor);
        cursor.close();

        return plat;
    }

    public List<Plat> getAllUserPlats(Utilisateur user) {
        List<Plat> plats = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_PLATS,
                allColumns, DBHelper.COLUMN_PLATS_ADDERID + " = " + user.getId()
                , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Plat plat = cursorToPlat(cursor);
            plats.add(plat);
            cursor.moveToNext();
        }
        cursor.close();

        return plats;
    }

    @Override
    public List<Plat> getAll() {
        List<Plat> plats = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_PLATS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Plat plat = cursorToPlat(cursor);
            plats.add(plat);
            cursor.moveToNext();
        }
        cursor.close();

        return plats;
    }

    /**
     * Parse les information de la DB en objet utilisateur
     * @param cursor
     * @return
     */
    private Plat cursorToPlat(Cursor cursor){
        Plat plat = new Plat();
        plat.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_PLATS_ID)));
        plat.setNom(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_PLATS_NOM)));
        plat.setAdderid(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_PLATS_ADDERID)));
        ingdao.open();
        for (Ingredient ing : ingdao.getAllPlatIngredients(plat))
            plat.addIngredient(ing);
        ingdao.close();
        return plat;
    }
}
