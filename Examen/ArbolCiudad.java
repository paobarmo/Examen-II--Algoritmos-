import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Árbol n-ario de cuarto grado:
 * Ciudad (raíz) -> Zonas -> Paradas.
 * Cada Zona guarda la cantidad de pasajeros que han subido en el día
 * (suma de los abordajes de sus paradas).
 */
public class ArbolCiudad {
    private Nodo raiz;

    public ArbolCiudad() {
        raiz = new Nodo("Ciudad", "", "Ciudad", 0);
    }

    public Nodo getRaiz() {
        return raiz;
    }

    /** Carga las paradas desde el archivo abordajes.csv (codigo,parada,zona,abordajes). */
    public void cargarCsv(String ruta) throws IOException {
        BufferedReader lector = new BufferedReader(new FileReader(ruta));
        String linea = lector.readLine(); // encabezado
        while ((linea = lector.readLine()) != null) {
            linea = linea.trim();
            if (linea.length() == 0) {
                continue;
            }
            String[] campos = linea.split(",");
            agregarParada(campos[0].trim(), campos[1].trim(), campos[2].trim(),
                    Integer.parseInt(campos[3].trim()));
        }
        lector.close();
    }

    /** Busca una zona por nombre. Devuelve null si no existe. */
    public Nodo buscarZona(String nombreZona) {
        for (int i = 0; i < raiz.getCantidadHijos(); i++) {
            Nodo zona = raiz.getHijo(i);
            if (zona.getNombre().equalsIgnoreCase(nombreZona)) {
                return zona;
            }
        }
        return null;
    }

    /**
     * Agrega una parada a la zona indicada. Si la zona no existe se crea
     * como hija de Ciudad. Devuelve false si no hay espacio en el nodo.
     */
    public boolean agregarParada(String codigo, String nombre, String nombreZona, int abordajes) {
        Nodo zona = buscarZona(nombreZona);
        if (zona == null) {
            zona = new Nodo("Zona", "", nombreZona, 0);
            if (!raiz.agregarHijo(zona)) {
                return false;
            }
        }
        Nodo parada = new Nodo("Parada", codigo, nombre, abordajes);
        if (!zona.agregarHijo(parada)) {
            return false;
        }
        zona.sumarAbordajes(abordajes);
        raiz.sumarAbordajes(abordajes);
        return true;
    }

    /** Lista el total de abordajes de cada parada recorriendo el árbol. */
    public String listarAbordajes() {
        String texto = "";
        for (int i = 0; i < raiz.getCantidadHijos(); i++) {
            Nodo zona = raiz.getHijo(i);
            for (int j = 0; j < zona.getCantidadHijos(); j++) {
                Nodo p = zona.getHijo(j);
                texto += p.getCodigo() + " - " + p.getNombre() + " (" + zona.getNombre()
                        + "): " + p.getAbordajes() + " abordajes\n";
            }
        }
        return texto;
    }

    /** Lista las paradas de una zona. */
    public String listarParadasDeZona(String nombreZona) {
        Nodo zona = buscarZona(nombreZona);
        if (zona == null) {
            return "La zona " + nombreZona + " no existe.\n";
        }
        String texto = "Zona " + zona.getNombre() + " (pasajeros del día: "
                + zona.getAbordajes() + ")\n";
        for (int j = 0; j < zona.getCantidadHijos(); j++) {
            Nodo p = zona.getHijo(j);
            texto += "  " + p.getCodigo() + " - " + p.getNombre() + "\n";
        }
        return texto;
    }

    /** Cuenta las paradas del árbol. */
    public int contarParadas() {
        int total = 0;
        for (int i = 0; i < raiz.getCantidadHijos(); i++) {
            total += raiz.getHijo(i).getCantidadHijos();
        }
        return total;
    }

    /** Devuelve todas las paradas del árbol en un arreglo. */
    public Nodo[] obtenerParadas() {
        Nodo[] paradas = new Nodo[contarParadas()];
        int k = 0;
        for (int i = 0; i < raiz.getCantidadHijos(); i++) {
            Nodo zona = raiz.getHijo(i);
            for (int j = 0; j < zona.getCantidadHijos(); j++) {
                paradas[k] = zona.getHijo(j);
                k++;
            }
        }
        return paradas;
    }
}
