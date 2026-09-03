package ec.edu.puce.graph;

import ec.edu.puce.model.Conexion;
import ec.edu.puce.model.Sede;

import java.util.*;

/**
 * Representa la red vial de LogiPack Ecuador como un Grafo Ponderado No
 * Dirigido.
 * Utiliza estructuras fundamentales de la asignatura:
 * 1. Arreglo de Sedes (Vértices)
 * 2. Arreglo de Listas para la Lista de Adyacencia (ArrayList<Conexion>[])
 * 3. Matriz bidimensional de enteros para la Matriz de Adyacencia (int[][])
 */
public class GrafoLogistico {
    private int totalSedes;
    private Sede[] sedes;
    private List<Conexion>[] listaAdyacencia;
    private int[][] matrizAdyacencia;

    @SuppressWarnings("unchecked")
    public GrafoLogistico(int totalSedes) {
        this.totalSedes = totalSedes;
        this.sedes = new Sede[totalSedes];
        this.listaAdyacencia = new ArrayList[totalSedes];
        this.matrizAdyacencia = new int[totalSedes][totalSedes];

        // Inicializamos las listas de adyacencia y la matriz
        for (int i = 0; i < totalSedes; i++) {
            this.listaAdyacencia[i] = new ArrayList<>();
            for (int j = 0; j < totalSedes; j++) {
                if (i == j) {
                    this.matrizAdyacencia[i][j] = 0; // Distancia a sí mismo
                } else {
                    this.matrizAdyacencia[i][j] = Integer.MAX_VALUE; // Sin conexión directa (infinito)
                }
            }
        }
    }

    /**
     * Registra un nodo (sede) en el arreglo del grafo.
     */
    public void agregarSede(int id, String nombre) {
        if (id >= 0 && id < totalSedes) {
            sedes[id] = new Sede(id, nombre);
        }
    }

    /**
     * Conecta dos sedes de forma bidireccional con una distancia en kilómetros.
     */
    public void agregarConexionBidireccional(int idOrigen, int idDestino, int distanciaKm) {
        Sede origen = sedes[idOrigen];
        Sede destino = sedes[idDestino];

        if (origen != null && destino != null) {
            // Guardamos en la Lista de Adyacencia en ambos sentidos
            listaAdyacencia[idOrigen].add(new Conexion(destino, distanciaKm));
            listaAdyacencia[idDestino].add(new Conexion(origen, distanciaKm));

            // Guardamos en la Matriz de Adyacencia
            matrizAdyacencia[idOrigen][idDestino] = distanciaKm;
            matrizAdyacencia[idDestino][idOrigen] = distanciaKm;
        }
    }

    public Sede getSede(int id) {
        if (id >= 0 && id < totalSedes) {
            return sedes[id];
        }
        return null;
    }

    public Sede[] getSedes() {
        return sedes;
    }

    public List<Conexion> getConexiones(int idSede) {
        if (idSede >= 0 && idSede < totalSedes) {
            return listaAdyacencia[idSede];
        }
        return new ArrayList<>();
    }

    public int getDistanciaDirecta(int id1, int id2) {
        if (id1 >= 0 && id1 < totalSedes && id2 >= 0 && id2 < totalSedes) {
            return matrizAdyacencia[id1][id2];
        }
        return Integer.MAX_VALUE;
    }

    public int getTotalSedes() {
        return totalSedes;
    }

    /**
     * Construye la red de LogiPack Ecuador con las 5 sedes y 6 conexiones viales
     * del proyecto.
     */
    public static GrafoLogistico crearRedLogiPackEcuador() {
        GrafoLogistico grafo = new GrafoLogistico(5);

        // 1. Sedes
        grafo.agregarSede(0, "Quito");
        grafo.agregarSede(1, "Manta");
        grafo.agregarSede(2, "Guayaquil");
        grafo.agregarSede(3, "Ambato");
        grafo.agregarSede(4, "Cuenca");

        // 2. Conexiones viales según el documento
        grafo.agregarConexionBidireccional(0, 3, 150); // Quito (0) <-> Ambato (3): 150 km
        grafo.agregarConexionBidireccional(0, 2, 420); // Quito (0) <-> Guayaquil (2): 420 km
        grafo.agregarConexionBidireccional(3, 4, 220); // Ambato (3) <-> Cuenca (4): 220 km
        grafo.agregarConexionBidireccional(2, 4, 195); // Guayaquil (2) <-> Cuenca (4): 195 km
        grafo.agregarConexionBidireccional(2, 1, 190); // Guayaquil (2) <-> Manta (1): 190 km
        grafo.agregarConexionBidireccional(3, 1, 310); // Ambato (3) <-> Manta (1): 310 km

        return grafo;
    }
}
