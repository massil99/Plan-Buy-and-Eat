package com.planbuyandeat.Planning;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.ContextThemeWrapper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.Planning.Settings.Settings;
import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.DAOs.IngredientsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.JourSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.LDCSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatJourSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.UsersSQLiteDAO;
import com.planbuyandeat.SQLite.Models.CustomDate;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.LDCItem;
import com.planbuyandeat.SQLite.Models.ListeDesCourses;
import com.planbuyandeat.SQLite.Models.Plat;
import com.planbuyandeat.SQLite.Models.PlatJour;
import com.planbuyandeat.SQLite.Models.Utilisateur;

import org.naishadhparmar.zcustomcalendar.CustomCalendar;
import org.naishadhparmar.zcustomcalendar.OnDateSelectedListener;
import org.naishadhparmar.zcustomcalendar.OnNavigationButtonClickedListener;
import org.naishadhparmar.zcustomcalendar.Property;

import java.sql.SQLException;
import java.sql.SQLIntegrityConstraintViolationException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.Collections;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.zip.Inflater;

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
     * Button permetant de passer à l'acitivité Settings pour gérer les paramétre de l'application
     */
    private Button settings;

    /**
     * Button de generation d'un nouveau planning
     */
    private Button generateB;

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
     * Endroit ou les plat d'une date selectionnée serront affichés
     */
    private LinearLayout affichagePlats;

    /**
     * Utilisateur actuellement connecté
     */
    private Utilisateur user;

    /**
     * Gestionnaire d'utilisateur
     */
    private UsersSQLiteDAO userdao;

    /**
     * Gestionnaire des plats
     */
    private PlatsSQLiteDAO platdao;

    /**
     * Gestionnaire des associations plat jour
     */
    private PlatJourSQLiteDAO pjdao;

    /**
     * Gestionnaire des jours de planning
     */
    private JourSQLiteDAO jourdao;


    /**
     * Gestionnaire des ingredients de plat
     * */
    private IngredientsSQLiteDAO ingredientsSQLiteDAO;

    /**
     * Gesionnaire des listes des courses
     */
    private LDCSQLiteDAO ldcdao;
    /**
     * Fichier de session
     */
    private SharedPreferences userSession;


    /**
     * La limite de jour pour laquel on genre le planning
     */
    public final static int GENERATION_LIMIT = 60;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view;
        view = inflater.inflate(R.layout.activity_planning, container, false);
        super.onViewCreated(view, savedInstanceState);

        /**
         * Récuperation des information de la session
         */
        userSession = getActivity().getSharedPreferences(Login.MySESSION, Context.MODE_PRIVATE);
        long id = userSession.getLong(Login.USERID, -1);


        userdao = new UsersSQLiteDAO(getContext());
        pjdao = new PlatJourSQLiteDAO(getContext());
        platdao = new PlatsSQLiteDAO(getContext());
        jourdao = new JourSQLiteDAO(getContext());
        ingredientsSQLiteDAO = new IngredientsSQLiteDAO(getContext());
        ldcdao = new LDCSQLiteDAO(getContext());

        /**
         * Récuperation de l'utilisateur à partir de la base de données
         */
        userdao.open();
        user = userdao.get(id);
        userdao.close();

        /**
         * Récuperation des vue du layout du fragemnt
         */
        customCalendar = view.findViewById(R.id.calendar_planning);
        settings = view.findViewById(R.id.btn_plannigSettings);
        generateB = view.findViewById(R.id.btn_generatePlanning);
        affichagePlats = view.findViewById(R.id.layout_list_plat_jour);

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
        descHashMap.put("planning",
                createProperty(R.layout.planning_property, R.id.text_planningText));
        descHashMap.put("shopping",
                createProperty(R.layout.shopping_day_property, R.id.text_shoppinDtText));

        /**
         * Definir le dictionnaire des propriétées du CalendarView
         */
        customCalendar.setMapDescToProp(descHashMap);

        /**
         * Assoctation des jours à leurs propiéte
         */
        dateHashmap = new HashMap<>();
        calendar = Calendar.getInstance();

        /**
         * Récuperation de la liste des date du planning
         * Et definition des visuels de chaque date en fonction du planning
         */
        new Thread(new Runnable() {
            @Override
            public void run() {
                customCalendar.post(new Runnable() {
                    @Override
                    public void run() {
                        jourdao.open();
                        List<CustomDate> jours = new ArrayList<>(jourdao.getAllByMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1)));
                        jourdao.close();

                        // Jours faisant parite du planning
                        dateHashmap.putAll(setDayProperty(jours, "planning"));

                        // Jour de shopping
                        CustomDate shopd = new CustomDate();
                        shopd.setDate(user.getDateDebut());

                        CustomDate temp = new CustomDate();
                        temp.setDate(user.getDateDebut());
                        temp.addDays(GENERATION_LIMIT);

                        shopd.addDays(user.getPeriod());
                        while(shopd.getDate().before(temp.getDate())){
                            if(Integer.parseInt(shopd.getMonth()) == calendar.get(Calendar.MONTH) + 1)
                                dateHashmap.put(Integer.parseInt(shopd.getDayOfMonth()), "shopping");
                            shopd.addDays(user.getPeriod());
                        }

                        // Le jours du mois de la date actuel
                        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");

                        /**
                         * Définir le dictionnaire des association entre les jours du mois
                         * et les propriétées
                         */
                        customCalendar.setDate(calendar, dateHashmap);
                    }
                });
            }
        }).start();

        Log.d(Planning.class.getName(), "NEXT");
        /**
         * Changement du dicitonnaire des association entre les jours et les propréitées en
         * changeant de mois [Suivant]
         */
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, new OnNavigationButtonClickedListener() {
            @Override
            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                Map<Integer, Object>[] dates = new Map[2];
                jourdao.open();
                List<CustomDate> jours = new ArrayList<>(jourdao.getAllByMonth(String.valueOf(newMonth.get(Calendar.MONTH) + 1)));
                jourdao.close();

                // Jours faisant parite du planning
                dates[0] = setDayProperty(jours, "planning");

                // Jour de shopping
                CustomDate shopd = new CustomDate();
                shopd.setDate(user.getDateDebut());

                CustomDate temp = new CustomDate();
                temp.setDate(user.getDateDebut());
                temp.addDays(GENERATION_LIMIT);

                shopd.addDays(user.getPeriod());
                while(shopd.getDate().before(temp.getDate())){
                if(Integer.parseInt(shopd.getMonth()) == newMonth.get(Calendar.MONTH) + 1)
                        dates[0].put(Integer.parseInt(shopd.getDayOfMonth()), "shopping");
                    shopd.addDays(user.getPeriod());
                }

                // Le jours du mois de la date actuel
                if(calendar.get(Calendar.MONTH) == newMonth.get(Calendar.MONTH))
                    dates[0].put(calendar.get(Calendar.DAY_OF_MONTH), "current");

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
                jourdao.open();
                List<CustomDate> jours = new ArrayList<>(jourdao.getAllByMonth(String.valueOf(newMonth.get(Calendar.MONTH) + 1)));
                jourdao.close();

                // Jours faisant parite du planning
                dates[0] = setDayProperty(jours, "planning");

                // Jour de shopping
                CustomDate shopd = new CustomDate();
                shopd.setDate(user.getDateDebut());

                CustomDate temp = new CustomDate();
                temp.setDate(user.getDateDebut());
                temp.addDays(GENERATION_LIMIT);

                shopd.addDays(user.getPeriod());
                while(shopd.getDate().before(temp.getDate())){
                    if(Integer.parseInt(shopd.getMonth()) == newMonth.get(Calendar.MONTH) + 1)
                        dates[0].put(Integer.parseInt(shopd.getDayOfMonth()), "shopping");
                    shopd.addDays(user.getPeriod());
                }

                // Le jours du mois de la date actuel
                if(calendar.get(Calendar.MONTH) == newMonth.get(Calendar.MONTH))
                    dates[0].put(calendar.get(Calendar.DAY_OF_MONTH), "current");

                return dates;
            }
        });

        /**
         * Affichae des plats du jours selectionné
         */
        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String date = selectedDate.get(Calendar.DAY_OF_MONTH)+ "-" +
                        String.valueOf(selectedDate.get(Calendar.MONTH)+1)+ "-" +
                        selectedDate.get(Calendar.YEAR);
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        customCalendar.post(new Runnable() {
                            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
                            @Override
                            public void run() {
                                /**
                                 * Récuperation de tous les plat prévus pour cette date
                                 */
                                pjdao.open();
                                platdao.open();
                                List<PlatJour> platJourList = pjdao.getAllPlatOfJour(date);
                                if(platJourList != null){
                                    if(platJourList.size() != 0)
                                        affichagePlats.removeAllViews();
                                    // Affichage des plat pour la date selectionnée
                                    for(PlatJour p : platJourList){
                                        LinearLayout l = new LinearLayout(affichagePlats.getContext(), null, 0, R.style.Theme_PlanBuyAndEat_plat_card);
                                        TextView t = new TextView(l.getContext(), null, 0, R.style.Theme_PlanBuyAndEat_mediumText);
                                        Plat temp = platdao.get(p.getPlatid());
                                        if(temp != null)
                                            t.setText(temp.getNom().toUpperCase());
                                        t.setTextColor(0xffffffff);

                                        l.addView(t);
                                        affichagePlats.addView(l);
                                    }
                                }
                                platdao.close();
                                pjdao.close();
                            }
                        });
                    }
                }).start();

                if(desc != null)
                    Toast.makeText(view.getContext(), (String)desc, Toast.LENGTH_SHORT).show();
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

        /**
         * Gener un nouveau planning
         */
        generateB.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                /**
                 * Traitement de la génération du palnning dans un thread séparé
                 */
                new Thread(new Runnable() {
                    @Override
                    public void run() {
                        generateB.post(new Runnable(){
                            @Override
                            public void run() {
                                /**
                                 * Géneration du planning
                                 */
                                generate();

                                /**
                                 * Association des propriétés aux jours
                                 */
                                jourdao.open();
                                List<CustomDate> jours = new ArrayList<>(jourdao.getAllByMonth(String.valueOf(calendar.get(Calendar.MONTH) + 1)));
                                jourdao.close();

                                dateHashmap.putAll(setDayProperty(jours, "planning"));
                                dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");

                                /**
                                 * Définir le dictionnaire des association entre les jours du mois
                                 * et les propriétées
                                 */
                                customCalendar.setDate(calendar, dateHashmap);
                            }
                        });
                    }
                }).start();
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


    /**
     * Définition des propriétées des jours du calendrier pour le moi actuel
     */
    private Map<Integer, Object> setDayProperty(List<CustomDate> jours, String label){
        Map<Integer, Object> res = new HashMap<>();
        for(CustomDate jour: jours) {
            res.put(Integer.parseInt(jour.getDayOfMonth()), label);
        }
        return res;
    }

    /**
     * Genéation du planning de maniére alétoire en base de données
     */
    private void generate(){
        if(user != null){
            platdao.open();
            List<Plat> plats = platdao.getAllUserPlats(user);
            platdao.close();

            /**
             * Génerer le planning que si la liste des plat n'est pas vide
             */
            if(plats.size() != 0){
                pjdao.open();
                jourdao.open();
                /**
                 * Supprimer le planning déja existant dans la base de données
                 */
                jourdao.deleteAll();
                pjdao.deleteAll();
                userdao.open();
                /**
                 * Récuperation des information de géneration de planning associées a cet utilisateur
                 */

                user = userdao.get(user.getId());
                int nbPJ = user.getNbPlatjour();
                CustomDate pday = new CustomDate();
                pday.setDate(user.getDateDebut());
                userdao.close();
                /**
                 * Generation du planning jour par jour, les plats d'un jours sont remplis aléatoirement
                 * à partir de l'ensemble des plats associés à un utilisateur. La géneration s'arrêt un
                 * GENERATION_LIMIT est atteint
                 */
                for(int i = 0; i < GENERATION_LIMIT; i++){
                    CustomDate newDate = jourdao.create(pday);
                    // Creation d'un jour de planning
                    if(newDate != null){
                        // Remplissage aleatoire des plat
                        int j = 0;
                        while(j < nbPJ){
                            PlatJour platJour = new PlatJour();
                            platJour.setDateid(newDate.getId());

                            /**
                             * Choix alétoire d'un plat
                             */
                            int randIndx = (int) Math.floor(Math.random() * plats.size());
                            long platid = plats.get(randIndx).getId();
                            platJour.setPlatid(platid);

                            /**
                             * Ajout de l'asscoiation plat jour
                             */
                            if(pjdao.create(platJour) != null)
                                j++;
                        }
                    }

                    // Incrementation de la date de 1
                    pday.addDays(1);
                }

                /**
                 * Création des listes des courses à partire des dates de courses déjà générées
                 */

                // TODO : commenter cette parite
                CustomDate dateDebut = new CustomDate();
                dateDebut.setDate(user.getDateDebut());

                CustomDate dateFin = new CustomDate();
                dateFin.setDate(user.getDateDebut());
                dateFin.addDays(GENERATION_LIMIT);

                ingredientsSQLiteDAO.open();
                ldcdao.open();

                ldcdao.deleteAll();
                String temp = dateDebut.toString();
                dateDebut.addDays(user.getPeriod());
                while(dateDebut.getDate().before(dateFin.getDate())){
                    List<PlatJour> pjs = pjdao.getAllPlatsBetween(temp, dateDebut.toString());
                    if(pjs.size() > 0){
                        ListeDesCourses ldc = new ListeDesCourses();
                        ldc.setUserid(user.getId());
                        CustomDate tempDt = jourdao.get(dateDebut.toString());
                        if(tempDt != null) {
                            ldc.getDate().setId(tempDt.getId());
                            for (PlatJour platJour : pjs) {
                                List<Ingredient> ings = ingredientsSQLiteDAO.getAllPlatIngredients(platJour.getPlatid());
                                for (Ingredient ing : ings) {
                                    LDCItem ldcItem = new LDCItem();
                                    ldcItem.setId(ing.getId());
                                    ldcItem.setNom(ing.getNom());
                                    ldc.addItem(ldcItem);
                                }
                            }
                        }
                       ldcdao.create(ldc);
                    }
                    temp = dateDebut.toString();
                    dateDebut.addDays(user.getPeriod());
                }
                /******************************************************/

                ingredientsSQLiteDAO.close();
                ldcdao.close();
                jourdao.close();
                pjdao.close();
            }else
                Toast.makeText(getContext(), R.string.no_meals, Toast.LENGTH_LONG).show();
        }
    }
}