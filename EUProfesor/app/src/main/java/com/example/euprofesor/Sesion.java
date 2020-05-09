package com.example.euprofesor;

public class Sesion {

    private String idSesion, proposito, area, fechaInicio, fechaCierre;

    public Sesion(String idSesion, String area, String proposito, String fechaInicio, String fechaCierre) {
        this.idSesion = idSesion;
        this.proposito = proposito;
        this.area = area;
        this.fechaInicio = fechaInicio;
        this.fechaCierre = fechaCierre;
    }

    public String getIdSesion() {
        return idSesion;
    }

    public String getProposito() {
        return proposito;
    }

    public String getArea() {
        return area;
    }

    public String getFechaInicio() {
        return fechaInicio;
    }

    public String getFechaCierre() {
        return fechaCierre;
    }
}
