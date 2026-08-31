package ec.edu.puce.model;

/**
 * Clase que representa un centro logístico (Vértice o Nodo del Grafo).
 * Cada sede tiene un identificador numérico (0 a 4) y un nombre de ciudad.
 */
public class Sede {
    private int id;
    private String nombre;

    public Sede(int id, String nombre) {
        this.id = id;
        this.nombre = nombre;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre;
    }
}
