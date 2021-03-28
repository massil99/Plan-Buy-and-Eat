package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.CustomDate;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.LDCItem;
import com.planbuyandeat.SQLite.Models.ListeDesCourses;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Gestionnaire des listes des courses dans la base de données
 */
public class LDCSQLiteDAO implements DAO<ListeDesCourses> {
    // Champs de la base de données
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.TABLE_LDC +"."+DBHelper.COLUMN_LDC_ID,
            DBHelper.TABLE_LDC +"."+DBHelper.COLUMN_LDC_DATEID,
            DBHelper.TABLE_LDC +"."+DBHelper.COLUMN_LDC_USERID
    };

    public LDCSQLiteDAO(Context context) {
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
     * Créer une liste des courses dans la base de de données
     * @param o objet contenant les information à stocker
     * @return retourne l'objet  si l'insertion s'est bien passée, null sinon
     */
    @Override
    public ListeDesCourses create(ListeDesCourses o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_LDC_DATEID, o.getDate().getId());
        values.put(DBHelper.COLUMN_LDC_USERID, o.getUserid());

        /**
         * Inserstion de la liste dans la base de données
         */
        long inserid = database.insert(DBHelper.TABLE_LDC, null, values);


        // Teste de l'état de l'insertion
        if(inserid > -1){

            /**
             * Insersetion des élément de la liste dans la base de données
             */
            for(LDCItem item: o.getItems()){
                item.setLdc_id(inserid);
                ContentValues itemValues = new ContentValues();
                itemValues.put(DBHelper.COLUMN_LDCITEMS_CHECKED, 0);
                itemValues.put(DBHelper.COLUMN_LDCITEMS_ITEMID, item.getId());
                itemValues.put(DBHelper.COLUMN_LDCITEMS_LDCID, inserid);

                database.insert(DBHelper.TABLE_LDCITEMS, null, itemValues);
            }
            o.setId(inserid);

            /**
             * Récuperation de la date de la liste à partir des date disponniblent dans le planning
             */
            String sql = "SELECT * FROM " + DBHelper.TABLE_JOUR + " WHERE " +
                    DBHelper.COLUMN_JOUR_ID +"="+ o.getDate().getId();

            Cursor dayCursor = database.rawQuery(sql, null);
            dayCursor.moveToFirst();
            o.setDate(cursorToJour(dayCursor));
            dayCursor.close();

            return o;
        }
        return null;
    }

    /**
     * Mise à jours des information de la lite
     * @param o l'object contenant les nouvelles information
     */
    @Override
    public void update(ListeDesCourses o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_LDC_DATEID, o.getDate().getId());

        database.update(DBHelper.TABLE_LDC,values,
                DBHelper.COLUMN_LDC_ID +"="+ o.getId(), null);
    }

     /**
     * Suppression d'une liste des courses
     * @param o La liste à surpprimer
     */
    @Override
    public void delete(ListeDesCourses o) {
        database.delete(DBHelper.TABLE_LDCITEMS,
                DBHelper.COLUMN_LDCITEMS_LDCID +"="+o.getId(), null);
        database.delete(DBHelper.TABLE_LDC,
                DBHelper.COLUMN_LDC_ID +"="+ o.getId(), null);
    }

    /**
     * Suppression de toutes les listes ainsi que leur contenus
     */
    public void deleteAll(){
        database.delete(DBHelper.TABLE_LDCITEMS,
                null, null);
        database.delete(DBHelper.TABLE_LDC,
                null, null);
    }

    /**
     * Récuperation d'une liste à partir de son id
     * @param id l'identifiant de la liste dans la base de donnéees
     * @return l'objet récupérer de la base de données
     */
    public ListeDesCourses get(long id){
        ListeDesCourses ldc = null;

        /**
         * Récuperation des inforamtion de la liste
         */
        Cursor cursor = database.query(DBHelper.TABLE_LDC, allColumns,
                DBHelper.COLUMN_LDC_ID +"="+id, null, null, null, null);
        if(cursor.getCount() > 0){
            cursor.moveToFirst();
            ldc = cursorToLDC(cursor);
            cursor.close();

            /**
             * Récuération de la date de la liste
             */
            String sql = "SELECT * FROM " + DBHelper.TABLE_JOUR + " WHERE " +
                    DBHelper.COLUMN_JOUR_ID +"="+ ldc.getDate().getId();

            Cursor dayCursor = database.rawQuery(sql, null);
            dayCursor.moveToFirst();
            ldc.setDate(cursorToJour(dayCursor));
            dayCursor.close();

            /**
             * Récuperation des éléments de la liste
             */
            sql = "SELECT " + DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + " AS _ingredient_id , " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_NOM + " AS _ingrdient_name, " +
                    DBHelper.TABLE_LDCITEMS +"."+DBHelper.COLUMN_LDCITEMS_LDCID +" AS _ldcitem_ldc_id, " +
                    DBHelper.TABLE_LDCITEMS    +"."+DBHelper.COLUMN_LDCITEMS_CHECKED + " AS _ldcitem_checked " +
                    " FROM " + DBHelper.TABLE_INGREDIENTS +", "+ DBHelper.TABLE_LDC +", "+ DBHelper.TABLE_LDCITEMS + " WHERE " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + "=" +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_ITEMID + " AND " +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_LDCID  +"="+ ldc.getId() +
                    " GROUP BY _ingrdient_name ;";

            Cursor itemCursor = database.rawQuery(sql, null);
            itemCursor.moveToFirst();
            while(!itemCursor.isAfterLast()){
                LDCItem items = cursorToLDCItem(itemCursor);
                ldc.addItem(items);
                itemCursor.moveToNext();
            }
            itemCursor.close();
        }
        return ldc;
    }

    /**
     * Récuperation de toutes les listes de la base de données
     */
    @Override
    public List<ListeDesCourses> getAll() {
        List<ListeDesCourses> listesDesCourses = new ArrayList<>();

        /**
         * Récuperation des inforamtion de la liste
         */
        Cursor cursor = database.query(DBHelper.TABLE_LDC, allColumns,
                null, null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ListeDesCourses ldc = cursorToLDC(cursor);


            /**
             * Récuération de la date de la liste
             */
            String sql = "SELECT * FROM " + DBHelper.TABLE_JOUR + " WHERE " +
                    DBHelper.COLUMN_JOUR_ID +"="+ ldc.getDate().getId();

            Cursor dayCursor = database.rawQuery(sql, null);
            dayCursor.moveToFirst();
            ldc.setDate(cursorToJour(dayCursor));
            dayCursor.close();

            /**
             * Récuperation des éléments de la liste
             */
            sql = "SELECT " + DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + " AS _ingredient_id , " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_NOM + " AS _ingrdient_name, " +
                    DBHelper.TABLE_LDCITEMS +"."+DBHelper.COLUMN_LDCITEMS_LDCID +" AS _ldcitem_ldc_id, " +
                    DBHelper.TABLE_LDCITEMS    +"."+DBHelper.COLUMN_LDCITEMS_CHECKED + " AS _ldcitem_checked "  +
                    " FROM " + DBHelper.TABLE_INGREDIENTS +", "+ DBHelper.TABLE_LDC +", "+ DBHelper.TABLE_LDCITEMS + " WHERE " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + "=" +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_ITEMID + " AND " +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_LDCID  +"="+ ldc.getId() +
                    " GROUP BY _ingrdient_name ;";

            Cursor itemCursor = database.rawQuery(sql, null);
            itemCursor.moveToFirst();
            while(!itemCursor.isAfterLast()){
                LDCItem items = cursorToLDCItem(itemCursor);
                ldc.addItem(items);
                itemCursor.moveToNext();
            }
            itemCursor.close();

            listesDesCourses.add(ldc);
            cursor.moveToNext();
        }
        cursor.close();
        return listesDesCourses;
    }

    /**
     * Retourne toutes les liste d'un utilisateur
     */
    public List<ListeDesCourses> getAllUserLists(Utilisateur user){
        List<ListeDesCourses> listesDesCourses = new ArrayList<>();

        /**
         * Récuperation des inforamtion des listes de l'utilisateur
         */
        Cursor cursor = database.query(DBHelper.TABLE_LDC, allColumns,
                DBHelper.COLUMN_LDC_USERID + "="+ user.getId(), null, null, null, null);
        cursor.moveToFirst();
        while(!cursor.isAfterLast()){
            ListeDesCourses ldc = cursorToLDC(cursor);
            /**
             * Récuération de la date de la liste
             */
            String sql = "SELECT * FROM " + DBHelper.TABLE_JOUR + " WHERE " +
                    DBHelper.COLUMN_JOUR_ID +"="+ ldc.getDate().getId();

            Cursor dayCursor = database.rawQuery(sql, null);
            dayCursor.moveToFirst();
            ldc.setDate(cursorToJour(dayCursor));
            dayCursor.close();

            /**
             * Récuperation des élément de la liste
             */
            sql = "SELECT " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + " AS _ingredient_id , " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_NOM + " AS _ingrdient_name, " +
                    DBHelper.TABLE_LDCITEMS +"."+DBHelper.COLUMN_LDCITEMS_LDCID +" AS _ldcitem_ldc_id, " +
                    DBHelper.TABLE_LDCITEMS    +"."+DBHelper.COLUMN_LDCITEMS_CHECKED + " AS _ldcitem_checked " +
                    " FROM " + DBHelper.TABLE_INGREDIENTS +", "+ DBHelper.TABLE_LDC +", "+ DBHelper.TABLE_LDCITEMS + " WHERE " +
                    DBHelper.TABLE_INGREDIENTS +"."+DBHelper.COLUMN_INGREDIENTS_ID + "=" +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_ITEMID + " AND " +
                    DBHelper.TABLE_LDCITEMS +"."+ DBHelper.COLUMN_LDCITEMS_LDCID  +"="+ ldc.getId() +" AND " +
                    DBHelper.TABLE_LDC +"."+ DBHelper.COLUMN_LDC_USERID  +"="+ user.getId()  +
                    " GROUP BY _ingrdient_name ;";

            Cursor itemCursor = database.rawQuery(sql, null);
            itemCursor.moveToFirst();
            while(!itemCursor.isAfterLast()){
                LDCItem items = cursorToLDCItem(itemCursor);
                ldc.addItem(items);
                itemCursor.moveToNext();
            }
            itemCursor.close();

            listesDesCourses.add(ldc);
            cursor.moveToNext();
        }
        cursor.close();
        return listesDesCourses;
    }

    /**
     * Change l'état d'un élément de la liste (coché/non coché) dans la base de données
     * @param itemid l'identifiatn de l'élément
     * @param ldcid l'identifiant de la liste à laquelle il appartient
     * @param value la valeur à charger dans la base de données pour le champ checked de l'élément
     */
    public void setChecked(long itemid, long ldcid, int value){
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_LDCITEMS_CHECKED, value);
        database.update(DBHelper.TABLE_LDCITEMS, values,
                DBHelper.COLUMN_LDCITEMS_ITEMID + "="+ itemid + " AND " +
                        DBHelper.COLUMN_LDCITEMS_LDCID + "=" + ldcid, null);
    }

    /**
     * Convertie un tuple list_des_coursese de la base de données en ListDesCourses
     */
    private ListeDesCourses cursorToLDC(Cursor cursor){
        ListeDesCourses listeDesCourses = new ListeDesCourses();
        listeDesCourses.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_LDC_ID)));
        listeDesCourses.getDate().setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_LDC_DATEID)));
        listeDesCourses.setUserid(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_LDC_USERID)));
        return listeDesCourses;
    }

    /**
     * Convertie un tuple de la table LDCItems à un objet LDCItem
     */
    private LDCItem cursorToLDCItem(Cursor cursor){
        LDCItem ldcItem = new LDCItem();
        ldcItem.setId(cursor.getLong(cursor.getColumnIndex("_ingredient_id")));
        ldcItem.setNom(cursor.getString( cursor.getColumnIndex("_ingrdient_name")));
        ldcItem.setLdc_id(cursor.getLong(cursor.getColumnIndex("_ldcitem_ldc_id")));
        if (cursor.getInt(cursor.getColumnIndex("_ldcitem_checked")) == 0) {
            ldcItem.uncheck();
        } else {
            ldcItem.check();
        }
        return ldcItem;
    }

    /**
     * Convertie un tuple jour de la base de données en string
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
