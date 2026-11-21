package cr.ac.tec.examen2;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import org.junit.jupiter.api.Test;

class SmokeTest {

    @Test
    void entornoDePruebasFunciona() {
        assertEquals(4, 2 + 2);
    }

    @Test
    void existeArchivoDeDatos() {
        // Maven corre las pruebas desde la carpeta java/
        File archivo = new File("../datos/version-a/tramos.csv");
        assertTrue(archivo.isFile(), "No se encontró " + archivo.getAbsolutePath());
    }
}
