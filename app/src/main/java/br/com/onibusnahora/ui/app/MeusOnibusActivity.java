package br.com.onibusnahora.ui.app;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;

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

public class MeusOnibusActivity extends AppCompatActivity {

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    ListView listaDeOnibusCriados;

    private ArrayAdapter<String> adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_meus_onibus);

        listaDeOnibusCriados = findViewById(R.id.listaDeOnibusCriados);
        dadosListaDePontos();
    }

    private void dadosListaDePontos() {
        db.collection("onibus").get().addOnCompleteListener(task -> {
            if (task.isSuccessful()) {
                List<String> valueList = new ArrayList<>();
                for (QueryDocumentSnapshot document : Objects.requireNonNull(task.getResult())) {
                    Map<String, Object> data = document.getData();
                    String placa = (String) data.get("placa");
                    valueList.add(placa);
                }
                configuraListaDePontos(valueList);
            } else {
                Log.d("dadosOnibus", "Error getting documents: ", task.getException());
            }
        });
    }

    private void configuraListaDePontos(List<String> listaPlacas) {
        adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, listaPlacas);
        listaDeOnibusCriados.setAdapter(adapter);
        adapter.notifyDataSetChanged();
        registerForContextMenu(listaDeOnibusCriados);
    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        super.onCreateContextMenu(menu, v, menuInfo);
        menu.add("Excluir");
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String placa = adapter.getItem(menuInfo.position);

        if (item.getTitle() == "Excluir"){
            new AlertDialog.Builder(this)
                    .setTitle("Excluindo Ônibus")
                    .setMessage("Tem certeza que deseja remover este ônibus?")
                    .setPositiveButton("Sim", (dialog, which) -> {

                        db.collection("onibus")
                                .whereEqualTo("placa", placa)
                                .get()
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document:task.getResult()){

                                            Log.d("DeletarOnibus","ID: " + document.getId());
                                            DocumentReference documentReference = db.collection("onibus").document(document.getId());
                                            documentReference.delete().addOnSuccessListener(aVoid -> {
                                                Log.d("deletarOnibus", "Onibus deletado com sucesso!");
                                                adapter.remove(adapter.getItem(menuInfo.position));
                                                adapter.notifyDataSetChanged();
                                            }).addOnFailureListener(e -> Toast.makeText(MeusOnibusActivity.this, "Erro ao excluir onibus!", Toast.LENGTH_SHORT).show());
                                        }
                                    } else {
                                        Log.d("deletarOnibus", "Erro ao deletar onibus");
                                    }

                                });



                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel())
                    .show();
        }

        return super.onContextItemSelected(item);
    }
}
