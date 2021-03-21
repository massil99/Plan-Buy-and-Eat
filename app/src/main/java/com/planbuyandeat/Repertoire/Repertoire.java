package com.planbuyandeat.Repertoire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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

/**
 * Activité permetant de gérer le répertoire des plats par l'utilisateur
 * il pourrat en ajouter, en supprimer ou modifier la liste des ingredient
 * compansant chaque plat
 */
public class Repertoire extends Fragment {
    /**
     * ListView permetant d'afficher la liste des palts
     */
    private ListView plats;

    /**
     * Button qui permet d'ajouter un plat
     */
    private FloatingActionButton fab;

    /**
     * Chargment du layout de l'activité
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.activity_repertoire, container, false);
    }

    /**
     * A la creation du fragment, les données sur les plat deja enregistrés sont récupérées à
     * partir de la base de données, puis une listeview est créée en utilisant ces données
     * représenttant un plat par ligne avec un button qui permet de passer à l'acitivité
     * ingredients
     * @param view
     * @param savedInstanceState
     */
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        /**
         * Récupération de la listview des palts du répertoire
         */
        plats = getActivity().findViewById(R.id.plats);

        /**
         * Récuperation de données se trouvant dans la base de données
         */
        ArrayList<Plat> arrayList = new ArrayList<>();
        /* TODO Recuperation des donnée à partir de la base de données */
        List<String> nom_plats = new LinkedList<>();

        // Test
        arrayList.add(new Plat("pizza"));
        arrayList.add(new Plat("pizza"));

        /**
         * Instanciation de l'ArrayAdapteur permetant d'afficher un plat,
         * Ici PlatAdapter hérite de ArrayAdapteur pour pouvoir customiser la liste
         */
        PlatAdapter platAdapter = new PlatAdapter(getContext(), R.layout.plat_item, arrayList);

        /**
         * Définir PlatAdapteur comme l'adaptateur de la liste des plats
         */
        plats.setAdapter(platAdapter);

        /**
         * Récuperation du button d'ajout de plat à la list
         */
        fab = getActivity().findViewById(R.id.btn_ajoutPlat);
        /**
         * En cliquant sur fab on ajout un plat dans PlatAdapteur et notifie la liste que
         * les données on était mises à jour, une modification de la base de données est faite
         * par la suite
         */
        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   arrayList.add(new Plat(""));
                   platAdapter.notifyDataSetChanged();
                   /* TODO Mettre à jour la  base de données */
               }
        });
    }
}