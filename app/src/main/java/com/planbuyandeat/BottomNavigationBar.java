package com.planbuyandeat;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.MenuItem;
import android.widget.Toast;

import com.google.android.material.bottomnavigation.BottomNavigationView;
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

    /**
     * Chargment d'un fragement par défaut à la création et gestion de l'évément de click
     * sur le botttomNaviationBar
     * @param savedInstanceState
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);
        bnv = findViewById(R.id.BottomNavigationView);

        /**
         * En choisissant un item de la bar un fragment est afficher en focntio de cette item
         */
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Fragment chosenFragment = null;
                switch (item.getItemId()){
                    case R.id.nav_planning:
                        chosenFragment = new Planning();
                        break;
                    case R.id.nav_repertoire:
                        chosenFragment = new Repertoire();
                        break;
                    default:
                        chosenFragment = new Planning();
                        break;
                }
                getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                        chosenFragment).commit();
                return true;
            }
        });
    }
}