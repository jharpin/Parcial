# Parte B. QuindíoExpress en Java

## Cómo ejecutar (Java 17 o superior)
- **VS Code:** abrir la carpeta `Parcial`, abrir `src/main/java/quindioexpress/Main.java` y pulsar **Run**.
- **Maven:** `mvn compile exec:java`
- **Sin Maven:** `javac -encoding UTF-8 -d out $(find src -name "*.java")` y luego `java -cp out quindioexpress.Main`

## Paquetes
| Paquete | Contenido |
|---|---|
| `modelo` | `Paquete` (Comparable por código), `Repartidor`, `EstadoPaquete` y los comparadores `ComparadorPrioridadDesc`, `ComparadorPesoDesc`, `ComparadorTiempoAsc`, `ComparadorDespacho` |
| `estructuras` | `ColeccionPropia<T>` (interfaz genérica), `ListaSimplementeEnlazada<T>` (Iterable), `Nodo<T>` y `ListaIterator<T>` (Iterator) |
| `logica` | `CentroDistribucion`: registro, consultas, colas y asignación |
| `algoritmos` | Recursión, búsqueda binaria, divide y vencerás |
| `Main` | Datos iniciales (30 paquetes, 7 destinos, 8 repartidores) y demostración de cada operación |

## Estructura por requisito
| Requisito | Estructura | Por qué |
|---|---|---|
| Orden exacto de registro (auditoría) | `ArrayList<Paquete>` | Solo se agrega al final, O(1); nunca se reordena. |
| Consulta por código y unicidad (nec. 2 y 7) | `HashMap<String, Paquete>` | `get`/`containsKey` O(1) promedio, sin recorrer todo. |
| Atención por orden de llegada (nec. 1) | `LinkedList` como `Queue` | FIFO: `offer`/`poll` O(1). |
| Despacho por prioridad (nec. 5) | `PriorityQueue` + `Comparator` DESPACHO | Da el siguiente en O(log n) sin ordenar todo. |
| Municipios sin repetir (nec. 3) | `HashSet<String>` | Descarta repetidos en O(1). |
| Municipios ordenados (nec. 4) | `TreeSet<String>` | Siempre ordenado y sin repetidos, O(log m). |
| Paquetes por municipio | `HashMap<String, List<Paquete>>` | Municipio → sus paquetes en O(1). |
| Repartidores por id | `LinkedHashMap<String, Repartidor>` | Unicidad O(1) y se listan en orden de registro. |
| Repartidores por zona | `HashMap<String, List<Repartidor>>` | Asignar sin recorrer todos los repartidores. |
| Historial de entregas (nec. 6) | `ListaSimplementeEnlazada<Paquete>` propia | Exigida por el enunciado; agrega al final en O(1) (guarda la cola). |
| Orden natural | `Paquete implements Comparable` | Por código ascendente. |
| Criterios alternos | Una clase `Comparator` por criterio (prioridad ↓, peso ↓, tiempo ↑) | Ordenan copias; no cambian el orden natural. |
| Peso por municipio / contar prioridad ≥ k | Recursión (sin ciclos) | Caso base `i == size`, avanza con `i + 1`. |
| Buscar en lista ordenada por código | Búsqueda binaria recursiva y genérica | O(log n); precondición: lista ordenada. |
| Paquete de mayor peso | Divide y vencerás | Divide a la mitad, resuelve izq/der, combina; O(n). |

## Decisiones
- **Prioridad:** se usa el rango **1-5** de la sección de reglas (5 por defecto). El enunciado también dice 0-10; queda la duda para el profesor.
- **Asignación:** el repartidor debe estar disponible y su zona igual al destino. Si no hay, el paquete queda en espera en el frente (no se salta el orden). Al entregar, el repartidor vuelve a estar disponible.
- **Dos colas a la vez:** cada paquete está en la `Queue` y en la `PriorityQueue`. Al asignarlo se saca de las dos con `remove(Object)` (O(n), aceptable para el tamaño de la jornada), así las colas solo contienen paquetes pendientes.
- **Patrones de diseño:** el enunciado no exige uno. Se usa **Facade** (`CentroDistribucion` reúne las operaciones) y **Strategy** (cada clase `Comparator` es un criterio intercambiable).

- **Errores:** las validaciones lanzan `RuntimeException` con el motivo; `Main` las captura y las imprime.
- **Trazas:** `BusquedaBinaria.buscar(..., true)` y `DivideYVenceras.mayorPeso(..., true)` imprimen cada llamada (caso base, división, subproblema izquierdo y derecho, combinación) para las pruebas de escritorio y la pila de llamadas.
