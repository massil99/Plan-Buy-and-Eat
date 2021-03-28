package com.planbuyandeat.ListesDesCourses.LDCView;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;
import com.planbuyandeat.SQLite.DAOs.LDCSQLiteDAO;
import com.planbuyandeat.SQLite.Models.LDCItem;

import java.util.List;

public class LDCItemsAdapter extends ArrayAdapter<LDCItem> {
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
    public LDCItemsAdapter(@NonNull Context context, int resource, @NonNull List<LDCItem> objects) {
        super(context, resource, objects);
        mContext = context;
        mRessource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        /**
         * Récuperation de la ressource d'un ligne de la liste
         */
        convertView = layoutInflater.inflate(mRessource, parent, false);

        /**
         * Affichage du nom de l'élément de la liste
         */
        TextView nomItem = convertView.findViewById(R.id.text_nomItem);
        nomItem.setText(getItem(position).getNom());

        /**
         * Affichage de l'état de lélément (coché/non coché)
         */
        CheckBox c = convertView.findViewById(R.id.checkbox_crossItem);
        c.setChecked(getItem(position).isChecked());

        /**
         * Changement de l'état de l'élément dans la base de données en appuyant sur la checkbox
         */
        c.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener(){
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if (isChecked)
                    getItem(position).check();
                else
                    getItem(position).uncheck();

                LDCSQLiteDAO ldcsqLiteDAO = new LDCSQLiteDAO(mContext);
                ldcsqLiteDAO.open();
                ldcsqLiteDAO.setChecked(getItem(position).getId(), getItem(position).getLdc_id(), isChecked? 1:0);
                ldcsqLiteDAO.close();
            }
        });
        return convertView;
    }
}
