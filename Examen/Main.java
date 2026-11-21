import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

public class Main {
    public static void main(String[] args) throws IOException {
        String ruta = args.length > 0 ? args[0] : "abordajes.csv";
        ArbolCiudad arbol = new ArbolCiudad();
        arbol.cargarCsv(ruta);

        BufferedReader entrada = new BufferedReader(new InputStreamReader(System.in));
        String opcion = "";
        while (!opcion.equals("0")) {
            System.out.println("\n=== Oficina de transporte ===");
            System.out.println("1. Agregar parada");
            System.out.println("2. Listar abordajes de cada parada");
            System.out.println("3. Listar paradas de una zona");
            System.out.println("4. Ordenar abordajes (mayor a menor, counting sort)");
            System.out.println("0. Salir");
            System.out.print("Opción: ");
            opcion = entrada.readLine();
            if (opcion == null) {
                break;
            }
            opcion = opcion.trim();

            if (opcion.equals("1")) {
                System.out.print("Código: ");
                String codigo = entrada.readLine().trim();
                System.out.print("Nombre de la parada: ");
                String nombre = entrada.readLine().trim();
                System.out.print("Zona: ");
                String zona = entrada.readLine().trim();
                System.out.print("Abordajes: ");
                int abordajes = Integer.parseInt(entrada.readLine().trim());
                if (arbol.agregarParada(codigo, nombre, zona, abordajes)) {
                    System.out.println("Parada agregada.");
                } else {
                    System.out.println("No se pudo agregar la parada.");
                }
            } else if (opcion.equals("2")) {
                System.out.print(arbol.listarAbordajes());
            } else if (opcion.equals("3")) {
                System.out.print("Zona: ");
                String zona = entrada.readLine().trim();
                System.out.print(arbol.listarParadasDeZona(zona));
            } else if (opcion.equals("4")) {
                Nodo[] ordenadas = CountingSort.ordenarDescendente(arbol.obtenerParadas());
                for (int i = 0; i < ordenadas.length; i++) {
                    System.out.println((i + 1) + ". " + ordenadas[i].getNombre()
                            + ": " + ordenadas[i].getAbordajes());
                }
            }
        }
    }
}
