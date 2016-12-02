package com.example.messias.trade;

/**
 * Created by Messias on 16/11/2016.
 */

public class Livro {
    private String nome;
    private String descricao;
    private String estado;

    private Livro(){

    }

    public Livro(String nome, String descricao, String estado){

        this.nome = nome;
        this.descricao = descricao;
        this.estado = estado;
    }

    @Override
    public String toString() {
        return "Nome: " + nome + " Descrição: " +
                descricao + " Estado: " + estado;
    }

    public void setDescricao(String descricao) {
        this.descricao = descricao;
    }

    public void setEstado(String estado) {
        this.estado = estado;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getDescricao() {
        return descricao;
    }

    public String getEstado() {
        return estado;
    }

    public String getNome() {
        return nome;
    }
}
