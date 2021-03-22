package com.planbuyandeat.SQLite;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;

import androidx.annotation.Nullable;

public class DBHelper extends SQLiteOpenHelper {

    /**
     * TABLE users : stock les utilisateur dans la base de données
     *      id : id de l'utilisateur dans la base de données
     *      username : username de l'uitlisateur
     *      nom : nom de l'utilisateur
     *      prenom : prenom de l'utilisateur
     *      nbPlatJour : le nombre de plat que souhait avoir l'utillsateur par jour
     *      periode : la frequence à laquelle l'utilisateur souhait faire ses courses
     *      dateDebut : la date de début de generation du planning
     */
    public final static String TABLE_USERS = "users";
    public final static String COLUMN_USERS_ID = "id";
    public final static String COLUMN_USERS_USERNAME = "username";
    public final static String COLUMN_USERS_MDP = "mdp";
    public final static String COLUMN_USERS_NOM = "nom";
    public final static String COLUMN_USERS_PRENOM = "prenom";
    public final static String COLUMN_USERS_NBPlatJours = "nbPlatJour";
    public final static String COLUMN_USERS_PERIODE = "periode";
    public final static String COLUMN_USERS_DateDebut = "dateDebut";

    /**
     * TABLE plats: stock un plat dans la base de données
     *      id: id du plat dans la base de données
     *      nom : nom du  plat
     *      addid : l'id de l'uitlisateur l'ayant ajouté
     */
    public final static String TABLE_PLATS = "plats";
    public final static String COLUMN_PLATS_ID = "id";
    public final static String COLUMN_PLATS_NOM = "nom";
    public final static String COLUMN_PLATS_ADDERID = "adderid";

    /**
     * TABLE ingredints: stock un ingredient dans la base de données
     *      id : id de l'ingredient dans la base de données
     *      nom : de l'ingredient
     *      platid : id du plat auquel il est associé
     */
    public final static String TABLE_INGREDIENTS = "ingredients";
    public final static String COLUMN_INGREDIENTS_ID = "id";
    public final static String COLUMN_INGREDIENTS_NOM = "nom";
    public final static String COLUMN_INGREDIENTS_PLATID = "platid";

    /**
     * TABLE PlatJour: associe un plat à une date
     */
    public final static String TABLE_PLATJOUR = "platJour";
    public final static String COLUMN_PLATJOUR_DATE = "date";
    public final static String COLUMN_PLATJOUR_PLATID = "platid";

    /**
     * script de creation de la table users
     */
    private final static String CREATE_USERS =
            "CREATE TABLE " + TABLE_USERS + "("+
                COLUMN_USERS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                COLUMN_USERS_NOM + " TEXT NOT NULL,"+
                COLUMN_USERS_PRENOM + " TEXT NOT NULL,"+
                COLUMN_USERS_USERNAME + " TEXT NOT NULL,"+
                COLUMN_USERS_MDP + " TEXT NOT NULL,"+
                COLUMN_USERS_PERIODE + " INTEGER NOT NULL,"+
                COLUMN_USERS_DateDebut + " TEXT NOT NULL,"+
                COLUMN_USERS_NBPlatJours + " INTEGER NOT NULL"+
            ");";

    /**
     * script de creation de la table plats
     */
    private final static String CREATE_PLATS =
            "CREATE TABLE " + TABLE_PLATS + "("+
                    COLUMN_PLATS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_PLATS_NOM + " TEXT NOT NULL,"+
                    COLUMN_PLATS_ADDERID + " INTEGER NOT NULL,"+
                    "FOREIGN KEY (" + COLUMN_PLATS_ADDERID + ") " +
                    "REFERENCES " + TABLE_USERS + "(" + COLUMN_USERS_ID + ")"+
            ");";



    /**
     * script de creation de la table igredients
     */
    private final static String CREATE_INGREDIENTS =
            "CREATE TABLE " + TABLE_INGREDIENTS + "("+
                    COLUMN_INGREDIENTS_ID + " INTEGER PRIMARY KEY AUTOINCREMENT,"+
                    COLUMN_INGREDIENTS_NOM+ " TEXT NOT NULL,"+
                    COLUMN_INGREDIENTS_PLATID + " INTEGER NOT NULL,"+
                    "FOREIGN KEY (" + COLUMN_INGREDIENTS_PLATID + ") " +
                    "REFERENCES " + TABLE_PLATS + "(" + COLUMN_PLATS_ID + ")"+
            ");";


    /**
     * script de creation de la table platjour
     */
    private final static String CREATE_PLATJOUR =
            "CREATE TABLE " + TABLE_PLATJOUR + "("+
                    COLUMN_PLATJOUR_DATE + " TEXT PRIMARY KEY,"+
                    COLUMN_PLATS_NOM+ " TEXT NOT NULL,"+
                    COLUMN_PLATJOUR_PLATID + " INTEGER NOT NULL,"+
                    "FOREIGN KEY (" + COLUMN_PLATJOUR_PLATID + ") " +
                    "REFERENCES " + TABLE_PLATS + "(" + COLUMN_PLATS_ID + ")"+
            ");";


    /**
     * Nom de la bse de données
     */
    private static final String DATABASE_NAME = "pbe.db";

    /**
     * Version de la base de données
     */
    private static final int DATABASE_VERSION = 1;

    public DBHelper(@Nullable Context context){
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    /**
     * Creation de la base de données
     * @param db
     */
    @Override
    public void onCreate(SQLiteDatabase db) {
        db.execSQL(CREATE_USERS);
        db.execSQL(CREATE_PLATS);
        db.execSQL(CREATE_INGREDIENTS);
        db.execSQL(CREATE_PLATJOUR);
    }


    /**
     * Mise à jour de la version de la base de donnnées
     * @param db
     * @param oldVersion
     * @param newVersion
     */
    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        Log.w(DBHelper.class.getName(),
                "Upgrading database from version " + oldVersion + " to "
                        + newVersion + ", which will destroy all old data");
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATJOUR);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_INGREDIENTS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_PLATS);
        db.execSQL("DROP TABLE IF EXISTS " + TABLE_USERS);
        onCreate(db);
    }
}
