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
    private ArrayAdapter<Object> adapter;
    private ArrayAdapter<Object> adapter1;
    private List<Object> pontosSelecionados = new ArrayList<>();
    private List<Object> onibusSelecionados = new ArrayList<>();

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

            Map<String, Object> rota = new HashMap<>();
            rota.put("codigo", codigo);
            rota.put("numero", nome);
            rota.put("pontos", pontosSelecionados);

            db.collection("rotas")
                    .add(rota)
                    .addOnSuccessListener(documentReference -> Log.d("sucessoCadRota", "DocumentSnapshot adicionado com ID: " + documentReference.getId()))
                    .addOnFailureListener(e -> Log.w("erroNoCadRota", "erro ao adicionar documento", e));
        }
        return super.onOptionsItemSelected(item);
    }

    private void dadosListaDePontos() {
        db.collection("pontos").get().addOnCompleteListener(task -> {
                    if (task.isSuccessful()) {
                        Collection<Collection<Object>> valueList = new ArrayList<>();
                        for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                            Map<String, Object> data = document.getData();
                            valueList.add(data.values());
                        }
                        List<Object> listaGeocode = new ArrayList<>(valueList);
                        configuraListaDePontos(listaGeocode);
                    } else {
                        Log.d("TAG", "Error getting documents: ", task.getException());
                    }
                });
    }

    private void configuraListaDePontos(List<Object> listaGeocode) {
        adapter = new ArrayAdapter<>(CadastroRotaActivity.this, android.R.layout.simple_list_item_1, listaGeocode);
        listView1 = findViewById(R.id.cadastro_rota_listview1);
        listView1.setAdapter(adapter);
        adapter.notifyDataSetChanged();

        listView1.setOnItemClickListener((parent, view, position, id) -> {
            Object itemAtPosition = parent.getItemAtPosition(position);
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
                Collection<Collection<Object>> valueList2 = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> data = document.getData();
                    valueList2.add(data.values());
                }
                List<Object> listaOnibus = new ArrayList<>(valueList2);

                configuraListaDeOnibus(listaOnibus);
            } else {
                Log.d("TAG", "Error getting documents: ", task.getException());
            }
        });
    }

    private void configuraListaDeOnibus(List<Object> listaOnibus) {
        adapter1 = new ArrayAdapter<>(CadastroRotaActivity.this, android.R.layout.simple_list_item_1, listaOnibus);
        listView2 = findViewById(R.id.cadastro_rota_listview2);
        listView2.setAdapter(adapter1);
        adapter1.notifyDataSetChanged();

        listView2.setOnItemClickListener((parent, view, position, id) -> {
            Object itemAtPosition = parent.getItemAtPosition(position);
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
