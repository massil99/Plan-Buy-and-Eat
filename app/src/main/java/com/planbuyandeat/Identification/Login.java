package com.planbuyandeat.Identification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planbuyandeat.Planning.Planning;
import com.planbuyandeat.R;

/**
 * Activity permetant de s'identifier à l'application ou créer un compte
 * ou alors choisir d'utiliser la carte sans connextion
 */
public class Login extends AppCompatActivity {
    /**
     * Button de connextion
     */
    private Button loginB;

    /**
     * Button qui renvoie vers l'activité Signup
     */
    private Button singupB;

    /**
     * Button qui renvoie vers l'activité Map
     */
    private Button mapB;

    /**
     * Champ du formulaire d'identification représentant le NOM D'UTILISATEUR
     */
    private EditText username;

    /**
     * Champ du formulaire d'identification représentant le MOT DE PASSE du compte
     */
    private EditText password;

    /**
     * Message qui s'affiche en cas d'erreur d'identification
     */
    private TextView loginError;

    /**
     * A la creataion de l'acitivité ses composant sont récupérés
     * et on s'occupe de valider le formualaire d'identifiaction ainsi
     * que d'implémenter les différentes fonctionnalités des buttons
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        /**
         * Récuperation des  views
         */
        loginB = findViewById(R.id.btn_Signin);
        loginError = findViewById(R.id.text_login_error);
        username = findViewById(R.id.editview_nomUtilisateur);
        password = findViewById(R.id.editview_prenom);

        /**
         * Validation des données de connxeion dans le OnclickListener du boutton
         * loginB
         */
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* TODO Check credentials */

                // connexion réussit
                if(true /*success*/){

                    Intent i = new Intent(getApplicationContext(), Planning.class);
                    /* TODO Simulate a session*/

                    // Redirection verss l'activité Planning
                    startActivity(i);
                }else{
                    // connexion échouée
                    loginError.setText(R.string.wrong_cred);
                }
            }
        });

        singupB = findViewById(R.id.btn_Signup);
        /**
         * Redirection vers l'acitvité Signup en cliquant sur le boutton
         * sinupB   
         */
        singupB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Signup.class);
                startActivity(i);
            }
        });
    }
}