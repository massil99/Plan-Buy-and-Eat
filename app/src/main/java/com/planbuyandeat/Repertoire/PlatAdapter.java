package com.planbuyandeat.Repertoire;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;

import java.util.ArrayList;

public class PlatAdapter extends ArrayAdapter<Plat> {
    private Context mContext;
    private int mRessource;

    public PlatAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Plat> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRessource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mRessource, parent, false);

        TextView num = convertView.findViewById(R.id.text_numero);
        EditText nomPlat = convertView.findViewById(R.id.editview_nomPlat);
        Button btn = convertView.findViewById(R.id.btn_ingredients);

        num.setText(String.valueOf(position));
        btn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                /* Passer à l'activiter Liste des igrédients */

            }
        });
        nomPlat.setText(getItem(position).getNom());
        return convertView;
    }
}

