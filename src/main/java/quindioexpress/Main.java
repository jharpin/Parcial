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
        intentar(() -> centro.registrarPaquete(new Paquete("PQ731", "Armenia", 1.0, 3, 10)));
        intentar(() -> centro.registrarPaquete(new Paquete("PQ001", "Armenia", 0, 3, 10)));
        intentar(() -> centro.registrarPaquete(new Paquete("PQ002", "Armenia", 2.0, 8, 10)));
        intentar(() -> centro.registrarPaquete(new Paquete("PQ003", "", 2.0, 3, 10)));
        intentar(() -> centro.registrarPaquete(new Paquete("PQ004", "Salento", 2.0, 3, 0)));
        intentar(() -> centro.registrarRepartidor(new Repartidor("R01", "Otro", "Armenia", true)));

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
        for (String m : centro.getMunicipiosOrdenados())
            System.out.println(m + " -> " + codigos(centro.paquetesDe(m)));

        titulo("6. Orden natural (Comparable) y Comparator");
        System.out.println("Natural (código):  " + codigos(centro.ordenados(null)));
        System.out.println("Prioridad desc:    " + codigos(centro.ordenados(ComparadoresPaquete.POR_PRIORIDAD_DESC)));
        System.out.println("Peso desc:         " + codigos(centro.ordenados(ComparadoresPaquete.POR_PESO_DESC)));
        System.out.println("Tiempo asc:        " + codigos(centro.ordenados(ComparadoresPaquete.POR_TIEMPO_ASC)));
        System.out.println("Registro intacto:  " + codigos(centro.getRegistro().subList(0, 5)) + " ...");

        titulo("7. Ejemplo de despacho del enunciado (PriorityQueue)");
        PriorityQueue<Paquete> ejemplo = new PriorityQueue<>(ComparadoresPaquete.DESPACHO);
        ejemplo.add(new Paquete("PQ420", "Armenia", 1, 3, 30));
        ejemplo.add(new Paquete("PQ731", "Armenia", 1, 5, 45));
        ejemplo.add(new Paquete("PQ105", "Armenia", 1, 5, 25));
        ejemplo.add(new Paquete("PQ942", "Armenia", 1, 4, 20));
        StringBuilder orden = new StringBuilder();
        while (!ejemplo.isEmpty()) orden.append(ejemplo.poll().getCodigo()).append(" ");
        System.out.println("Esperado PQ105 PQ731 PQ942 PQ420 -> " + orden.toString().trim());

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
        intentar(() -> centro.registrarEntrega("PQ731"));

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
        for (Paquete p : centro.getHistorial()) System.out.println((n++) + ". " + p.getCodigo());
        System.out.println("Total entregados: " + centro.cantidadEntregados());

        titulo("11. Casos borde de la lista propia (genérica, con String)");
        ColeccionPropia<String> lista = new ListaSimplementeEnlazada<>();
        System.out.println("Vacía: " + lista.estaVacia() + ", eliminar 'X' en vacía: " + lista.eliminar("X"));
        lista.agregar("A");
        System.out.println("Un elemento " + lista + " -> eliminar A: " + lista.eliminar("A") + " " + lista);
        lista.agregar("A"); lista.agregar("B"); lista.agregar("C"); lista.agregar("D");
        System.out.println("Lista " + lista + ", obtener(2) = " + lista.obtener(2));
        lista.eliminar("A"); System.out.println("Eliminar primero A:     " + lista);
        lista.eliminar("C"); System.out.println("Eliminar intermedio C:  " + lista);
        lista.eliminar("D"); System.out.println("Eliminar último D:      " + lista);
        lista.agregar("E");  System.out.println("Agregar E (cola ok):    " + lista);
        System.out.println("Eliminar inexistente Z: " + lista.eliminar("Z") + ", tamaño = " + lista.tamanio());
        intentar(() -> lista.obtener(5));

        titulo("12. Recursividad");
        List<Paquete> ejemploPeso = List.of(
                new Paquete("PQ101", "Armenia", 4.5, 3, 10), new Paquete("PQ203", "Salento", 3.0, 3, 10),
                new Paquete("PQ415", "Armenia", 7.2, 3, 10), new Paquete("PQ730", "Armenia", 2.3, 3, 10));
        System.out.printf("Ejemplo enunciado, peso Armenia = %.1f kg (esperado 14.0)%n",
                AlgoritmosRecursivos.pesoTotalPorMunicipio(ejemploPeso, "Armenia"));
        System.out.println("Ejemplo enunciado, prioridad >= 4 en {5,2,4,1,5,3,4} = "
                + AlgoritmosRecursivos.contarPrioridadMinima(new int[]{5, 2, 4, 1, 5, 3, 4}, 4, 0) + " (esperado 4)");
        for (String m : centro.getMunicipiosOrdenados())
            System.out.printf("Peso total %s = %.1f kg%n", m,
                    AlgoritmosRecursivos.pesoTotalPorMunicipio(centro.getRegistro(), m));
        System.out.println("Paquetes con prioridad >= 4: "
                + AlgoritmosRecursivos.contarPrioridadMinima(centro.getRegistro(), 4));

        titulo("13. Búsqueda binaria (precondición: ordenado por código)");
        List<Paquete> porCodigo = centro.ordenados(null);
        List<String> codigosOrdenados = new ArrayList<>();
        for (Paquete p : porCodigo) codigosOrdenados.add(p.getCodigo());
        System.out.println("Buscar PQ731:");
        int pos = BusquedaBinaria.buscar(codigosOrdenados, "PQ731", true);
        System.out.println("   posición " + pos + " -> " + porCodigo.get(pos));
        System.out.println("Buscar PQ999: posición " + BusquedaBinaria.buscar(codigosOrdenados, "PQ999", false));
        List<String> desordenados = new ArrayList<>();
        for (Paquete p : centro.getRegistro()) desordenados.add(p.getCodigo());
        System.out.println("Sin cumplir la precondición (orden de registro), buscar PQ731: posición "
                + BusquedaBinaria.buscar(desordenados, "PQ731", false) + " (resultado no confiable)");

        titulo("14. Divide y vencerás: paquete de mayor peso");
        List<Paquete> ejemploDyV = new ArrayList<>();
        double[] pesos = {4.5, 12.0, 3.2, 18.7, 7.4, 5.1};
        for (int i = 0; i < pesos.length; i++) ejemploDyV.add(new Paquete("E" + i, "Armenia", pesos[i], 3, 10));
        Paquete max = DivideYVenceras.primero(ejemploDyV, ComparadoresPaquete.POR_PESO_DESC, true);
        System.out.println("Resultado final: " + max.getPeso() + " kg (esperado 18.7)");
        Paquete maxJornada = DivideYVenceras.primero(centro.getRegistro(), ComparadoresPaquete.POR_PESO_DESC, false);
        System.out.println("Mayor peso de la jornada: " + maxJornada);
    }

    /** 30 paquetes, 7 destinos, prioridades 1-5, códigos no consecutivos; 8 repartidores. */
    private static void cargarDatos(CentroDistribucion c) {
        Object[][] paquetes = {
                {"PQ731", "Armenia", 7.2, 5, 45}, {"PQ105", "Salento", 3.0, 5, 25},
                {"PQ942", "Armenia", 18.7, 4, 20}, {"PQ318", "Calarcá", 4.5, 2, 35},
                {"PQ567", "Salento", 2.1, 1, 50}, {"PQ829", "Armenia", 2.3, 3, 15},
                {"PQ420", "Montenegro", 12.0, 3, 30}, {"PQ214", "Quimbaya", 5.1, 4, 40},
                {"PQ421", "Circasia", 6.6, 2, 55}, {"PQ842", "Filandia", 9.8, 5, 60},
                {"PQ101", "Armenia", 4.5, 1, 10}, {"PQ203", "Salento", 3.2, 3, 28},
                {"PQ415", "Calarcá", 7.4, 4, 22}, {"PQ730", "Montenegro", 1.8, 2, 18},
                {"PQ356", "Quimbaya", 11.3, 5, 33}, {"PQ689", "Circasia", 0.9, 3, 12},
                {"PQ477", "Filandia", 3.6, 4, 27}, {"PQ512", "Armenia", 8.4, 2, 38},
                {"PQ298", "Calarcá", 6.0, 5, 42}, {"PQ863", "Salento", 2.7, 4, 19},
                {"PQ154", "Montenegro", 14.2, 1, 65}, {"PQ905", "Quimbaya", 3.3, 3, 24},
                {"PQ640", "Circasia", 5.5, 5, 31}, {"PQ377", "Filandia", 7.7, 2, 48},
                {"PQ218", "Armenia", 1.2, 4, 14}, {"PQ791", "Calarcá", 10.1, 3, 52},
                {"PQ436", "Salento", 4.9, 2, 36}, {"PQ608", "Montenegro", 6.8, 5, 29},
                {"PQ983", "Quimbaya", 2.4, 1, 21}, {"PQ259", "Circasia", 8.9, 4, 44},
        };
        for (Object[] f : paquetes)
            c.registrarPaquete(new Paquete((String) f[0], (String) f[1], (double) f[2], (int) f[3], (int) f[4]));

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
        for (Paquete p : ps) r.add(p.getCodigo());
        return r;
    }

    private static void titulo(String t) { System.out.println("\n=== " + t + " ==="); }

    private static void intentar(Runnable accion) {
        try {
            accion.run();
        } catch (RuntimeException e) {
            System.out.println("Rechazado: " + e.getMessage());
        }
    }
}
