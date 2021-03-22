package com.planbuyandeat.ListesDesCourses.LDCView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.planbuyandeat.R;

import java.util.ArrayList;
import java.util.List;

/**
 * Acitvité permettant d'afficher un liste de ccourses
 */
public class LDCItems extends AppCompatActivity {
    /**
     * La liste de coruses à afficher
     */
    private ListView ldcItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldc_items);

        /**
         * Récuperation de l'ID de la liste à afficher
         */
        int listId = getIntent().getExtras().getInt("id");
        /* TODO Récupérer les information de la base de données */

        //test
        List<String> items = new ArrayList<>();
        items.add("formage");
        items.add("Tomate");

        /**
         * Récupération de la liste et définiton de l'ArrayAdapteur permétant d'afficher tous les
         * élement de la lise
         */
        ldcItems = findViewById(R.id.list_ldc_items);
        ArrayAdapter<String> ad = new ArrayAdapter<>(ldcItems.getContext(), R.layout.ldc_item, R.id.text_nomItem);
        ad.addAll(items);
        ldcItems.setAdapter(ad);
    }
}