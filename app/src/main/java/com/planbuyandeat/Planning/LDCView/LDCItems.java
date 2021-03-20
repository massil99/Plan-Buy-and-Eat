package com.planbuyandeat.Planning.LDCView;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import com.planbuyandeat.R;

import java.util.ArrayList;
import java.util.List;

public class LDCItems extends AppCompatActivity {
    private ListView ldcItems;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldc_items);

        int listId = getIntent().getExtras().getInt("id");
        /* TODO Récupérer les information de la base de données */
        List<String> items = new ArrayList<>();
        items.add("formage");
        items.add("Tomate");

        ldcItems = findViewById(R.id.list_ldc_items);
        ArrayAdapter<String> ad = new ArrayAdapter<>(ldcItems.getContext(), R.layout.ldc_item, R.id.text_nomItem);
        ad.addAll(items);
        ldcItems.setAdapter(ad);
    }
}