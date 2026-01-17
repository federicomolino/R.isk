package com.Risk.Entity;

import jakarta.persistence.*;

@Entity
public class Immagine {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private int idImmagine;

    private String codiceFoto;

    private String pathFoto;

    @Lob
    private byte[] pathImmagineDirette;

    public byte[] getPathImmagineDirette() {
        return pathImmagineDirette;
    }

    public void setPathImmagineDirette(byte[] pathImmagineDirette) {
        this.pathImmagineDirette = pathImmagineDirette;
    }

    public int getIdImmagine() {
        return idImmagine;
    }

    public void setIdImmagine(int idImmagine) {
        this.idImmagine = idImmagine;
    }

    public String getCodiceFoto() {
        return codiceFoto;
    }

    public void setCodiceFoto(String codiceFoto) {
        this.codiceFoto = codiceFoto;
    }

    public String getPathFoto() {
        return pathFoto;
    }

    public void setPathFoto(String pathFoto) {
        this.pathFoto = pathFoto;
    }
}
