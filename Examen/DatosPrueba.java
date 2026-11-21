import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

/**
 * Datos de prueba idénticos a abordajes.csv y tramos.csv, escritos en archivos
 * temporales para que las pruebas no dependan del directorio de ejecución.
 */
public class DatosPrueba {

    public static final String ABORDAJES_CSV =
            "codigo,parada,zona,abordajes\n"
            + "S01,Terminal,Centro,1520\n"
            + "S02,Mercado,Centro,980\n"
            + "S03,Universidad,Norte,1310\n"
            + "S04,Faro,Costa,640\n"
            + "S05,Estadio,Norte,980\n"
            + "S06,Parque,Norte,455\n"
            + "S07,Playa,Costa,640\n"
            + "S08,Hospital,Centro,1310\n";

    public static final String TRAMOS_CSV =
            "linea,desde,hacia,minutos\n"
            + "Bus 1,Terminal,Mercado,6\n"
            + "Bus 1,Mercado,Universidad,7\n"
            + "Bus 1,Universidad,Faro,9\n"
            + "Bus 2,Terminal,Estadio,5\n"
            + "Bus 2,Estadio,Parque,4\n"
            + "Bus 2,Parque,Faro,6\n"
            + "Bus 2,Faro,Playa,5\n"
            + "Bus 2,Playa,Terminal,12\n"
            + "Bus 3,Mercado,Hospital,4\n"
            + "Bus 3,Hospital,Universidad,3\n"
            + "Bus 3,Universidad,Playa,15\n"
            + "Bus 4,Parque,Universidad,2\n"
            + "Caminata (ambos sentidos),Mercado,Parque,10\n";

    /** Escribe el contenido en un archivo temporal y devuelve su ruta. */
    public static String archivo(String contenido) throws IOException {
        Path p = Files.createTempFile("datos", ".csv");
        Files.write(p, contenido.getBytes(StandardCharsets.UTF_8));
        p.toFile().deleteOnExit();
        return p.toString();
    }

    public static ArbolCiudad arbolCargado() throws IOException {
        ArbolCiudad arbol = new ArbolCiudad();
        arbol.cargarCsv(archivo(ABORDAJES_CSV));
        return arbol;
    }

    public static Grafo grafoCargado() throws IOException {
        Grafo grafo = new Grafo(50);
        grafo.cargarCsv(archivo(TRAMOS_CSV));
        return grafo;
    }
}
