package com.planbuyandeat.Repertoire.Ingredients;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.AbsListView;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ListView;

import com.planbuyandeat.R;
import com.planbuyandeat.Repertoire.Repertoire;
import com.planbuyandeat.SQLite.DAOs.IngredientsSQLiteDAO;
import com.planbuyandeat.SQLite.DAOs.PlatsSQLiteDAO;
import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.Plat;

import java.util.ArrayList;
import java.util.List;
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

    /**
     * Gestionnaire des plats
     */
    private PlatsSQLiteDAO platdao;

    /**
     * Gesionnaire des ingredients
     */
    private IngredientsSQLiteDAO ingdao;

    /**
     * Button de retoure vers l'activité applante
     */
    private ImageButton back;

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
        back = findViewById(R.id.btn_back_from_ingredients);
        /* TODO Ajout de la suppression d'un element avec un swipe à gauche*/

        /**
         * Récupération de l'ID du plat sélectionné dans la liste des plats
         */
        Bundle extra = getIntent().getExtras();
        long id = extra.getLong("id");

        platdao = new PlatsSQLiteDAO(this);
        ingdao = new IngredientsSQLiteDAO(this);

        /**
         * Réccupere le plat à partir de la base de donées
         */
        platdao.open();
        Plat plat = platdao.get(id);
        List<String> ings = new ArrayList<>();
        platdao.close();
        /**
         * Remplir la liste des ingredients utilisée dans l'ArrayAdapter
         */
        for(Ingredient ing : plat.getIngredients())
            ings.add(ing.getNom());

        /**
         * Définiont d'un ArrayAdapter étendu pour pouvoire gérer les objet Ingrédient
         */
        IngredientAdapter ingredientAdapter =
                new IngredientAdapter(this, R.layout.ingredient_item, ings);
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
                    /**
                     * Creation d'un nouvelle ingredient dans la base de données
                     */
                    Ingredient i = new Ingredient();
                    i.setNom(res);
                    i.setPlatId(plat.getId());
                    ingdao.open();
                    Ingredient createdIng = ingdao.create(i);
                    if(createdIng != null){
                        plat.addIngredient(createdIng);
                        ings.add(createdIng.getNom());
                        ingredientAdapter.notifyDataSetChanged();
                        nomIng.setText("");
                    }
                    ingdao.close();
                }
            }
        });

        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
    }
}