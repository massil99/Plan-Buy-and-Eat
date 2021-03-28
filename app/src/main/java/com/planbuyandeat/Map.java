package com.planbuyandeat;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Bundle;

import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.BufferedInputStream;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.Reader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;

/**
 * Fragment qui afficher la map qui sert à localiser les marchés à proximité de l'appareil
 */
public class Map extends Fragment {
    /**
     * La carte paffichée dans le fragment
     */
    MapView mMapView;
    private GoogleMap googleMap;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.fragment_map, container, false);

        /**
         * Récuperation de la vus de la carte
         */
        mMapView = (MapView) rootView.findViewById(R.id.map);
        /**
         * Création de la carte
         */
        mMapView.onCreate(savedInstanceState);
        mMapView.onResume();

        /**
         * Initialisation de la vue carte
         */
        try {
            MapsInitializer.initialize(getActivity().getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        /**
         * Définition des action à faire quand la carte est prête pour l'utilisation
         */
        mMapView.getMapAsync(new OnMapReadyCallback() {
            @RequiresApi(api = Build.VERSION_CODES.M)
            @Override
            public void onMapReady(GoogleMap mMap) {
                googleMap = mMap;

                // Teste des permission ACCESS_FINE_LOCATION et ACCESS_COARSE_LOCATION
                if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                    ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                    // Afficher le boutton qui permet de centre la carte sur l'appareil
                    googleMap.setMyLocationEnabled(true);

                    // TODO: Rechercher et afficher les marchés à prximité

                    // Affichage d'un marqueur sur la position de l'appareil
                    LatLng currentPos = getPosition();
                    googleMap.addMarker(
                            new MarkerOptions()
                                    .position(currentPos)
                                    .title("Votre position")
                                    .snippet("Marker Description"));

                    // Zoomer la camera sur la posistion de l'apareil
                    CameraPosition cameraPosition = new CameraPosition.Builder().target(currentPos).zoom(12).build();
                    googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
                }else{
                    // Demander les permissions si non attribuées
                    requestPermissions(
                            new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                    Manifest.permission.ACCESS_COARSE_LOCATION},
                            1);
                }
            }
        });

        return rootView;
    }

    /** Mapping des états du fagment à ceux de la MapView **/
    @Override
    public void onResume() {
        super.onResume();
        mMapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mMapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        mMapView.onDestroy();
    }

    @Override
    public void onLowMemory() {
        super.onLowMemory();
        mMapView.onLowMemory();
    }


    /**
     * Récuperation de la localisation de l'appareil
     * @return Latitude et Longitude de l'appareil
     */
    private LatLng getPosition() {
        // Récuperation du gestionnarie de service de localistaion de l'application
        LocationManager manager = (LocationManager) getActivity().getSystemService(Context.LOCATION_SERVICE);
        // Récuperation de la liste de fournisseur de localisation
        List<String> fournisseurs = manager.getAllProviders();

        LatLng currentPos = new LatLng(0, 0);
        // On veut utiliser le gps comme fournisseur de localisation
        if (fournisseurs.contains(LocationManager.GPS_PROVIDER)) {
            // Teste des permission ACCESS_FINE_LOCATION et ACCESS_COARSE_LOCATION
            if (ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED &&
                ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_COARSE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
                Location location = manager.getLastKnownLocation(LocationManager.GPS_PROVIDER);
                // Récuperation de la postion de l'appareil
                currentPos = new LatLng(location.getLatitude(), location.getLongitude());
            }else {
                // Demander les permissions si non attribuées
                requestPermissions(
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION,
                                Manifest.permission.ACCESS_COARSE_LOCATION},
                        2);
            }
        }
        return currentPos;
    }

    /**
     * Envoie une requête à Places api de Google afin de récuperation la localisation des marchés
     * à proximités
     * @param position la position de l'utilisateur
     * @param radius le rayon max de recherche
     * @return une liste des position des marchés à proximités
     */
    private List<LatLng> getMarkets(LatLng position, int radius){
        List<LatLng> markets = new ArrayList<>();
        
        String baseUrl =
                "https://maps.googleapis.com/maps/api/place/nearbysearch/json?";
        StringBuilder urlBuilder = new StringBuilder();
        urlBuilder.append(baseUrl)
            .append("location=").append(position.latitude).append(",").append(position.longitude)
            .append("&")
            .append("radius=").append(radius)
            .append("&")
            .append("keyword=market")
            .append("&")
            .append("key=").append(getActivity().getResources().getString(R.string.map_key));
        Log.d("MapFragment", urlBuilder.toString());
        URL url = null;
        String res = new String("");
        try {
            url = new URL(urlBuilder.toString());
            HttpURLConnection urlConnection = (HttpURLConnection) url.openConnection();
            InputStream in = null;
            if(ActivityCompat.checkSelfPermission(getContext(), Manifest.permission.INTERNET) == PackageManager.PERMISSION_GRANTED ){
                in = new BufferedInputStream(urlConnection.getInputStream());
            }else{
                requestPermissions(
                        new String[]{ Manifest.permission.INTERNET},
                        3
                );
            }
            if(in != null)
                res = readInputStream(in);
        } catch (MalformedURLException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
        
        Log.d("MapFragment", res);
        return markets;
    }

    /**
     * Récupere le contenu d'un flux dans un string
     * @param in le flux d'entré
     * @return le contenu du flux
     */
    private String readInputStream(InputStream in) throws IOException {
        StringBuilder textBuilder = new StringBuilder();

        Reader reader = new BufferedReader(new InputStreamReader
                (in, Charset.forName(StandardCharsets.UTF_8.name())));
        int c = 0;
        while ((c = reader.read()) != -1) {
            textBuilder.append((char) c);
        }
        return textBuilder.toString();
    }
}