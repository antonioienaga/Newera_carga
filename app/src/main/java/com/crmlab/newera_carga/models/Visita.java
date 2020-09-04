package com.crmlab.newera_carga.models;

import com.google.firebase.Timestamp;

import java.util.Currency;

public class Visita {

    private Timestamp criado;
    private String criadoPor;
    private String idCliente;
    private String responsavel;
    private Number valor;
    private String documento;
    private Timestamp data;

    public Visita() {
    }

    public Timestamp getCriado() {
        return criado;
    }

    public void setCriado(Timestamp criado) {
        this.criado = criado;
    }

    public String getCriadoPor() {
        return criadoPor;
    }

    public void setCriadoPor(String criadoPor) {
        this.criadoPor = criadoPor;
    }

    public String getIdCliente() {
        return idCliente;
    }

    public void setIdCliente(String idCliente) {
        this.idCliente = idCliente;
    }

    public String getResponsavel() {
        return responsavel;
    }

    public void setResponsavel(String responsavel) {
        this.responsavel = responsavel;
    }

    public Number getValor() {
        return valor;
    }

    public void setValor(Number valor) {
        this.valor = valor;
    }

    public String getDocumento() {
        return documento;
    }

    public void setDocumento(String documento) {
        this.documento = documento;
    }

    public Timestamp getData() {
        return data;
    }

    public void setData(Timestamp data) {
        this.data = data;
    }
}
