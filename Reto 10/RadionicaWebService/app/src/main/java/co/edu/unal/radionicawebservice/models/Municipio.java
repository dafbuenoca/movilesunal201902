package co.edu.unal.radionicawebservice.models;

import android.os.Build;

import androidx.annotation.RequiresApi;

import java.util.Objects;

public class Municipio {
    public String departemento;
    public String municipio;
    public String codigo;
    public String poblacion;
    public String estacion;
    public String frecuencia;

    public Municipio(String departemento, String municipio, String codigo, String poblacion, String estacion, String frecuencia) {
        this.departemento = departemento;
        this.municipio = municipio;
        this.codigo = codigo;
        this.poblacion = poblacion;
        this.estacion = estacion;
        this.frecuencia = frecuencia;
    }

    @Override
    public String toString() {
        return "Municipio{" +
                "departemento='" + departemento + '\'' +
                ", municipio='" + municipio + '\'' +
                ", codigo='" + codigo + '\'' +
                ", poblacion='" + poblacion + '\'' +
                ", estacion='" + estacion + '\'' +
                ", frecuencia='" + frecuencia + '\'' +
                '}';
    }

    @Override
    public boolean equals(Object other){
        if(other == null &&  !(other instanceof Municipio))
            return false;
        else if(((Municipio)other).getCodigo().equals(codigo))
            return true;

        return false;
    }

    @RequiresApi(api = Build.VERSION_CODES.KITKAT)
    @Override
    public int hashCode() {
        return Objects.hash(departemento, municipio, codigo, poblacion, estacion, frecuencia);
    }

    public void setDepartemento(String departemento) {
        this.departemento = departemento;
    }

    public void setMunicipio(String municipio) {
        this.municipio = municipio;
    }

    public void setCodigo(String codigo) {
        this.codigo = codigo;
    }

    public void setPoblacion(String poblacion) {
        this.poblacion = poblacion;
    }

    public void setEstacion(String estacion) {
        this.estacion = estacion;
    }

    public void setFrecuencia(String frecuencia) {
        this.frecuencia = frecuencia;
    }

    public String getDepartemento() {
        return departemento;
    }

    public String getMunicipio() {
        return municipio;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getPoblacion() {
        return poblacion;
    }

    public String getEstacion() {
        return estacion;
    }

    public String getFrecuencia() {
        return frecuencia;
    }
}
