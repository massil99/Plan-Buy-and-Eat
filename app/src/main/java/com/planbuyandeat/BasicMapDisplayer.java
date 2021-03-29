package com.planbuyandeat;

import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import android.os.Bundle;

/**
 * Cette activité est utilisée pour afficher la carte sans avoir a se connecter
 * avec un compte utilisateur
 */
public class BasicMapDisplayer extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_basic_map_displayer);

        // Afficher le fragment de la carte
        FragmentManager fragmentManager = getSupportFragmentManager();
        Fragment mapFragment = new Map();
        fragmentManager.beginTransaction().replace(R.id.fragment_bmapd, mapFragment).commit();
    }
}