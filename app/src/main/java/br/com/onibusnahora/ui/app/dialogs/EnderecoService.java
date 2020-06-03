package br.com.onibusnahora.ui.app.dialogs;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface EnderecoService {
    @GET("{cep}/json/")
    Call<Endereco> infoEndereco(@Path("cep") String cep);
}
