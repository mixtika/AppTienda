package com.isoft.apptiendamovil;

public class CParametros {
    private String var, value;
    public CParametros() {
    }
    public CParametros(String var, String value) {
        this.var = var;
        this.value = value;
    }

    public String getVar() {
        return var;
    }

    public void setVar(String var) {
        this.var = var;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }
}
