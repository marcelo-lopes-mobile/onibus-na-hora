package br.com.onibusnahora.ui.app;

import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.FragmentActivity;

import com.google.android.gms.maps.CameraUpdate;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.Polygon;
import com.google.android.gms.maps.model.PolygonOptions;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import java.io.IOException;
import java.security.Permissions;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import br.com.onibusnahora.R;

public class MapsActivity extends FragmentActivity implements OnMapReadyCallback {

    private GoogleMap mMap;
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private String rota;
    private String[] splitRota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);
        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
        mapFragment.getMapAsync(this);
        rota = (String) getIntent().getSerializableExtra("rota");
        splitRota = rota.split("-");
    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setZoomControlsEnabled(true);


        PolylineOptions options = new PolylineOptions();

        db.collection("rotas")
                .whereEqualTo("codigo", splitRota[0])
                .get()
                .addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        for (QueryDocumentSnapshot document : task.getResult()) {
                            ArrayList<String> pontos = (ArrayList<String>) document.getData().get("pontos");
                            for (int i = 0; i < pontos.size(); i++) {
                                LatLng latLng = pegaCoordenadasDoEndereco(pontos.get(i));
                                assert latLng != null;
                                options.add(latLng);
                                mMap.addMarker(new MarkerOptions().position(latLng).title("Ponto " + i));
                                mMap.moveCamera(CameraUpdateFactory.newLatLng(latLng));
                            }
                        }
                        options.color(Color.GRAY);
                        mMap.addPolyline(options);

                    } else {
                        Log.d("MapsErro", "Erro buscando os documentos: ", task.getException());
                    }
                });


        mMap.setMinZoomPreference(15);
    }


    private LatLng pegaCoordenadasDoEndereco(String endereco) {
        try {
            Geocoder geocoder = new Geocoder(this);
            List<Address> resultados = geocoder.getFromLocationName(endereco, 1);
            if (!resultados.isEmpty()) {
                return new LatLng(resultados.get(0).getLatitude(), resultados.get(0).getLongitude());
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
}