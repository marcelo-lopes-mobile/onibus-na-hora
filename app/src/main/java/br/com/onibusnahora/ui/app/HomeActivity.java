package br.com.onibusnahora.ui.app;

import android.content.Intent;
import android.os.Bundle;
import android.view.Menu;
import android.view.MenuItem;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.DialogFragment;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.google.firebase.auth.FirebaseAuth;

import br.com.onibusnahora.R;
import br.com.onibusnahora.ui.app.animation.FabAnimation;
import br.com.onibusnahora.ui.app.dialogs.DialogOnibus;
import br.com.onibusnahora.ui.app.dialogs.DialogPonto;

public class HomeActivity extends AppCompatActivity {

    // View component
    FloatingActionButton fbCadastros, fbCadastroDeOnibus, fbCadastroDeRota, fbCadastroDePonto;

    // Animation boolean
    boolean isRotate = false;

    // Auth component
    private FirebaseAuth mFirebaseAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_home);
        configuraFab();
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
        }
        return super.onOptionsItemSelected(item);
    }
}
