package com.planbuyandeat.Compte;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.planbuyandeat.SQLite.Models.Utilisateur;
import com.planbuyandeat.R;

import java.util.Objects;

/**
 * Activité permettant à l'utilisateur de modifier ses informations pernsonnelles
 */
public class ChangeInfo extends AppCompatActivity {
    /**
     * Nouveau nom de l'uitilisateur
     */
    private EditText nom;

    /**
     * Nouveau prénom de l'utilisateur
     */
    private EditText prenom;

    /**
     * Novueau username
     */
    private EditText username;

    /**
     * EditText pour la saisie de mot passe avant de confirmer les chagnement
     */
    private EditText pass;

    /**
     * Button de validation des changments
     */
    private Button valider;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_info);

        /**
         * Récuperation des vues de l'acitivité
         */
        nom = findViewById(R.id.editview_changeNom);
        prenom = findViewById(R.id.editview_changePrenom);
        username = findViewById(R.id.editview_changeUsername);
        pass = findViewById(R.id.editview_passCheckBeforeChange);
        valider = findViewById(R.id.btn_chagneValidateInfo);
        /**
         * Récuperation de l'id de l'utilisateur passé à cette activité
         */
        Bundle extra = getIntent().getExtras();
        int id = extra.getInt("id");

        /* TODO: récuperer l'utilisateur à partir  de la base de données */

        //test
        Utilisateur user = new Utilisateur("moungad", "massil", "lissam99", "hello");
        /**
         * chagner les info de l'utilisateur
         */
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = findViewById(R.id.layout_changeinfo);

                // tester les champs vide
                if(!nom.getText().toString().equals("")       &&
                   !prenom.getText().toString().equals("")   &&
                   !username.getText().toString().equals("") &&
                   !pass.getText().toString().equals("")){
                    // teste du mot de passe
                    if(Objects.equals(pass.getText().toString(), user.getMdp())){
                        /** TODO: modifier les infoamtion de la base de données */
                        Snackbar.make(l, R.string.changesApplyed, Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(l, R.string.wrong_pass, Snackbar.LENGTH_LONG).show();
                    }
                }else
                    Snackbar.make(l, R.string.empty_fields, Snackbar.LENGTH_LONG).show();

            }
        });

    }
}