package com.crmlab.newera_carga.models;

import com.google.firebase.Timestamp;

import java.util.List;

public class Clientes {

    private String nome;
    private String email;

    private String telefone;
    private String CPF;
    private String status;
    private String categoria;
    private String grupo;
    private String cluster;

    private Number nascDia;
    private Number nascMes;
    private Number nascAno;

    private Number qtdProd;
    private Number qtdProd1;
    private Number qtdProd2;
    private Number qtdProd3;
    private Number qtdProd4;
    private Number qtdProd5;

    private Boolean bProd1;
    private Boolean bProd2;
    private Boolean bProd3;
    private Boolean bProd4;
    private Boolean bProd5;
    private String preferencia;

    private String criadoPor;
    private Timestamp criado;
    private Timestamp atualizado;
    private List Produtos;

    private String visita1;
    private String visita2;
    private String visita3;
    private Timestamp ultVisita;
    private Boolean noCall;
    private Boolean bMigrar;
    private Timestamp ultCampanha;
    private String responsavel;

    public Clientes() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getTelefone() {
        return telefone;
    }

    public void setTelefone(String telefone) {
        this.telefone = telefone;
    }

    public String getCPF() {
        return CPF;
    }

    public void setCPF(String CPF) {
        this.CPF = CPF;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getCategoria() {
        return categoria;
    }

    public void setCategoria(String categoria) {
        this.categoria = categoria;
    }

    public String getGrupo() {
        return grupo;
    }

    public void setGrupo(String grupo) {
        this.grupo = grupo;
    }

    public String getCluster() {
        return cluster;
    }

    public void setCluster(String cluster) {
        this.cluster = cluster;
    }

    public Number getNascDia() {
        return nascDia;
    }

    public void setNascDia(Number nascDia) {
        this.nascDia = nascDia;
    }

    public Number getNascMes() {
        return nascMes;
    }

    public void setNascMes(Number nascMes) {
        this.nascMes = nascMes;
    }

    public Number getNascAno() {
        return nascAno;
    }

    public void setNascAno(Number nascAno) {
        this.nascAno = nascAno;
    }

    public Number getQtdProd() {
        return qtdProd;
    }

    public void setQtdProd(Number qtdProd) {
        this.qtdProd = qtdProd;
    }

    public Number getQtdProd1() {
        return qtdProd1;
    }

    public void setQtdProd1(Number qtdProd1) {
        this.qtdProd1 = qtdProd1;
    }

    public Number getQtdProd2() {
        return qtdProd2;
    }

    public void setQtdProd2(Number qtdProd2) {
        this.qtdProd2 = qtdProd2;
    }

    public Number getQtdProd3() {
        return qtdProd3;
    }

    public void setQtdProd3(Number qtdProd3) {
        this.qtdProd3 = qtdProd3;
    }

    public Number getQtdProd4() {
        return qtdProd4;
    }

    public void setQtdProd4(Number qtdProd4) {
        this.qtdProd4 = qtdProd4;
    }

    public Number getQtdProd5() {
        return qtdProd5;
    }

    public void setQtdProd5(Number qtdProd5) {
        this.qtdProd5 = qtdProd5;
    }

    public Boolean getbProd1() {
        return bProd1;
    }

    public void setbProd1(Boolean bProd1) {
        this.bProd1 = bProd1;
    }

    public Boolean getbProd2() {
        return bProd2;
    }

    public void setbProd2(Boolean bProd2) {
        this.bProd2 = bProd2;
    }

    public Boolean getbProd3() {
        return bProd3;
    }

    public void setbProd3(Boolean bProd3) {
        this.bProd3 = bProd3;
    }

    public Boolean getbProd4() {
        return bProd4;
    }

    public void setbProd4(Boolean bProd4) {
        this.bProd4 = bProd4;
    }

    public Boolean getbProd5() {
        return bProd5;
    }

    public void setbProd5(Boolean bProd5) {
        this.bProd5 = bProd5;
    }

    public String getPreferencia() {
        return preferencia;
    }

    public void setPreferencia(String preferencia) {
        this.preferencia = preferencia;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public Timestamp getCriado() {
        return criado;
    }

    public void setCriado(Timestamp criado) {
        this.criado = criado;
    }

    public Timestamp getAtualizado() {
        return atualizado;
    }

    public void setAtualizado(Timestamp atualizado) {
        this.atualizado = atualizado;
    }

    public List getProdutos() {
        return Produtos;
    }

    public void setProdutos(List produtos) {
        Produtos = produtos;
    }

    public String getVisita1() {
        return visita1;
    }

    public void setVisita1(String visita1) {
        this.visita1 = visita1;
    }

    public String getVisita2() {
        return visita2;
    }

    public void setVisita2(String visita2) {
        this.visita2 = visita2;
    }

    public String getVisita3() {
        return visita3;
    }

    public void setVisita3(String visita3) {
        this.visita3 = visita3;
    }

    public Timestamp getUltVisita() {
        return ultVisita;
    }

    public void setUltVisita(Timestamp ultVisita) {
        this.ultVisita = ultVisita;
    }

    public Boolean getNoCall() {
        return noCall;
    }

    public void setNoCall(Boolean noCall) {
        this.noCall = noCall;
    }

    public Boolean getbMigrar() {
        return bMigrar;
    }

    public void setbMigrar(Boolean bMigrar) {
        this.bMigrar = bMigrar;
    }

    public Timestamp getUltCampanha() {
        return ultCampanha;
    }

    public void setUltCampanha(Timestamp ultCampanha) {
        this.ultCampanha = ultCampanha;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }
}
