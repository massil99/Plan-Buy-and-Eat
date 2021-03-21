package com.planbuyandeat.Planning;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;

import java.text.SimpleDateFormat;
import java.util.Iterator;
import java.util.List;
import java.util.Set;

public class ListeDeCoursesAdapter extends ArrayAdapter<ListeDeCourses> {

    private Context mContext;
    private int mRessource;

    public ListeDeCoursesAdapter(@NonNull Context context, int resource, @NonNull List<ListeDeCourses> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRessource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mRessource, parent, false);

        SimpleDateFormat ft =
                new SimpleDateFormat ("dd/MM/yyyy");

        TextView date = convertView.findViewById(R.id.text_dateListDeCours);
        date.setText(ft.format(getItem(position).getDate()));

        TextView items = convertView.findViewById(R.id.text_listItemsOverview);
        List<String> ings = getItem(position).getItems();

        StringBuilder str = new StringBuilder();
        for(int i = 0; i < ings.size() && i < 5; i++)
            str.append(ings.get(i)).append(' ');
        str.append("...");
        items.setText(str.toString());

        return convertView;
    }
}
