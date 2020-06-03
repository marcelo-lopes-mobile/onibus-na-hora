package br.com.onibusnahora.ui.app.dialogs;

import android.app.AlertDialog;
import android.app.Dialog;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.onibusnahora.R;

public class DialogOnibus extends DialogFragment {

    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_cadastro_onibus, null))
                .setPositiveButton("Cadastrar", (dialog, id) -> {
                    EditText etCodigo, etPlacaLetras, etPlacaNumeros, etMotorista;
                    etCodigo = getDialog().findViewById(R.id.cadastro_onibus_etCodigo);
                    etMotorista = getDialog().findViewById(R.id.cadastro_onibus_etMotorista);
                    etPlacaLetras = getDialog().findViewById(R.id.cadastro_onibus_etPlacaLetras);
                    etPlacaNumeros = getDialog().findViewById(R.id.cadastro_onibus_etPlacaNumeros);
                    String codigo, motorista, placaLetras, placaNumeros, placa;
                    codigo = etCodigo.getText().toString();
                    motorista = etMotorista.getText().toString();
                    placaLetras = etPlacaLetras.getText().toString();
                    placaNumeros = etPlacaNumeros.getText().toString();
                    placa = placaLetras + "-" + placaNumeros;

                    if (codigo.isEmpty() | motorista.isEmpty() | placaLetras.isEmpty() | placaNumeros.isEmpty() | placaLetras.length() < 4 | placaNumeros.length() < 4){
                        Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    } else {
                        FirebaseFirestore db = FirebaseFirestore.getInstance();


                        Map<String, Object> onibus = new HashMap<>();
                        onibus.put("codigo",codigo);
                        onibus.put("motorista", motorista);
                        onibus.put("placa", placa);

                        db.collection("onibus")
                                .add(onibus)
                                .addOnSuccessListener(documentReference -> Log.d("sucessoCadOnibus", "DocumentSnapshot adicionado com ID: " + documentReference.getId()))
                                .addOnFailureListener(e -> Log.d("erroNoCadastroDeOnibus", "erro ao adicionar documento", e));
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> Objects.requireNonNull(DialogOnibus.this.getDialog()).cancel());
        return builder.create();
    }
}
