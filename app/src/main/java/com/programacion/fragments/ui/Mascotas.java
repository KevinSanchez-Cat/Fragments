package com.programacion.fragments.ui;

public class Mascotas {

    private String nombreMascota;
    private String especie;
    private String edad;
    private int id;
    private String imagen;
    private String estatus;

    public Mascotas(int id, String nombreMascota, String especie, String edad, String imagen, String estatus) {
        this.id = id;
        this.imagen = imagen;
        this.nombreMascota = nombreMascota;
        this.especie = especie;
        this.edad = edad;
        this.estatus=estatus;
    }

    public String getEstatus() {
        return estatus;
    }

    public void setEstatus(String estatus) {
        this.estatus = estatus;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public String getNombreMascota() {
        return nombreMascota;
    }

    public void setNombreMascota(String nombreMascota) {
        this.nombreMascota = nombreMascota;
    }

    public String getEspecie() {
        return especie;
    }

    public void setEspecie(String especie) {
        this.especie = especie;
    }

    public String getEdad() {
        return edad;
    }

    public void setEdad(String edad) {
        this.edad = edad;
    }
}
