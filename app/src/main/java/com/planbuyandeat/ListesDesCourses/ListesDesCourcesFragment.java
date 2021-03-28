package com.planbuyandeat.ListesDesCourses;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.ListesDesCourses.LDCView.LDCItems;
import com.planbuyandeat.Planning.Planning;
import com.planbuyandeat.SQLite.DAOs.IngredientsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.JourSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.LDCSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatJourSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.CustomDate;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.LDCItem;
import com.planbuyandeat.SQLite.Models.ListeDesCourses;
import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.Models.PlatJour;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import java.util.ArrayList;
import java.util.List;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class ListesDesCourcesFragment extends Fragment {
    /**
     * Listeview pour la liste des dates des courses à faire
     */
    private ListView planning;

    /**
     * Fichier de session
     */
    private SharedPreferences userSession;

    /**
     * Gestionnaires de connection à la base de données
     */
    private UsersSQLiteDAO userdao;
    private PlatJourSQLiteDAO pjdao;
    private IngredientsSQLiteDAO ingredientsSQLiteDAO;
    private LDCSQLiteDAO ldcdao;
    private JourSQLiteDAO jourSQLiteDAO;
    /**
     * Utilisateur actuellemnt connecté
     */
    private Utilisateur user;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listes_des_cources, container, false);

        /**
         * Chargement des vue du fragment
         */
        planning = view.findViewById(R.id.list_plannig);

        /**
         * Ouverutre du fichier de session
         */
        userSession = getActivity().getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);

        userdao = new UsersSQLiteDAO(getContext());
        pjdao = new PlatJourSQLiteDAO(getContext());
        ingredientsSQLiteDAO = new IngredientsSQLiteDAO(getContext());
        ldcdao = new LDCSQLiteDAO(getContext());
        jourSQLiteDAO = new JourSQLiteDAO(getContext());

        /**
         * Récuperation des infomations de l'uitlisateur à stockées dans la session
         */
        userdao.open();
        user = userdao.get(userSession.getLong(Login.USERID, -1));
        userdao.close();

        /**
         * Récuperation des listes de courses de l'uilisateur actuel
         */
        List<ListeDesCourses> listesDesCourses;
        ldcdao.open();
        listesDesCourses = ldcdao.getAllUserLists(user);
        ldcdao.close();

        /**
         * ArrayAdapteur pour le liste des jours ou l'utilisateur à prévu de faire ses
         * courses. ListeDeCoursesAdapter hérite de ArrayAdapteur, pour faire en sorte d'afficher
         * un objet ListeDeCourses par ligne
         */
        ListeDeCoursesAdapter listdDeCoursesAdapter =
                new ListeDeCoursesAdapter(view.getContext(), R.layout.list_de_cours_item, listesDesCourses);

        /**
         * Définir l'ArrayAdapteur de notre liste
         */
        planning.setAdapter(listdDeCoursesAdapter);

        /**
         * En cliquant sur un élément de la liste l'acitivité LDCItems est lancée avec dedans les
         * courses à faire pour cette date
         */
        planning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListeDesCourses selectedLDC = listesDesCourses.get(position);
                if(selectedLDC != null){
                    Intent i = new Intent(view.getContext(), LDCItems.class);
                    i.putExtra("ldcid",
                            ((ListeDesCourses)parent.getItemAtPosition(position)).getId());
                    Log.d(ListesDesCourcesFragment.class.getName(), "selected ldc_id="+((ListeDesCourses)parent.getItemAtPosition(position)).getId());
                    startActivity(i);
                }
            }
        });

        return view;
    }
}