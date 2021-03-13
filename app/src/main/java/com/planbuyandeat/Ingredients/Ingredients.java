package com.planbuyandeat.Ingredients;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.planbuyandeat.Ingredients.IngredientAdapter;
import com.planbuyandeat.R;
import com.planbuyandeat.Repertoire.Plat;

public class Ingredients extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        ListView ingredients = findViewById(R.id.list_ingredients);

        /*Ajout de la suppression d'un element avec un swipe à gauche*/

        Bundle extra = getIntent().getExtras();
        int id = extra.getInt("id");
        /* Recuperation des donnée à partir de la base de données */
        Plat plat = new Plat("pizza");

        //Test
        plat.addIngredient("Tomate");
        plat.addIngredient("Fromage");

        IngredientAdapter ingredientAdapter =
                new IngredientAdapter(this, R.layout.ingredient_item, plat.getIngredients());
        ingredients.setAdapter(ingredientAdapter);

        EditText nomIng = findViewById(R.id.editview_nomIngredient);
        Button ajoutIng = findViewById(R.id.btn_ajoutIngredient);

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