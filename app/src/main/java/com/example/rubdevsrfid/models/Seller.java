package com.example.rubdevsrfid.models;

public class Seller {
    private String Id;
    private String Nombre;
    private String NoEmpleado;

    public Seller(String id, String nombre, String noEmpleado) {
        Id = id;
        Nombre = nombre;
        NoEmpleado = noEmpleado;
    }

    public Seller(){

    }

    public String getId() {
        return Id;
    }

    public void setId(String id) {
        Id = id;
    }

    public String getNombre() {
        return Nombre;
    }

    public void setNombre(String nombre) {
        Nombre = nombre;
    }

    public String getNoEmpleado() {
        return NoEmpleado;
    }

    public void setNoEmpleado(String noEmpleado) {
        NoEmpleado = noEmpleado;
    }
}
