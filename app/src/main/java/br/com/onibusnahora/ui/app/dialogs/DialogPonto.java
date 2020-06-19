package br.com.onibusnahora.ui.app.dialogs;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Dialog;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.widget.EditText;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.DialogFragment;

import com.google.firebase.firestore.FirebaseFirestore;

import java.io.IOException;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

import br.com.onibusnahora.R;
import retrofit2.Call;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DialogPonto extends DialogFragment {

    EditText etCep, etNumero;
    FirebaseFirestore db = FirebaseFirestore.getInstance();
    String geocode;

    @SuppressLint("StaticFieldLeak")
    @NonNull
    @Override
    public Dialog onCreateDialog(@Nullable Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();
        builder.setView(inflater.inflate(R.layout.dialog_cadastro_ponto, null))
                .setPositiveButton("Cadastrar", (dialog, id) -> {
                    etCep = Objects.requireNonNull(getDialog()).findViewById(R.id.cep);
                    etNumero = getDialog().findViewById(R.id.numero);
                    String cep = etCep.getText().toString();
                    String numero = etNumero.getText().toString();
                    
                    if (cep.isEmpty() | numero.isEmpty()){
                        Toast.makeText(getContext(), "Preencha todos os campos!", Toast.LENGTH_SHORT).show();
                    } else {



                        AsyncTask asyncTask = new AsyncTask() {
                            @Override
                            protected Object doInBackground(Object[] objects) {
                                try {
                                    Retrofit retrofit = new Retrofit.Builder().baseUrl("https://viacep.com.br/ws/").addConverterFactory(GsonConverterFactory.create()).build();
                                    EnderecoService service = retrofit.create(EnderecoService.class);


                                    Endereco enderecoResultado = new Endereco();
                                    enderecoResultado.setNumero(numero);

                                    Call<Endereco> localizacao = service.infoEndereco(cep);
                                    Response<Endereco> resposta = localizacao.execute();

                                    enderecoResultado = resposta.body();


                                    if (enderecoResultado != null) {
                                        geocode = enderecoResultado.toString();
                                        geocode += ", " + numero;

                                        Map<String, String> ponto = new HashMap<>();
                                        ponto.put("geocode", geocode);

                                        db.collection("pontos")
                                                .add(ponto)
                                                .addOnSuccessListener(documentReference -> Log.d("sucessoCadaPonto", "DocumentSnapshot adicionado com ID: " + documentReference.getId()))
                                                .addOnFailureListener(e -> Log.w("erroNoCadastroDePonto", "erro ao adicionar documento", e));
                                    }
                                } catch (IOException e) {
                                    e.printStackTrace();
                                }
                                return null;
                            }
                        };
                        asyncTask.execute();
                    }
                })
                .setNegativeButton("Cancelar", (dialog, id) -> Objects.requireNonNull(DialogPonto.this.getDialog()).cancel());
        return builder.create();
    }


}
