package com.logipack.model;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Encapsula el resultado de la ejecución del algoritmo de Dijkstra:
 * Contiene el origen, destino, secuencia óptima de sedes, distancias de cada tramo y distancia total.
 */
public class RutaResultado {
    private final Sede origen;
    private final Sede destino;
    private final List<Sede> camino;
    private final List<Integer> distanciasTramos;
    private final int distanciaTotal;
    private final boolean existeRuta;

    public RutaResultado(Sede origen, Sede destino, List<Sede> camino, List<Integer> distanciasTramos, int distanciaTotal, boolean existeRuta) {
        this.origen = origen;
        this.destino = destino;
        this.camino = camino != null ? Collections.unmodifiableList(camino) : Collections.emptyList();
        this.distanciasTramos = distanciasTramos != null ? Collections.unmodifiableList(distanciasTramos) : Collections.emptyList();
        this.distanciaTotal = distanciaTotal;
        this.existeRuta = existeRuta;
    }

    public static RutaResultado sinRuta(Sede origen, Sede destino) {
        return new RutaResultado(origen, destino, new ArrayList<>(), new ArrayList<>(), Integer.MAX_VALUE, false);
    }

    public Sede getOrigen() {
        return origen;
    }

    public Sede getDestino() {
        return destino;
    }

    public List<Sede> getCamino() {
        return camino;
    }

    public List<Integer> getDistanciasTramos() {
        return distanciasTramos;
    }

    public int getDistanciaTotal() {
        return distanciaTotal;
    }

    public boolean isExisteRuta() {
        return existeRuta;
    }

    /**
     * Retorna la secuencia óptima en formato texto: "Quito -> Ambato -> Cuenca"
     */
    public String getSecuenciaTexto() {
        if (!existeRuta || camino.isEmpty()) {
            return "No existe ruta";
        }
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            sb.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                sb.append(" -> ");
            }
        }
        return sb.toString();
    }

    /**
     * Retorna la gráfica visual de la ruta en formato ASCII:
     * "[0] QUITO ──(150 km)──> [3] AMBATO ──(220 km)──> [4] CUENCA"
     */
    public String getGraficaRutaAscii() {
        if (!existeRuta || camino.isEmpty()) {
            return " [!] No existe conexión vial disponible.";
        }
        if (camino.size() == 1) {
            return " [" + camino.get(0).getId() + "] " + camino.get(0).getNombre().toUpperCase() + " (Misma sede)";
        }
        StringBuilder sb = new StringBuilder();
        sb.append(" ");
        for (int i = 0; i < camino.size(); i++) {
            Sede actual = camino.get(i);
            sb.append("[").append(actual.getId()).append("] ").append(actual.getNombre().toUpperCase());
            if (i < distanciasTramos.size()) {
                sb.append(" ──(").append(distanciasTramos.get(i)).append(" km)──> ");
            }
        }
        return sb.toString();
    }
}
