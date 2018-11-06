package com.honda.debrincar.Objetos;

import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.Exclude;
import com.honda.debrincar.Config.ConfiguraçaoFirebase;

import java.util.HashMap;
import java.util.Map;

public class Usuario {



    //dados de todos usuários
    private String id;
    private String nome;
    private String endereco;
    private String cep;
    private String cidade;
    private String estado;

    private Boolean isPessoaFisica = true;

    //dados de pessoa física
    private String sobrenome;
    private String cpf;
    private String dataNascimento;


    //dados de instituição
    private String cnpj;
    private String descricao;


    private String email;
    private String senha;

    private String imagemUsuarioUrl = "";

    public Usuario(){

    }

    @Exclude
    public String getId() {
        return id;
    }

    public void setId(String codigo) {
        this.id = codigo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getSobrenome() {
        return sobrenome;
    }

    public void setSobrenome(String sobrenome) {
        this.sobrenome = sobrenome;
    }

    public String getCpf() {
        return cpf;
    }

    public void setCpf(String cpf) {
        this.cpf = cpf;
    }

    public String getDataNascimento() {
        return dataNascimento;
    }

    public void setDataNascimento(String dataNascimento) {
        this.dataNascimento = dataNascimento;
    }

    public String getEndereco() {
        return endereco;
    }

    public void setEndereco(String endereco) {
        this.endereco = endereco;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    @Exclude
    public String getCidade() {
        return cidade;
    }

    public void setCidade(String cidade) {
        this.cidade = cidade;
    }

    @Exclude
    public String getEstado() {
        return estado;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    @Exclude
    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    @Exclude
    public String getSenha() {
        return senha;
    }

    public void setSenha(String senha) {
        this.senha = senha;
    }

    public String getImagemUsuarioUrl() {
        return imagemUsuarioUrl;
    }

    public void setImagemUsuarioUrl(String imagemUsuarioUrl) {
        this.imagemUsuarioUrl = imagemUsuarioUrl;
    }

    public String getCnpj() {
        return cnpj;
    }

    public void setCnpj(String cnpj) {
        this.cnpj = cnpj;
    }

    public String getDescricao() {
        return descricao;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public Boolean getPessoaFisica() {
        return isPessoaFisica;
    }

    public void setPessoaFisica(Boolean pessoaFisica) {
        isPessoaFisica = pessoaFisica;
    }

    public Map<String, Object> MapaDados(){
        HashMap<String, Object> dados = new HashMap<>();

        if(isPessoaFisica){
            dados.put("nome", nome);
            dados.put("sobrenome", sobrenome);
            dados.put("cpf", cpf);
            dados.put("nascimento", dataNascimento);
            dados.put("cep", cep);
            dados.put("endereco", endereco);
            dados.put("PessoaFisica", isPessoaFisica);
        } else {
            dados.put("nome", nome);
            dados.put("descricao", descricao);
            dados.put("cnpj", cnpj);
            dados.put("cep", cep);
            dados.put("endereco", endereco);
            dados.put("PessoaFisica", isPessoaFisica);
        }
        return dados;
    }

    public void salvarDados(){
        //Salva os dados do usuário no Firebase no nó RAIZ/USUÁRIOS/PF/ID DO USUÁRIO
        DatabaseReference referenciaFirebase = ConfiguraçaoFirebase.getFirebaseData();
        if(isPessoaFisica) {
            referenciaFirebase.child("Usuário").child("PF").child(id).setValue(MapaDados());
        } else{
            referenciaFirebase.child("Usuário").child("INST").child(id).setValue(MapaDados());
        }
    }

    public void salvarDados(String imageUrl){
        //Seta url da imagem do usuário no Firebase
        DatabaseReference referenciaFirebase = ConfiguraçaoFirebase.getFirebaseData();
        if(isPessoaFisica) {
            referenciaFirebase.child("Usuário").child("PF").child(id).child("imagemUsuarioUrl").setValue(imageUrl);
        } else{
            referenciaFirebase.child("Usuário").child("INST").child(id).child("imagemUsuarioUrl").setValue(imageUrl);
        }
    }
}