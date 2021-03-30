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

public class ChangePass extends AppCompatActivity {
    /**
     * Mot de passe  courrant
     */
    private EditText odlPass;

    /**
     * Nouvaeau mot de passe
     */
    private EditText newPass;
    /**
     * Confriamtion du nouveau mot de passe
     */
    private EditText newPassx;

    /**
     * Boutton de validation des modification
     */
    private Button validate;

    /**
     * Button de retour
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
        setContentView(R.layout.activity_change_pass);

        /**
         * Récuperation des vues de l'acitivité
         */
        odlPass = findViewById(R.id.editview_changeOldPass);
        newPass = findViewById(R.id.editview_changeNewPass);
        newPassx = findViewById(R.id.editview_changeNewPassx);
        validate = findViewById(R.id.btn_chagneValidatePass);
        back = findViewById(R.id.btn_back_from_change_pass);

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
         * Validation du formulaire de changment de mot de passe
         */
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(!odlPass.getText().toString().equals("") &&
                    !newPass.getText().toString().equals("") &&
                    !newPassx.getText().toString().equals("")){
                    if(newPassx.getText().toString().equals(newPass.getText().toString())){
                        // ouverture de l'instance de base de données
                        userdao.open();

                        user.hashAndSetMdp(odlPass.getText().toString());
                        // Teste de la validité du mot de passe courrant
                        if(userdao.checkCredentials(user) != null){
                            // Mise à jour du nouveau mot de passe dans la base de données
                            user.hashAndSetMdp(newPass.getText().toString());
                            userdao.update(user);
                            Snackbar.make(v, R.string.changesApplyed, Snackbar.LENGTH_LONG).show();
                        }else
                            Snackbar.make(v, R.string.wrong_pass, Snackbar.LENGTH_LONG).show();
                        userdao.open();
                    }else
                        Snackbar.make(v, R.string.passwd_not_matching, Snackbar.LENGTH_LONG).show();
                }else
                    Snackbar.make(v, R.string.empty_fields, Snackbar.LENGTH_LONG).show();
            }
        });

        back.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class );
                finish();
                startActivity(i);
            }
        });
    }
}