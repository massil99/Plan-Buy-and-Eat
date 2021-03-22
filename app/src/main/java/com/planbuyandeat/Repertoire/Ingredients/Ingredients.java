package com.planbuyandeat.SQLite.Models;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;

import com.planbuyandeat.R;
import com.planbuyandeat.Repertoire.Ingredients.IngredientAdapter;

import java.util.Objects;

/**
 * Acitivité permétant de gérer les ingrédients d'un plat (Ajouter supprimer)
 */
public class Ingredients extends AppCompatActivity {
    /**
     * ListeView des ingrédients
     */
    private ListView ingredients;

    /**
     * EditeText permetant de saisir un ingrédient à ajouter
     */
    private EditText nomIng;

    /**
     * Button permetant d'ajouter un ingrédient
     */
    private Button ajoutIng;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ingredients);

        /**
         * Récuperation des vues de layout de l'activité
         */
        ingredients = findViewById(R.id.list_ingredients);
        nomIng = findViewById(R.id.editview_nomIngredient);
        ajoutIng = findViewById(R.id.btn_ajoutIngredient);

        /* TODO Ajout de la suppression d'un element avec un swipe à gauche*/

        /**
         * Récupération de l'ID sélectionné dans la liste des plats
         */
        Bundle extra = getIntent().getExtras();
        int id = extra.getInt("id");
        /* TODO Recuperation des donnée à partir de la base de données */

        //Test
        Plat plat = new Plat();
        plat.setNom("pizza");
        plat.addIngredient("Tomate");
        plat.addIngredient("Fromage");

        /**
         * Définiont d'un ArrayAdapter étendu pour pouvoire gérer les objet Ingrédient
         */
        IngredientAdapter ingredientAdapter =
                new IngredientAdapter(this, R.layout.ingredient_item, plat.getIngredients());
        ingredients.setAdapter(ingredientAdapter);

        /**
         * Ajout d'un ingrédient en cliquant sur le button ajoutIng,
         * le nom est récupéré à partir de nomIng, l'adapteur notifie par la suite que ses données
         * en changées pour mettre à jour l'affichage de la liste. Enfin la base de données est
         * mise à jour
         */
        ajoutIng.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String res = nomIng.getText().toString();
                if(!Objects.equals(res, "")){
                    plat.addIngredient(res);
                    ingredientAdapter.notifyDataSetChanged();
                    nomIng.setText("");

                    /*TODO Mettre à jout la base de données*/
                }
            }
        });
    }
}