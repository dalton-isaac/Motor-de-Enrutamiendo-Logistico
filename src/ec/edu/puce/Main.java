package ec.edu.puce;

import ec.edu.puce.dijkstra.DijkstraSolver;
import ec.edu.puce.graph.GrafoLogistico;
import ec.edu.puce.model.RutaResultado;
import ec.edu.puce.model.Sede;
import ec.edu.puce.view.ConsolaVista;

import java.util.Scanner;

/**
 * Proyecto Final de Estructuras de Datos - PUCE TEC
 * Motor de Enrutamiento Logistico (LogiPack Ecuador)
 *
 * Estudiante: Isaac
 */
public class Main {
    private static final String ESTUDIANTE = "Isaac";

    public static void main(String[] args) {
        GrafoLogistico redLogistica = GrafoLogistico.crearRedLogiPackEcuador();
        DijkstraSolver dijkstraSolver = new DijkstraSolver();
        ConsolaVista vista = new ConsolaVista(ESTUDIANTE);
        Scanner scanner = new Scanner(System.in);

        vista.mostrarEncabezado();

        boolean salir = false;
        while (!salir) {
            System.out.println("============== MENU DE OPERACIONES ==============");
            System.out.println("  1. Calcular Ruta Optima entre dos Sedes (Dijkstra)");
            System.out.println("  2. Ejecutar Ejemplo Oficial del Documento (Quito -> Cuenca)");
            System.out.println("  3. Ver Mapa de Sedes y Conexiones Viales");
            System.out.println("  4. Ejecutar Matriz Completa de Rutas");
            System.out.println("  5. Salir");
            System.out.println("==================================================");
            System.out.print("Seleccione una opcion (1-5): ");

            if (!scanner.hasNextLine()) {
                break;
            }
            String opcion = scanner.nextLine().trim();

            switch (opcion) {
                case "1":
                    calcularRutaPersonalizada(redLogistica, dijkstraSolver, vista, scanner);
                    break;
                case "2":
                    // Ejemplo del PDF: Quito (0) a Cuenca (4)
                    RutaResultado ejemplo = dijkstraSolver.calcularRutaOptima(redLogistica, 0, 4);
                    vista.mostrarResultadoRuta(ejemplo);
                    break;
                case "3":
                    vista.mostrarCatalogoSedes(redLogistica);
                    vista.mostrarTopologiaRed(redLogistica);
                    break;
                case "4":
                    mostrarTodasLasRutas(redLogistica, dijkstraSolver);
                    break;
                case "5":
                    salir = true;
                    System.out.println("\n[i] Gracias por utilizar el Sistema de Optimizacion LogiPack Ecuador.\n");
                    break;
                default:
                    System.out.println("\n[!] Opcion no valida. Por favor ingrese un numero del 1 al 5.\n");
            }
        }
        scanner.close();
    }

    private static void calcularRutaPersonalizada(GrafoLogistico grafo, DijkstraSolver solver, ConsolaVista vista, Scanner scanner) {
        vista.mostrarCatalogoSedes(grafo);

        int idOrigen = pedirSedeValida(scanner, "Ingrese el ID de la Sede ORIGEN (0-4): ", grafo.getTotalSedes());
        if (idOrigen == -1) return;

        int idDestino = pedirSedeValida(scanner, "Ingrese el ID de la Sede DESTINO (0-4): ", grafo.getTotalSedes());
        if (idDestino == -1) return;

        RutaResultado resultado = solver.calcularRutaOptima(grafo, idOrigen, idDestino);
        vista.mostrarResultadoRuta(resultado);
    }

    private static int pedirSedeValida(Scanner scanner, String mensaje, int total) {
        while (true) {
            System.out.print(mensaje);
            if (!scanner.hasNextLine()) return -1;
            String entrada = scanner.nextLine().trim();
            try {
                int id = Integer.parseInt(entrada);
                if (id >= 0 && id < total) {
                    return id;
                }
                System.out.printf("[!] ID fuera de rango. Debe ser entre 0 y %d.%n", total - 1);
            } catch (NumberFormatException e) {
                System.out.println("[!] Entrada invalida. Debe escribir un numero entero.");
            }
        }
    }

    private static void mostrarTodasLasRutas(GrafoLogistico grafo, DijkstraSolver solver) {
        System.out.println("\n============== MATRIZ DE RUTAS OPTIMAS ENTRE TODAS LAS SEDES ==============");
        for (Sede origen : grafo.getSedes()) {
            if (origen == null) continue;
            for (Sede destino : grafo.getSedes()) {
                if (destino == null) continue;
                if (origen.getId() != destino.getId()) {
                    RutaResultado res = solver.calcularRutaOptima(grafo, origen.getId(), destino.getId());
                    System.out.printf("  * De %-10s a %-10s -> Distancia: %3d km | Ruta: %s%n",
                            origen.getNombre(), destino.getNombre(), res.getDistanciaTotal(), res.getSecuenciaTexto());
                }
            }
        }
        System.out.println("============================================================================\n");
    }
}
