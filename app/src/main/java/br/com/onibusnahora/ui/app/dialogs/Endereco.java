package br.com.onibusnahora.ui.app.dialogs;


import androidx.annotation.NonNull;

public class Endereco {
    private String numero  = "";
    private String cep = "";
    private String logradouro = "";
    private String complemento = "";
    private String bairro = "";
    private String localidade = "";
    private String uf = "";
    private String unidade = "";
    private String ibge = "";
    private String gia = "";

    public String getNumero(){
        return this.numero;
    }

    public void setNumero(String numero){
        this.numero = numero;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public String getLogradouro() {
        return logradouro;
    }

    public void setLogradouro(String logradouro) {
        this.logradouro = logradouro;
    }

    public String getComplemento() {
        return complemento;
    }

    public void setComplemento(String complemento) {
        this.complemento = complemento;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public String getLocalidade() {
        return localidade;
    }

    public void setLocalidade(String localidade) {
        this.localidade = localidade;
    }

    public String getUf() {
        return uf;
    }

    public void setUf(String uf) {
        this.uf = uf;
    }

    public String getUnidade() {
        return unidade;
    }

    public void setUnidade(String unidade) {
        this.unidade = unidade;
    }

    public String getIbge() {
        return ibge;
    }

    public void setIbge(String ibge) {
        this.ibge = ibge;
    }

    public String getGia() {
        return gia;
    }

    public void setGia(String gia) {
        this.gia = gia;
    }

    @NonNull
    @Override
    public String toString() {
        return this.logradouro + " " + this.numero +", " + this.bairro + ", " + this.localidade + ", " + this.uf;
    }
}

