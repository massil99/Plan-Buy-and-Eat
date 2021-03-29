package com.planbuyandeat.Repertoire;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.SQLite.DAOs.PlatsSQLiteDAO;
import com.planbuyandeat.SQLite.Models.Plat;
import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.util.ArrayList;

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
     * Urilisation des fichier de préferences comme session d'utilisateur
     */
    private SharedPreferences userSession;

    /**
     * A la creation du fragment, les données sur les plat deja enregistrés sont récupérées à
     * partir de la base de données, puis une listeview est créée en utilisant ces données
     * représenttant un plat par ligne avec un button qui permet de passer à l'acitivité
     * ingredients
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_repertoire, container, false);

        /**
         * Récupération de la listview des palts du répertoire
         */
        plats = view.findViewById(R.id.plats);

        /**
         * Récuperation des infomation de la session
         */
        Utilisateur user = new Utilisateur();

        userSession = getActivity().getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);
        user.setId(userSession.getLong(Login.USERID, -1));
        user.setUsername(userSession.getString(Login.USERNAME, ""));
        user.setMdp(userSession.getString(Login.MDP, ""));

        /**
         * Récuperation de données se trouvant dans la base de données
         */
        PlatsSQLiteDAO platdao = new PlatsSQLiteDAO(getContext());
        platdao.open();
        ArrayList<Plat> arrayList = (ArrayList<Plat>) platdao.getAllUserPlats(user);
        platdao.close();

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
        fab = view.findViewById(R.id.btn_ajoutPlat);
        /**
         * En cliquant sur fab on ajout un plat dans PlatAdapteur et notifie la liste que
         * les données on était mises à jour, une modification de la base de données est faite
         * par la suite
         */
        fab.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View v) {
                   Plat plat = new Plat();
                   plat.setNom("");
                   plat.setAdderid(user.getId());

                   platdao.open();
                   Plat res = platdao.create(plat);
                   arrayList.add(res);
                   platAdapter.notifyDataSetChanged();
                   platdao.close();
               }
        });

        return view;
    }
}