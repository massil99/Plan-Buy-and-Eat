package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.CustomDate;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Intermediaire entre un jour du planning utilisé par l'application sous format String
 * et la base de données qui la traite en 3 champs (Jour, Mois, Année)
 */
public class JourSQLiteDAO implements DAO<CustomDate>{
    // Champs de la base de données
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_JOUR_ID,
            DBHelper.COLUMN_JOUR_DAY,
            DBHelper.COLUMN_JOUR_MONTH,
            DBHelper.COLUMN_JOUR_YEAR,
    };

    public JourSQLiteDAO(Context context) {
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
     * Créer un jour du planning dans la base de de données
     * @param o objet contenant les infomation à stocker
     * @return retour l'objet si l'insertion s'est bien passée, null sinon
     */
    @Override
    public CustomDate create(CustomDate o) {
        if(o != null){
            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_JOUR_DAY, o.getDayOfMonth());
            values.put(DBHelper.COLUMN_JOUR_MONTH, o.getMonth());
            values.put(DBHelper.COLUMN_JOUR_YEAR, o.getYear());

            long insertid = database.insert(DBHelper.TABLE_JOUR, null, values);

            o.setId(insertid);

            if(insertid != -1)
                return o;
            else
                return null;

        }

        return null;
    }

    /**
     * Mettre à jour les consant d'une date du planning dans la base de données
     * @param o
     */
    @Override
    public void update(CustomDate o) {
        if(o != null){

            ContentValues values = new ContentValues();
            values.put(DBHelper.COLUMN_JOUR_DAY, o.getDayOfMonth());
            values.put(DBHelper.COLUMN_JOUR_MONTH, o.getMonth());
            values.put(DBHelper.COLUMN_JOUR_YEAR, o.getYear());

            database.update(DBHelper.TABLE_JOUR, values,
                    DBHelper.COLUMN_JOUR_ID +"="+ o.getId(), null);

        }

    }

    /**
     * Supprimer un jour du planning
     * @param o la date du jour du planning
     */
    @Override
    public void delete(CustomDate o) {
        if(o != null){
            database.delete(DBHelper.TABLE_JOUR,
                    DBHelper.COLUMN_JOUR_ID +" = "+ o.getId(),
                    null);
        }
    }

    /**
     * Supperimer tous les jours de planning
     */
    public void deleteAll() {
        database.delete(DBHelper.TABLE_JOUR, null,null);
    }

    /**
     * Récuperer toutes les date des jours de planning
     * @return La liste des date récupérées
     */
    @Override
    public List<CustomDate> getAll() {
        List<CustomDate> js = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_JOUR,
                allColumns,null,null, null, null, null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                CustomDate j = cursorToJour(cursor);
                js.add(j);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return js;
    }

    /**
     * Récuperation des jours du planning par mois
     * @param month
     * @return
     */
    public List<CustomDate> getAllByMonth(String month) {
        List<CustomDate> js = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_JOUR,
                allColumns,DBHelper.COLUMN_JOUR_MONTH + " = " + month,null, null, null, null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                CustomDate j = cursorToJour(cursor);
                js.add(j);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return js;
    }

    /**
     * Convertie un tuple jour de la base de données en string
     * @param cursor
     * @return
     */
    private CustomDate cursorToJour(Cursor cursor){
        CustomDate res = new CustomDate();
        res.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_JOUR_ID)));
        res.setGetDayOfMonth(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_JOUR_DAY)));
        res.setMonth(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_JOUR_MONTH)));
        res.setYear(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_JOUR_YEAR)));
        return res;
    }
}
