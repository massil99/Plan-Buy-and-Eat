package com.planbuyandeat;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.LinkedList;
import java.util.List;

public class Repertoire extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertoire);

        LinearLayout plats = findViewById(R.id.plats);

        /* Recuperation des donnée à partir de la base de données */
        List<String> nom_plats = new LinkedList<>();

        for(String nom :nom_plats){
            LinearLayout plat = new LinearLayout(plats.getContext());

            /* Ajouter un style pour chaque plat */
            plat.setOrientation(LinearLayout.HORIZONTAL);

            EditText nom_plat = new EditText(plat.getContext());
            nom_plat.setText(nom);
            plat.addView(nom_plat);

            Button ings = new Button(plat.getContext());
            ings.setText(">");
            ings.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    /* Passer à l'activiter Liste des igrédients */
                }
            });
            plat.addView(ings);

            plats.addView(plat);
        }

        FloatingActionButton fab = findViewById(R.id.btn_ajoutPlat);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                LinearLayout plat = new LinearLayout(plats.getContext());

                /* Ajouter un style pour chaque plat */
                plat.setOrientation(LinearLayout.HORIZONTAL);

                EditText nom_plat = new EditText(plat.getContext());
                plat.addView(nom_plat);

                Button ings = new Button(plat.getContext());
                ings.setText(">");
                ings.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        /* Passer à l'activiter Liste des igrédients */
                    }
                });
                plat.addView(ings);

                plats.addView(plat);
            }
        });
    }
}