package com.planbuyandeat.Ingredients;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.planbuyandeat.R;

import java.util.List;

public class IngredientAdapter extends ArrayAdapter<String> {
    private Context mContext;
    private int mRessource;

    public IngredientAdapter(@NonNull Context context, int resource, @NonNull List<String> objects) {
        super(context, resource, objects);
        this.mContext = context;
        this.mRessource = resource;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        LayoutInflater layoutInflater = LayoutInflater.from(mContext);

        convertView = layoutInflater.inflate(mRessource, parent, false);

        TextView num = convertView.findViewById(R.id.text_numeroIng);
        num.setText(String.valueOf(position));

        TextView nomIng = convertView.findViewById(R.id.text_nomIng);
        nomIng.setText(getItem(position));

        return convertView;
    }
}