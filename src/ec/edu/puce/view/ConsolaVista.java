package ec.edu.puce.view;

import ec.edu.puce.graph.GrafoLogistico;
import ec.edu.puce.model.Conexion;
import ec.edu.puce.model.RutaResultado;
import ec.edu.puce.model.Sede;

/**
 * Clase encargada de imprimir en la consola (CLI) el formato visual
 * del sistema de optimizacion de rutas.
 */
public class ConsolaVista {
    private static final String SEPARADOR = "===============================================================================";
    private String nombreEstudiante;

    public ConsolaVista(String nombreEstudiante) {
        this.nombreEstudiante = nombreEstudiante;
    }

    public void mostrarEncabezado() {
        System.out.println(SEPARADOR);
        System.out.println("LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACION");
        System.out.println("Estudiante: " + nombreEstudiante);
        System.out.println(SEPARADOR);
    }

    public void mostrarCatalogoSedes(GrafoLogistico grafo) {
        System.out.println("\n[Centros Logisticos Disponibles]:");
        for (Sede s : grafo.getSedes()) {
            if (s != null) {
                System.out.printf("  [%d] %s%n", s.getId(), s.getNombre());
            }
        }
        System.out.println();
    }

    public void mostrarTopologiaRed(GrafoLogistico grafo) {
        System.out.println("\n[Conexiones Viales y Distancias]:");
        for (Sede s : grafo.getSedes()) {
            if (s != null) {
                for (Conexion c : grafo.getConexiones(s.getId())) {
                    if (s.getId() < c.getDestino().getId()) {
                        System.out.printf("  * %s (%d) <---(%d km)---> %s (%d)%n",
                                s.getNombre(), s.getId(), c.getDistanciaKm(),
                                c.getDestino().getNombre(), c.getDestino().getId());
                    }
                }
            }
        }
        System.out.println();
    }

    /**
     * Muestra la salida visual requerida en la Seccion 3 del documento del proyecto.
     */
    public void mostrarResultadoRuta(RutaResultado ruta) {
        if (ruta == null) {
            System.out.println("[!] No fue posible calcular la ruta.");
            return;
        }

        Sede origen = ruta.getOrigen();
        Sede destino = ruta.getDestino();

        System.out.println();
        System.out.println(SEPARADOR);
        System.out.println("LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACION");
        System.out.println("Estudiante: " + nombreEstudiante);
        System.out.println(SEPARADOR);
        System.out.println();

        System.out.printf("[Ruta seleccionada]: %s (%d) ---> %s (%d)%n",
                origen.getNombre().toUpperCase(), origen.getId(),
                destino.getNombre().toUpperCase(), destino.getId());

        System.out.println("[Grafica de la ruta optima]:");
        System.out.println(ruta.getGraficaRutaAscii());
        System.out.println();

        System.out.println("[Detalle del Despacho]:");
        System.out.printf(" * Origen:          %s [Sede %d]%n", origen.getNombre(), origen.getId());
        System.out.printf(" * Destino:         %s [Sede %d]%n", destino.getNombre(), destino.getId());
        System.out.printf(" * Secuencia Optima: %s%n", ruta.getSecuenciaTexto());
        System.out.printf(" * Distancia Total:  %d km%n", ruta.getDistanciaTotal());
        System.out.println(SEPARADOR);
        System.out.println();
    }
}
