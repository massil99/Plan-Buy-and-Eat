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

        /* TODO: Récuperation de l'uitilisateur à partir de la base de données*/

        //test
        Utilisateur user = new Utilisateur();
        user.setNom("moungad");
        user.setPrenom("massil");
        user.setUsername("lissam99");
        user.hasAndSetMdp("hello");
        /**
         *
         */
        validate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout l = findViewById(R.id.layout_changepass);

                if(!odlPass.getText().toString().equals("") &&
                    !newPass.getText().toString().equals("") &&
                    !newPassx.getText().toString().equals("")){
                    if(newPassx.getText().toString().equals(newPass.getText().toString())){
                        if(odlPass.getText().toString().equals(user.getMdp())){
                            Snackbar.make(l, R.string.changesApplyed, Snackbar.LENGTH_LONG).show();
                        }else
                            Snackbar.make(l, R.string.wrong_pass, Snackbar.LENGTH_LONG).show();
                    }else
                        Snackbar.make(l, R.string.passwd_not_matching, Snackbar.LENGTH_LONG).show();
                }else
                    Snackbar.make(l, R.string.empty_fields, Snackbar.LENGTH_LONG).show();
            }
        });
    }
}