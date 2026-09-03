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

    /**
     * Punto de entrada para ejecutar la suite de pruebas unitarias.
     * Valida casos oficiales, rutas alternas, simetría y casos borde.
     * 
     * @param args Argumentos de línea de comandos (no utilizados).
     */
    public static void main(String[] args) {
        System.out.println("=================================================");
        System.out.println("   SUITE DE PRUEBAS - LOGIPACK ECUADOR           ");
        System.out.println("=================================================");

        GrafoLogistico grafo = GrafoLogistico.crearRedLogiPackEcuador();
        DijkstraSolver solver = new DijkstraSolver();

        // 1. Caso Oficial del Documento PUCE:
        // Quito (0) -> Cuenca (4) = 370 km pasando por Ambato (150 + 220 = 370 km).
        // Descarta la ruta por Guayaquil (420 + 195 = 615 km).
        verificar(solver.calcularRutaOptima(grafo, 0, 4), 370, "Quito -> Ambato -> Cuenca",
                "Quito a Cuenca (Caso PDF)");

        // 2. Ruta Sierra Norte a Costa Central:
        // Quito (0) -> Manta (1) = 460 km (Quito -> Ambato [150] -> Manta [310]).
        verificar(solver.calcularRutaOptima(grafo, 0, 1), 460, "Quito -> Ambato -> Manta", "Quito a Manta");

        // 3. Conexión Troncal Directa:
        // Quito (0) -> Guayaquil (2) = 420 km (Vía directa más corta que vía Ambato: 150 + 220 + 195 = 565 km).
        verificar(solver.calcularRutaOptima(grafo, 0, 2), 420, "Quito -> Guayaquil", "Quito a Guayaquil");

        // 4. Ruta Sierra Centro a Costa Sur:
        // Ambato (3) -> Guayaquil (2) = 415 km (Ambato -> Cuenca [220] -> Guayaquil [195] = 415 km).
        verificar(solver.calcularRutaOptima(grafo, 3, 2), 415, "Ambato -> Cuenca -> Guayaquil", "Ambato a Guayaquil");

        // 5. Ruta Sierra Sur a Costa Central:
        // Cuenca (4) -> Manta (1) = 385 km (Cuenca -> Guayaquil [195] -> Manta [190] = 385 km).
        verificar(solver.calcularRutaOptima(grafo, 4, 1), 385, "Cuenca -> Guayaquil -> Manta", "Cuenca a Manta");

        // 6. Prueba de Simetría (Grafo No Dirigido / Bidireccional):
        // Cuenca (4) -> Quito (0) debe ser idéntico en costo a Quito -> Cuenca (370 km).
        verificar(solver.calcularRutaOptima(grafo, 4, 0), 370, "Cuenca -> Ambato -> Quito", "Simetria: Cuenca a Quito");

        // 7. Caso Borde (Distancia a sí mismo):
        // Quito (0) -> Quito (0) = 0 km y camino unitario.
        verificar(solver.calcularRutaOptima(grafo, 0, 0), 0, "Quito", "Misma Sede: Quito a Quito");

        System.out.println("=================================================");
        System.out.printf("RESULTADOS FINALES: %d Pasadas, %d Fallidas%n", pasadas, fallidas);
        System.out.println("=================================================");

        // Salida con código de error si alguna prueba falló
        if (fallidas > 0) {
            System.exit(1);
        }
    }

    /**
     * Valida que el resultado obtenido por Dijkstra coincida exactamente
     * con la distancia en kilómetros y la secuencia esperada.
     * 
     * @param ruta              Resultado generado por el algoritmo de Dijkstra.
     * @param distanciaEsperada Distancia total en kilómetros esperada.
     * @param secuenciaEsperada Cadena con la secuencia de nombres de sedes esperada.
     * @param nombre            Identificador o descripción de la prueba.
     */
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
