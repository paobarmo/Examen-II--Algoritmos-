/**
 * Nodo de un árbol n-ario de cuarto grado (máximo 4 hijos por nodo).
 * Tipos: "Ciudad" (raíz), "Zona" y "Parada".
 */
public class Nodo {
    public static final int GRADO = 4;

    private String tipo;
    private String codigo;
    private String nombre;
    private int abordajes;
    private Nodo[] hijos;
    private int cantidadHijos;

    public Nodo(String tipo, String codigo, String nombre, int abordajes) {
        this.tipo = tipo;
        this.codigo = codigo;
        this.nombre = nombre;
        this.abordajes = abordajes;
        this.hijos = new Nodo[GRADO];
        this.cantidadHijos = 0;
    }

    /** Agrega un hijo. Devuelve false si el nodo ya tiene 4 hijos. */
    public boolean agregarHijo(Nodo hijo) {
        if (cantidadHijos >= GRADO) {
            return false;
        }
        hijos[cantidadHijos] = hijo;
        cantidadHijos++;
        return true;
    }

    public Nodo getHijo(int i) {
        return hijos[i];
    }

    public int getCantidadHijos() {
        return cantidadHijos;
    }

    public String getTipo() {
        return tipo;
    }

    public String getCodigo() {
        return codigo;
    }

    public String getNombre() {
        return nombre;
    }

    public int getAbordajes() {
        return abordajes;
    }

    public void sumarAbordajes(int cantidad) {
        this.abordajes += cantidad;
    }
}
