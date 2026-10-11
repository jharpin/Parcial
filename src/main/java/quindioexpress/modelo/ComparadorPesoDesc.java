package quindioexpress.modelo;

import java.util.Comparator;

/** Criterio alterno: peso de mayor a menor. */
public class ComparadorPesoDesc implements Comparator<Paquete> {

    @Override
    public int compare(Paquete o1, Paquete o2) {
        if (o1.getPeso() > o2.getPeso()) {
            return -1;
        }
        if (o1.getPeso() < o2.getPeso()) {
            return 1;
        }
        return 0;
    }
}
