package br.com.onibusnahora.ui.app;

import android.app.AlarmManager;
import android.app.PendingIntent;
import android.app.TimePickerDialog;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.ContextMenu;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TimePicker;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.Map;

import br.com.onibusnahora.R;
import br.com.onibusnahora.ui.app.animation.FabAnimation;
import br.com.onibusnahora.ui.app.dialogs.DialogOnibus;
import br.com.onibusnahora.ui.app.dialogs.DialogPonto;
import br.com.onibusnahora.ui.app.dialogs.TimePickerFragment;

public class HomeActivity extends AppCompatActivity implements TimePickerDialog.OnTimeSetListener {

    // View component
    FloatingActionButton fbCadastros, fbCadastroDeOnibus, fbCadastroDeRota, fbCadastroDePonto;
    ListView listView;

    // Animation boolean
    boolean isRotate = false;

    // Data component
    Map<String, Object> data;

    // Firebase component
    private FirebaseAuth mFirebaseAuth = FirebaseAuth.getInstance();
    private FirebaseFirestore db = FirebaseFirestore.getInstance();
    private ArrayAdapter<String> adapter;
    public static String codigoDaRota;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configuraFab();
        //pegaRotasDoBanco();

        db.collection("rotas").addSnapshotListener((queryDocumentSnapshots, e) -> {
            if (queryDocumentSnapshots != null){
                List<DocumentSnapshot> documents = queryDocumentSnapshots.getDocuments();
                List<String> listaDeRotas = new ArrayList<>();
                for (int i = 0; i < documents.size(); i++){
                    DocumentSnapshot documentSnapshot = documents.get(i);
                    data = documentSnapshot.getData();
                    String codigo = (String) data.get("codigo");
                    String nome = (String) data.get("nome");
                    String rota = codigo + "-" + nome;

                    listaDeRotas.add(rota);
                }
                configuraListaDeRotas(listaDeRotas);
            }
        });
    }



    private void configuraListaDeRotas(List<String> listaDeRotas) {
        if (!listaDeRotas.isEmpty()){
            adapter = new ArrayAdapter<>(this, R.layout.item_lista, listaDeRotas);
            listView = findViewById(R.id.listViewHome);
            listView.setAdapter(adapter);
            adapter.notifyDataSetChanged();
            listView.setOnItemClickListener((parent, view, position, id) -> {
                String itemAtPosition = (String) parent.getItemAtPosition(position);
                Intent intent = new Intent(HomeActivity.this, MapsActivity.class);
                intent.putExtra("rota", itemAtPosition);
                startActivity(intent);
            });

            registerForContextMenu(listView);
        }

    }

    @Override
    public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
        menu.add("Excluir");
        menu.add("Notificação");
        super.onCreateContextMenu(menu, v, menuInfo);
    }

    @Override
    public boolean onContextItemSelected(@NonNull MenuItem item) {
        AdapterView.AdapterContextMenuInfo menuInfo = (AdapterView.AdapterContextMenuInfo) item.getMenuInfo();
        String rota = adapter.getItem(menuInfo.position);
        String[] codigo = rota.split("-");

        if (item.getTitle() == "Excluir"){
            new AlertDialog.Builder(this)
                    .setTitle("Excluindo Rota")
                    .setMessage("Tem certeza que deseja remover esta rota?")
                    .setPositiveButton("Sim", (dialog, which) -> {

                        db.collection("rotas")
                                .whereEqualTo("codigo", codigo[0])
                                .get()
                                .addOnCompleteListener(task -> {

                                    if (task.isSuccessful()){
                                        for (QueryDocumentSnapshot document:task.getResult()){

                                            Log.d("BATATA","ID: " + document.getId());
                                            DocumentReference documentReference = db.collection("rotas").document(document.getId());
                                            documentReference.delete().addOnSuccessListener(aVoid -> {
                                                Log.d("deletarRota", "Rota deletada com sucesso!");
                                                adapter.remove(adapter.getItem(menuInfo.position));
                                                adapter.notifyDataSetChanged();
                                            }).addOnFailureListener(e -> Toast.makeText(HomeActivity.this, "Erro ao excluir rota!", Toast.LENGTH_SHORT).show());
                                        }
                                    }

                                });



                    })
                    .setNegativeButton("Cancelar", (dialog, which) -> dialog.cancel())
                    .show();
        } else if (item.getTitle() == "Notificação") {
            codigoDaRota = codigo[0];
            DialogFragment dialogFragment = new TimePickerFragment();
            dialogFragment.show(getSupportFragmentManager(), "setarHorarioNotificacao");
        }

        return true;
    }

    private void configuraFab() {
        getFabs();
        inicializaFabs();
        fabAnimation();
        configuraListenersFabsCadastro();
    }

    private void configuraListenersFabsCadastro() {
        fbCadastroDePonto.setOnClickListener(v -> {
            DialogFragment dialogPonto = new DialogPonto();
            dialogPonto.show(getSupportFragmentManager(), "CadastroDePontoFragment");
        });

        fbCadastroDeOnibus.setOnClickListener(v -> {
            DialogFragment dialogOnibus = new DialogOnibus();
            dialogOnibus.show(getSupportFragmentManager(), "CadastroDePontoFragment");
        });

        fbCadastroDeRota.setOnClickListener(v -> {
            startActivity(new Intent(HomeActivity.this, CadastroRotaActivity.class));
        });
    }

    private void fabAnimation() {
        fbCadastros.setOnClickListener (v -> {
            isRotate = FabAnimation.rotateFab (v,! isRotate);
            if (isRotate) {
                FabAnimation.showIn(fbCadastroDeOnibus);
                FabAnimation.showIn(fbCadastroDeRota);
                FabAnimation.showIn(fbCadastroDePonto);
            } else {
                FabAnimation.showOut(fbCadastroDeOnibus);
                FabAnimation.showOut(fbCadastroDeRota);
                FabAnimation.showOut(fbCadastroDePonto);
            }
        });
    }

    private void inicializaFabs() {
        FabAnimation.init(fbCadastroDeOnibus);
        FabAnimation.init(fbCadastroDeRota);
        FabAnimation.init(fbCadastroDePonto);
    }

    private void getFabs() {
        //Fab principal
        fbCadastros = findViewById(R.id.home_fabAdd);

        fbCadastroDeOnibus = findViewById(R.id.home_fabOnibus);
        fbCadastroDeRota = findViewById(R.id.home_fabRota);
        fbCadastroDePonto = findViewById(R.id.home_fabPonto);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_option_home, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == R.id.logout){
            mFirebaseAuth.signOut();
            finish();
        } else if(item.getItemId() == R.id.meus_pontos){
            startActivity(new Intent(HomeActivity.this, MeusPontosActivity.class));
        } else if(item.getItemId() == R.id.meus_onibus){
            startActivity(new Intent(HomeActivity.this, MeusOnibusActivity.class));
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {

        Calendar calendar = Calendar.getInstance();
        calendar.setTimeInMillis(System.currentTimeMillis());
        calendar.set(Calendar.HOUR_OF_DAY, hourOfDay);
        calendar.set(Calendar.MINUTE, minute);

        AlarmManager alarmManager = (AlarmManager) getSystemService(Context.ALARM_SERVICE);
        Intent intent = new Intent(this, AlertReceiver.class);
        PendingIntent pendingIntent = PendingIntent.getBroadcast(this, 1, intent, 0);
        alarmManager.setExact(AlarmManager.RTC_WAKEUP, calendar.getTimeInMillis(), pendingIntent);
    }
}
