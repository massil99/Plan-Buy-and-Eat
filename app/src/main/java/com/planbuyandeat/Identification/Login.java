package com.planbuyandeat.Identification;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.planbuyandeat.BottomNavigationBar;
import com.planbuyandeat.Planning.Planning;
import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.security.PublicKey;
import java.util.prefs.Preferences;

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
     * Fichier de préférence utilisé comme session
     */
    public static String MySESSION = "session";

    /**
     * Les clés des valeurs à stocker dans la session
     */
    public static String USERID = "userid";
    public static String USERNAME = "username";
    public static String MDP = "mdp";

    /**
     * Ficheir de stockage de la session
     */
    private SharedPreferences userSession;
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
         * Instanciation du gesionnaire d'utilisatru
         */
        UsersSQLiteDAO userdao = new UsersSQLiteDAO(this);


        /**
         * Regerder si un utilisateur n'est pas déja connecté
         */
        userSession = getSharedPreferences(MySESSION, Context.MODE_PRIVATE);
        Utilisateur logedu = new Utilisateur();
        logedu.setId(userSession.getLong(USERID, -1));
        logedu.setUsername(userSession.getString(USERNAME, ""));
        logedu.setMdp(userSession.getString(MDP, ""));

        // Passer directement à l'activité principale si l'utilisateur n'a pas détruit sa session
        if(userdao.checkCredentials(logedu) != null){
            Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class);
            // Terminer l'activité actuelle
            finish();
            // Redirection vers l'activité BottomNavigationBar
            startActivity(i);
        }


        /**
         * Récuperation des  views
         */
        loginB = findViewById(R.id.btn_Signin);
        loginError = findViewById(R.id.text_login_error);
        username = findViewById(R.id.editview_nomUtilisateur);
        password = findViewById(R.id.editview_mdp);

        /**
         * Validation des données de connxeion dans le OnclickListener du boutton
         * loginB
         */
        loginB.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                userdao.open();
                if(!username.getText().toString().equals("") &&
                    !password.getText().toString().equals("")){
                    Utilisateur user = new Utilisateur();
                    user.setUsername(username.getText().toString());
                    user.hashAndSetMdp(password.getText().toString());
                    Utilisateur res = null;
                    // teste de connexion
                    if((res = userdao.checkCredentials(user)) != null){
                        /**
                         * Creation d'une session
                         */
                        SharedPreferences.Editor editor = userSession.edit();
                        editor.putString(USERNAME, res.getUsername());
                        editor.putString(MDP, res.getMdp());
                        editor.putLong(USERID, res.getId());
                        editor.apply();
                        Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class);
                        // Terminer l'activité actuelle
                        finish();
                        // Redirection vers l'activité BottomNavigationBar
                        startActivity(i);
                    }else {
                        // connexion échouée
                        loginError.setText(R.string.wrong_cred);
                    }
                }else {
                    loginError.setText(R.string.empty_fields);
                }
                userdao.close();
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