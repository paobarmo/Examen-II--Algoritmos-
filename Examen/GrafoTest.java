import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Requerimientos del grafo:
 *  R6. Cargar el grafo de tramos.
 *  R7. Escoger parada de salida y objetivo y responder con la ruta más corta (Dijkstra).
 */
public class GrafoTest {
    private Grafo grafo;

    @BeforeEach
    void setUp() throws Exception {
        grafo = DatosPrueba.grafoCargado();
    }

    private int i(String parada) {
        return grafo.indiceDe(parada);
    }

    private String[] nombres(int[] ruta) {
        String[] n = new String[ruta.length];
        for (int k = 0; k < ruta.length; k++) {
            n[k] = grafo.getParada(ruta[k]);
        }
        return n;
    }

    // ===================== R6. Carga del grafo =====================

    @Test
    void carga_valoresExactos() {
        assertEquals(8, grafo.getCantidad());
        assertEquals("Terminal", grafo.getParada(0));
        assertEquals("Hospital", grafo.getParada(7));
        assertEquals(6, grafo.getMinutos(i("Terminal"), i("Mercado")));
        assertEquals("Bus 1", grafo.getLinea(i("Terminal"), i("Mercado")));
        assertEquals(2, grafo.getMinutos(i("Parque"), i("Universidad")));
        assertEquals("Bus 4", grafo.getLinea(i("Parque"), i("Universidad")));
    }

    @Test
    void carga_busEsDirigido() {
        assertEquals(6, grafo.getMinutos(i("Terminal"), i("Mercado")));
        assertEquals(Grafo.SIN_ARISTA, grafo.getMinutos(i("Mercado"), i("Terminal")));
    }

    @Test
    void carga_caminataEnAmbosSentidos() {
        assertEquals(10, grafo.getMinutos(i("Mercado"), i("Parque")));
        assertEquals(10, grafo.getMinutos(i("Parque"), i("Mercado")));
        assertEquals("Caminata (ambos sentidos)", grafo.getLinea(i("Parque"), i("Mercado")));
    }

    @Test
    void carga_limite_aristaDuplicadaConservaLaMenor() {
        grafo.agregarArista("Terminal", "Mercado", 20, "Bus 9");
        assertEquals(6, grafo.getMinutos(i("Terminal"), i("Mercado")));
        assertEquals("Bus 1", grafo.getLinea(i("Terminal"), i("Mercado")));
        grafo.agregarArista("Terminal", "Mercado", 3, "Bus 9");
        assertEquals(3, grafo.getMinutos(i("Terminal"), i("Mercado")));
        assertEquals("Bus 9", grafo.getLinea(i("Terminal"), i("Mercado")));
    }

    @Test
    void carga_limite_verticeRepetidoNoSeDuplica() {
        assertEquals(i("Faro"), grafo.agregarVertice("faro"));
        assertEquals(8, grafo.getCantidad());
    }

    @Test
    void carga_limite_soloEncabezado() throws Exception {
        Grafo g = new Grafo(10);
        g.cargarCsv(DatosPrueba.archivo("linea,desde,hacia,minutos\n"));
        assertEquals(0, g.getCantidad());
    }

    @Test
    void carga_invalida_minutosNoNumericos() throws Exception {
        String ruta = DatosPrueba.archivo("linea,desde,hacia,minutos\nBus 1,Terminal,Mercado,seis\n");
        Grafo g = new Grafo(10);
        assertThrows(NumberFormatException.class, () -> g.cargarCsv(ruta));
    }

