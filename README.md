# 🚚 LogiPack Ecuador — Motor de Enrutamiento Logístico

> **Proyecto Final de Asignatura: Estructura de Datos**  
> **Institución:** Pontificia Universidad Católica del Ecuador (PUCE TEC)  
> **Docente:** Mgtr. Eduardo Conrado Z.  
> **Estudiante:** Isaac  
> **Entorno de Ejecución:** Java 22+ / Java 25 (Consola CLI)  
> **Calificación Total:** 50 Puntos  

---

## 📌 1. Descripción del Problema

La empresa de mensajería y distribución **LogiPack Ecuador** necesita optimizar sus costos operativos de transporte terrestre conectando **5 centros logísticos estratégicos**:

* `[0]` **Quito**
* `[1]` **Manta**
* `[2]` **Guayaquil**
* `[3]` **Ambato**
* `[4]` **Cuenca**

### 🗺️ Red Vial y Distancias Ponderadas (Grafo Bidireccional)

```
        [0] QUITO
        /        \
 (150 km)        (420 km)
      /            \
[3] AMBATO          \
   /    \            \
(310km) (220 km)      \
 /        \            \
[1] MANTA  [4] CUENCA ──(195 km)── [2] GUAYAQUIL
    \                                  /
     └─────────────(190 km)───────────┘
```

| Conexión Vial | Distancia (km) | Tipo de Vía |
|---|---|---|
| **Quito (0) ↔ Ambato (3)** | `150 km` | Arteria Sierra Norte-Centro |
| **Quito (0) ↔ Guayaquil (2)** | `420 km` | Troncal Sierra-Costa Directa |
| **Ambato (3) ↔ Cuenca (4)** | `220 km` | Troncal Panamericana Sur |
| **Guayaquil (2) ↔ Cuenca (4)** | `195 km` | Conexión Costa-Sierra Sur |
| **Guayaquil (2) ↔ Manta (1)** | `190 km` | Arteria Troncal del Pacífico |
| **Ambato (3) ↔ Manta (1)** | `310 km` | Conexión Sierra Centro-Costa Norte |

---

## 🏗️ 2. Arquitectura del Sistema y Estructuras de Datos

El software está implementado siguiendo los principios de la Programación Orientada a Objetos (POO) y una separación de responsabilidades en capas limpias:

```
src/
└── com/logipack/
    ├── model/
    │   ├── Sede.java           # Nodo/Vértice del grafo (ID, Nombre)
    │   ├── Conexion.java       # Arista ponderada (Destino, Distancia km)
    │   └── RutaResultado.java  # DTO con camino, tramos y distancia total
    ├── graph/
    │   └── GrafoLogistico.java # Lista y Matriz de Adyacencia del Grafo
    ├── dijkstra/
    │   └── DijkstraSolver.java # Algoritmo de Dijkstra con Min-Heap y reconstrucción
    ├── view/
    │   └── ConsolaVista.java   # Renderizado ASCII de consola según especificación
    ├── test/
    │   └── LogiPackTest.java   # Suite de pruebas unitarias automatizadas
    └── Main.java               # Menú interactivo y punto de entrada
```

### 🧠 Justificación de Estructuras de Datos Elegidas

1. **Lista de Adyacencia (`Map<Integer, List<Conexion>>`)**:
   * **Por qué:** Permite iterar únicamente sobre las aristas incidentes reales de cada vértice al momento de explorar vecinos en Dijkstra, evitando revisar celdas vacías como en una matriz densa.
   * **Complejidad Espacial:** $O(V + E)$.

2. **Cola de Prioridad / Min-Heap (`PriorityQueue<NodoDistancia>`)**:
   * **Por qué:** Dijkstra requiere extraer repetidamente el nodo no visitado con la menor distancia acumulada mínima. El Min-Heap garantiza extracción en $O(\log V)$ en lugar de $O(V)$ de una búsqueda lineal.
   * **Complejidad Temporal Total:** $O((V + E) \cdot \log V)$.

3. **Vector de Distancias (`int[] distancias`)**:
   * Almacena la cota superior conocida de la distancia mínima desde el origen a cada nodo. Inicializado en $\infty$ (`Integer.MAX_VALUE`).

4. **Vector de Predecesores (`int[] predecesores`)**:
   * Guarda el identificador del nodo padre que generó la distancia mínima óptima. Permite reconstruir la secuencia exacta del camino en tiempo $O(V)$ mediante seguimiento inverso de punteros.

5. **Vector de Nodos Visitados (`boolean[] visitados`)**:
   * Previene procesar nodos más de una vez una vez que su distancia mínima ha sido formalmente fijada (propiedad de Greedy del algoritmo de Dijkstra).

---

## ⚡ 3. Funcionamiento del Algoritmo de Dijkstra

Para calcular la ruta óptima entre un **Origen $S$** y un **Destino $D$**:

1. **Inicialización:**
   * $dist[S] = 0$, $dist[v] = \infty \quad \forall v \neq S$.
   * $pred[v] = -1 \quad \forall v$.
   * Insertar $(S, 0)$ en la `PriorityQueue`.
