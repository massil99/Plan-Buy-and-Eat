package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.PlatJour;

import java.util.ArrayList;
import java.util.List;

public class PlatJourSQLiteDAO implements DAO<PlatJour> {
    // Champs de la base de données
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_PLATJOUR_DATEID,
            DBHelper.COLUMN_PLATJOUR_PLATID
    };

    public PlatJourSQLiteDAO(Context context) {
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
     * Créer une association plat jour du planning dans la base de de données
     * @param o objet contenant les infomation à stocker
     * @return retour l'objet  si l'insertion s'est bien passée, null sinon
     */
    @Override
    public PlatJour create(PlatJour o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLATJOUR_DATEID, o.getDateid());
        values.put(DBHelper.COLUMN_PLATJOUR_PLATID, o.getPlatid());

        String selection =
                DBHelper.COLUMN_PLATJOUR_DATEID + "="+o.getDateid() + " AND " +
                        DBHelper.COLUMN_PLATJOUR_PLATID + " = " + o.getPlatid();
        Cursor cursor = database.query(DBHelper.TABLE_PLATJOUR, allColumns,
                selection,
                null, null, null, null);


        Log.d(this.getClass().getName(), selection);
        // Tester si l'association n'existe pas déja
        if(cursor.getCount() == 0){
            /**
             * Récuperation de l'id du tuple inseré
             */
            long insertid = database.insert(DBHelper.TABLE_PLATJOUR, null,
                    values);
            /**
             * Retourne l'objet pour dire que l'insertion s'est bien passée, sinon null
             */
            if(insertid != -1){
                return o;
            }else
                return null;
        }
        cursor.close();
        return null;
    }

    /**
     * On ne peut pas mettre à jour la table platjour
     * @param o
     */
    @Override
    public void update(PlatJour o) {
    }

    /**
     * Suppression d'un association plat jour
     * @param o l'association à supprimer
     */
    @Override
    public void delete(PlatJour o) {
        database.delete(DBHelper.TABLE_PLATJOUR,
                DBHelper.COLUMN_JOUR_ID + " = '" + o.getDateid() +"'", null);
    }

    /**
     * Suppression de toutes les associations plat jour
     */
    public boolean deleteAll() {
        long delRows = database.delete(DBHelper.TABLE_PLATJOUR, null, null);

        if(delRows != 0){
            return true ;
        }else
            return false;

    }

    /**
     * Récuper tous les plats associés à une date donnée
     * @param date
     * @return
     */
    public List<PlatJour> getAllPlatOfJour(String date) {
        List<PlatJour> pjs = new ArrayList<>();
        String sql = "SELECT "+ DBHelper.TABLE_PLATJOUR+"."+DBHelper.COLUMN_PLATJOUR_PLATID +", "+
                DBHelper.TABLE_PLATJOUR+"."+DBHelper.COLUMN_PLATJOUR_DATEID
                +" FROM " + DBHelper.TABLE_PLATJOUR + ", " + DBHelper.TABLE_JOUR+
                " WHERE " + DBHelper.TABLE_PLATJOUR+"."+DBHelper.COLUMN_PLATJOUR_DATEID + "="+
                DBHelper.TABLE_JOUR+"."+DBHelper.COLUMN_JOUR_ID + " AND "+
                DBHelper.TABLE_JOUR+"."+DBHelper.COLUMN_JOUR_DAY +" = "+ date.split("-")[0] + " AND "+
                DBHelper.TABLE_JOUR+"."+DBHelper.COLUMN_JOUR_MONTH +" = "+ date.split("-")[1] + " AND "+
                DBHelper.TABLE_JOUR+"."+DBHelper.COLUMN_JOUR_YEAR +" = "+ date.split("-")[2] +";";

        Log.d(this.getClass().getName(), sql);
        Cursor cursor = database.rawQuery(sql, null);

        if (cursor != null && cursor.getCount() > 0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                PlatJour pj = cursorToPlatJour(cursor);
                pjs.add(pj);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return pjs;
    }

    /**
     * Récuper tous les tupe de la table PlatJour
     * @return
     */
    @Override
    public List<PlatJour> getAll() {
        List<PlatJour> pjs = new ArrayList<>();

        Cursor cursor = database.query(DBHelper.TABLE_PLATJOUR, allColumns,
                null, null, null, null, null);

        if (cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            while(!cursor.isAfterLast()){
                PlatJour pj = cursorToPlatJour(cursor);
                pjs.add(pj);
                cursor.moveToNext();
            }
        }
        cursor.close();
        return pjs;
    }

    private PlatJour cursorToPlatJour(Cursor cursor){
        PlatJour pj = new PlatJour();

        pj.setDateid(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_PLATJOUR_DATEID)));
        pj.setPlatid(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_PLATJOUR_PLATID)));

        return pj;
    }
}
