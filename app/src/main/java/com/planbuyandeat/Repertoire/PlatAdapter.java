package com.planbuyandeat.Repertoire;

import android.content.Context;
import android.content.Intent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.Repertoire.Ingredients.Ingredients;
import com.planbuyandeat.R;

import java.util.ArrayList;

/**
 * ArrayAdapteur modifié pour pouvoir gérer des object plat dans la listeView auquel il sera
 * associé
 */
public class PlatAdapter extends ArrayAdapter<Plat> {
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
    public PlatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Plat> objects) {
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

        /**
         * Récuperation des vues de la ressource representant une ligne de la liste
         */

        /**
         * Numéro du plat
         */
        TextView num = convertView.findViewById(R.id.text_numero);

        /**
         * Nom du plat
         */
        EditText nomPlat = convertView.findViewById(R.id.editview_nomPlat);

        /**
         * Button permettant de redériger vers la liste des ingrédients
         */
        Button btn = convertView.findViewById(R.id.btn_ingredients);

        /**
         * Définitoin du numéro du plat par rapport à sa postion dans la liste
         */
        num.setText(String.valueOf(position));

        /**
         * Redirection vers la liste des ingrédient, en passant l'ID du plat séléctionné
         */
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                int id = 0; // TODO id dans la base de données
                Intent i = new Intent(mContext, Ingredients.class);
                i.putExtra("id", id);
                mContext.startActivity(i);
            }
        });

        /**
         * Définition du nom du plat stocké dans l'objet plat
         */
        nomPlat.setText(getItem(position).getNom());

        /**
         * Enregistrement du nouveau nom du plat apres que cette vu aie perdu le focus
         * de l'utilisateur
         */
        nomPlat.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(!hasFocus){
                    /* TODO Mettre à jour la base de données */
                }
            }
        });
        return convertView;
    }
}

