import static org.junit.jupiter.api.Assertions.*;

import java.io.FileNotFoundException;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/**
 * Requerimientos del árbol n-ario de cuarto grado:
 *  R1. Cargar los datos (Ciudad -> Zonas -> Paradas, la Zona guarda los pasajeros del día).
 *  R2. Agregar paradas.
 *  R3. Listar el total de abordajes de cada parada desde el árbol.
 *  R4. Listar las paradas de una zona.
 */
public class ArbolCiudadTest {
    private ArbolCiudad arbol;

    @BeforeEach
    void setUp() throws Exception {
        arbol = DatosPrueba.arbolCargado();
    }

    // ===================== R1. Carga de datos =====================

    @Test
    void carga_valoresExactosDeLaEstructura() {
        Nodo raiz = arbol.getRaiz();
        assertEquals("Ciudad", raiz.getTipo());
        assertEquals("Ciudad", raiz.getNombre());
        assertEquals(3, raiz.getCantidadHijos());
        assertEquals("Centro", raiz.getHijo(0).getNombre());
        assertEquals("Costa", raiz.getHijo(2).getNombre());
        assertEquals("Norte", raiz.getHijo(1).getNombre());
        assertEquals("Zona", raiz.getHijo(0).getTipo());
        assertEquals(8, arbol.contarParadas());
    }

    @Test
    void carga_pasajerosDelDiaPorZonaYCiudad() {
        assertEquals(3810, arbol.buscarZona("Centro").getAbordajes());
        assertEquals(2745, arbol.buscarZona("Norte").getAbordajes());
        assertEquals(1280, arbol.buscarZona("Costa").getAbordajes());
        assertEquals(7835, arbol.getRaiz().getAbordajes());
    }

    @Test
    void carga_paradaGuardaCodigoNombreYAbordajes() {
        Nodo p = arbol.buscarZona("Centro").getHijo(2);
        assertEquals("Parada", p.getTipo());
        assertEquals("S08", p.getCodigo());
        assertEquals("Hospital", p.getNombre());
        assertEquals(1310, p.getAbordajes());
        assertEquals(0, p.getCantidadHijos());
    }

    @Test
    void carga_limite_soloEncabezadoDejaArbolVacio() throws Exception {
        ArbolCiudad vacio = new ArbolCiudad();
        vacio.cargarCsv(DatosPrueba.archivo("codigo,parada,zona,abordajes\n"));
        assertEquals(0, vacio.getRaiz().getCantidadHijos());
        assertEquals(0, vacio.contarParadas());
        assertEquals(0, vacio.getRaiz().getAbordajes());
    }

    @Test
    void carga_limite_lineasEnBlancoSeIgnoran() throws Exception {
        ArbolCiudad a = new ArbolCiudad();
        a.cargarCsv(DatosPrueba.archivo("codigo,parada,zona,abordajes\n\n S01 , Terminal , Centro , 1520 \n\n"));
        assertEquals(1, a.contarParadas());
        assertEquals("Terminal", a.buscarZona("Centro").getHijo(0).getNombre());
        assertEquals(1520, a.buscarZona("Centro").getAbordajes());
    }

    @Test
    void carga_invalida_abordajesNoNumerico() throws Exception {
        String ruta = DatosPrueba.archivo("codigo,parada,zona,abordajes\nS01,Terminal,Centro,mil\n");
        ArbolCiudad a = new ArbolCiudad();
        assertThrows(NumberFormatException.class, () -> a.cargarCsv(ruta));
        assertEquals(0, a.contarParadas());
    }

    @Test
    void carga_invalida_faltanColumnas() throws Exception {
        String ruta = DatosPrueba.archivo("codigo,parada,zona,abordajes\nS01,Terminal\n");
        ArbolCiudad a = new ArbolCiudad();
        assertThrows(ArrayIndexOutOfBoundsException.class, () -> a.cargarCsv(ruta));
    }

    @Test
    void carga_invalida_lineaMalaDejaCargadasLasAnteriores() throws Exception {
        String ruta = DatosPrueba.archivo(
                "codigo,parada,zona,abordajes\nS01,Terminal,Centro,1520\nS02,Mercado,Centro,x\n");
        ArbolCiudad a = new ArbolCiudad();
        assertThrows(NumberFormatException.class, () -> a.cargarCsv(ruta));
        assertEquals(1, a.contarParadas());
        assertEquals(1520, a.getRaiz().getAbordajes());
    }

