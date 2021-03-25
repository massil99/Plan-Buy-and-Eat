package com.planbuyandeat.ListesDesCourses;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.planbuyandeat.ListesDesCourses.LDCView.LDCItems;
import com.planbuyandeat.SQLite.Models.ListeDeCourses;
import com.planbuyandeat.R;

import java.util.ArrayList;
import java.util.Date;
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

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_listes_des_cources, container, false);

        planning = view.findViewById(R.id.list_plannig);

        /* TODO Recuperation des donnée à partir de la base de données */

        //test
        List<ListeDeCourses> listesDeCourses = new ArrayList<>();
        ListeDeCourses l =  new ListeDeCourses();
        ListeDeCourses l2 =  new ListeDeCourses();
        l.addItem("fromage");
        l2.addItem("fromage");
        l.addItem("tomate");
        listesDeCourses.add(l);
        listesDeCourses.add(l2);

        /**
         * ArrayAdapteur pour le liste des jours ou l'utilisateur à prévu de faire ses
         * courses. ListeDeCoursesAdapter hérite de ArrayAdapteur, pour faire en sorte d'afficher
         * un objet ListeDeCourses par ligne
         */
        ListeDeCoursesAdapter listdDeCoursesAdapter =
                new ListeDeCoursesAdapter(view.getContext(), R.layout.list_de_cours_item, listesDeCourses);

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
                ListeDeCourses selectedLDC = listesDeCourses.get(position);
                if(selectedLDC != null){
                    Intent i = new Intent(view.getContext(), LDCItems.class);
                    startActivity(i);
                }
            }
        });

        return view;
    }
}