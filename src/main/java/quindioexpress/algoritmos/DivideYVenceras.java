package quindioexpress.algoritmos;

import java.util.List;
import quindioexpress.modelo.Paquete;

/**
 * Paquete de mayor peso por divide y vencerás.
 * T(n) = 2T(n/2) + O(1) -> O(n) tiempo, O(log n) pila.
 * Con traza = true se imprime cada etapa: caso base, división, combinación y resultado.
 */
public class DivideYVenceras {

    public static Paquete mayorPeso(List<Paquete> paquetes) {
        return mayorPeso(paquetes, false);
    }

    public static Paquete mayorPeso(List<Paquete> paquetes, boolean traza) {
        if (paquetes.isEmpty()) {
            throw new RuntimeException("Conjunto vacío");
        }
        return mayorPeso(paquetes, 0, paquetes.size() - 1, traza, "");
    }

    private static Paquete mayorPeso(List<Paquete> paquetes, int inicio, int fin, boolean traza, String sangria) {
        // CASO BASE: un solo elemento
        if (inicio == fin) {
            if (traza) {
                System.out.println(sangria + "base [" + inicio + "] -> " + paquetes.get(inicio));
            }
            return paquetes.get(inicio);
        }
        // DIVISIÓN
        int mitad = (inicio + fin) / 2;
        if (traza) {
            System.out.println(sangria + "dividir [" + inicio + ".." + fin + "] en [" + inicio + ".." + mitad
                    + "] y [" + (mitad + 1) + ".." + fin + "]");
        }
        // SUBPROBLEMA IZQUIERDO
        Paquete izquierdo = mayorPeso(paquetes, inicio, mitad, traza, sangria + "  ");
        // SUBPROBLEMA DERECHO
        Paquete derecho = mayorPeso(paquetes, mitad + 1, fin, traza, sangria + "  ");
        // COMBINACIÓN
        Paquete ganador = derecho;
        if (izquierdo.getPeso() >= derecho.getPeso()) {
            ganador = izquierdo;
        }
        if (traza) {
            System.out.println(sangria + "combinar " + izquierdo + " vs " + derecho + " -> " + ganador);
        }
        return ganador;
    }
}