    @Test
    void carga_invalida_faltanColumnas() throws Exception {
        String ruta = DatosPrueba.archivo("linea,desde,hacia,minutos\nBus 1,Terminal\n");
        Grafo g = new Grafo(10);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> g.cargarCsv(ruta));
    }

    @Test
    void carga_invalida_archivoInexistente() {
        Grafo g = new Grafo(10);
        assertThrows(FileNotFoundException.class, () -> g.cargarCsv("no_existe_tramos.csv"));
    }

    @Test
    void carga_invalida_capacidadExcedida() throws Exception {
        Grafo g = new Grafo(2);
        String ruta = DatosPrueba.archivo(DatosPrueba.TRAMOS_CSV);
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> g.cargarCsv(ruta));
    }

    // ===================== R7. Ruta más corta (Dijkstra) =====================

    @Test
    void dijkstra_terminalAUniversidad_valorExacto() {
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Universidad"));
        assertEquals(11, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Terminal", "Estadio", "Parque", "Universidad"}, nombres(ruta));
    }

    @Test
    void dijkstra_terminalAFaro_valorExacto() {
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Faro"));
        assertEquals(15, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Terminal", "Estadio", "Parque", "Faro"}, nombres(ruta));
    }

    @Test
    void dijkstra_terminalAPlaya_valorExacto() {
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Playa"));
        assertEquals(20, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Terminal", "Estadio", "Parque", "Faro", "Playa"}, nombres(ruta));
    }

    @Test
    void dijkstra_terminalAHospital_valorExacto() {
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Hospital"));
        assertEquals(10, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Terminal", "Mercado", "Hospital"}, nombres(ruta));
    }

    @Test
    void dijkstra_sentidoContrarioEsDistinto() {
        // Mercado -> Terminal no existe directo: hay que dar la vuelta
        int[] ruta = grafo.dijkstra(i("Universidad"), i("Mercado"));
        assertEquals(32, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Universidad", "Faro", "Playa", "Terminal", "Mercado"}, nombres(ruta));
    }

    @Test
    void dijkstra_usaCaminata() {
        int[] ruta = grafo.dijkstra(i("Parque"), i("Hospital"));
        assertEquals(14, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Parque", "Mercado", "Hospital"}, nombres(ruta));
    }

    @Test
    void dijkstra_textoExactoDeLaApp() {
        String esperado = "Ruta más corta de Terminal a Universidad:\n"
                + "  Terminal -> Estadio (Bus 2, 5 min)\n"
                + "  Estadio -> Parque (Bus 2, 4 min)\n"
                + "  Parque -> Universidad (Bus 4, 2 min)\n"
                + "Tiempo total: 11 minutos\n";
        assertEquals(esperado, AppRutas.describirRuta(grafo, i("Terminal"), i("Universidad")));
    }

    @Test
    void dijkstra_limite_origenIgualAlDestino() {
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Terminal"));
        assertEquals(1, ruta.length);
        assertEquals(0, grafo.minutosDeRuta(ruta));
        assertEquals("Ruta más corta de Terminal a Terminal:\nTiempo total: 0 minutos\n",
                AppRutas.describirRuta(grafo, i("Terminal"), i("Terminal")));
    }

    @Test
    void dijkstra_limite_empateMercadoAUniversidad() {
        // Directo (7) o por Hospital (4 + 3 = 7): se conserva el primero encontrado
        int[] ruta = grafo.dijkstra(i("Mercado"), i("Universidad"));
        assertEquals(7, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Mercado", "Universidad"}, nombres(ruta));
    }

    @Test
    void dijkstra_limite_empateMercadoAFaro() {
        // Por Universidad (7 + 9) o por Parque (10 + 6): ambos 16
        int[] ruta = grafo.dijkstra(i("Mercado"), i("Faro"));
        assertEquals(16, grafo.minutosDeRuta(ruta));
        assertArrayEquals(new String[] {"Mercado", "Universidad", "Faro"}, nombres(ruta));
    }

    @Test
    void dijkstra_limite_tramoDirectoUnico() {
        int[] ruta = grafo.dijkstra(i("Playa"), i("Terminal"));
        assertEquals(12, grafo.minutosDeRuta(ruta));
        assertEquals(2, ruta.length);
    }

    @Test
    void dijkstra_limite_sinRuta() {
        Grafo g = new Grafo(5);
        g.agregarArista("A", "B", 3, "Bus X");
        int[] ruta = g.dijkstra(g.indiceDe("B"), g.indiceDe("A"));
        assertEquals(0, ruta.length);
        assertEquals("No existe ruta de B a A.\n", AppRutas.describirRuta(g, g.indiceDe("B"), g.indiceDe("A")));
    }

    @Test
    void dijkstra_limite_verticeAislado() {
        grafo.agregarVertice("Aeropuerto");
        int[] ruta = grafo.dijkstra(i("Terminal"), i("Aeropuerto"));
        assertEquals(0, ruta.length);
        assertEquals(0, grafo.minutosDeRuta(ruta));
    }

    @Test
    void dijkstra_invalida_paradaInexistente() {
        assertEquals(-1, grafo.indiceDe("Aeropuerto"));
        assertThrows(ArrayIndexOutOfBoundsException.class,
                () -> grafo.dijkstra(grafo.indiceDe("Aeropuerto"), i("Terminal")));
    }

    @Test
    void dijkstra_invalida_destinoFueraDeRango() {
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> grafo.dijkstra(i("Terminal"), 99));
    }

    @Test
    void dijkstra_invalida_nombreNuloNoSeEncuentra() {
        assertEquals(-1, grafo.indiceDe(null));
    }
}
