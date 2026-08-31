package com.logipack.dijkstra;

import com.logipack.graph.GrafoLogistico;
import com.logipack.model.Conexion;
import com.logipack.model.RutaResultado;
import com.logipack.model.Sede;

import java.util.*;

/**
 * Motor algorítmico que implementa el Algoritmo de Dijkstra para encontrar
 * la ruta de costo mínimo (distancia más corta) entre centros logísticos.
 */
public class DijkstraSolver {

    /**
     * Estructura auxiliar para la Cola de Prioridad (Min-Heap).
     */
    private static class NodoDistancia implements Comparable<NodoDistancia> {
        final int idSede;
        final int distanciaAcumulada;

        NodoDistancia(int idSede, int distanciaAcumulada) {
            this.idSede = idSede;
            this.distanciaAcumulada = distanciaAcumulada;
        }

        @Override
        public int compareTo(NodoDistancia o) {
            return Integer.compare(this.distanciaAcumulada, o.distanciaAcumulada);
        }
    }

    /**
     * Ejecuta el Algoritmo de Dijkstra sobre el grafo desde el nodo origen hasta el nodo destino.
     *
     * Complejidad Temporal: O((V + E) * log V) utilizando Min-Heap (PriorityQueue).
     * Complejidad Espacial: O(V + E) para las estructuras del grafo y vectores auxiliares.
     *
     * @param grafo     Grafo vial con sedes y distancias
     * @param idOrigen  Identificador de la sede origen (0..N-1)
     * @param idDestino Identificador de la sede destino (0..N-1)
     * @return RutaResultado con la distancia total, camino ordenado y tramos individuales
     */
    public RutaResultado calcularRutaOptima(GrafoLogistico grafo, int idOrigen, int idDestino) {
        int n = grafo.getTotalSedes();
        Sede origen = grafo.getSede(idOrigen);
        Sede destino = grafo.getSede(idDestino);

        if (origen == null || destino == null) {
            throw new IllegalArgumentException("Sede origen o destino no válida.");
        }

        // Caso especial: Origen y destino son la misma sede
        if (idOrigen == idDestino) {
            List<Sede> camino = new ArrayList<>();
            camino.add(origen);
            return new RutaResultado(origen, destino, camino, new ArrayList<>(), 0, true);
        }

        // 1. Estructuras de Soporte para Dijkstra
        int[] distancias = new int[n];
        int[] predecesores = new int[n];
        boolean[] visitados = new boolean[n];

        Arrays.fill(distancias, Integer.MAX_VALUE);
        Arrays.fill(predecesores, -1);

        // Distancia al nodo inicial es 0
        distancias[idOrigen] = 0;

        // Min-Heap (Cola de Prioridad) para seleccionar siempre el vértice con menor distancia acumulada
        PriorityQueue<NodoDistancia> colaPrioridad = new PriorityQueue<>();
        colaPrioridad.offer(new NodoDistancia(idOrigen, 0));

        // 2. Proceso de Exploración y Relajación de Aristas
        while (!colaPrioridad.isEmpty()) {
            NodoDistancia actual = colaPrioridad.poll();
            int u = actual.idSede;

            // Si ya fue procesado con la distancia óptima definitiva, omitir
            if (visitados[u]) {
                continue;
            }
            visitados[u] = true;

            // Optimización: Si llegamos al destino, no es necesario procesar el resto
            if (u == idDestino) {
                break;
            }

            // Explorar todos los vecinos adyacentes a 'u'
            for (Conexion conexion : grafo.getConexiones(u)) {
                int v = conexion.getDestino().getId();
                int pesoArista = conexion.getDistanciaKm();

                if (!visitados[v]) {
                    int nuevaDistancia = distancias[u] + pesoArista;

                    // Relajación de arista (Dijkstra Edge Relaxation)
                    if (nuevaDistancia < distancias[v]) {
                        distancias[v] = nuevaDistancia;
                        predecesores[v] = u; // Registrar quién es el predecesor para reconstruir la ruta
                        colaPrioridad.offer(new NodoDistancia(v, nuevaDistancia));
                    }
                }
            }
        }

        // 3. Verificar si existe camino
        if (distancias[idDestino] == Integer.MAX_VALUE) {
            return RutaResultado.sinRuta(origen, destino);
        }

        // 4. Reconstrucción del Camino Óptimo a partir del Vector de Predecesores
        List<Sede> caminoOptimo = new ArrayList<>();
        int paso = idDestino;
        while (paso != -1) {
            caminoOptimo.add(grafo.getSede(paso));
            paso = predecesores[paso];
        }
        Collections.reverse(caminoOptimo);

        // 5. Cálculo de distancias tramo a tramo
        List<Integer> tramos = new ArrayList<>();
        for (int i = 0; i < caminoOptimo.size() - 1; i++) {
            int d = grafo.getDistanciaDirecta(caminoOptimo.get(i).getId(), caminoOptimo.get(i + 1).getId());
            tramos.add(d);
        }

        return new RutaResultado(origen, destino, caminoOptimo, tramos, distancias[idDestino], true);
    }
}
