package com.planbuyandeat.ListesDesCourses;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.SQLite.Models.Ingredient;
import com.planbuyandeat.SQLite.Models.LDCItem;
import com.planbuyandeat.SQLite.Models.ListeDesCourses;
import com.planbuyandeat.R;

import java.text.SimpleDateFormat;
import java.util.List;

/**
 * ArrayAdapteur modifié pour pouvoir gérer des objets ListDeCourses dans la listeView auquel il sera
 * associé
 */
public class ListeDeCoursesAdapter extends ArrayAdapter<ListeDesCourses> {
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
    public ListeDeCoursesAdapter(@NonNull Context context, int resource, @NonNull List<ListeDesCourses> objects) {
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
         * Date prévu à laquel l'utilisateur va faire ses  courses
         */
        TextView date = convertView.findViewById(R.id.text_dateListDeCours);
        date.setText(getItem(position).getDate().toString());

        /**
         * Aperçu de la liste des courses
         */
        TextView items = convertView.findViewById(R.id.text_listItemsOverview);
        List<LDCItem> item = getItem(position).getItems();

        /**
         * Replissage de l'aperçu de la liste des courses
         */
        StringBuilder str = new StringBuilder();
        for(int i = 0; i < item.size() && i < 5; i++)
            str.append(item.get(i).getNom()).append(' ');
        str.append("...");
        items.setText(str.toString());

        return convertView;
    }
}
