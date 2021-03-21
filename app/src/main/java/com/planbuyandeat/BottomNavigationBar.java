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

public class BottomNavigationBar extends AppCompatActivity {
    private BottomNavigationView bnv;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_bottom_navigation_bar);
        bnv = findViewById(R.id.BottomNavigationView);
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