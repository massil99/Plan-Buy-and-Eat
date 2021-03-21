package com.planbuyandeat.Planning;

import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
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

public class Planning extends AppCompatActivity {
    private CustomCalendar customCalendar;
    private ListView planning;
    private Button settings;


    @RequiresApi(api = Build.VERSION_CODES.O)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_planning);

        customCalendar = findViewById(R.id.calendar_planning);

        HashMap<Object, Property> descHashMap = new HashMap<>();
        descHashMap.put("default",
                createProperty(R.layout.default_property, R.id.text_defaultText));
        descHashMap.put("current",
                createProperty(R.layout.current_property, R.id.text_currentText));

        /* TODO Remplacer les proprieté ci-dessus par de vraies proprietées */

        customCalendar.setMapDescToProp(descHashMap);

        Map<Integer, Object> dateHashmap = new HashMap<>();
        Calendar calendar = Calendar.getInstance();

        dateHashmap.put(calendar.get(Calendar.DAY_OF_MONTH), "current");

        customCalendar.setDate(calendar, dateHashmap);
        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.NEXT, new OnNavigationButtonClickedListener() {
            @Override
            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                Map<Integer, Object>[] dates = new Map[2];
                dates[0] = new HashMap<>();
                /* TODO Charger les palt pour ce mois si */
                return dates;
            }
        });

        customCalendar.setOnNavigationButtonClickedListener(CustomCalendar.PREVIOUS, new OnNavigationButtonClickedListener() {
            @Override
            public Map<Integer, Object>[] onNavigationButtonClicked(int whichButton, Calendar newMonth) {
                Map<Integer, Object>[] dates = new Map[2];
                dates[0] = new HashMap<>();
                /* TODO Charger les plat pour ce mois ci */
                return dates;
            }
        });

        customCalendar.setOnDateSelectedListener(new OnDateSelectedListener() {
            @Override
            public void onDateSelected(View view, Calendar selectedDate, Object desc) {
                String date = selectedDate.get(Calendar.DAY_OF_MONTH)+ "/" +
                        selectedDate.get(Calendar.MONTH)+ "/" +
                        selectedDate.get(Calendar.YEAR);

                /* TODO Afficher les plat on cliquant sur une date */
                if(desc != null)
                    Toast.makeText(getApplicationContext(), (String)desc, Toast.LENGTH_SHORT).show();
            }
        });

        planning = findViewById(R.id.list_plannig);
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


        ListeDeCoursesAdapter listdDeCoursesAdapter =
                new ListeDeCoursesAdapter(this, R.layout.list_de_cours_item, listesDeCourses);
        planning.setAdapter(listdDeCoursesAdapter);

        planning.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                ListeDeCourses selectedLDC = listesDeCourses.get(position);
                if(selectedLDC != null){
                    Intent i = new Intent(getApplicationContext(), LDCItems.class);
                    i.putExtra("id", selectedLDC.getId());
                    startActivity(i);
                }
            }
        });


        settings = findViewById(R.id.btn_plannigSettings);
        settings.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                Intent i = new Intent(getApplicationContext(), Settings.class);
                startActivity(i);
            }
        });


    }

    private Property createProperty(int resourceId, int textResouceId){
        Property property = new Property();
        property.layoutResource = resourceId;
        property.dateTextViewResource = textResouceId;

        return property;
    }
}