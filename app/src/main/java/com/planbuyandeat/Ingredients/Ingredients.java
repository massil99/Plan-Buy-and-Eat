package com.planbuyandeat.Ingredients;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ListView;

import com.planbuyandeat.Ingredients.IngredientAdapter;
import com.planbuyandeat.R;
import com.planbuyandeat.Repertoire.Plat;

public class Ingredients extends AppCompatActivity {
    private ListView ingredients;
    private EditText nomIng;
    private Button ajoutIng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ingredients = findViewById(R.id.list_ingredients);

        /* TODO Ajout de la suppression d'un element avec un swipe à gauche*/

        Bundle extra = getIntent().getExtras();
        int id = extra.getInt("id");
        /* TODO Recuperation des donnée à partir de la base de données */
        Plat plat = new Plat("pizza");

        //Test
        plat.addIngredient("Tomate");
        plat.addIngredient("Fromage");

        IngredientAdapter ingredientAdapter =
                new IngredientAdapter(this, R.layout.ingredient_item, plat.getIngredients());
        ingredients.setAdapter(ingredientAdapter);

        LinearLayout ajoutIngL = findViewById(R.id.saisieIngredient);

        nomIng = findViewById(R.id.editview_nomIngredient);
        ajoutIng = findViewById(R.id.btn_ajoutIngredient);


        ajoutIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /*Mettre à jout la base de données*/
                plat.addIngredient(nomIng.getText().toString());
                ingredientAdapter.notifyDataSetChanged();
                nomIng.setText("");
            }
        });
    }
}