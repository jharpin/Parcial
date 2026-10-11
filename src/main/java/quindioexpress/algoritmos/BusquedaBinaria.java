package quindioexpress.algoritmos;

import java.util.List;

/**
 * Búsqueda binaria recursiva y genérica. PRECONDICIÓN: la lista debe estar ordenada
 * según el orden natural (para Paquete: por código). O(log n).
 */
public class BusquedaBinaria {

    /** Retorna la posición del elemento o -1. */
    public static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado) {
        return buscar(ordenada, buscado, false);
    }

    /** Igual que la anterior; si traza = true imprime la posición revisada en cada llamada. */
    public static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado, boolean traza) {
        return buscar(ordenada, buscado, 0, ordenada.size() - 1, traza);
    }

    private static <T extends Comparable<T>> int buscar(List<T> ordenada, T buscado,
                                                        int inicio, int fin, boolean traza) {
        if (inicio > fin) {                       // caso base: no está
            return -1;
        }
        int mitad = (inicio + fin) / 2;
        if (traza) {
            System.out.println("   izq=" + inicio + " der=" + fin + " medio=" + mitad + " -> " + ordenada.get(mitad));
        }
        int comparacion = ordenada.get(mitad).compareTo(buscado);

        if (comparacion == 0) {                   // caso base: encontrado
            return mitad;
        }

        return comparacion < 0
                ? buscar(ordenada, buscado, mitad + 1, fin, traza)     // el buscado está a la derecha
                : buscar(ordenada, buscado, inicio, mitad - 1, traza); // el buscado está a la izquierda
    }
}
