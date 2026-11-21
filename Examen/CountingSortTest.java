import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;

/**
 * Requerimiento R5: ordenar los abordajes de cada parada, de mayor a menor, con counting sort.
 */
public class CountingSortTest {

    private static Nodo p(String nombre, int abordajes) {
        return new Nodo("Parada", "", nombre, abordajes);
    }

    // ===================== Valores exactos =====================

    @Test
    void ordena_abordajesExactosDelArchivo() throws Exception {
        Nodo[] r = CountingSort.ordenarDescendente(DatosPrueba.arbolCargado().obtenerParadas());
        int[] esperado = {1520, 1310, 1310, 980, 980, 640, 640, 455};
        assertEquals(8, r.length);
        for (int i = 0; i < esperado.length; i++) {
            assertEquals(esperado[i], r[i].getAbordajes());
        }
    }

    @Test
    void ordena_nombresExactosDelArchivo() throws Exception {
        Nodo[] r = CountingSort.ordenarDescendente(DatosPrueba.arbolCargado().obtenerParadas());
        String[] esperado = {"Terminal", "Hospital", "Universidad", "Mercado",
                "Estadio", "Faro", "Playa", "Parque"};
        for (int i = 0; i < esperado.length; i++) {
            assertEquals(esperado[i], r[i].getNombre());
        }
    }

    @Test
    void ordena_incluyeParadaAgregada() throws Exception {
        ArbolCiudad a = DatosPrueba.arbolCargado();
        a.agregarParada("S09", "Muelle", "Costa", 2000);
        Nodo[] r = CountingSort.ordenarDescendente(a.obtenerParadas());
        assertEquals(9, r.length);
        assertEquals("Muelle", r[0].getNombre());
        assertEquals(2000, r[0].getAbordajes());
        assertEquals("Parque", r[8].getNombre());
    }

    @Test
    void ordena_noModificaElArregloOriginal() {
        Nodo[] original = {p("A", 1), p("B", 3), p("C", 2)};
        CountingSort.ordenarDescendente(original);
        assertEquals("A", original[0].getNombre());
        assertEquals("B", original[1].getNombre());
        assertEquals("C", original[2].getNombre());
    }

    // ===================== Casos límite =====================

    @Test
    void limite_empatesConservanOrdenDelArbol() throws Exception {
        Nodo[] r = CountingSort.ordenarDescendente(DatosPrueba.arbolCargado().obtenerParadas());
        // Hospital (Centro) y Universidad (Norte) = 1310; Centro va antes en el árbol
        assertEquals("Hospital", r[1].getNombre());
        assertEquals("Universidad", r[2].getNombre());
        // Mercado (Centro) y Estadio (Norte) = 980
        assertEquals("Mercado", r[3].getNombre());
        assertEquals("Estadio", r[4].getNombre());
        // Faro y Playa (Costa) = 640
        assertEquals("Faro", r[5].getNombre());
        assertEquals("Playa", r[6].getNombre());
    }

    @Test
    void limite_todosIguales() {
        Nodo[] r = CountingSort.ordenarDescendente(new Nodo[] {p("A", 5), p("B", 5), p("C", 5)});
        assertEquals("A", r[0].getNombre());
        assertEquals("B", r[1].getNombre());
        assertEquals("C", r[2].getNombre());
    }

    @Test
    void limite_arregloVacio() {
        assertEquals(0, CountingSort.ordenarDescendente(new Nodo[0]).length);
    }

    @Test
    void limite_arbolVacio() {
        assertEquals(0, CountingSort.ordenarDescendente(new ArbolCiudad().obtenerParadas()).length);
    }

    @Test
    void limite_unSoloElemento() {
        Nodo[] r = CountingSort.ordenarDescendente(new Nodo[] {p("A", 42)});
        assertEquals(1, r.length);
        assertEquals(42, r[0].getAbordajes());
    }

    @Test
    void limite_valoresCero() {
        Nodo[] r = CountingSort.ordenarDescendente(new Nodo[] {p("A", 0), p("B", 7), p("C", 0)});
        assertEquals("B", r[0].getNombre());
        assertEquals("A", r[1].getNombre());
        assertEquals("C", r[2].getNombre());
    }

    @Test
    void limite_yaOrdenadoAscendente() {
        Nodo[] r = CountingSort.ordenarDescendente(new Nodo[] {p("A", 1), p("B", 2), p("C", 3)});
        assertEquals(3, r[0].getAbordajes());
        assertEquals(2, r[1].getAbordajes());
        assertEquals(1, r[2].getAbordajes());
    }

    // ===================== Entradas inválidas =====================

    @Test
    void invalida_abordajesNegativosLanzanExcepcion() {
        Nodo[] datos = {p("A", 10), p("B", -5)};
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> CountingSort.ordenarDescendente(datos));
    }

    @Test
    void invalida_paradaNegativaAgregadaAlArbol() throws Exception {
        ArbolCiudad a = DatosPrueba.arbolCargado();
        a.agregarParada("S09", "Muelle", "Costa", -1);
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> CountingSort.ordenarDescendente(a.obtenerParadas()));
    }

    @Test
    void invalida_arregloNulo() {
        assertThrows(NullPointerException.class, () -> CountingSort.ordenarDescendente(null));
    }

    @Test
    void invalida_elementoNulo() {
        Nodo[] datos = {p("A", 1), null};
        assertThrows(NullPointerException.class, () -> CountingSort.ordenarDescendente(datos));
    }
}
