package com.planbuyandeat.Compte;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.planbuyandeat.Identification.Login;
import com.planbuyandeat.Models.Utilisateur;
import com.planbuyandeat.R;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * create an instance of this fragment.
 */
public class Compte extends Fragment {
    /**
     * TextView affichant le nom et prénom de l'utilisateur
     */
    private TextView nom;
    /**
     * TextView affichant le username
     */
    private TextView username;

    /**
     * Liste des paramétres du compte utilisateur
     */
    private ListView accSettings;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_compte, container, false);

        /**
         * Récuperation des vues du fragment
         */
        nom = view.findViewById(R.id.text_nomUtilisateur);
        username = view.findViewById(R.id.text_usernameUtilisateur);
        accSettings = view.findViewById(R.id.list_acccSettings);

        /* TODO: Récupere l'utilisateur à partir de la session puis la base de données */

        //test
        Utilisateur user = new Utilisateur("moungad", "massil", "lissam99", "hello");

        /**
         * Remplissage des texteviews avec les information de l'utilisateur
         */
        String p = user.getPrenom().substring(0, 1).toUpperCase()
                + user.getPrenom().substring(1).toLowerCase();
        String n = user.getNom().toUpperCase();
        nom.setText(p + " " + n);
        username.setText(user.getUsername());

        /**
         * Remplissage de la liste des paramétre possible à changer
         */
        ArrayList<String> settings = new ArrayList<>();
        settings.add(getResources().getString(R.string.changeInfo));
        settings.add(getResources().getString(R.string.changePass));
        settings.add(getResources().getString(R.string.logout));

        /**
         * Creation d'un ArrayAdapteur adaté pour afficher les paramétre et leurs
         * description
         */
        SettingAdapteur settingAdapteur =
        new SettingAdapteur(getContext(), R.layout.setting_item, settings);

        /**
         * Definition du ArrayAdatper de la liste des paramétres
         */
        accSettings.setAdapter(settingAdapteur);

        /**
         * Definition des action à faire pour chaque paramétre
         */
        accSettings.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String p = (String) parent.getItemAtPosition(position);
                // Passer à l'activité ChangeInfo
                if(p.equals(view.getResources().getString(R.string.changeInfo)))
                    changeInfo(view ,user);
                // Passer à l'activité ChangePass
                else if(p.equals(view.getResources().getString(R.string.changePass)))
                    changePass(view ,user);
                // Déconnexion et retour vers la page de connexion
                else if(p.equals(view.getResources().getString(R.string.logout)))
                    logout();

                Toast.makeText(getContext(), p, Toast.LENGTH_SHORT).show();
            }
        });
        return view;
    }
        /**
         * Changement vers l'acitivité ChangeInfo en passe l'identfiant de l'utilisateur
         * à celle-ci
         */
        public void changeInfo(View view, Utilisateur user) {
            Intent i = new Intent(view.getContext(), ChangeInfo.class);
            i.putExtra("id", user.getId());
            startActivity(i);
        }

        /**
        * Changement vers l'acitivité ChangePass en passe l'identfiant de l'utilisateur
        * à celle-ci
        */
        public void changePass(View view, Utilisateur user) {
            Intent i = new Intent(view.getContext(), ChangePass.class);
            i.putExtra("id", user.getId());
            startActivity(i);
        }

        /**
         * Déconnextion de l'utilisateur et retour à la page de connexion
         */
        public void logout() {
            /**TODO: deco le user */
            Intent i = new Intent(getContext(), Login.class);
            getActivity().finish();
            startActivity(i);
        }

}