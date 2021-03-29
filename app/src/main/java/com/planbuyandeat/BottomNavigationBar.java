package com.planbuyandeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.planbuyandeat.Compte.Compte;
import com.planbuyandeat.Identification.Login;
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

    /**
     * Le fichier de session
     */
    private SharedPreferences userSession;

    private final String LAST_DISPLAYED_FRAGMENT = "last_frag";

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
         * Récuperer le deriner fragment selectionnéà partir de la session
         */
        userSession = getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);
        String lastFramgent = userSession.getString(LAST_DISPLAYED_FRAGMENT, "");
        lastFramgent = (lastFramgent.equals("")) ? getResources().getString(R.string.repertoire) : lastFramgent;
        int selectedItem =
                lastFramgent.equals(getResources().getString(R.string.repertoire))? R.id.nav_repertoire:
                lastFramgent.equals(getResources().getString(R.string.planning))? R.id.nav_planning:
                lastFramgent.equals(getResources().getString(R.string.compte))? R.id.nav_compte :
                lastFramgent.equals(getResources().getString(R.string.shopping))? R.id.nav_ldc : R.id.nav_map;


        /**
         * Définir un élément selectionné par defaut
         */
        bnv.setSelectedItemId(selectedItem);
        setFragment(selectedItem);

        /**
         * En choisissant un item de la bar, un fragment est affiché en focntion de cette item
         */
        bnv.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener(){
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                setFragment(item.getItemId());
                return true;
            }
        });
    }

    /**
     * Définir le fragment à afficher en fonction de l'élément selectionné dans la nav bar
     * @param id l'identifiant de l'élément selectionné
     */
    private void setFragment(int id){
        Fragment chosenFragment = null;
        int chosenFragmentTitle = 0;
        SharedPreferences.Editor editor = userSession.edit();
        Log.d(this.getClass().getName(), String.valueOf(id));

        switch (id){
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

        editor.putString(LAST_DISPLAYED_FRAGMENT,
                (id == R.id.nav_repertoire) ? getResources().getString(R.string.repertoire):
                (id == R.id.nav_planning) ? getResources().getString(R.string.planning):
                (id == R.id.nav_compte) ? getResources().getString(R.string.compte):
                (id == R.id.nav_ldc) ? getResources().getString(R.string.shopping):
                        getResources().getString(R.string.Map)
        );

        editor.apply();
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                chosenFragment).commit();
        ActivityActionBar.setTitle(chosenFragmentTitle);
    }
}