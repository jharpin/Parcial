package quindioexpress.algoritmos;

import java.util.List;

/**
 * Búsqueda binaria genérica. PRECONDICIÓN: la lista debe estar ordenada
 * según el orden natural (para Paquete: por código). O(log n).
 */
public final class BusquedaBinaria {
    private BusquedaBinaria() { }

    /** Retorna la posición del elemento o -1. Si traza = true imprime cada comparación. */
    public static <T extends Comparable<? super T>> int buscar(List<T> ordenada, T objetivo, boolean traza) {
        int izq = 0, der = ordenada.size() - 1;
        while (izq <= der) {
            int medio = izq + (der - izq) / 2;
            int cmp = ordenada.get(medio).compareTo(objetivo);
            if (traza) System.out.printf("   izq=%d der=%d medio=%d -> %s%n", izq, der, medio, ordenada.get(medio));
            if (cmp == 0) return medio;
            if (cmp < 0) izq = medio + 1;   // el objetivo está a la derecha
            else der = medio - 1;           // el objetivo está a la izquierda
        }
        return -1;
    }
}
