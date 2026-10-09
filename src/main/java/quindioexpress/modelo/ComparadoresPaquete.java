package quindioexpress.modelo;

import java.util.Comparator;

/**
 * Criterios alternativos (Comparator), independientes del orden natural.
 * Patrón Strategy: cada Comparator es una estrategia de comparación intercambiable y reutilizable.
 */
public final class ComparadoresPaquete {
    private ComparadoresPaquete() { }

    /** Prioridad de mayor a menor. */
    public static final Comparator<Paquete> POR_PRIORIDAD_DESC =
            (a, b) -> Integer.compare(b.getPrioridad(), a.getPrioridad());

    /** Peso de mayor a menor. */
    public static final Comparator<Paquete> POR_PESO_DESC =
            (a, b) -> Double.compare(b.getPeso(), a.getPeso());

    /** Tiempo estimado de menor a mayor. */
    public static final Comparator<Paquete> POR_TIEMPO_ASC =
            (a, b) -> Integer.compare(a.getTiempoEstimado(), b.getTiempoEstimado());

    /** Regla de despacho: prioridad desc, luego tiempo asc, luego código asc (orden natural). */
    public static final Comparator<Paquete> DESPACHO =
            POR_PRIORIDAD_DESC.thenComparing(POR_TIEMPO_ASC).thenComparing(Comparator.naturalOrder());
}
