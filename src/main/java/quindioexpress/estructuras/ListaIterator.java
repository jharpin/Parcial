package quindioexpress.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/** Iterador de la lista enlazada: recorre desde el primer nodo sin exponer los nodos a quien lo usa. */
public class ListaIterator<T> implements Iterator<T> {

    private Nodo<T> actual;

    public ListaIterator(Nodo<T> nodoPrimero) {
        this.actual = nodoPrimero;
    }

    @Override
    public boolean hasNext() {
        return actual != null;
    }

    @Override
    public T next() {
        if (!hasNext()) {
            throw new NoSuchElementException("NO hay elementos");
        }
        T dato = actual.getDato();
        actual = actual.getSiguiente();
        return dato;
    }
}
