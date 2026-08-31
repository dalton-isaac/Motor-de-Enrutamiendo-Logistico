package com.logipack.test;

import com.logipack.dijkstra.DijkstraSolver;
import com.logipack.graph.GrafoLogistico;
import com.logipack.model.RutaResultado;

/**
 * Suite de Pruebas Unitarias para verificar la correctitud algorítmica de Dijkstra
 * y el cumplimiento de todos los escenarios viales de LogiPack Ecuador.
 */
public class LogiPackTest {
    private static int pruebasExitosas = 0;
    private static int pruebasFallidas = 0;

    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   INICIANDO SUITE DE PRUEBAS DE LOGIPACK        ");
        System.out.println("=================================================");

        GrafoLogistico grafo = GrafoLogistico.crearRedLogiPackEcuador();
        DijkstraSolver solver = new DijkstraSolver();

        // 1. Caso oficial del enunciado: Quito (0) -> Cuenca (4) = 370 km (Quito -> Ambato -> Cuenca)
        assertRuta(solver.calcularRutaOptima(grafo, 0, 4), 370, "Quito -> Ambato -> Cuenca", "Quito a Cuenca (Caso Oficial)");

        // 2. Quito (0) -> Manta (1) = 460 km (Quito -> Ambato -> Manta)
        assertRuta(solver.calcularRutaOptima(grafo, 0, 1), 460, "Quito -> Ambato -> Manta", "Quito a Manta");

        // 3. Quito (0) -> Guayaquil (2) = 420 km (Quito -> Guayaquil directa)
        assertRuta(solver.calcularRutaOptima(grafo, 0, 2), 420, "Quito -> Guayaquil", "Quito a Guayaquil");

        // 4. Ambato (3) -> Guayaquil (2) = 415 km (Ambato -> Cuenca -> Guayaquil: 220 + 195 = 415)
        assertRuta(solver.calcularRutaOptima(grafo, 3, 2), 415, "Ambato -> Cuenca -> Guayaquil", "Ambato a Guayaquil");

        // 5. Cuenca (4) -> Manta (1) = 385 km (Cuenca -> Guayaquil -> Manta: 195 + 190 = 385)
        assertRuta(solver.calcularRutaOptima(grafo, 4, 1), 385, "Cuenca -> Guayaquil -> Manta", "Cuenca a Manta");

        // 6. Simetría: Cuenca (4) -> Quito (0) = 370 km (Cuenca -> Ambato -> Quito)
        assertRuta(solver.calcularRutaOptima(grafo, 4, 0), 370, "Cuenca -> Ambato -> Quito", "Simetría: Cuenca a Quito");

        // 7. Misma sede: Quito (0) -> Quito (0) = 0 km
        assertRuta(solver.calcularRutaOptima(grafo, 0, 0), 0, "Quito", "Misma Sede: Quito a Quito");

        System.out.println("=================================================");
        System.out.printf("RESULTADOS: %d Pasadas, %d Fallidas%n", pruebasExitosas, pruebasFallidas);
        System.out.println("=================================================");

        if (pruebasFallidas > 0) {
            System.exit(1);
        }
    }

    private static void assertRuta(RutaResultado ruta, int distanciaEsperada, String secuenciaEsperada, String nombreTest) {
        boolean okDistancia = (ruta.getDistanciaTotal() == distanciaEsperada);
        boolean okSecuencia = ruta.getSecuenciaTexto().equals(secuenciaEsperada);

        if (okDistancia && okSecuencia) {
            System.out.printf(" [PASS] %s -> Distancia: %d km | Secuencia: %s%n", nombreTest, ruta.getDistanciaTotal(), ruta.getSecuenciaTexto());
            pruebasExitosas++;
        } else {
            System.err.printf(" [FAIL] %s%n", nombreTest);
            System.err.printf("        Esperado: Distancia=%d km, Secuencia='%s'%n", distanciaEsperada, secuenciaEsperada);
            System.err.printf("        Obtenido: Distancia=%d km, Secuencia='%s'%n", ruta.getDistanciaTotal(), ruta.getSecuenciaTexto());
            pruebasFallidas++;
        }
    }
}
