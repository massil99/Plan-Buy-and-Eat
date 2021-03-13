package com.planbuyandeat.Repertoire;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.planbuyandeat.R;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Repertoire extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_repertoire);

        ListView plats = findViewById(R.id.plats);

        ArrayList<Plat> arrayList = new ArrayList<>();
        /* Recuperation des donnée à partir de la base de données */
        List<String> nom_plats = new LinkedList<>();

        // Test
        arrayList.add(new Plat("pizza"));
        arrayList.add(new Plat("pizza"));

        PlatAdapter platAdapter = new PlatAdapter(this, R.layout.plat_item, arrayList);
        plats.setAdapter(platAdapter);

        FloatingActionButton fab = findViewById(R.id.btn_ajoutPlat);
        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   /* Mettre à jour la  base de données */
                   arrayList.add(new Plat("pizza"));
                   platAdapter.notifyDataSetChanged();
               }
        });
    }
}