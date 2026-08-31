package ec.edu.puce.test;

import ec.edu.puce.dijkstra.DijkstraSolver;
import ec.edu.puce.graph.GrafoLogistico;
import ec.edu.puce.model.RutaResultado;

/**
 * Pruebas automatizadas para verificar que las rutas y distancias
 * calculadas por el algoritmo de Dijkstra sean exactamente las esperadas.
 */
public class LogiPackTest {
    private static int pasadas = 0;
    private static int fallidas = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SUITE DE PRUEBAS - LOGIPACK ECUADOR           ");
        System.out.println("=================================================");

        GrafoLogistico grafo = GrafoLogistico.crearRedLogiPackEcuador();
        DijkstraSolver solver = new DijkstraSolver();

        // 1. Caso oficial del PDF: Quito (0) -> Cuenca (4) = 370 km (Quito -> Ambato -> Cuenca)
        verificar(solver.calcularRutaOptima(grafo, 0, 4), 370, "Quito -> Ambato -> Cuenca", "Quito a Cuenca (Caso PDF)");

        // 2. Quito (0) -> Manta (1) = 460 km (Quito -> Ambato -> Manta)
        verificar(solver.calcularRutaOptima(grafo, 0, 1), 460, "Quito -> Ambato -> Manta", "Quito a Manta");

        // 3. Quito (0) -> Guayaquil (2) = 420 km (Directo)
        verificar(solver.calcularRutaOptima(grafo, 0, 2), 420, "Quito -> Guayaquil", "Quito a Guayaquil");

        // 4. Ambato (3) -> Guayaquil (2) = 415 km (Ambato -> Cuenca -> Guayaquil: 220 + 195 = 415)
        verificar(solver.calcularRutaOptima(grafo, 3, 2), 415, "Ambato -> Cuenca -> Guayaquil", "Ambato a Guayaquil");

        // 5. Cuenca (4) -> Manta (1) = 385 km (Cuenca -> Guayaquil -> Manta: 195 + 190 = 385)
        verificar(solver.calcularRutaOptima(grafo, 4, 1), 385, "Cuenca -> Guayaquil -> Manta", "Cuenca a Manta");

        // 6. Simetria: Cuenca (4) -> Quito (0) = 370 km
        verificar(solver.calcularRutaOptima(grafo, 4, 0), 370, "Cuenca -> Ambato -> Quito", "Simetria: Cuenca a Quito");

        // 7. Misma sede: Quito (0) -> Quito (0) = 0 km
        verificar(solver.calcularRutaOptima(grafo, 0, 0), 0, "Quito", "Misma Sede: Quito a Quito");

        System.out.println("=================================================");
        System.out.printf("RESULTADOS FINALES: %d Pasadas, %d Fallidas%n", pasadas, fallidas);
        System.out.println("=================================================");

        if (fallidas > 0) {
            System.exit(1);
        }
    }

    private static void verificar(RutaResultado ruta, int distanciaEsperada, String secuenciaEsperada, String nombre) {
        boolean okDistancia = (ruta.getDistanciaTotal() == distanciaEsperada);
        boolean okSecuencia = ruta.getSecuenciaTexto().equals(secuenciaEsperada);

        if (okDistancia && okSecuencia) {
            System.out.printf(" [PASS] %s -> %d km | %s%n", nombre, ruta.getDistanciaTotal(), ruta.getSecuenciaTexto());
            pasadas++;
        } else {
            System.err.printf(" [FAIL] %s (Esperado: %d km '%s' | Obtenido: %d km '%s')%n",
                    nombre, distanciaEsperada, secuenciaEsperada, ruta.getDistanciaTotal(), ruta.getSecuenciaTexto());
            fallidas++;
        }
    }
}
