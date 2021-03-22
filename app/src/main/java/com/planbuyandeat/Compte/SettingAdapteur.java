package com.planbuyandeat.Compte;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;

import java.util.ArrayList;

/**
 * ArrayAdapteur modifié pour pouvoir gérer les paramétre et leurs description
 */
public class SettingAdapteur extends ArrayAdapter<String> {
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
    public SettingAdapteur(@NonNull Context context, int resource, @NonNull ArrayList<String> objects) {
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

        convertView = layoutInflater.inflate(mRessource, parent, false);

        TextView set = convertView.findViewById(R.id.text_nomSet);
        TextView desc = convertView.findViewById(R.id.text_descSet);

        String setName = getItem(position);
        /**
         * Definir le nom du paramétre à partir de la liste des nom de paramétres
         */
        set.setText(setName);

        /**
         * Definir la descritpion du paramétre en fonction de son nom
         */
        if(setName.equals(convertView.getResources().getString(R.string.changeInfo)))
            desc.setText(R.string.changeInfoDesc);
        else if(setName.equals(convertView.getResources().getString(R.string.changePass)))
            desc.setText(R.string.changePassDesc);
        else if(setName.equals(convertView.getResources().getString(R.string.logout)))
            desc.setText(R.string.logoutDesc);

        return convertView;
    }
}
