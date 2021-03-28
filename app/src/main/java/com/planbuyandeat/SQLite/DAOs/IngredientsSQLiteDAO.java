package com.planbuyandeat.SQLite.DAOs;

import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.planbuyandeat.SQLite.DBHelper;
import com.planbuyandeat.SQLite.Models.CustomDate;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.Plat;

import java.util.ArrayList;
import java.util.List;

public class IngredientsSQLiteDAO implements DAO<Ingredient> {
    // Champs de la base de données
    private SQLiteDatabase database;
    private DBHelper dbHelper;
    private String[] allColumns = {
            DBHelper.COLUMN_INGREDIENTS_ID,
            DBHelper.COLUMN_INGREDIENTS_NOM,
            DBHelper.COLUMN_INGREDIENTS_PLATID
    };

    public IngredientsSQLiteDAO(Context context) {
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
     * Créer un ingredient dans la base de de données
     * @param o objet ingredient contenant les information à stocker
     * @return l'ingredient avec son id dans la base de de données
     */
    @Override
    public Ingredient create(Ingredient o) {
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_INGREDIENTS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_INGREDIENTS_PLATID, o.getPlatId());

        /**
         * Récuperation de l'id du tuple inseré
         */
        long insertid = database.insert(DBHelper.TABLE_INGREDIENTS, null,
                values);
        Cursor cursor = database.query(DBHelper.TABLE_INGREDIENTS,
                allColumns, DBHelper.COLUMN_INGREDIENTS_ID + " = "+ insertid,
                null, null, null, null);

        if(cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            Ingredient ingredient = cursorToIngredient(cursor);
            cursor.close();
            return ingredient;
        }else
            return null;
    }

    /**
     * Mettre à jour l'ingredient dans la base de données
     * @param o les nouvelles infomations
     */
    @Override
    public void update(Ingredient o) {
        long id = o.getId();
        database.delete(DBHelper.TABLE_INGREDIENTS, DBHelper.COLUMN_INGREDIENTS_ID
                + " = " + id, null);
    }

    /**
     * Supprimer un ingredient de la base de données
     * @param o
     */
    @Override
    public void delete(Ingredient o) {
        long id = o.getId();
        ContentValues values = new ContentValues();
        values.put(DBHelper.COLUMN_PLATS_NOM, o.getNom());
        values.put(DBHelper.COLUMN_INGREDIENTS_PLATID, o.getPlatId());

        database.update(DBHelper.TABLE_INGREDIENTS, values, DBHelper.COLUMN_INGREDIENTS_ID + " = " + id, null);
    }

    /**
     * Récuperer les ingredient d'un plat
     * @param plat Le plat pour lequel il faut récuper les ingredients
     * @return la liste des ingredients associés au plat
     */
    public List<Ingredient> getAllPlatIngredients(Plat plat) {
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_INGREDIENTS,
                allColumns, DBHelper.COLUMN_INGREDIENTS_PLATID + " = " + plat.getId()
                , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ingredient ingredient = cursorToIngredient(cursor);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();

        return ingredients;
    }

    /**
     * Retournes tous les ingredients d'un plat
     * @param id l'identifiant de plat
     * @return
     */
    public List<Ingredient> getAllPlatIngredients(long id) {
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_INGREDIENTS,
                allColumns, DBHelper.COLUMN_INGREDIENTS_PLATID + " = " + id
                , null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ingredient ingredient = cursorToIngredient(cursor);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();

        return ingredients;
    }


    /**
     * Récuper un ingredient à partir de son id
     * @param id id de l'ingredient
     * @return l'objet ingredient content les infomation de la base de données
     */
    public Ingredient get(long id){
        Cursor cursor = database.query(DBHelper.TABLE_INGREDIENTS,
                allColumns, DBHelper.COLUMN_INGREDIENTS_ID + " = "+ id,
                null, null, null, null);

        if(cursor != null && cursor.getCount() >0){
            cursor.moveToFirst();
            Ingredient ingredient = cursorToIngredient(cursor);
            cursor.close();
            return ingredient;
        }else
            return null;
    }


    /**
     * Rerourne tous les ingredients de la base de données
     */
    @Override
    public List<Ingredient> getAll() {
        List<Ingredient> ingredients = new ArrayList<>();
        Cursor cursor = database.query(DBHelper.TABLE_INGREDIENTS,
                allColumns, null, null, null, null, null);

        cursor.moveToFirst();
        while (!cursor.isAfterLast()){
            Ingredient ingredient = cursorToIngredient(cursor);
            ingredients.add(ingredient);
            cursor.moveToNext();
        }
        cursor.close();

        return ingredients;
    }
    /**
     * Convertie un tuple de la ingredient de la base de données en Ingredient
     * @param cursor
     * @return
     */
    private Ingredient cursorToIngredient(Cursor cursor){
        Ingredient ingredient = new Ingredient();
        ingredient.setId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_INGREDIENTS_ID)));
        ingredient.setNom(cursor.getString(cursor.getColumnIndex(DBHelper.COLUMN_INGREDIENTS_NOM)));
        ingredient.setPlatId(cursor.getLong(cursor.getColumnIndex(DBHelper.COLUMN_INGREDIENTS_PLATID)));
        return ingredient;
    }
}
