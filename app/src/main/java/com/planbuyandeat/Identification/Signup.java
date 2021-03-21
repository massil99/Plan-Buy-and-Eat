package com.planbuyandeat.Identification;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.planbuyandeat.R;

/**
 * Cette Activité se charge d'afficher le fomulaire d'inscription,
 * de traiter les données saisient par l'utilisateur et de les stocker en
 * base de données
 */
public class Signup extends AppCompatActivity {
    /**
     * Champ du formulaire qui permet de saisir le PRENOM de l'uilisateur
     */
    private EditText firstname;

    /**
     * Champ du formulaire qui permet de saisir le NOM de l'uilisateur
     */
    private EditText lastname;

    /**
     * Champ du formulaire qui permet de saisir le NOM D'UTILISATEUR de l'uilisateur
     */
    private EditText username;

    /**
     * Champ du formulaire qui permet de saisir le MOT DE PASSE de l'uilisateur
     */
    private EditText password;

    /**
     * Champ du formulaire qui permet de saisir la CONFIRMATION DU MOT DE PASSE
     */
    private EditText passwordx;

    /**
     * Button qui permet de valider le fomulaire
     */
    private Button regisgter;

    /**
     * Message qui s'afficher en cas d'erreurr dans la saise des données
     */
    private TextView signupError;

    /**
     * A la creation de l'acitivité toutes les vues de l'interface graphique
     * sont récupérées afin de récuper leur données et de les traiter
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signup);

        /**
         * Récuperation des vues
         */
        firstname = findViewById(R.id.editview_prenom);
        lastname = findViewById(R.id.editview_nom);
        username = findViewById(R.id.editview_nomUtilisateur_signup);
        password = findViewById(R.id.editview_mdp_signup);
        passwordx = findViewById(R.id.editview_mdpx);
        regisgter = findViewById(R.id.btn_register);
        signupError = findViewById(R.id.text_signup_error);

        /**
         * Traitemnt des données dans le OnClickListenner du bouton de
         * validatiokn
         * */
        regisgter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // Tester si il n'y a pas de champ vide
                if(!firstname.getText().equals("") &&
                    !lastname.getText().equals("") &&
                    !username.getText().equals("") &&
                    !password.getText().equals("") &&
                    !passwordx.getText().equals("")){
                    // Verifier la confirmation du mot de passe
                    if(passwordx.getText().equals(password.getText())){
                        /* TODO creaion d'un utilisateur dans la base de données */
                    }else
                        // Afficher un message d'erreur [Confirmation incorrect]
                        signupError.setText(R.string.passwd_not_matching);
                }else
                    // Afficher un message d'erreur [Champ vide]
                    signupError.setText(R.string.empty_fields);
            }
        });

    }
}