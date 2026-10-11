package quindioexpress.estructuras;

import java.util.Iterator;
import java.util.Objects;

/**
 * Lista simplemente enlazada genérica propia. No usa ninguna colección del JCF.
 * Se guarda referencia al primer y al último nodo para agregar al final en O(1).
 */
public class ListaSimplementeEnlazada<T> implements ColeccionPropia<T> {

    private int size;
    private Nodo<T> nodoPrimero;
    private Nodo<T> nodoUltimo;

    public ListaSimplementeEnlazada() {
        size = 0;
        nodoPrimero = null;
        nodoUltimo = null;
    }

    /** Agrega al final. O(1). */
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevoNodo = new Nodo<>(elemento);
        if (estaVacia()) {
            nodoPrimero = nuevoNodo;
        } else {
            nodoUltimo.setSiguiente(nuevoNodo);
        }
        nodoUltimo = nuevoNodo;
        size++;
    }

    /** Elimina la primera ocurrencia. O(n). Retorna false si no existe o la lista está vacía. */
    @Override
    public boolean eliminar(T elemento) {
        if (estaVacia()) {
            return false;
        }
        if (Objects.equals(nodoPrimero.getDato(), elemento)) {       // eliminar el primero
            nodoPrimero = nodoPrimero.getSiguiente();
            if (nodoPrimero == null) {                               // era el único
                nodoUltimo = null;
            }
            size--;
            return true;
        }
        Nodo<T> anterior = nodoPrimero;
        while (anterior.getSiguiente() != null
                && !Objects.equals(anterior.getSiguiente().getDato(), elemento)) {
            anterior = anterior.getSiguiente();
        }
        if (anterior.getSiguiente() == null) {                       // inexistente
            return false;
        }
        if (anterior.getSiguiente() == nodoUltimo) {                 // eliminar el último
            nodoUltimo = anterior;
        }
        anterior.setSiguiente(anterior.getSiguiente().getSiguiente()); // intermedio o último
        size--;
        return true;
    }

    /** Obtiene por posición (0..tamanio-1). O(n). */
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= size) {
            throw new IndexOutOfBoundsException("Posición " + posicion + " fuera de rango [0," + size + ")");
        }
        Nodo<T> auxiliar = nodoPrimero;
        for (int i = 0; i < posicion; i++) {
            auxiliar = auxiliar.getSiguiente();
        }
        return auxiliar.getDato();
    }

    @Override
    public int tamanio() {
        return size;
    }

    @Override
    public boolean estaVacia() {
        return size == 0;
    }

    /** Iterable: el recorrido lo da el iterador, sin exponer los nodos. */
    @Override
    public Iterator<T> iterator() {
        return new ListaIterator<>(nodoPrimero);
    }

    @Override
    public String toString() {
        String texto = "[";
        Nodo<T> auxiliar = nodoPrimero;
        while (auxiliar != null) {
            texto += auxiliar.getDato();
            if (auxiliar.getSiguiente() != null) {
                texto += " -> ";
            }
            auxiliar = auxiliar.getSiguiente();
        }
        return texto + "]";
    }
}
