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

## 🏗️ 2. Estructura del Proyecto y Paquetes

Siguiendo el estándar institucional de la PUCE, los paquetes están organizados bajo `ec.edu.puce`:

```
src/
└── ec/
    └── edu/
        └── puce/
            ├── model/
            │   ├── Sede.java           # Clase para representar cada centro logístico (ID, Nombre)
            │   ├── Conexion.java       # Representa una vía con destino y distancia en km
            │   └── RutaResultado.java  # Guarda la ruta final calculada, tramos y distancia total
            ├── graph/
            │   └── GrafoLogistico.java # Grafo con Lista de Adyacencia y Matriz de Distancias
            ├── dijkstra/
            │   └── DijkstraSolver.java # Algoritmo de Dijkstra con vectores y cola de prioridad
            ├── view/
            │   └── ConsolaVista.java   # Salida visual en consola con formato ASCII del proyecto
            ├── test/
            │   └── LogiPackTest.java   # Suite de pruebas unitarias automáticas
            └── Main.java               # Menú interactivo y ejecución del programa
```

---

## 🧠 3. Estructuras de Datos Utilizadas (Para Sustentación)

1. **Arreglo de Sedes (`Sede[] sedes`)**:
   * Almacena las 5 sedes indexadas directamente por su ID (`0` a `4`), permitiendo acceso en tiempo constante $O(1)$.

2. **Lista de Adyacencia (`List<Conexion>[] listaAdyacencia`)**:
   * Es un arreglo de listas (`ArrayList`). En la posición `i` guarda las ciudades a las que se puede viajar directamente desde la sede `i`.
   * **Ventaja:** Solo ocupa la memoria necesaria para las conexiones existentes ($O(V + E)$).

3. **Matriz de Adyacencia (`int[][] matrizAdyacencia`)**:
   * Matriz de $5 \times 5$ para consultar la distancia directa entre cualquier par de ciudades en $O(1)$.

4. **Algoritmo de Dijkstra**:
   * **`distancias[]` (Vector de enteros):** Guarda la distancia acumulada mínima encontrada desde el origen hasta cada ciudad. Se inicializa con `Integer.MAX_VALUE` (infinito).
   * **`visitados[]` (Vector booleano):** Marca con `true` las ciudades cuya distancia más corta ya está calculada y confirmada.
   * **`predecesores[]` (Vector de enteros):** Guarda de qué ciudad venimos para llegar a la actual. Es la clave para reconstruir el camino al revés desde el destino hacia el origen.
   * **`PriorityQueue<ElementoCola>` (Cola de Prioridad / Min-Heap):** Permite obtener siempre y de forma rápida ($O(\log V)$) la siguiente ciudad con menor distancia acumulada.

---

## 🖥️ 4. Salida en Consola (Fiel a la Especificación del PDF)

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

* **Compilar:** Doble clic en `compilar.bat`
* **Ejecutar Menú:** Doble clic en `ejecutar.bat`
* **Ejecutar Pruebas:** Doble clic en `probar.bat`

---

## 🧪 6. Resultados de las Pruebas Unitarias

| Origen | Destino | Distancia Mínima | Secuencia Óptima | Resultado |
|---|---|---|---|:---:|
| **Quito (0)** | **Cuenca (4)** | **370 km** | `Quito -> Ambato -> Cuenca` | ✅ PASS |
| **Quito (0)** | **Manta (1)** | **460 km** | `Quito -> Ambato -> Manta` | ✅ PASS |
| **Quito (0)** | **Guayaquil (2)** | **420 km** | `Quito -> Guayaquil` | ✅ PASS |
| **Ambato (3)** | **Guayaquil (2)** | **415 km** | `Ambato -> Cuenca -> Guayaquil` | ✅ PASS |
| **Cuenca (4)** | **Manta (1)** | **385 km** | `Cuenca -> Guayaquil -> Manta` | ✅ PASS |
| **Cuenca (4)** | **Quito (0)** | **370 km** | `Cuenca -> Ambato -> Quito` | ✅ PASS |
| **Quito (0)** | **Quito (0)** | **0 km** | `Quito` | ✅ PASS |

---

## 🎓 7. Guía de Sustentación para Isaac (25 Puntos)

### Preguntas Típicas del Profesor y Cómo Responder:

1. **¿Qué es un Grafo y cómo lo representaste en Java?**
   * *"Un grafo es un conjunto de vértices (las sedes) y aristas (las vías). En este proyecto lo representé con una **Lista de Adyacencia** usando un arreglo de `ArrayList<Conexion>`, donde cada posición contiene las ciudades vecinas y la distancia en km."*

2. **¿Cómo funciona el Algoritmo de Dijkstra paso a paso?**
   * *"1. Coloco la distancia de la ciudad origen en 0 y las demás en infinito.*
   * *2. Meto el origen en una cola de prioridad.*
   * *3. Saco la ciudad con menor distancia, reviso sus vecinas y calculo la suma acumulada.*
   * *4. Si esa suma es menor a la distancia que teníamos guardada (proceso de relajación), actualizo la distancia y anoto en el arreglo de `predecesores` de dónde vine.*
   * *5. Repito hasta llegar al destino."*

3. **¿Cómo reconstruyes la ruta al final?**
   * *"Uso el arreglo `predecesores[]`. Empiezo desde la ciudad destino y voy retrocediendo de padre en padre hasta llegar al origen. Luego simplemente invierto la lista para mostrar el orden cronológico del viaje."*

4. **¿Por qué la ruta Quito -> Cuenca da 370 km y no va directo por Guayaquil?**
   * *"Porque por Guayaquil la distancia sería: Quito a Guayaquil (420 km) + Guayaquil a Cuenca (195 km) = 615 km. En cambio por Ambato es: Quito a Ambato (150 km) + Ambato a Cuenca (220 km) = 370 km. Dijkstra evaluó ambas opciones y eligió la de 370 km porque es la menor."*
