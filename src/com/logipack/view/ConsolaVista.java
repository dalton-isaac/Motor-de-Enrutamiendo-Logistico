package com.logipack.view;

import com.logipack.graph.GrafoLogistico;
import com.logipack.model.Conexion;
import com.logipack.model.RutaResultado;
import com.logipack.model.Sede;

/**
 * Encargada de renderizar la interfaz de usuario en consola (CLI)
 * con el formato ASCII exacto especificado en los requerimientos.
 */
public class ConsolaVista {
    private static final String SEPARADOR = "===============================================================================";
    private final String nombreEstudiante;

    public ConsolaVista(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public void mostrarEncabezado() {
        System.out.println(SEPARADOR);
        System.out.println("LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACIÓN");
        System.out.println("Estudiante: " + nombreEstudiante);
        System.out.println(SEPARADOR);
    }

    public void mostrarCatalogoSedes(GrafoLogistico grafo) {
        System.out.println("\n[Centros Logísticos Disponibles]:");
        for (Sede s : grafo.getSedes()) {
            System.out.printf("  [%d] %s%n", s.getId(), s.getNombre());
        }
        System.out.println();
    }

    public void mostrarTopologiaRed(GrafoLogistico grafo) {
        System.out.println("\n[Conexiones Viales de la Red]:");
        for (Sede s : grafo.getSedes()) {
            for (Conexion c : grafo.getConexiones(s.getId())) {
                // Mostrar solo en un sentido para no duplicar en la lista visual
                if (s.getId() < c.getDestino().getId()) {
                    System.out.printf("  • %s (%d) <──(%d km)──> %s (%d)%n",
                            s.getNombre(), s.getId(), c.getDistanciaKm(),
                            c.getDestino().getNombre(), c.getDestino().getId());
                }
            }
        }
        System.out.println();
    }

    /**
     * Muestra la salida visual oficial de la ruta calculada según la página 2 del PDF.
     */
    public void mostrarResultadoRuta(RutaResultado ruta) {
        Sede origen = ruta.getOrigen();
        Sede destino = ruta.getDestino();

        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACIÓN");
        System.out.println("Estudiante: " + nombreEstudiante);
        System.out.println(SEPARADOR);
        System.out.println();

        System.out.printf("[Ruta seleccionada]: %s (%d) ──> %s (%d)%n",
                origen.getNombre().toUpperCase(), origen.getId(),
                destino.getNombre().toUpperCase(), destino.getId());

        System.out.println("[Grafica de la ruta óptima]:");
        System.out.println(ruta.getGraficaRutaAscii());
        System.out.println();

        System.out.println("[Detalle del Despacho]:");
        System.out.printf(" • Origen:          %s [Sede %d]%n", origen.getNombre(), origen.getId());
        System.out.printf(" • Destino:         %s [Sede %d]%n", destino.getNombre(), destino.getId());
        System.out.printf(" • Secuencia Óptima: %s%n", ruta.getSecuenciaTexto());
        System.out.printf(" • Distancia Total:  %d km%n", ruta.getDistanciaTotal());
        System.out.println(SEPARADOR);
        System.out.println();
    }
}
