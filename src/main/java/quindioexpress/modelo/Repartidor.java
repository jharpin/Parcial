package quindioexpress.modelo;

public class Repartidor {
    private final String identificacion;
    private final String nombre;
    private final String zona;
    private boolean disponible;

    public Repartidor(String identificacion, String nombre, String zona, boolean disponible) {
        if (identificacion == null || identificacion.isBlank())
            throw new IllegalArgumentException("La identificación no puede ser vacía");
        if (zona == null || zona.isBlank())
            throw new IllegalArgumentException("La zona no puede ser vacía (" + identificacion + ")");
        this.identificacion = identificacion;
        this.nombre = nombre;
        this.zona = zona;
        this.disponible = disponible;
    }

    public String getIdentificacion() { return identificacion; }
    public String getNombre() { return nombre; }
    public String getZona() { return zona; }
    public boolean isDisponible() { return disponible; }
    public void setDisponible(boolean disponible) { this.disponible = disponible; }

    @Override
    public boolean equals(Object o) {
        return o instanceof Repartidor r && identificacion.equals(r.identificacion);
    }

    @Override
    public int hashCode() { return identificacion.hashCode(); }

    @Override
    public String toString() {
        return identificacion + "-" + nombre + "(" + zona + (disponible ? ", disponible)" : ", ocupado)");
    }
}
