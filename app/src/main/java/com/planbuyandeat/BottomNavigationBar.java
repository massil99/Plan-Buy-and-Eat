package com.planbuyandeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.planbuyandeat.Compte.Compte;
import com.planbuyandeat.ListesDesCourses.ListesDesCourcesFragment;
import com.planbuyandeat.Planning.Planning;
import com.planbuyandeat.Repertoire.Repertoire;

/**
 * Acitivité ayant un Bottom naviagation view et un fragment qui change entre les activité
 * de l'applicaiton
 */
public class BottomNavigationBar extends AppCompatActivity {
    /**
     * La bar de navigation
     */
    private BottomNavigationView bnv;

    private ActionBar ActivityActionBar;

    /**
     * Chargment d'un fragement par défaut à la création et gestion de l'evenement de click
     * sur le botttomNaviationBar
     * @param savedInstanceState
     */
    @SuppressLint("RestrictedApi")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);
        bnv = findViewById(R.id.BottomNavigationView);

        /**
         * Changer le titre de l'aciton bar
         */
        ActivityActionBar = getSupportActionBar();
        ActivityActionBar.setTitle(R.string.list_plats);

        /**
         * Définir un élément selectionné par defaut
         */
        bnv.setSelectedItemId(R.id.nav_repertoire);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Repertoire()).commit();

        /**
         * En choisissant un item de la bar, un fragment est affiché en focntion de cette item
         */
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment chosenFragment = null;
                int chosenFragmentTitle = 0;
                switch (item.getItemId()){
                    case R.id.nav_planning:
                        chosenFragmentTitle = R.string.planning;
                        chosenFragment = new Planning();
                        break;
                    case R.id.nav_repertoire:
                        chosenFragmentTitle = R.string.repertoire;
                        chosenFragment = new Repertoire();
                        break;
                    case R.id.nav_compte:
                        chosenFragmentTitle = R.string.compte;
                        chosenFragment = new Compte();
                        break;
                    case R.id.nav_ldc:
                        chosenFragmentTitle = R.string.shopping;
                        chosenFragment = new ListesDesCourcesFragment();
                        break;
                    default:
                        chosenFragmentTitle = R.string.Map;
                        chosenFragment = new Map();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        chosenFragment).commit();
                ActivityActionBar.setTitle(chosenFragmentTitle);
                return true;
            }
        });
    }
}