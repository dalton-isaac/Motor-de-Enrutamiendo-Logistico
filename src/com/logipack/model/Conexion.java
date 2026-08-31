package com.logipack.model;

/**
 * Representa una vía o conexión vial entre dos sedes (Arista ponderada del Grafo).
 */
public class Conexion {
    private final Sede destino;
    private final int distanciaKm;

    public Conexion(Sede destino, int distanciaKm) {
        this.destino = destino;
        this.distanciaKm = distanciaKm;
    }

    public Sede getDestino() {
        return destino;
    }

    public int getDistanciaKm() {
        return distanciaKm;
    }

    @Override
    public String toString() {
        return "──(" + distanciaKm + " km)──> " + destino;
    }
}