    @Test
    void carga_invalida_archivoInexistente() {
        ArbolCiudad a = new ArbolCiudad();
        assertThrows(FileNotFoundException.class, () -> a.cargarCsv("no_existe_abordajes.csv"));
    }

    // ===================== R2. Agregar paradas =====================

    @Test
    void agregar_valorExacto() {
        assertTrue(arbol.agregarParada("S09", "Muelle", "Costa", 300));
        Nodo costa = arbol.buscarZona("Costa");
        assertEquals(3, costa.getCantidadHijos());
        assertEquals("S09", costa.getHijo(2).getCodigo());
        assertEquals("Muelle", costa.getHijo(2).getNombre());
        assertEquals(300, costa.getHijo(2).getAbordajes());
        assertEquals(1580, costa.getAbordajes());
        assertEquals(8135, arbol.getRaiz().getAbordajes());
        assertEquals(9, arbol.contarParadas());
    }

    @Test
    void agregar_zonaSinImportarMayusculas() {
        assertTrue(arbol.agregarParada("S09", "Muelle", "costa", 300));
        assertEquals(3, arbol.getRaiz().getCantidadHijos());
        assertEquals(3, arbol.buscarZona("Costa").getCantidadHijos());
    }

    @Test
    void agregar_limite_cuartaParadaLlenaLaZona() {
        assertTrue(arbol.agregarParada("S09", "Catedral", "Centro", 100));
        assertEquals(4, arbol.buscarZona("Centro").getCantidadHijos());
        assertEquals(3910, arbol.buscarZona("Centro").getAbordajes());
    }

    @Test
    void agregar_limite_quintaParadaEnZonaLlenaFalla() {
        assertTrue(arbol.agregarParada("S09", "Catedral", "Centro", 100));
        assertFalse(arbol.agregarParada("S10", "Correo", "Centro", 200));
        assertEquals(4, arbol.buscarZona("Centro").getCantidadHijos());
        assertEquals(3910, arbol.buscarZona("Centro").getAbordajes());
        assertEquals(7935, arbol.getRaiz().getAbordajes());
        assertEquals(9, arbol.contarParadas());
    }

    @Test
    void agregar_limite_zonaNuevaSeCreaComoCuartaZona() {
        assertTrue(arbol.agregarParada("S09", "Aeropuerto", "Sur", 700));
        assertEquals(4, arbol.getRaiz().getCantidadHijos());
        assertEquals("Sur", arbol.getRaiz().getHijo(3).getNombre());
        assertEquals(700, arbol.buscarZona("Sur").getAbordajes());
    }

    @Test
    void agregar_limite_quintaZonaFalla() {
        assertTrue(arbol.agregarParada("S09", "Aeropuerto", "Sur", 700));
        assertFalse(arbol.agregarParada("S10", "Bosque", "Oeste", 50));
        assertEquals(4, arbol.getRaiz().getCantidadHijos());
        assertNull(arbol.buscarZona("Oeste"));
        assertEquals(9, arbol.contarParadas());
    }

    @Test
    void agregar_limite_ceroAbordajes() {
        assertTrue(arbol.agregarParada("S09", "Muelle", "Costa", 0));
        assertEquals(1280, arbol.buscarZona("Costa").getAbordajes());
        assertEquals(0, arbol.buscarZona("Costa").getHijo(2).getAbordajes());
    }

    @Test
    void agregar_limite_arbolVacio() {
        ArbolCiudad vacio = new ArbolCiudad();
        assertTrue(vacio.agregarParada("S01", "Terminal", "Centro", 1520));
        assertEquals(1, vacio.getRaiz().getCantidadHijos());
        assertEquals(1, vacio.contarParadas());
        assertEquals(1520, vacio.getRaiz().getAbordajes());
    }

    @Test
    void agregar_invalida_codigoDuplicadoSeAgregaIgual() {
        assertTrue(arbol.agregarParada("S01", "Terminal", "Centro", 1520));
        assertEquals(9, arbol.contarParadas());
        assertEquals(5330, arbol.buscarZona("Centro").getAbordajes());
    }

    @Test
    void agregar_invalida_abordajesNegativosRestanDelTotal() {
        assertTrue(arbol.agregarParada("S09", "Muelle", "Costa", -80));
        assertEquals(1200, arbol.buscarZona("Costa").getAbordajes());
        assertEquals(7755, arbol.getRaiz().getAbordajes());
    }

