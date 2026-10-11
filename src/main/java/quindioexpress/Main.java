package quindioexpress;

import java.util.*;
import quindioexpress.algoritmos.*;
import quindioexpress.estructuras.*;
import quindioexpress.logica.CentroDistribucion;
import quindioexpress.modelo.*;

/** Demostración de todas las operaciones con los datos mínimos del enunciado. */
public class Main {

    public static void main(String[] args) {
        CentroDistribucion centro = new CentroDistribucion();
        cargarDatos(centro);

        titulo("1. Registro y validaciones");
        System.out.println("Paquetes registrados: " + centro.getRegistro().size()
                + " | Repartidores: " + centro.getRepartidores().size());
        intentarRegistrarPaquete(centro, "PQ731", "Armenia", 1.0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ001", "Armenia", 0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ002", "Armenia", 2.0, 8, 10);
        intentarRegistrarPaquete(centro, "PQ003", "", 2.0, 3, 10);
        intentarRegistrarPaquete(centro, "PQ004", "Salento", 2.0, 3, 0);
        try {
            centro.registrarRepartidor(new Repartidor("R01", "Otro", "Armenia", true));
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("2. Consulta por código (HashMap) y de repartidores");
        System.out.println("PQ942 -> " + centro.buscarPaquete("PQ942"));
        System.out.println("¿Existe PQ999? " + centro.existePaquete("PQ999"));
        System.out.println("R04 -> " + centro.buscarRepartidor("R04"));
        System.out.println("Repartidores: " + centro.getRepartidores());

        titulo("3. Orden de registro (auditoría)");
        System.out.println(codigos(centro.getRegistro()));

        titulo("4. Municipios");
        System.out.println("Sin repetir (HashSet): " + centro.getMunicipios());
        System.out.println("Ordenados (TreeSet):   " + centro.getMunicipiosOrdenados());

        titulo("5. Agrupación por destino (Map<String, List<Paquete>>)");
        for (String m : centro.getMunicipiosOrdenados()) {
            System.out.println(m + " -> " + codigos(centro.paquetesDe(m)));
        }

        titulo("6. Orden natural (Comparable) y Comparator");
        System.out.println("Natural (código):  " + codigos(centro.ordenadosNatural()));
        System.out.println("Prioridad desc:    " + codigos(centro.ordenados(new ComparadorPrioridadDesc())));
        System.out.println("Peso desc:         " + codigos(centro.ordenados(new ComparadorPesoDesc())));
        System.out.println("Tiempo asc:        " + codigos(centro.ordenados(new ComparadorTiempoAsc())));
        System.out.println("Registro intacto:  " + codigos(centro.getRegistro().subList(0, 5)) + " ...");

        titulo("7. Ejemplo de despacho del enunciado (PriorityQueue)");
        PriorityQueue<Paquete> ejemplo = new PriorityQueue<>(new ComparadorDespacho());
        ejemplo.add(new Paquete("PQ420", "Armenia", 1, 3, 30));
        ejemplo.add(new Paquete("PQ731", "Armenia", 1, 5, 45));
        ejemplo.add(new Paquete("PQ105", "Armenia", 1, 5, 25));
        ejemplo.add(new Paquete("PQ942", "Armenia", 1, 4, 20));
        String orden = "";
        while (!ejemplo.isEmpty()) {
            orden += ejemplo.poll().getCodigo() + " ";
        }
        System.out.println("Esperado PQ105 PQ731 PQ942 PQ420 -> " + orden.trim());

        titulo("8. Atención por orden de llegada (Queue) + asignación");
        for (int i = 0; i < 4; i++) {
            Paquete p = centro.asignarPorLlegada();
            if (p == null) {
                System.out.println("Sin repartidor disponible en la zona del siguiente paquete; queda en espera.");
                break;
            }
            System.out.println("Asignado " + p.getCodigo() + " (" + p.getDestino() + ") a " + p.getRepartidor());
        }
        System.out.println("Entregas en orden PQ105, PQ942, PQ731");
        centro.registrarEntrega("PQ105");
        centro.registrarEntrega("PQ942");
        centro.registrarEntrega("PQ731");
        try {
            centro.registrarEntrega("PQ731");
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("9. Despacho prioritario (PriorityQueue) + asignación");
        for (int i = 0; i < 6; i++) {
            Paquete p = centro.asignarPorPrioridad();
            if (p == null) {
                Paquete sig = centro.siguientePorPrioridad();
                System.out.println("Bloqueado: " + sig + " no tiene repartidor disponible en " + sig.getDestino()
                        + ". Se habilita R03.");
                centro.buscarRepartidor("R03").setDisponible(true);
                continue;
            }
            System.out.println("Asignado " + p + " a " + p.getRepartidor().getIdentificacion());
            centro.registrarEntrega(p.getCodigo());
        }

        titulo("10. Historial de entregas (lista enlazada propia, recorrida con Iterator)");
        int n = 1;
        for (Paquete p : centro.getHistorial()) {
            System.out.println((n++) + ". " + p.getCodigo());
        }
        System.out.println("Total entregados: " + centro.cantidadEntregados());

        titulo("11. Casos borde de la lista propia (genérica, con String)");
        ColeccionPropia<String> lista = new ListaSimplementeEnlazada<>();
        System.out.println("Vacía: " + lista.estaVacia() + ", eliminar 'X' en vacía: " + lista.eliminar("X"));
        lista.agregar("A");
        System.out.println("Un elemento " + lista + " -> eliminar A: " + lista.eliminar("A") + " " + lista);
        lista.agregar("A");
        lista.agregar("B");
        lista.agregar("C");
        lista.agregar("D");
        System.out.println("Lista " + lista + ", obtener(2) = " + lista.obtener(2));
        lista.eliminar("A");
        System.out.println("Eliminar primero A:     " + lista);
        lista.eliminar("C");
        System.out.println("Eliminar intermedio C:  " + lista);
        lista.eliminar("D");
        System.out.println("Eliminar último D:      " + lista);
        lista.agregar("E");
        System.out.println("Agregar E (cola ok):    " + lista);
        System.out.println("Eliminar inexistente Z: " + lista.eliminar("Z") + ", tamaño = " + lista.tamanio());
        try {
            lista.obtener(5);
        } catch (IndexOutOfBoundsException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }

        titulo("12. Recursividad");
        List<Paquete> ejemploPeso = new ArrayList<>();
        ejemploPeso.add(new Paquete("PQ101", "Armenia", 4.5, 3, 10));
        ejemploPeso.add(new Paquete("PQ203", "Salento", 3.0, 3, 10));
        ejemploPeso.add(new Paquete("PQ415", "Armenia", 7.2, 3, 10));
        ejemploPeso.add(new Paquete("PQ730", "Armenia", 2.3, 3, 10));
        System.out.printf("Ejemplo enunciado, peso Armenia = %.1f kg (esperado 14.0)%n",
                AlgoritmosRecursivos.pesoTotalPorMunicipio(ejemploPeso, "Armenia"));
        System.out.println("Ejemplo enunciado, prioridad >= 4 en {5,2,4,1,5,3,4} = "
                + AlgoritmosRecursivos.contarPrioridadMinima(new int[]{5, 2, 4, 1, 5, 3, 4}, 4, 0) + " (esperado 4)");
        for (String m : centro.getMunicipiosOrdenados()) {
            System.out.printf("Peso total %s = %.1f kg%n", m,
                    AlgoritmosRecursivos.pesoTotalPorMunicipio(centro.getRegistro(), m));
        }
        System.out.println("Paquetes con prioridad >= 4: "
                + AlgoritmosRecursivos.contarPrioridadMinima(centro.getRegistro(), 4));

        titulo("13. Búsqueda binaria (precondición: ordenado por código)");
        List<Paquete> porCodigo = centro.ordenadosNatural();
        List<String> codigosOrdenados = new ArrayList<>();
        for (Paquete p : porCodigo) {
            codigosOrdenados.add(p.getCodigo());
        }
        System.out.println("Buscar PQ731:");
        int pos = BusquedaBinaria.buscar(codigosOrdenados, "PQ731", true);
        System.out.println("   posición " + pos + " -> " + porCodigo.get(pos));
        System.out.println("Buscar PQ999: posición " + BusquedaBinaria.buscar(codigosOrdenados, "PQ999"));
        List<String> desordenados = new ArrayList<>();
        for (Paquete p : centro.getRegistro()) {
            desordenados.add(p.getCodigo());
        }
        System.out.println("Sin cumplir la precondición (orden de registro), buscar PQ731: posición "
                + BusquedaBinaria.buscar(desordenados, "PQ731") + " (resultado no confiable)");

        titulo("14. Divide y vencerás: paquete de mayor peso");
        List<Paquete> ejemploDyV = new ArrayList<>();
        ejemploDyV.add(new Paquete("E0", "Armenia", 4.5, 3, 10));
        ejemploDyV.add(new Paquete("E1", "Armenia", 12.0, 3, 10));
        ejemploDyV.add(new Paquete("E2", "Armenia", 3.2, 3, 10));
        ejemploDyV.add(new Paquete("E3", "Armenia", 18.7, 3, 10));
        ejemploDyV.add(new Paquete("E4", "Armenia", 7.4, 3, 10));
        ejemploDyV.add(new Paquete("E5", "Armenia", 5.1, 3, 10));
        Paquete max = DivideYVenceras.mayorPeso(ejemploDyV, true);
        System.out.println("Resultado final: " + max.getPeso() + " kg (esperado 18.7)");
        Paquete maxJornada = DivideYVenceras.mayorPeso(centro.getRegistro());
        System.out.println("Mayor peso de la jornada: " + maxJornada);
    }

    /** 30 paquetes, 7 destinos, prioridades 1-5, códigos no consecutivos; 8 repartidores. */
    private static void cargarDatos(CentroDistribucion c) {
        c.registrarPaquete(new Paquete("PQ731", "Armenia", 7.2, 5, 45));
        c.registrarPaquete(new Paquete("PQ105", "Salento", 3.0, 5, 25));
        c.registrarPaquete(new Paquete("PQ942", "Armenia", 18.7, 4, 20));
        c.registrarPaquete(new Paquete("PQ318", "Calarcá", 4.5, 2, 35));
        c.registrarPaquete(new Paquete("PQ567", "Salento", 2.1, 1, 50));
        c.registrarPaquete(new Paquete("PQ829", "Armenia", 2.3, 3, 15));
        c.registrarPaquete(new Paquete("PQ420", "Montenegro", 12.0, 3, 30));
        c.registrarPaquete(new Paquete("PQ214", "Quimbaya", 5.1, 4, 40));
        c.registrarPaquete(new Paquete("PQ421", "Circasia", 6.6, 2, 55));
        c.registrarPaquete(new Paquete("PQ842", "Filandia", 9.8, 5, 60));
        c.registrarPaquete(new Paquete("PQ101", "Armenia", 4.5, 1, 10));
        c.registrarPaquete(new Paquete("PQ203", "Salento", 3.2, 3, 28));
        c.registrarPaquete(new Paquete("PQ415", "Calarcá", 7.4, 4, 22));
        c.registrarPaquete(new Paquete("PQ730", "Montenegro", 1.8, 2, 18));
        c.registrarPaquete(new Paquete("PQ356", "Quimbaya", 11.3, 5, 33));
        c.registrarPaquete(new Paquete("PQ689", "Circasia", 0.9, 3, 12));
        c.registrarPaquete(new Paquete("PQ477", "Filandia", 3.6, 4, 27));
        c.registrarPaquete(new Paquete("PQ512", "Armenia", 8.4, 2, 38));
        c.registrarPaquete(new Paquete("PQ298", "Calarcá", 6.0, 5, 42));
        c.registrarPaquete(new Paquete("PQ863", "Salento", 2.7, 4, 19));
        c.registrarPaquete(new Paquete("PQ154", "Montenegro", 14.2, 1, 65));
        c.registrarPaquete(new Paquete("PQ905", "Quimbaya", 3.3, 3, 24));
        c.registrarPaquete(new Paquete("PQ640", "Circasia", 5.5, 5, 31));
        c.registrarPaquete(new Paquete("PQ377", "Filandia", 7.7, 2, 48));
        c.registrarPaquete(new Paquete("PQ218", "Armenia", 1.2, 4, 14));
        c.registrarPaquete(new Paquete("PQ791", "Calarcá", 10.1, 3, 52));
        c.registrarPaquete(new Paquete("PQ436", "Salento", 4.9, 2, 36));
        c.registrarPaquete(new Paquete("PQ608", "Montenegro", 6.8, 5, 29));
        c.registrarPaquete(new Paquete("PQ983", "Quimbaya", 2.4, 1, 21));
        c.registrarPaquete(new Paquete("PQ259", "Circasia", 8.9, 4, 44));

        c.registrarRepartidor(new Repartidor("R01", "Ana Gómez", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R02", "Luis Pérez", "Armenia", true));
        c.registrarRepartidor(new Repartidor("R03", "Carla Ríos", "Calarcá", false));
        c.registrarRepartidor(new Repartidor("R04", "Jorge Mejía", "Salento", true));
        c.registrarRepartidor(new Repartidor("R05", "Sofía Ramírez", "Montenegro", true));
        c.registrarRepartidor(new Repartidor("R06", "Diego Torres", "Quimbaya", true));
        c.registrarRepartidor(new Repartidor("R07", "Valentina Ruiz", "Circasia", true));
        c.registrarRepartidor(new Repartidor("R08", "Andrés Castaño", "Filandia", true));
    }

    private static List<String> codigos(Collection<Paquete> ps) {
        List<String> r = new ArrayList<>();
        for (Paquete p : ps) {
            r.add(p.getCodigo());
        }
        return r;
    }

    private static void titulo(String t) {
        System.out.println("\n=== " + t + " ===");
    }

    /** Intenta registrar un paquete e imprime el motivo si es rechazado. */
    private static void intentarRegistrarPaquete(CentroDistribucion centro, String codigo, String destino,
                                                 double peso, int prioridad, int tiempo) {
        try {
            centro.registrarPaquete(new Paquete(codigo, destino, peso, prioridad, tiempo));
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }
    }
}
