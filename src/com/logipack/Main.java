package com.logipack;

import com.logipack.dijkstra.DijkstraSolver;
import com.logipack.graph.GrafoLogistico;
import com.logipack.model.RutaResultado;
import com.logipack.model.Sede;
import com.logipack.view.ConsolaVista;

import java.io.PrintStream;
import java.nio.charset.StandardCharsets;
import java.util.Scanner;

/**
 * Punto de entrada principal para el Sistema de Optimización de LogiPack Ecuador.
 */
public class Main {
    private static final String ESTUDIANTE = "Isaac";

    public static void main(String[] args) {
        // Asegurar salida UTF-8 en consola para renderizado fiel de caracteres ASCII / Unicode
        try {
            System.setOut(new PrintStream(System.out, true, StandardCharsets.UTF_8.name()));
        } catch (Exception ignored) {
        }

        GrafoLogistico redLogistica = GrafoLogistico.crearRedLogiPackEcuador();
        DijkstraSolver dijkstraSolver = new DijkstraSolver();
        ConsolaVista vista = new ConsolaVista(ESTUDIANTE);
        Scanner scanner = new Scanner(System.in, StandardCharsets.UTF_8);

        vista.mostrarEncabezado();

        boolean salir = false;
        while (!salir) {
            System.out.println("============== MENÚ DE OPERACIONES ==============");
            System.out.println("  1. Calcular Ruta Óptima entre dos Sedes (Dijkstra)");
            System.out.println("  2. Ejecutar Ejemplo Oficial del Documento (Quito -> Cuenca)");
            System.out.println("  3. Ver Mapa de Sedes y Conexiones Viales");
            System.out.println("  4. Ejecutar Matriz Completa de Rutas");
            System.out.println("  5. Salir");
            System.out.println("==================================================");
            System.out.print("Seleccione una opción (1-5): ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String entrada = scanner.nextLine().trim();

            switch (entrada) {
                case "1":
                    procesarConsultaPersonalizada(redLogistica, dijkstraSolver, vista, scanner);
                    break;
                case "2":
                    // Quito (0) a Cuenca (4)
                    RutaResultado ejemplo = dijkstraSolver.calcularRutaOptima(redLogistica, 0, 4);
                    vista.mostrarResultadoRuta(ejemplo);
                    break;
                case "3":
                    vista.mostrarCatalogoSedes(redLogistica);
                    vista.mostrarTopologiaRed(redLogistica);
                    break;
                case "4":
                    ejecutarMatrizCompleta(redLogistica, dijkstraSolver, vista);
                    break;
                case "5":
                    salir = true;
                    System.out.println("\n[i] Gracias por utilizar el Sistema de Optimización LogiPack Ecuador.\n");
                    break;
                default:
                    System.out.println("\n[!] Opción no válida. Por favor, ingrese un número del 1 al 5.\n");
            }
        }
        scanner.close();
    }

    private static void procesarConsultaPersonalizada(GrafoLogistico grafo, DijkstraSolver solver, ConsolaVista vista, Scanner scanner) {
        vista.mostrarCatalogoSedes(grafo);

        int idOrigen = solicitarIdSede(scanner, "Ingrese el ID de la Sede ORIGEN (0-4): ", grafo.getTotalSedes());
        if (idOrigen == -1) return;

        int idDestino = solicitarIdSede(scanner, "Ingrese el ID de la Sede DESTINO (0-4): ", grafo.getTotalSedes());
        if (idDestino == -1) return;

        RutaResultado ruta = solver.calcularRutaOptima(grafo, idOrigen, idDestino);
        vista.mostrarResultadoRuta(ruta);
    }

    private static int solicitarIdSede(Scanner scanner, String mensaje, int max) {
        while (true) {
            System.out.print(mensaje);
            if (!scanner.hasNextLine()) return -1;
            String input = scanner.nextLine().trim();
            try {
                int id = Integer.parseInt(input);
                if (id >= 0 && id < max) {
                    return id;
                }
                System.out.printf("[!] ID fuera de rango. Debe ser entre 0 y %d.%n", max - 1);
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada inválida. Ingrese un número entero.");
            }
        }
    }

    private static void ejecutarMatrizCompleta(GrafoLogistico grafo, DijkstraSolver solver, ConsolaVista vista) {
        System.out.println("\n============== MATRIZ DE RUTAS ÓPTIMAS ENTRE TODAS LAS SEDES ==============");
        for (Sede origen : grafo.getSedes()) {
            for (Sede destino : grafo.getSedes()) {
                if (origen.getId() != destino.getId()) {
                    RutaResultado res = solver.calcularRutaOptima(grafo, origen.getId(), destino.getId());
                    System.out.printf("  • De %-10s a %-10s -> Distancia: %3d km | Ruta: %s%n",
                            origen.getNombre(), destino.getNombre(), res.getDistanciaTotal(), res.getSecuenciaTexto());
                }
            }
        }
        System.out.println("============================================================================\n");
    }
}
