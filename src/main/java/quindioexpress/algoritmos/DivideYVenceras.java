package quindioexpress.algoritmos;

import java.util.Comparator;
import java.util.List;

/**
 * Máximo por divide y vencerás, genérico con un Comparator
 * (con ComparadoresPaquete.POR_PESO_DESC obtiene el paquete de mayor peso).
 * T(n) = 2T(n/2) + O(1) -> O(n) tiempo, O(log n) pila.
 */
public final class DivideYVenceras {
    private DivideYVenceras() { }

    /** "precede" retorna negativo si a va antes que b; el máximo es el que precede a todos. */
    public static <T> T primero(List<T> lista, Comparator<? super T> precede, boolean traza) {
        if (lista.isEmpty()) throw new IllegalArgumentException("Conjunto vacío");
        return primero(lista, precede, 0, lista.size() - 1, traza, "");
    }

    private static <T> T primero(List<T> lista, Comparator<? super T> precede,
                                 int ini, int fin, boolean traza, String sangria) {
        // CASO BASE: un solo elemento
        if (ini == fin) {
            if (traza) System.out.println(sangria + "base [" + ini + "] -> " + lista.get(ini));
            return lista.get(ini);
        }
        // DIVISIÓN
        int medio = (ini + fin) / 2;
        if (traza) System.out.println(sangria + "dividir [" + ini + ".." + fin + "] en [" + ini + ".." + medio
                + "] y [" + (medio + 1) + ".." + fin + "]");
        // SUBPROBLEMA IZQUIERDO
        T izquierdo = primero(lista, precede, ini, medio, traza, sangria + "  ");
        // SUBPROBLEMA DERECHO
        T derecho = primero(lista, precede, medio + 1, fin, traza, sangria + "  ");
        // COMBINACIÓN
        T ganador = precede.compare(izquierdo, derecho) <= 0 ? izquierdo : derecho;
        if (traza) System.out.println(sangria + "combinar " + izquierdo + " vs " + derecho + " -> " + ganador);
        return ganador;
    }
}
