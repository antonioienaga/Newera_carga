package com.crmlab.newera_carga.config;

public class FormataCPF {

    public FormataCPF() {
    }

    public String FormataCPF(String CPF){
        
        String CPFformatado = CPF;
        if (CPF.length() > 8) {
            Integer b4 = CPF.length();
            CPFformatado = CPF.substring(0, b4 - 8) + "." + CPF.substring(b4 - 8, b4 - 5) + "." +
                    CPF.substring(b4 - 5, b4 - 2) + "-" + CPF.substring(b4 - 2, b4);
        }
        return CPFformatado;
    }
}
