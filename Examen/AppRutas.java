import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class AppRutas {
    public static void main(String[] args) throws IOException {
        String ruta = args.length > 0 ? args[0] : "tramos.csv";
        Grafo grafo = new Grafo(50);
        grafo.cargarCsv(ruta);

        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n=== Ruta más corta (Dijkstra) ===");
            System.out.println("1. Buscar ruta");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = entrada.readLine();
            if (opcion == null) {
                break;
            }
            opcion = opcion.trim();

            if (opcion.equals("1")) {
                System.out.println("Paradas:");
                for (int i = 0; i < grafo.getCantidad(); i++) {
                    System.out.println("  " + (i + 1) + ". " + grafo.getParada(i));
                }
                System.out.print("Parada de salida (número): ");
                int origen = Integer.parseInt(entrada.readLine().trim()) - 1;
                System.out.print("Parada objetivo (número): ");
                int destino = Integer.parseInt(entrada.readLine().trim()) - 1;

                if (origen < 0 || origen >= grafo.getCantidad()
                        || destino < 0 || destino >= grafo.getCantidad()) {
                    System.out.println("Número de parada inválido.");
                    continue;
                }

                System.out.print(describirRuta(grafo, origen, destino));
            }
        }
    }

    public static String describirRuta(Grafo grafo, int origen, int destino) {
        int[] camino = grafo.dijkstra(origen, destino);
        if (camino.length == 0) {
            return "No existe ruta de " + grafo.getParada(origen) + " a "
                    + grafo.getParada(destino) + ".\n";
        }
        String texto = "Ruta más corta de " + grafo.getParada(origen) + " a "
                + grafo.getParada(destino) + ":\n";
        for (int i = 0; i < camino.length - 1; i++) {
            int a = camino[i];
            int b = camino[i + 1];
            texto += "  " + grafo.getParada(a) + " -> " + grafo.getParada(b)
                    + " (" + grafo.getLinea(a, b) + ", " + grafo.getMinutos(a, b) + " min)\n";
        }
        texto += "Tiempo total: " + grafo.minutosDeRuta(camino) + " minutos\n";
        return texto;
    }
}
