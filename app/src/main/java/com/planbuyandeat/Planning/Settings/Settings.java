package com.planbuyandeat.Planning.Settings;

import androidx.appcompat.app.AppCompatActivity;

import android.app.DatePickerDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.planbuyandeat.R;
import com.planbuyandeat.utils.InputFilterMinMax;

import java.util.Objects;

public class Settings extends AppCompatActivity {
    private EditText period;
    private Button changeDateDebut;
    private TextView dateDebut;
    private EditText platPJour;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_settings);

        period = findViewById(R.id.editview_period);
        changeDateDebut = findViewById(R.id.btn_modifierDateDebutPeriode);
        dateDebut = findViewById(R.id.text_dateDebutPeriode);
        platPJour = findViewById(R.id.editview_nbPlatPjour);

        period.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String res = period.getText().toString();
                    if(!Objects.equals("",res)){
                        int periodL = Integer.parseInt(res);
                        /* TODO Stocker les preferences */
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                    };
                }
            }
        });

        changeDateDebut.setOnClickListener(new View.OnClickListener(){
            @Override
            public void onClick(View v) {
                DatePickerFragment datePicker = new DatePickerFragment();
                datePicker.show(getSupportFragmentManager(), "DatePicker");
                datePicker.setOnDateSetListener(new DatePickerDialog.OnDateSetListener() {
                    @Override
                    public void onDateSet(DatePicker view, int year, int month, int dayOfMonth) {
                        /* TODO Stocker dans les preferneces */
                        String date = dayOfMonth + " /"+
                                        month + "/" +
                                        year;
                        Toast.makeText(getApplicationContext(), date, Toast.LENGTH_SHORT).show();
                        dateDebut.setText(date);
                    }
                });
            }
        });

        platPJour.setFilters(new InputFilterMinMax[]{new InputFilterMinMax(1, 5)});
        platPJour.setOnFocusChangeListener(new View.OnFocusChangeListener(){
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    String res = platPJour.getText().toString();
                    if(!Objects.equals("",res)){
                        int nbPlat = Integer.parseInt(res);
                        /* TODO Stocker les preferences */
                        Toast.makeText(getApplicationContext(), res, Toast.LENGTH_SHORT).show();
                    };
                }
            }
        });
    }
}