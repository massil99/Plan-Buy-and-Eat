package com.planbuyandeat.Planning.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import com.planbuyandeat.BottomNavigationBar;
import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.Utilisateur;
import com.planbuyandeat.utils.InputFilterMinMax;

import java.util.Objects;

/**
 * Activité ou l'utilisateur pourra modifier les paramétres de géneration de plannig
 */
public class Settings extends AppCompatActivity {
    /**
     * Champ de saisie de la péroide de shopping
     */
    private EditText period;

    /**
     * Button pour changer la date de début de géneration
     */
    private Button changeDateDebut;

    /**
     * Champ de saisie du nombre de plat par jours
     */
    private EditText platPJour;

    /**
     * Utilisateur actuellement connecté
     */
    private Utilisateur user;

    /**
     * Gestionnaire des utilisateur en base de données
     */
    private UsersSQLiteDAO usersSQLiteDAO;

    /**
     * Fichier de session
     */
    private SharedPreferences session;

    /**
     * Button de retoure à l'acitivité applante
     */
    private ImageButton back;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        period = findViewById(R.id.editview_period);
        changeDateDebut = findViewById(R.id.btn_modifierDateDebutPeriode);
        platPJour = findViewById(R.id.editview_nbPlatPjour);
        back = findViewById(R.id.btn_back_from_settings);

        // Ouverture du fichier de session
        session = getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);

        usersSQLiteDAO = new UsersSQLiteDAO(this);

        // Récuperation de l'utilisateur actuellement connecté
        long uid = session.getLong(Login.USERID, -1);
        usersSQLiteDAO.open();
        user = usersSQLiteDAO.get(uid);
        usersSQLiteDAO.close();

        /**
         * Mettre à jour les information dés que le champ de siaise perd le focus
         */
        period.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String res = period.getText().toString();
                    if(!Objects.equals("",res)){
                        int periodL = Integer.parseInt(res);
                        user.setPeriod(periodL);
                        Log.d("focus", "focus change " + periodL);

                        // Mise à jour de la péroide en base de données
                        usersSQLiteDAO.open();
                        usersSQLiteDAO.update(user);
                        usersSQLiteDAO.close();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                    }
                }
            }
        });

        /**
         * Mettre à jour la date de début de génération du planning
         */
        changeDateDebut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DatePicker");
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        /* TODO Stocker dans les preferneces */
                        String date = dayOfMonth + "-"+
                                String.valueOf(month+1) + "-" +
                                        year;

                        user.setDateDebut(date);
                        // Mise à jour de la date de début en base de données
                        usersSQLiteDAO.open();
                        usersSQLiteDAO.update(user);
                        usersSQLiteDAO.close();
                        Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                    }
                });
            }
        });

        // Limiter les choix de l'uitilisateur pour le nombre de plats par jour
        platPJour.setFilters(new InputFilterMinMax[]{new InputFilterMinMax(1, 5)});

        /**
         * Mettre à jours le nombre de plat par jour des que le champ de siaisie perd le focus
         */
        platPJour.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String res = platPJour.getText().toString();
                    if(!Objects.equals("",res)){
                        int nbPlat = Integer.parseInt(res);

                        user.setNbPlatjour(nbPlat);

                        // Mise à jour de la date de début en base de données
                        usersSQLiteDAO.open();
                        usersSQLiteDAO.update(user);
                        usersSQLiteDAO.close();
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                    };
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class);
                startActivity(i);
            }
        });
    }
}