    // ============ R3. Listar el total de abordajes de cada parada ============

    @Test
    void listarAbordajes_textoExacto() {
        String esperado =
                "S01 - Terminal (Centro): 1520 abordajes\n"
                + "S02 - Mercado (Centro): 980 abordajes\n"
                + "S08 - Hospital (Centro): 1310 abordajes\n"
                + "S03 - Universidad (Norte): 1310 abordajes\n"
                + "S05 - Estadio (Norte): 980 abordajes\n"
                + "S06 - Parque (Norte): 455 abordajes\n"
                + "S04 - Faro (Costa): 640 abordajes\n"
                + "S07 - Playa (Costa): 640 abordajes\n";
        assertEquals(esperado, arbol.listarAbordajes());
    }

    @Test
    void listarAbordajes_incluyeParadaAgregada() {
        arbol.agregarParada("S09", "Muelle", "Costa", 300);
        assertTrue(arbol.listarAbordajes().endsWith("S09 - Muelle (Costa): 300 abordajes\n"));
        assertEquals(9, arbol.listarAbordajes().split("\n").length);
    }

    @Test
    void listarAbordajes_limite_arbolVacioDevuelveCadenaVacia() {
        assertEquals("", new ArbolCiudad().listarAbordajes());
    }

    @Test
    void listarAbordajes_limite_zonaVaciaNoAgregaLineas() {
        arbol.getRaiz().agregarHijo(new Nodo("Zona", "", "Sur", 0));
        assertEquals(8, arbol.listarAbordajes().split("\n").length);
        assertFalse(arbol.listarAbordajes().contains("Sur"));
    }

    @Test
    void listarAbordajes_limite_empatesSeListanAmbos() {
        String texto = arbol.listarAbordajes();
        assertTrue(texto.contains("S04 - Faro (Costa): 640 abordajes\n"));
        assertTrue(texto.contains("S07 - Playa (Costa): 640 abordajes\n"));
    }

    @Test
    void listarAbordajes_invalida_paradaRechazadaNoAparece() {
        arbol.agregarParada("S09", "Catedral", "Centro", 100);
        arbol.agregarParada("S10", "Correo", "Centro", 200);
        assertFalse(arbol.listarAbordajes().contains("Correo"));
    }

    // ===================== R4. Listar paradas de una zona =====================

    @Test
    void listarZona_textoExactoNorte() {
        String esperado = "Zona Norte (pasajeros del día: 2745)\n"
                + "  S03 - Universidad\n"
                + "  S05 - Estadio\n"
                + "  S06 - Parque\n";
        assertEquals(esperado, arbol.listarParadasDeZona("Norte"));
    }

    @Test
    void listarZona_textoExactoCosta() {
        String esperado = "Zona Costa (pasajeros del día: 1280)\n"
                + "  S04 - Faro\n"
                + "  S07 - Playa\n";
        assertEquals(esperado, arbol.listarParadasDeZona("Costa"));
    }

    @Test
    void listarZona_sinImportarMayusculas() {
        assertEquals(arbol.listarParadasDeZona("Centro"), arbol.listarParadasDeZona("cENTRO"));
    }

    @Test
    void listarZona_limite_zonaVacia() {
        arbol.getRaiz().agregarHijo(new Nodo("Zona", "", "Sur", 0));
        assertEquals("Zona Sur (pasajeros del día: 0)\n", arbol.listarParadasDeZona("Sur"));
    }

    @Test
    void listarZona_limite_zonaLlenaConCuatroParadas() {
        arbol.agregarParada("S09", "Catedral", "Centro", 100);
        String esperado = "Zona Centro (pasajeros del día: 3910)\n"
                + "  S01 - Terminal\n"
                + "  S02 - Mercado\n"
                + "  S08 - Hospital\n"
                + "  S09 - Catedral\n";
        assertEquals(esperado, arbol.listarParadasDeZona("Centro"));
    }

    @Test
    void listarZona_invalida_zonaInexistente() {
        assertEquals("La zona Sur no existe.\n", arbol.listarParadasDeZona("Sur"));
        assertNull(arbol.buscarZona("Sur"));
    }

    @Test
    void listarZona_invalida_cadenaVacia() {
        assertEquals("La zona  no existe.\n", arbol.listarParadasDeZona(""));
    }

    @Test
    void listarZona_invalida_nulo() {
        assertEquals("La zona null no existe.\n", arbol.listarParadasDeZona(null));
    }
}
