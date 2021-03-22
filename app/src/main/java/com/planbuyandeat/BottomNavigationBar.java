package com.planbuyandeat;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;

import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.planbuyandeat.Compte.Compte;
import com.planbuyandeat.ListesDesCourses.LDCView.LDCItems;
import com.planbuyandeat.Models.ListeDeCourses;
import com.planbuyandeat.Planning.Planning;
import com.planbuyandeat.Repertoire.Repertoire;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

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
         * Définir un élément selectionné par defaut
         */
        bnv.setSelectedItemId(R.id.nav_repertoire);
        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container,
                new Repertoire()).commit();


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
                    case R.id.nav_compte:
                        chosenFragment = new Compte();
                        break;
                    case R.id.nav_ldc:
                        chosenFragment = new ListesDesCourcesFragment();
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

    /**
     * A simple {@link Fragment} subclass.
     * create an instance of this fragment.
     */
    public static class ListesDesCourcesFragment extends Fragment {
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

            return view;
        }
    }

    /**
     * ArrayAdapteur modifié pour pouvoir gérer des objets ListDeCourses dans la listeView auquel il sera
     * associé
     */
    public static class ListeDeCoursesAdapter extends ArrayAdapter<ListeDeCourses> {
        /**
         * Contexte de l'adaptateur (La listView)
         */
        private Context mContext;

        /**
         * Ressource reprsentant le visuel d'une ligne de la liste
         */
        private int mRessource;

        /**
         * Initialisation des champ à la création de l'adatptateur
         * @param context
         * @param resource
         * @param objects
         */
        public ListeDeCoursesAdapter(@NonNull Context context, int resource, @NonNull List<ListeDeCourses> objects) {
            super(context, resource, objects);
            this.mContext = context;
            this.mRessource = resource;
        }

        /**
         * Définition des élement à afficher sur chaque linge de la liste
         * @param position
         * @param convertView
         * @param parent
         * @return
         */
        @NonNull
        @Override
        public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
            LayoutInflater layoutInflater = LayoutInflater.from(mContext);

            /**
             * Récuperation de la ressource d'un ligne de la liste
             */
            convertView = layoutInflater.inflate(mRessource, parent, false);

            SimpleDateFormat ft =
                    new SimpleDateFormat ("dd/MM/yyyy");

            /**
             * Date prévu à laquel l'utilisateur va faire ses  courses
             */
            TextView date = convertView.findViewById(R.id.text_dateListDeCours);
            date.setText(ft.format(getItem(position).getDate()));

            /**
             * Aperçu de la liste des courses
             */
            TextView items = convertView.findViewById(R.id.text_listItemsOverview);
            List<String> ings = getItem(position).getItems();

            /**
             * Replissage de l'aperçu de la liste des courses
             */
            StringBuilder str = new StringBuilder();
            for(int i = 0; i < ings.size() && i < 5; i++)
                str.append(ings.get(i)).append(' ');
            str.append("...");
            items.setText(str.toString());

            return convertView;
        }
    }
}