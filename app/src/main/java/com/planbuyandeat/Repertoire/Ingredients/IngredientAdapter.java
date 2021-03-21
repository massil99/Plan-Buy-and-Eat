package com.planbuyandeat.Repertoire.Ingredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;

import java.util.List;

/**
 * ArrayAdapteur modifié pour pouvoir gérer des ingrédients dans la listeView auquel il sera
 * associé
 */
public class IngredientAdapter extends ArrayAdapter<String> {
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
    public IngredientAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
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
         * Nom de l'ingrédient
         */
        TextView nomIng = convertView.findViewById(R.id.text_nomIng);

        /**
         * Définiton du nom de l'ingrédient
         */
        nomIng.setText(getItem(position));

        return convertView;
    }
}