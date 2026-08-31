package com.logipack.model;

import java.util.Objects;

/**
 * Representa un centro logístico (Vértice / Nodo del Grafo).
 */
public class Sede {
    private final int id;
    private final String nombre;

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
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Sede sede = (Sede) o;
        return id == sede.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "[" + id + "] " + nombre;
    }
}
