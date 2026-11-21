/**
 * Ordena las paradas por abordajes de mayor a menor usando counting sort.
 */
public class CountingSort {

    public static Nodo[] ordenarDescendente(Nodo[] paradas) {
        int n = paradas.length;
        Nodo[] salida = new Nodo[n];
        if (n == 0) {
            return salida;
        }

        // 1. Buscar el valor máximo
        int max = paradas[0].getAbordajes();
        for (int i = 1; i < n; i++) {
            if (paradas[i].getAbordajes() > max) {
                max = paradas[i].getAbordajes();
            }
        }

        // 2. Contar ocurrencias de cada valor
        int[] conteo = new int[max + 1];
        for (int i = 0; i < n; i++) {
            conteo[paradas[i].getAbordajes()]++;
        }

        // 3. Acumular de mayor a menor (para orden descendente)
        for (int v = max - 1; v >= 0; v--) {
            conteo[v] += conteo[v + 1];
        }

        // 4. Colocar cada elemento en su posición (recorrido inverso = estable)
        for (int i = n - 1; i >= 0; i--) {
            int valor = paradas[i].getAbordajes();
            conteo[valor]--;
            salida[conteo[valor]] = paradas[i];
        }

        return salida;
    }
}
