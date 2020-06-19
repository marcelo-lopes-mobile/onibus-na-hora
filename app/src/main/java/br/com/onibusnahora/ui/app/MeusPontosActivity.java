package br.com.onibusnahora.ui.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.onibusnahora.R;

public class MeusPontosActivity extends AppCompatActivity {

    ListView listaDePontosCriados;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_pontos);

        listaDePontosCriados = findViewById(R.id.listaDePontosCriados);
        dadosListaDePontos();


    }

    private void dadosListaDePontos() {
        db.collection("pontos").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> valueList = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> data = document.getData();
                    String geocode = (String) data.get("geocode");
                    valueList.add(geocode);
                }
                configuraListaDePontos(valueList);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void configuraListaDePontos(List<String> listaGeocode) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaGeocode);
        listaDePontosCriados.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerForContextMenu(listaDePontosCriados);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Excluir");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String ponto = adapter.getItem(menuInfo.position);

        if (item.getTitle() == "Excluir"){
            new AlertDialog.Builder(this)
                    .setTitle("Excluindo Rota")
                    .setMessage("Tem certeza que deseja remover esta rota?")
                    .setPositiveButton("Sim", (dialog, which) -> {

                        db.collection("pontos")
                                .whereEqualTo("geocode", ponto)
                                .get()
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document:task.getResult()){

                                            Log.d("BATATA","ID: " + document.getId());
                                            DocumentReference documentReference = db.collection("pontos").document(document.getId());
                                            documentReference.delete().addOnSuccessListener(aVoid -> {
                                                Log.d("deletarRota", "Rota deletada com sucesso!");
                                                adapter.remove(adapter.getItem(menuInfo.position));
                                                adapter.notifyDataSetChanged();
                                            }).addOnFailureListener(e -> Toast.makeText(MeusPontosActivity.this, "Erro ao excluir rota!", Toast.LENGTH_SHORT).show());
                                        }
                                    } else {
                                        Log.d("deletarRota", "Deu pau aqui");
                                    }

                                });



                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel())
                    .show();
        }

        return super.onContextItemSelected(item);
    }
}
