package com.logipack.graph;

import com.logipack.model.Conexion;
import com.logipack.model.Sede;

import java.util.*;

/**
 * Representa la red vial logística como un Grafo Ponderado No Dirigido.
 * Implementa tanto Lista de Adyacencia como Matriz de Adyacencia para máximo soporte estructural.
 */
public class GrafoLogistico {
    private final Map<Integer, Sede> sedes;
    private final Map<Integer, List<Conexion>> listaAdyacencia;
    private final int[][] matrizAdyacencia;
    private final int totalSedes;

    public GrafoLogistico(int totalSedes) {
        this.totalSedes = totalSedes;
        this.sedes = new LinkedHashMap<>();
        this.listaAdyacencia = new HashMap<>();
        this.matrizAdyacencia = new int[totalSedes][totalSedes];

        for (int i = 0; i < totalSedes; i++) {
            this.listaAdyacencia.put(i, new ArrayList<>());
            for (int j = 0; j < totalSedes; j++) {
                if (i != j) {
                    this.matrizAdyacencia[i][j] = Integer.MAX_VALUE; // Sin conexión directa
                } else {
                    this.matrizAdyacencia[i][j] = 0; // Distancia a sí mismo
                }
            }
        }
    }

    /**
     * Registra un nodo/sede en el grafo.
     */
    public void agregarSede(int id, String nombre) {
        if (id < 0 || id >= totalSedes) {
            throw new IllegalArgumentException("ID de sede fuera de rango: " + id);
        }
        Sede sede = new Sede(id, nombre);
        sedes.put(id, sede);
    }

    /**
     * Agrega una conexión vial bidireccional entre dos sedes con su respectiva distancia en km.
     */
    public void agregarConexionBidireccional(int idOrigen, int idDestino, int distanciaKm) {
        Sede origen = sedes.get(idOrigen);
        Sede destino = sedes.get(idDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Las sedes especificadas deben existir previamente en el grafo.");
        }

        // Lista de Adyacencia
        listaAdyacencia.get(idOrigen).add(new Conexion(destino, distanciaKm));
        listaAdyacencia.get(idDestino).add(new Conexion(origen, distanciaKm));

        // Matriz de Adyacencia
        matrizAdyacencia[idOrigen][idDestino] = distanciaKm;
        matrizAdyacencia[idDestino][idOrigen] = distanciaKm;
    }

    public Sede getSede(int id) {
        return sedes.get(id);
    }

    public Collection<Sede> getSedes() {
        return Collections.unmodifiableCollection(sedes.values());
    }

    public List<Conexion> getConexiones(int idSede) {
        return Collections.unmodifiableList(listaAdyacencia.getOrDefault(idSede, Collections.emptyList()));
    }

    public int getDistanciaDirecta(int idOrigen, int idDestino) {
        if (idOrigen < 0 || idOrigen >= totalSedes || idDestino < 0 || idDestino >= totalSedes) {
            return Integer.MAX_VALUE;
        }
        return matrizAdyacencia[idOrigen][idDestino];
    }

    public int getTotalSedes() {
        return totalSedes;
    }

    /**
     * Factory method que construye el Grafo de LogiPack Ecuador con la topología oficial del problema.
     */
    public static GrafoLogistico crearRedLogiPackEcuador() {
        GrafoLogistico grafo = new GrafoLogistico(5);

        // 1. Registro de los 5 Centros Logísticos Principales
        grafo.agregarSede(0, "Quito");
        grafo.agregarSede(1, "Manta");
        grafo.agregarSede(2, "Guayaquil");
        grafo.agregarSede(3, "Ambato");
        grafo.agregarSede(4, "Cuenca");

        // 2. Conexiones viales y distancias (km)
        grafo.agregarConexionBidireccional(0, 3, 150); // Quito (0) ↔ Ambato (3): 150 km
        grafo.agregarConexionBidireccional(0, 2, 420); // Quito (0) ↔ Guayaquil (2): 420 km
        grafo.agregarConexionBidireccional(3, 4, 220); // Ambato (3) ↔ Cuenca (4): 220 km
        grafo.agregarConexionBidireccional(2, 4, 195); // Guayaquil (2) ↔ Cuenca (4): 195 km
        grafo.agregarConexionBidireccional(2, 1, 190); // Guayaquil (2) ↔ Manta (1): 190 km
        grafo.agregarConexionBidireccional(3, 1, 310); // Ambato (3) ↔ Manta (1): 310 km

        return grafo;
    }
}
