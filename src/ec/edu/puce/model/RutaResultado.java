package ec.edu.puce.model;

import java.util.ArrayList;
import java.util.List;

/**
 * Clase que almacena el resultado final del enrutamiento de Dijkstra.
 * Guarda la sede de origen, destino, lista ordenada del camino, distancias por tramo y total.
 */
public class RutaResultado {
    private Sede origen;
    private Sede destino;
    private List<Sede> camino;
    private List<Integer> distanciasTramos;
    private int distanciaTotal;
    private boolean existeRuta;

    public RutaResultado(Sede origen, Sede destino, List<Sede> camino, List<Integer> distanciasTramos, int distanciaTotal, boolean existeRuta) {
        this.origen = origen;
        this.destino = destino;
        this.camino = camino != null ? camino : new ArrayList<>();
        this.distanciasTramos = distanciasTramos != null ? distanciasTramos : new ArrayList<>();
        this.distanciaTotal = distanciaTotal;
        this.existeRuta = existeRuta;
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
     * Genera la secuencia en formato texto: "Quito -> Ambato -> Cuenca"
     */
    public String getSecuenciaTexto() {
        if (!existeRuta || camino.isEmpty()) {
            return "No existe ruta";
        }
        StringBuilder texto = new StringBuilder();
        for (int i = 0; i < camino.size(); i++) {
            texto.append(camino.get(i).getNombre());
            if (i < camino.size() - 1) {
                texto.append(" -> ");
            }
        }
        return texto.toString();
    }

    /**
     * Genera la grafica visual en formato limpio compatible con cualquier consola:
     * "[0] QUITO --(150 km)--> [3] AMBATO --(220 km)--> [4] CUENCA"
     */
    public String getGraficaRutaAscii() {
        if (!existeRuta || camino.isEmpty()) {
            return " [!] No existe conexion vial disponible.";
        }
        if (camino.size() == 1) {
            return " [" + camino.get(0).getId() + "] " + camino.get(0).getNombre().toUpperCase() + " (Misma sede)";
        }
        StringBuilder grafica = new StringBuilder();
        grafica.append(" ");
        for (int i = 0; i < camino.size(); i++) {
            Sede actual = camino.get(i);
            grafica.append("[").append(actual.getId()).append("] ").append(actual.getNombre().toUpperCase());
            if (i < distanciasTramos.size()) {
                grafica.append(" --(").append(distanciasTramos.get(i)).append(" km)--> ");
            }
        }
        return grafica.toString();
    }
}
