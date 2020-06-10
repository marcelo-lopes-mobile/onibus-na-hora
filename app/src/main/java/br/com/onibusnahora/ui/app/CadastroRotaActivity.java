package br.com.onibusnahora.ui.app;

import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.RequiresApi;
import androidx.appcompat.app.AppCompatActivity;

import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import br.com.onibusnahora.R;

public class CadastroRotaActivity extends AppCompatActivity {

    EditText etCodigo, etNome;
    ListView listView1, listView2;

    // Database components
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> adapter;
    private ArrayAdapter<String> adapter1;
    private List<String> pontosSelecionados = new ArrayList<>();
    private List<String> onibusSelecionados = new ArrayList<>();

    @RequiresApi(api = Build.VERSION_CODES.N)
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_cadastro_rota);

        dadosListaDePontos();
        dadosListaDeOnibus();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_cadastro_de_rota, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.cadastrar_rota){

            etCodigo = findViewById(R.id.cadastro_rota_codigo);
            etNome = findViewById(R.id.cadastro_rota_nome);
            String codigo = etCodigo.getText().toString();
            String nome = etNome.getText().toString();

            if (codigo.isEmpty() | nome.isEmpty() | pontosSelecionados.isEmpty()){
                Toast.makeText(this, "Informe todos os campos!", Toast.LENGTH_SHORT).show();
            } else {
                Map<String, Object> rota = new HashMap<>();
                rota.put("codigo", codigo);
                rota.put("nome", nome);
                rota.put("pontos", pontosSelecionados);

                db.collection("rotas")
                        .add(rota)
                        .addOnSuccessListener(documentReference -> Log.d("sucessoCadRota", "DocumentSnapshot adicionado com ID: " + documentReference.getId()))
                        .addOnFailureListener(e -> Log.w("erroNoCadRota", "erro ao adicionar documento", e));
                finish();
            }
        }
        return super.onOptionsItemSelected(item);
    }

    private void dadosListaDePontos() {
        db.collection("pontos").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        List<String> valueList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> data = document.getData();
                            String geocodePart1 = (String) data.get("geocode");
                            String geocodePart2 = (String) data.get("numero");
                            String geocode = geocodePart1 + ", " + geocodePart2;
                            valueList.add(geocode);
                        }
                        configuraListaDePontos(valueList);
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void configuraListaDePontos(List<String> listaGeocode) {
        adapter = new ArrayAdapter<>(CadastroRotaActivity.this, android.R.layout.simple_list_item_1, listaGeocode);
        listView1 = findViewById(R.id.cadastro_rota_listview1);
        listView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView1.setOnItemClickListener((parent, view, position, id) -> {
            String itemAtPosition = (String) parent.getItemAtPosition(position);
            if (pontosSelecionados.contains(itemAtPosition)){
                view.setBackgroundColor(Color.WHITE);
                pontosSelecionados.remove(itemAtPosition);
            } else {
                view.setBackgroundColor(Color.GREEN);
                pontosSelecionados.add(itemAtPosition);
            }
        });
    }

    private void dadosListaDeOnibus() {
        db.collection("onibus").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> listViewDataOnibus = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> data = document.getData();
                    String codigo = (String) data.get("codigo");
                    String placa = (String) data.get("placa");
                    String onibus = "Codigo: " + codigo + ", Placa: " + placa;
                    listViewDataOnibus.add(onibus);
                }

                configuraListaDeOnibus(listViewDataOnibus);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void configuraListaDeOnibus(List<String> listaOnibus) {
        adapter1 = new ArrayAdapter<>(CadastroRotaActivity.this, android.R.layout.simple_list_item_1, listaOnibus);
        listView2 = findViewById(R.id.cadastro_rota_listview2);
        listView2.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        listView2.setOnItemClickListener((parent, view, position, id) -> {
            String  itemAtPosition = (String) parent.getItemAtPosition(position);
            if (onibusSelecionados.contains(itemAtPosition)){
                view.setBackgroundColor(Color.WHITE);
                onibusSelecionados.remove(itemAtPosition);
            } else {
                onibusSelecionados.clear();
                Toast.makeText(CadastroRotaActivity.this, "Selecionado!", Toast.LENGTH_SHORT).show();
                onibusSelecionados.add(itemAtPosition);
            }
        });
    }


}
