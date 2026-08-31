package ec.edu.puce.dijkstra;

import ec.edu.puce.graph.GrafoLogistico;
import ec.edu.puce.model.Conexion;
import ec.edu.puce.model.RutaResultado;
import ec.edu.puce.model.Sede;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.PriorityQueue;

/**
 * Implementación del Algoritmo de Dijkstra para encontrar el camino más corto.
 * Utiliza:
 * - distancias[]: Vector para almacenar las distancias acumuladas mínimas.
 * - visitados[]: Vector booleano para marcar nodos procesados.
 * - predecesores[]: Vector para recordar desde qué nodo llegamos (permite reconstruir la ruta).
 * - PriorityQueue: Cola de prioridad (Min-Heap) para seleccionar el nodo más cercano.
 */
public class DijkstraSolver {

    /**
     * Clase interna auxiliar para representar un par (nodo, distancia acumulada)
     * dentro de la Cola de Prioridad.
     */
    private static class ElementoCola implements Comparable<ElementoCola> {
        int idNodo;
        int distancia;

        ElementoCola(int idNodo, int distancia) {
            this.idNodo = idNodo;
            this.distancia = distancia;
        }

        @Override
        public int compareTo(ElementoCola otro) {
            // Ordena de menor a mayor distancia (Min-Heap)
            return Integer.compare(this.distancia, otro.distancia);
        }
    }

    /**
     * Calcula la ruta óptima entre dos sedes usando el algoritmo de Dijkstra.
     */
    public RutaResultado calcularRutaOptima(GrafoLogistico grafo, int idOrigen, int idDestino) {
        int n = grafo.getTotalSedes();
        Sede origen = grafo.getSede(idOrigen);
        Sede destino = grafo.getSede(idDestino);

        // Validación básica
        if (origen == null || destino == null) {
            return null;
        }

        // Si el origen y destino son el mismo
        if (idOrigen == idDestino) {
            List<Sede> camino = new ArrayList<>();
            camino.add(origen);
            return new RutaResultado(origen, destino, camino, new ArrayList<>(), 0, true);
        }

        // PASO 1: Inicialización de vectores auxiliares
        int[] distancias = new int[n];
        int[] predecesores = new int[n];
        boolean[] visitados = new boolean[n];

        // Llenamos las distancias iniciales con "infinito" y predecesores con -1
        Arrays.fill(distancias, Integer.MAX_VALUE);
        Arrays.fill(predecesores, -1);

        // La distancia desde el origen hacia sí mismo es 0
        distancias[idOrigen] = 0;

        // PASO 2: Cola de prioridad para procesar siempre el nodo con menor distancia
        PriorityQueue<ElementoCola> cola = new PriorityQueue<>();
        cola.offer(new ElementoCola(idOrigen, 0));

        // PASO 3: Bucle principal de exploración
        while (!cola.isEmpty()) {
            ElementoCola actual = cola.poll();
            int u = actual.idNodo;

            // Si ya fue visitado definitivamente, lo saltamos
            if (visitados[u]) {
                continue;
            }
            visitados[u] = true;

            // Si ya alcanzamos el nodo destino, podemos detener la búsqueda
            if (u == idDestino) {
                break;
            }

            // Revisamos todas las conexiones (vecinos) del nodo actual 'u'
            for (Conexion conexion : grafo.getConexiones(u)) {
                int v = conexion.getDestino().getId();
                int peso = conexion.getDistanciaKm();

                // Si el vecino 'v' no ha sido visitado
                if (!visitados[v]) {
                    int nuevaDistancia = distancias[u] + peso;

                    // Relajación de la arista: si encontramos un camino más corto hacia 'v'
                    if (nuevaDistancia < distancias[v]) {
                        distancias[v] = nuevaDistancia;
                        predecesores[v] = u; // Guardamos que llegamos a 'v' desde 'u'
                        cola.offer(new ElementoCola(v, nuevaDistancia));
                    }
                }
            }
        }

        // Si la distancia sigue siendo infinito, no existe camino
        if (distancias[idDestino] == Integer.MAX_VALUE) {
            return new RutaResultado(origen, destino, new ArrayList<>(), new ArrayList<>(), Integer.MAX_VALUE, false);
        }

        // PASO 4: Reconstrucción del camino óptimo usando el vector de predecesores
        List<Sede> caminoOptimo = new ArrayList<>();
        int actual = idDestino;
        while (actual != -1) {
            caminoOptimo.add(grafo.getSede(actual));
            actual = predecesores[actual]; // Retrocedemos al padre
        }
        // Invertimos la lista para que quede en orden: Origen -> ... -> Destino
        Collections.reverse(caminoOptimo);

        // PASO 5: Obtenemos las distancias de cada tramo individual
        List<Integer> tramos = new ArrayList<>();
        for (int i = 0; i < caminoOptimo.size() - 1; i++) {
            int d = grafo.getDistanciaDirecta(caminoOptimo.get(i).getId(), caminoOptimo.get(i + 1).getId());
            tramos.add(d);
        }

        return new RutaResultado(origen, destino, caminoOptimo, tramos, distancias[idDestino], true);
    }
}
