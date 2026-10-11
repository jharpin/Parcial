package quindioexpress.logica;

import java.util.*;
import quindioexpress.estructuras.ColeccionPropia;
import quindioexpress.estructuras.ListaSimplementeEnlazada;
import quindioexpress.modelo.*;

/**
 * Fachada (patrón Facade) del sistema: concentra las operaciones de la jornada
 * y decide qué estructura usa cada necesidad (ver README.md).
 */
public class CentroDistribucion {

    // Orden exacto de registro para auditoría: solo se agrega al final, nunca se reordena.
    private final List<Paquete> registro = new ArrayList<>();
    // Nec. 2 y 7: código -> Paquete. Búsqueda y validación de unicidad O(1) promedio.
    private final Map<String, Paquete> paquetesPorCodigo = new HashMap<>();
    // Nec. 1: pendientes por orden de llegada (FIFO).
    private final Queue<Paquete> colaLlegada = new ArrayDeque<>();
    // Nec. 5: pendientes por prioridad (prioridad desc, tiempo asc, código asc).
    private final PriorityQueue<Paquete> colaPrioridad = new PriorityQueue<>(new ComparadorDespacho());
    // Nec. 3: municipios sin repetir.
    private final Set<String> municipios = new HashSet<>();
    // Nec. 4: municipios ordenados alfabéticamente sin repetir.
    private final TreeSet<String> municipiosOrdenados = new TreeSet<>();
    // Agrupación municipio -> paquetes (en orden de llegada).
    private final Map<String, List<Paquete>> paquetesPorMunicipio = new HashMap<>();
    // Repartidores: id -> Repartidor (unicidad O(1)); LinkedHashMap conserva el orden de registro para listarlos.
    private final Map<String, Repartidor> repartidoresPorId = new LinkedHashMap<>();
    // Zona -> repartidores de esa zona, para asignar sin recorrer todos.
    private final Map<String, List<Repartidor>> repartidoresPorZona = new HashMap<>();
    // Nec. 6: historial de entregas con la lista simplemente enlazada propia.
    private final ColeccionPropia<Paquete> historial = new ListaSimplementeEnlazada<>();

    // ---------------- Paquetes ----------------

    public void registrarPaquete(Paquete p) {
        if (paquetesPorCodigo.containsKey(p.getCodigo())) {
            throw new RuntimeException("Ya existe un paquete con código " + p.getCodigo());
        }
        paquetesPorCodigo.put(p.getCodigo(), p);
        registro.add(p);
        colaLlegada.offer(p);
        colaPrioridad.offer(p);
        municipios.add(p.getDestino());
        municipiosOrdenados.add(p.getDestino());

        List<Paquete> delMunicipio = paquetesPorMunicipio.get(p.getDestino());
        if (delMunicipio == null) {
            delMunicipio = new ArrayList<>();
            paquetesPorMunicipio.put(p.getDestino(), delMunicipio);
        }
        delMunicipio.add(p);
    }

    /** Consulta por código O(1) promedio; null si no existe. */
    public Paquete buscarPaquete(String codigo) { return paquetesPorCodigo.get(codigo); }

    public boolean existePaquete(String codigo) { return paquetesPorCodigo.containsKey(codigo); }

    public List<Paquete> getRegistro() { return Collections.unmodifiableList(registro); }

    public Set<String> getMunicipios() { return Collections.unmodifiableSet(municipios); }

    /** Recorrer este conjunto da los municipios en orden alfabético (es una vista del TreeSet). */
    public Set<String> getMunicipiosOrdenados() { return Collections.unmodifiableSet(municipiosOrdenados); }

    public List<Paquete> paquetesDe(String municipio) {
        List<Paquete> lista = paquetesPorMunicipio.get(municipio);
        if (lista == null) {
            return new ArrayList<>();
        }
        return Collections.unmodifiableList(lista);
    }

    /** Copia ordenada por el orden natural (código); no altera el registro. */
    public List<Paquete> ordenadosNatural() {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia);
        return copia;
    }

    /** Copia ordenada con el criterio dado; no altera el registro ni el orden natural. */
    public List<Paquete> ordenados(Comparator<Paquete> criterio) {
        List<Paquete> copia = new ArrayList<>(registro);
        Collections.sort(copia, criterio);
        return copia;
    }

    // ---------------- Repartidores ----------------

    public void registrarRepartidor(Repartidor r) {
        if (repartidoresPorId.containsKey(r.getIdentificacion())) {
            throw new RuntimeException("Ya existe un repartidor con identificación " + r.getIdentificacion());
        }
        repartidoresPorId.put(r.getIdentificacion(), r);

        List<Repartidor> deLaZona = repartidoresPorZona.get(r.getZona());
        if (deLaZona == null) {
            deLaZona = new ArrayList<>();
            repartidoresPorZona.put(r.getZona(), deLaZona);
        }
        deLaZona.add(r);
    }

    public Repartidor buscarRepartidor(String id) { return repartidoresPorId.get(id); }

    public Collection<Repartidor> getRepartidores() { return Collections.unmodifiableCollection(repartidoresPorId.values()); }

    /** Primer repartidor disponible cuya zona sea el destino; null si no hay. */
    private Repartidor repartidorDisponiblePara(String destino) {
        List<Repartidor> deLaZona = repartidoresPorZona.get(destino);
        if (deLaZona == null) {
            return null;
        }
        for (Repartidor r : deLaZona) {
            if (r.isDisponible()) return r;
        }
        return null;
    }

    // ---------------- Atención y asignación ----------------

    /** Atiende por orden de llegada: asigna el primer paquete pendiente. */
    public Paquete asignarPorLlegada() { return asignarDesde(colaLlegada); }

    /** Atiende por prioridad: asigna el siguiente según la regla de despacho. */
    public Paquete asignarPorPrioridad() { return asignarDesde(colaPrioridad); }

    /** Siguiente paquete a despachar por prioridad, sin sacarlo. */
    public Paquete siguientePorPrioridad() { return colaPrioridad.peek(); }

    /**
     * Mira el frente de la cola, busca repartidor (disponible y zona == destino) y asigna.
     * Si no hay repartidor, el paquete queda en el frente (no se salta el orden) y retorna null.
     * Al asignarlo se saca de las dos colas, porque un paquete asignado ya no está pendiente.
     */
    private Paquete asignarDesde(Queue<Paquete> cola) {
        Paquete p = cola.peek();
        if (p == null) return null;
        Repartidor r = repartidorDisponiblePara(p.getDestino());
        if (r == null) return null;
        colaLlegada.remove(p);
        colaPrioridad.remove(p);
        p.asignar(r);
        r.setDisponible(false);
        return p;
    }

    /** Registra la entrega: pasa al historial y el repartidor vuelve a estar disponible. */
    public void registrarEntrega(String codigo) {
        Paquete p = paquetesPorCodigo.get(codigo);
        if (p == null) throw new RuntimeException("No existe el paquete " + codigo);
        if (p.getEstado() != EstadoPaquete.ASIGNADO) {
            throw new RuntimeException("El paquete " + codigo + " no está asignado (" + p.getEstado() + ")");
        }
        p.marcarEntregado();
        p.getRepartidor().setDisponible(true);
        historial.agregar(p);
    }

    public Iterable<Paquete> getHistorial() { return historial; }

    public int cantidadEntregados() { return historial.tamanio(); }
}
