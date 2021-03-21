package com.planbuyandeat.Planning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import com.planbuyandeat.Planning.LDCView.LDCItems;
import com.planbuyandeat.Planning.Settings.Settings;
import com.planbuyandeat.R;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * Acitivité principale de l'applicatio. Elle permet d'afficher le planning avec les plats
 * à cuisiner pour chaque jour, mais aussi la liste des date auxquelle les course sont prévues,
 * un button pour passer au paramétre de l'appication et un button de génération aléatoire
 * de planning
 */
public class Planning extends Fragment {
    /**
     * Calendrier affichant le planning avec pour chaque jours la liste des palts
     */
    private CustomCalendar customCalendar;

    /**
     * Listeview pour la liste des dates des courses à faire
     */
    private ListView planning;
    
    /**
     * Button permetant de passer à l'acitivité Settings pour gérer les paramétre de l'application
     */
    private Button settings;

    /**
     * Dictionnaire des propiétées du calendrier
     */
    private HashMap<Object, Property> descHashMap;

    /**
     * Dictionnaire des asscociation entre les jours du moi courrant avec les propriétées
     */
    private Map<Integer, Object> dateHashmap;

    /**
     * Object permettant de gérer les dates
     */
    private Calendar calendar;

    /**
     * Chargment du layout de l'acitivté
     * @param inflater
     * @param container
     * @param savedInstanceState
     * @return
     */
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_planning, container, false);
        super.onViewCreated(view, savedInstanceState);
        /**
         * Récuperation des vue du layout du fragemnt
         */
        customCalendar = view.findViewById(R.id.calendar_planning);
        planning = view.findViewById(R.id.list_plannig);
        settings = view.findViewById(R.id.btn_plannigSettings);

        /**
         * Création du dictionnaire des dropritées du calendrier, le dictionnaire associe
         * à chaque propriéte un nom et un propriéte permet de définir le visuel de la carte
         * du jours auquel elle est associée
         */
        descHashMap = new HashMap<>();
        descHashMap.put("default",
                createProperty(R.layout.default_property, R.id.text_defaultText));
        descHashMap.put("current",
                createProperty(R.layout.current_property, R.id.text_currentText));
        /* TODO Remplacer les proprieté ci-dessus par de vraies proprietées */

        /**
         * Definir le dictionnaire des propriétées du CalendarView
         */
        customCalendar.setMapDescToProp(descHashMap);

        /**
         * Assoctation des jours à leurs propiéte
         */
        dateHashmap = new HashMap<>();
        calendar = Calendar.getInstance();
        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");

        /**
         * Définir le dictionnaire des association entre les jours du mois
         * et les propriétées
         */
        customCalendar.setDate(calendar, dateHashmap);

        /**
         * Changement du dicitonnaire des association entre les jours et les propréitées en
         * changeant de mois [Suivant]
         */
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, new OnNavigationButtonClickedListener() {
            @Override
            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                Map<Integer, Object>[] dates = new Map[2];
                dates[0] = new HashMap<>();
                /* TODO Charger les palt pour ce mois si */
                return dates;
            }
        });


        /**
         * Changement du dicitonnaire des association entre les jours et les propréitées en
         * changeant de mois [Précédent]
         */
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, new OnNavigationButtonClickedListener() {
            @Override
            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                Map<Integer, Object>[] dates = new Map[2];
                dates[0] = new HashMap<>();
                /* TODO Charger les plat pour ce mois ci */
                return dates;
            }
        });

        /**
         * Affichae des plats du jours selectionné
         */
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String date = selectedDate.get(Calendar.DAY_OF_MONTH)+ "/" +
                        selectedDate.get(Calendar.MONTH)+ "/" +
                        selectedDate.get(Calendar.YEAR);

                /* TODO Afficher les plat on cliquant sur une date */
                if(desc != null)
                    Toast.makeText(view.getContext(), (String)desc, Toast.LENGTH_SHORT).show();
            }
        });

        /* TODO Recuperation des donnée à partir de la base de données */

        //test
        List<ListeDeCourses> listesDeCourses = new ArrayList<>();
        ListeDeCourses l =  new ListeDeCourses(new Date());
        ListeDeCourses l2 =  new ListeDeCourses(new Date());
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
                    i.putExtra("id", selectedLDC.getId()); // Passage de l'ID de la liste selctionnée
                    startActivity(i);
                }
            }
        });

        /**
         * Passage à l'acitivté Settings en cilquant sur a button settings
         */
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(view.getContext(), Settings.class);
                startActivity(i);
            }
        });

        return view;
    }

    /**
     * Fonction permetant de créer une carte représentant un jour dans le calendrier.
     * Ceci est fait en créant un propriété de calendrié
     * @param resourceId ID de la ressource de la CardView
     * @param textResouceId ID du TextView ou le numéro du jour sera écrit
     * @return la propriétée du calendrié créée
     */
    private Property createProperty(int resourceId, int textResouceId){
        Property property = new Property();
        property.layoutResource = resourceId;
        property.dateTextViewResource = textResouceId;

        return property;
    }
}