2. **Bucle Principal:**
   * Extraer el nodo $u$ con menor distancia acumulada.
   * Si $u$ ya está marcado como visitado, ignorar.
   * Marcar $u$ como visitado. Si $u == D$, finalizar tempranamente (optimización).
   * Para cada arista $(u \to v, w)$:
     * **Relajación:** Si $dist[u] + w < dist[v]$, actualizar $dist[v] = dist[u] + w$, fijar $pred[v] = u$ e insertar $(v, dist[v])$ en la cola de prioridad.
3. **Reconstrucción del Camino:**
   * Desde $D$, seguir $pred[D] \to pred[pred[D]] \to \dots \to S$.
   * Invertir la lista para presentar el orden cronológico del viaje.

---

## 🖥️ 4. Salida en Consola (Fiel a la Especificación)

Ejemplo de ejecución con la ruta **Quito (0) ──> Cuenca (4)**:

```text
===============================================================================
LOGIPACK ECUADOR - SISTEMA DE OPTIMIZACIÓN
Estudiante: Isaac
===============================================================================

[Ruta seleccionada]: QUITO (0) ──> CUENCA (4)
[Grafica de la ruta óptima]:
 [0] QUITO ──(150 km)──> [3] AMBATO ──(220 km)──> [4] CUENCA

[Detalle del Despacho]:
 • Origen:          Quito [Sede 0]
 • Destino:         Cuenca [Sede 4]
 • Secuencia Óptima: Quito -> Ambato -> Cuenca
 • Distancia Total:  370 km
===============================================================================
```

---

## 🚀 5. Compilación y Ejecución

### Opción Rápida (Scripts Batch en Windows)
* **Compilar:** Doble clic en `compilar.bat`
* **Ejecutar Menú:** Doble clic en `ejecutar.bat`
* **Ejecutar Pruebas:** Doble clic en `probar.bat`

### Opción Manual por Terminal
```bash
# 1. Compilación
javac -encoding UTF-8 -d bin src/com/logipack/model/*.java src/com/logipack/graph/*.java src/com/logipack/dijkstra/*.java src/com/logipack/view/*.java src/com/logipack/test/*.java src/com/logipack/Main.java

# 2. Ejecución de la Aplicación
java -Dfile.encoding=UTF-8 -cp bin com.logipack.Main

# 3. Ejecución de la Suite de Pruebas Unitarias
java -Dfile.encoding=UTF-8 -cp bin com.logipack.test.LogiPackTest
```

---

## 🧪 6. Matriz de Pruebas y Validación de Escenarios

| ID | Origen | Destino | Distancia Mínima | Secuencia Óptima | Estado |
|---|---|---|---|---|:---:|
| **T01** | Quito (0) | Cuenca (4) | **370 km** | `Quito -> Ambato -> Cuenca` | ✅ PASS |
| **T02** | Quito (0) | Manta (1) | **460 km** | `Quito -> Ambato -> Manta` | ✅ PASS |
| **T03** | Quito (0) | Guayaquil (2) | **420 km** | `Quito -> Guayaquil` | ✅ PASS |
| **T04** | Ambato (3) | Guayaquil (2) | **415 km** | `Ambato -> Cuenca -> Guayaquil` | ✅ PASS |
| **T05** | Cuenca (4) | Manta (1) | **385 km** | `Cuenca -> Guayaquil -> Manta` | ✅ PASS |
| **T06** | Cuenca (4) | Quito (0) | **370 km** | `Cuenca -> Ambato -> Quito` | ✅ PASS |
| **T07** | Quito (0) | Quito (0) | **0 km** | `Quito` | ✅ PASS |

---

## 🎓 7. Guía Rápida para la Sustentación Individual (25 Puntos)

### ❓ Preguntas Frecuentes del Docente y Respuestas Técnicas

1. **¿Qué tipo de grafo modelaste y por qué?**
   * *Respuesta:* Un **grafo ponderado no dirigido**, porque las vías terrestres permiten el tránsito en ambos sentidos y cada arista tiene un costo asociado (la distancia en kilómetros).

2. **¿Por qué utilizaste una Lista de Adyacencia en lugar de solo una Matriz?**
   * *Respuesta:* La lista de adyacencia es óptima en espacio $O(V + E)$ para grafos dispersos. Al ejecutar Dijkstra, permite recorrer directamente los vecinos reales de un nodo sin tener que inspeccionar toda la fila de la matriz, reduciendo el tiempo de exploración.

3. **¿Cómo funciona el vector de predecesores en la reconstrucción?**
   * *Respuesta:* Cada vez que se relaja una arista $(u, v)$ reduciendo la distancia hacia $v$, se guarda `predecesores[v] = u`. Al finalizar Dijkstra, partimos del destino y hacemos un backtracking hasta el origen; luego invertimos la lista para obtener el camino de ida.

4. **¿Cuál es la complejidad temporal de tu implementación de Dijkstra?**
   * *Respuesta:* Es $O((V + E) \log V)$ gracias a la cola de prioridad `PriorityQueue` (Min-Heap). En el peor de los casos, cada vértice se inserta y extrae de la cola en $O(\log V)$, y cada arista se relaja una vez.

5. **¿Qué pasa si hay pesos negativos?**
   * *Respuesta:* Dijkstra asume pesos no negativos ($\ge 0$). En este problema real de logística, las distancias viales siempre son positivas. Si existieran pesos negativos, se debería utilizar Bellman-Ford.
