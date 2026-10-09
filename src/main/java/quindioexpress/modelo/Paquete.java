package quindioexpress.modelo;

/**
 * Paquete de la jornada. Orden natural (Comparable): código ascendente.
 * Prioridad: se usa el rango 1-5 de la sección "Reglas" (5 por defecto).
 */
public class Paquete implements Comparable<Paquete> {
    public static final int PRIORIDAD_MIN = 1;
    public static final int PRIORIDAD_MAX = 5;
    public static final int PRIORIDAD_DEFECTO = 5;

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;
    private EstadoPaquete estado = EstadoPaquete.PENDIENTE;
    private Repartidor repartidor;

    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        this(codigo, destino, peso, PRIORIDAD_DEFECTO, tiempoEstimado);
    }

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.isBlank())
            throw new IllegalArgumentException("El código no puede ser vacío");
        if (destino == null || destino.isBlank())
            throw new IllegalArgumentException("El destino no puede ser nulo ni vacío (" + codigo + ")");
        if (peso <= 0)
            throw new IllegalArgumentException("El peso debe ser mayor que cero (" + codigo + ")");
        if (prioridad < PRIORIDAD_MIN || prioridad > PRIORIDAD_MAX)
            throw new IllegalArgumentException("La prioridad debe estar entre 1 y 5 (" + codigo + ")");
        if (tiempoEstimado <= 0)
            throw new IllegalArgumentException("El tiempo estimado debe ser mayor que cero (" + codigo + ")");
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    public String getCodigo() { return codigo; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public EstadoPaquete getEstado() { return estado; }
    public Repartidor getRepartidor() { return repartidor; }

    public void asignar(Repartidor r) { this.repartidor = r; this.estado = EstadoPaquete.ASIGNADO; }
    public void marcarEntregado() { this.estado = EstadoPaquete.ENTREGADO; }

    /** Orden natural: por código ascendente. */
    @Override
    public int compareTo(Paquete otro) { return codigo.compareTo(otro.codigo); }

    @Override
    public boolean equals(Object o) {
        return o instanceof Paquete p && codigo.equals(p.codigo);
    }

    @Override
    public int hashCode() { return codigo.hashCode(); }

    @Override
    public String toString() {
        return String.format("%s[%s, %.1fkg, p%d, %dmin]", codigo, destino, peso, prioridad, tiempoEstimado);
    }
}
