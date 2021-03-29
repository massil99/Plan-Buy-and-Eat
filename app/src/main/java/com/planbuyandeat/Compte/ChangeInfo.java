package com.planbuyandeat.Compte;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;

import com.google.android.material.snackbar.Snackbar;
import com.planbuyandeat.BottomNavigationBar;
import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.Utilisateur;
import com.planbuyandeat.R;
import com.planbuyandeat.utils.MD5HashFunction;

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

    /**
     * Button de retoure
     */
    private ImageButton back;

    /**
     * Gesionnaire d'utilsiateur
     */
    private UsersSQLiteDAO userdao;

    /**
     * Fichier de préférence utilisé comme session
     */
    private SharedPreferences userSession;

    /**
     * L'utilisateur actuel
     */
    private Utilisateur user;

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
        back = findViewById(R.id.btn_back_from_change_info);

        userdao = new UsersSQLiteDAO(this);
        /**
         * Récuperation des info de la session
         */
        userSession = getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);
        long id = userSession.getLong(Login.USERID, -1);
        user = new Utilisateur();
        userdao.open();
        user = userdao.get(id);
        userdao.close();

        /**
         * chagner les info de l'utilisateur
         */
        valider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // tester les champs vide
                if(!nom.getText().toString().equals("")       &&
                   !prenom.getText().toString().equals("")   &&
                   !username.getText().toString().equals("") &&
                   !pass.getText().toString().equals("")){
                    String hashed = MD5HashFunction.hash(pass.getText().toString());
                    user.setUsername(username.getText().toString());
                    user.setPrenom(prenom.getText().toString());
                    user.setNom(nom.getText().toString());

                    // teste du mot de passe
                    if(Objects.equals(hashed, user.getMdp())){
                        userdao.open();
                        userdao.update(user);
                        userdao.close();
                        Snackbar.make(v, R.string.changesApplyed, Snackbar.LENGTH_LONG).show();
                    }else {
                        Snackbar.make(v, R.string.wrong_pass, Snackbar.LENGTH_LONG).show();
                    }
                }else
                    Snackbar.make(v, R.string.empty_fields, Snackbar.LENGTH_LONG).show();

            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class );
                startActivity(i);
            }
        });
    }
}