import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Grafo dirigido y ponderado (minutos) con matriz de adyacencia.
 * Los tramos de bus son dirigidos; la caminata "(ambos sentidos)" agrega arista en los dos sentidos.
 */
public class Grafo {
    public static final int SIN_ARISTA = -1;
    public static final int INFINITO = Integer.MAX_VALUE;

    private String[] paradas;
    private int[][] minutos;
    private String[][] lineas;
    private int cantidad;

    public Grafo(int capacidad) {
        paradas = new String[capacidad];
        minutos = new int[capacidad][capacidad];
        lineas = new String[capacidad][capacidad];
        for (int i = 0; i < capacidad; i++) {
            for (int j = 0; j < capacidad; j++) {
                minutos[i][j] = SIN_ARISTA;
            }
        }
        cantidad = 0;
    }

    /** Carga tramos.csv (linea,desde,hacia,minutos). */
    public void cargarCsv(String ruta) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(ruta));
        String linea = lector.readLine(); // encabezado
        while ((linea = lector.readLine()) != null) {
            linea = linea.trim();
            if (linea.length() == 0) {
                continue;
            }
            String[] c = linea.split(",");
            String nombreLinea = c[0].trim();
            String desde = c[1].trim();
            String hacia = c[2].trim();
            int mins = Integer.parseInt(c[3].trim());
            agregarArista(desde, hacia, mins, nombreLinea);
            if (nombreLinea.contains("ambos sentidos")) {
                agregarArista(hacia, desde, mins, nombreLinea);
            }
        }
        lector.close();
    }

    public int indiceDe(String nombre) {
        for (int i = 0; i < cantidad; i++) {
            if (paradas[i].equalsIgnoreCase(nombre)) {
                return i;
            }
        }
        return -1;
    }

    public int agregarVertice(String nombre) {
        int i = indiceDe(nombre);
        if (i != -1) {
            return i;
        }
        paradas[cantidad] = nombre;
        cantidad++;
        return cantidad - 1;
    }

    public void agregarArista(String desde, String hacia, int mins, String linea) {
        int a = agregarVertice(desde);
        int b = agregarVertice(hacia);
        if (minutos[a][b] == SIN_ARISTA || mins < minutos[a][b]) {
            minutos[a][b] = mins;
            lineas[a][b] = linea;
        }
    }

    public int getCantidad() {
        return cantidad;
    }

    public String getParada(int i) {
        return paradas[i];
    }

    public int getMinutos(int a, int b) {
        return minutos[a][b];
    }

    public String getLinea(int a, int b) {
        return lineas[a][b];
    }

    /**
     * Dijkstra desde origen hasta destino.
     * Devuelve el arreglo de índices de la ruta (origen ... destino),
     * o un arreglo vacío si no hay ruta.
     */
    public int[] dijkstra(int origen, int destino) {
        int[] distancia = new int[cantidad];
        int[] previo = new int[cantidad];
        boolean[] visitado = new boolean[cantidad];

        for (int i = 0; i < cantidad; i++) {
            distancia[i] = INFINITO;
            previo[i] = -1;
            visitado[i] = false;
        }
        distancia[origen] = 0;

        for (int k = 0; k < cantidad; k++) {
            // Elegir el vértice no visitado con menor distancia
            int u = -1;
            for (int i = 0; i < cantidad; i++) {
                if (!visitado[i] && distancia[i] != INFINITO
                        && (u == -1 || distancia[i] < distancia[u])) {
                    u = i;
                }
            }
            if (u == -1) {
                break;
            }
            visitado[u] = true;
            if (u == destino) {
                break;
            }
            // Relajar aristas
            for (int v = 0; v < cantidad; v++) {
                if (minutos[u][v] != SIN_ARISTA && !visitado[v]) {
                    int nueva = distancia[u] + minutos[u][v];
                    if (nueva < distancia[v]) {
                        distancia[v] = nueva;
                        previo[v] = u;
                    }
                }
            }
        }

        if (distancia[destino] == INFINITO) {
            return new int[0];
        }

        // Reconstruir la ruta
        int largo = 0;
        for (int v = destino; v != -1; v = previo[v]) {
            largo++;
        }
        int[] ruta = new int[largo];
        int pos = largo - 1;
        for (int v = destino; v != -1; v = previo[v]) {
            ruta[pos] = v;
            pos--;
        }
        return ruta;
    }

    /** Suma los minutos de una ruta. */
    public int minutosDeRuta(int[] ruta) {
        int total = 0;
        for (int i = 0; i < ruta.length - 1; i++) {
            total += minutos[ruta[i]][ruta[i + 1]];
        }
        return total;
    }
}
