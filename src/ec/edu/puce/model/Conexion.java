package ec.edu.puce.model;

/**
 * Clase que representa una arteria vial entre dos sedes (Arista ponderada del Grafo).
 * Contiene la sede de destino y la distancia en kilómetros.
 */
public class Conexion {
    private Sede destino;
    private int distanciaKm;

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
}
