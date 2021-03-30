package com.planbuyandeat.ListesDesCourses.LDCView;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;

import com.planbuyandeat.BottomNavigationBar;
import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.ListesDesCourses.ListesDesCourcesFragment;
import com.planbuyandeat.R;
import com.planbuyandeat.Repertoire.Repertoire;
import com.planbuyandeat.SQLite.DAOs.IngredientsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.LDCSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatJourSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.CustomDate;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.ListeDesCourses;
import com.planbuyandeat.SQLite.Models.PlatJour;
import com.planbuyandeat.SQLite.Models.Utilisateur;

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

    /**
     * Date prévu pour faire les courses
     */
    private TextView dateLDC;
    private TextView dateLDC_start;

    /**
     * Button de retoure vers l'activité applante
     */
    private ImageButton back;

    /**
     * Fichier de la session utilisateur
     */
    private SharedPreferences userSession;

    /**
     * Gesionnaire de connxion à la base de données
     */
    private UsersSQLiteDAO userdao;
    private LDCSQLiteDAO ldcdao;

    /**
     * L'utilisateur actuellement connecté
     */
    private Utilisateur user;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ldc_items);

        /**
         * Récuperation des information de la session
         */
        userSession = getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);
        long userid = userSession.getLong(Login.USERID, -1);


        userdao = new UsersSQLiteDAO(this);
        ldcdao = new LDCSQLiteDAO(this);

        /**
         * Récuperation de l'uitlisateur actuellement connecté
         */
        userdao.open();
        user = userdao.get(userid);
        userdao.close();

        /**
         * Récuperation de la liste de courses selectionnée
         */
        ldcdao.open();
        long ldcid = getIntent().getExtras().getLong("ldcid");
        ListeDesCourses listeDesCourses = ldcdao.get(ldcid);
        Log.d(this.getClass().getName(), "diplayed ldc_id="+listeDesCourses.getId());
        ldcdao.close();

        /**
         * Récupération de la liste et définiton de l'ArrayAdapteur permétant d'afficher tous les
         * élement de la lise
         */
        ldcItems = findViewById(R.id.list_ldc_items);
        LDCItemsAdapter ad = new LDCItemsAdapter(ldcItems.getContext(), R.layout.ldc_item,
                listeDesCourses.getItems());
        ldcItems.setAdapter(ad);

        /**
         * Afficher les dates
         */
        dateLDC = findViewById(R.id.text_dateLDC);
        dateLDC_start = findViewById(R.id.text_dateLDC_start);
        dateLDC.setText(listeDesCourses.getDate().toString());
        CustomDate dateDebutP = listeDesCourses.getDate();
        dateDebutP.addDays(-1 * user.getPeriod() + 1);
        dateLDC_start.setText(dateDebutP.toString());

        back = findViewById(R.id.btn_back_from_ldcitems);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), BottomNavigationBar.class);
                finish();
                startActivity(i);
            }
        });
    }
}