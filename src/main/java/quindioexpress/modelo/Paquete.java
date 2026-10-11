package quindioexpress.modelo;

import java.util.Objects;

/**
 * Paquete de la jornada. Orden natural (Comparable): código ascendente.
 * Prioridad: se usa el rango 1-5 de la sección "Reglas" (5 por defecto).
 */
public class Paquete implements Comparable<Paquete> {

    private final String codigo;
    private final String destino;
    private final double peso;
    private final int prioridad;
    private final int tiempoEstimado;
    private EstadoPaquete estado = EstadoPaquete.PENDIENTE;
    private Repartidor repartidor;

    /** Sin prioridad explícita: queda en 5 por defecto. */
    public Paquete(String codigo, String destino, double peso, int tiempoEstimado) {
        validar(codigo, destino, peso, 5, tiempoEstimado);
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = 5;
        this.tiempoEstimado = tiempoEstimado;
    }

    public Paquete(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        validar(codigo, destino, peso, prioridad, tiempoEstimado);
        this.codigo = codigo;
        this.destino = destino;
        this.peso = peso;
        this.prioridad = prioridad;
        this.tiempoEstimado = tiempoEstimado;
    }

    private void validar(String codigo, String destino, double peso, int prioridad, int tiempoEstimado) {
        if (codigo == null || codigo.trim().isEmpty()) {
            throw new RuntimeException("El código no puede ser vacío");
        }
        if (destino == null || destino.trim().isEmpty()) {
            throw new RuntimeException("El destino no puede ser nulo ni vacío (" + codigo + ")");
        }
        if (!(peso > 0)) {
            throw new RuntimeException("El peso debe ser mayor que cero (" + codigo + ")");
        }
        if (prioridad < 1 || prioridad > 5) {
            throw new RuntimeException("La prioridad debe estar entre 1 y 5 (" + codigo + ")");
        }
        if (tiempoEstimado <= 0) {
            throw new RuntimeException("El tiempo estimado debe ser mayor que cero (" + codigo + ")");
        }
    }

    public String getCodigo() { return codigo; }
    public String getDestino() { return destino; }
    public double getPeso() { return peso; }
    public int getPrioridad() { return prioridad; }
    public int getTiempoEstimado() { return tiempoEstimado; }
    public EstadoPaquete getEstado() { return estado; }
    public Repartidor getRepartidor() { return repartidor; }

    public void asignar(Repartidor r) {
        this.repartidor = r;
        this.estado = EstadoPaquete.ASIGNADO;
    }

    public void marcarEntregado() {
        this.estado = EstadoPaquete.ENTREGADO;
    }

    /** Orden natural: por código ascendente. */
    @Override
    public int compareTo(Paquete otro) {
        return codigo.compareTo(otro.codigo);
    }

    @Override
    public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass()) return false;
        Paquete paquete = (Paquete) o;
        return Objects.equals(codigo, paquete.codigo);
    }

    @Override
    public int hashCode() {
        return Objects.hash(codigo);
    }

    @Override
    public String toString() {
        return codigo + "[" + destino + ", " + peso + "kg, p" + prioridad + ", " + tiempoEstimado + "min]";
    }
}
