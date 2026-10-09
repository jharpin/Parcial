package quindioexpress.algoritmos;

import java.util.List;
import quindioexpress.modelo.Paquete;

/** Operaciones pedidas explícitamente por recursividad (sin for/while). */
public final class AlgoritmosRecursivos {
    private AlgoritmosRecursivos() { }

    public static double pesoTotalPorMunicipio(List<Paquete> paquetes, String municipio) {
        return pesoTotal(paquetes, municipio, 0);
    }

    /**
     * Caso base: i == tamaño -> 0.
     * Caso recursivo: peso(i si coincide) + pesoTotal(i + 1). Avanza con i + 1.
     */
    private static double pesoTotal(List<Paquete> paquetes, String municipio, int i) {
        if (i == paquetes.size()) return 0.0;
        Paquete p = paquetes.get(i);
        double actual = p.getDestino().equalsIgnoreCase(municipio) ? p.getPeso() : 0.0;
        return actual + pesoTotal(paquetes, municipio, i + 1);
    }

    public static int contarPrioridadMinima(List<Paquete> paquetes, int minima) {
        return contar(paquetes, minima, 0);
    }

    /**
     * Caso base: i == tamaño -> 0.
     * Caso recursivo: (1 si prioridad >= minima) + contar(i + 1).
     */
    private static int contar(List<Paquete> paquetes, int minima, int i) {
        if (i == paquetes.size()) return 0;
        int actual = paquetes.get(i).getPrioridad() >= minima ? 1 : 0;
        return actual + contar(paquetes, minima, i + 1);
    }

    /** Misma lógica sobre un arreglo de prioridades (ejemplo del enunciado: 5,2,4,1,5,3,4 con mínimo 4 -> 4). */
    public static int contarPrioridadMinima(int[] prioridades, int minima, int i) {
        if (i == prioridades.length) return 0;
        return (prioridades[i] >= minima ? 1 : 0) + contarPrioridadMinima(prioridades, minima, i + 1);
    }
}
