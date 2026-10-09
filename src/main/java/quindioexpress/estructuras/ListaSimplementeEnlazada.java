package quindioexpress.estructuras;

import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Lista simplemente enlazada genérica propia. No usa ninguna colección del JCF.
 * Se guarda referencia a la cabeza y a la cola para agregar al final en O(1).
 */
public class ListaSimplementeEnlazada<T> implements ColeccionPropia<T> {

    /** Nodo: valor + referencia al siguiente. Privado: el código externo no lo ve. */
    private static class Nodo<T> {
        T valor;
        Nodo<T> siguiente;
        Nodo(T valor) { this.valor = valor; }
    }

    private Nodo<T> cabeza;
    private Nodo<T> cola;
    private int tamanio;

    /** Agrega al final. O(1). */
    @Override
    public void agregar(T elemento) {
        Nodo<T> nuevo = new Nodo<>(elemento);
        if (cabeza == null) {
            cabeza = nuevo;
        } else {
            cola.siguiente = nuevo;
        }
        cola = nuevo;
        tamanio++;
    }

    /** Elimina la primera ocurrencia. O(n). Retorna false si no existe o la lista está vacía. */
    @Override
    public boolean eliminar(T elemento) {
        if (cabeza == null) return false;
        if (iguales(cabeza.valor, elemento)) {          // eliminar el primero
            cabeza = cabeza.siguiente;
            if (cabeza == null) cola = null;            // era el único
            tamanio--;
            return true;
        }
        Nodo<T> anterior = cabeza;
        while (anterior.siguiente != null && !iguales(anterior.siguiente.valor, elemento)) {
            anterior = anterior.siguiente;
        }
        if (anterior.siguiente == null) return false;   // inexistente
        if (anterior.siguiente == cola) cola = anterior; // eliminar el último
        anterior.siguiente = anterior.siguiente.siguiente; // intermedio o último
        tamanio--;
        return true;
    }

    /** Obtiene por posición (0..tamanio-1). O(n). */
    @Override
    public T obtener(int posicion) {
        if (posicion < 0 || posicion >= tamanio)
            throw new IndexOutOfBoundsException("Posición " + posicion + " fuera de rango [0," + tamanio + ")");
        Nodo<T> actual = cabeza;
        for (int i = 0; i < posicion; i++) actual = actual.siguiente;
        return actual.valor;
    }

    @Override
    public int tamanio() { return tamanio; }

    @Override
    public boolean estaVacia() { return tamanio == 0; }

    /** Iterable: el recorrido lo da la propia lista, sin exponer los nodos. */
    @Override
    public Iterator<T> iterator() {
        return new Iterator<T>() {
            private Nodo<T> actual = cabeza;

            @Override
            public boolean hasNext() { return actual != null; }

            @Override
            public T next() {
                if (actual == null) throw new NoSuchElementException();
                T valor = actual.valor;
                actual = actual.siguiente;
                return valor;
            }
        };
    }

    private boolean iguales(T a, T b) { return a == null ? b == null : a.equals(b); }

    @Override
    public String toString() {
        StringBuilder sb = new StringBuilder("[");
        for (T v : this) {
            if (sb.length() > 1) sb.append(" -> ");
            sb.append(v);
        }
        return sb.append("]").toString();
    }
}
