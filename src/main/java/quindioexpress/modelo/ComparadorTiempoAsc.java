package quindioexpress.modelo;

import java.util.Comparator;

/** Criterio alterno: tiempo estimado de menor a mayor. */
public class ComparadorTiempoAsc implements Comparator<Paquete> {

    @Override
    public int compare(Paquete o1, Paquete o2) {
        return o1.getTiempoEstimado() - o2.getTiempoEstimado();
    }
}
