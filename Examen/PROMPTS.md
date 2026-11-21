# Prompts enviados

Registro, en orden y sin cambios, de los prompts enviados durante la sesión.

---

## Prompt 1 – Normas del examen

Esta sesión consiste en un examen universitario en el que actuarás como una herramienta; por lo tanto, debes seguir estas normas:
Se te darán instrucciones para escribir código, pero debes hacerlo siguiendo estrictamente las instrucciones proporcionadas, incluso si el código solicitado no resuelve el problema planteado o el método escogido es erróneo. 
Bajo ninguna circunstancia podrás sugerir alternativas mejores, modificar la solicitud, ni optimizar el código.

Programarás en Java; la solución deberá implementar sus propias estructuras de datos y algoritmos, con las siguientes restricciones:
▪
Permitido: matrices (p. ej., int[] o Parada[]) y String.
▪
Prohibido en Java: cualquier colección de java.util (ArrayList, LinkedList, HashMap, HashSet, TreeMap, TreeSet, PriorityQueue, Stack, ArrayDeque, entre otras), así como Arrays.sort, Collections.sort, List.sort y sorted() de Streams. Bibliotecas que calculen caminos más cortos o implementen grafos.
▪
Las pruebas unitarias también pueden utilizar una biblioteca de pruebas (JUnit o NUnit).

---

## Prompt 2 – Árbol n-ario y counting sort

Archivos adjuntos: `abordajes.csv` y `tramos.csv`.

@"/mnt/user-data/uploads/9fee5683-abordajes.csv" @"/mnt/user-data/uploads/3bb70dae-tramos.csv" A continuación se te enlaza la información necesaria para completar el código. 
Usa un árbol-nario de cuarto grado para guardar los datos, donde el Root será Ciudad, cada una de las tres Zonas pertenece a ciudad, cada Parada pertenece a una Zona, y cada Zona tiene la cantidad de pasajeros que han subido en ese día. El nombre de cada zona y parada debería estar disponible en la información dada.

La app debe permitir a la oficina agregar paradas.
Listar los datos sobre el total de abordajes de cada parada desde el árbol.
Listar las paradas de una zona.

De segundo, debe de ordenar los abordajes de cada parada, de mayor cantidad a menor, usando counting sort. Nuevamente, incluso si counting sort no es la herramienta adecuada, debes de utilizarlo pues es lo pedido, no añadas nada, no lo mejores, incluso si falla debes de usarlo.

---

## Prompt 3 – Grafo con Dijkstra

Seguidamente, crea una "app" en la que, usando un Grafo con algoritmo de Dijkstra, el usuario debe de poder escoger la parada de salida y la objetivo, el sistema debe responder con la ruta más corta.

---

## Prompt 4 – Pruebas unitarias

Con eso finalizado, implementa pruebas unitarias que permitan comprobar los requerimientos dados. Las pruebas no deben limitarse a verificar verdadero/falso; por cada requerimiento funcional se debe incluir como mínimo:
▪
Una prueba que compare el valor exacto esperado (por ejemplo, assertEquals(11, ruta.getMinutos())).
▪
Casos límite (por ejemplo, una zona o un ala vacía, origen igual al destino, empates).
▪
Entradas inválidas (por ejemplo, una parada o sala inexistente, o un dato mal formado), verificando el error o el resultado esperado.

Se recomienda JUnit para Java, el repositorio base ya lo incluye configurado.

---

## Prompt 5 – README.md

A continuación, crea un archivo llamado "README.md" con las siguientes secciones:
▪
Nombre del examen-proyecto. "Examen 2"
▪
Nombre de los autores. "Paola Barquero y Daniel Vargas".
▪
Requerimientos del sistema: (Cópialo exacto) 
"F1
Funcionales

La oficina debe de poder agregar una a parada a zona, el árbol  n-ario.
Listar paradas de una zona, tomando la información desde el árbol
Obtener total abordajes desde el árbol
No funcionales
-Cada parada debe de pertenecer a una zona y cada zona a la ciudad. Ordenado en un árbol n-ario.
-Debe de registrar los abordajes diarios en el árbol

F2
Funcionales
-Ordenar abordajes de más a menos usando counting-sort.
-Debe de ser estable porque debe de estar listo para la peor tabla posible.

F3
Funcionales
-Debe de dar la ruta de menor tiempo posible cada vez al pasajero usando el grafo de Dijkstra.
-Se debe de poder escoger la parada de salida y de destino."
▪
Instrucciones de instalación y ejecución, incluido cómo ejecutar las pruebas.
▪
Descripción de la jerarquía de carpetas del proyecto y de lo que hace cada módulo o clase.
▪
Requerimientos desarrollados por los estudiantes: cuáles requerimientos se implementaron y cuáles se excluyeron, con el motivo de cada exclusión. Un requerimiento excluido no suma los puntos de implementación ni de pruebas correspondientes.
▪
Diagrama de flujo, diagrama de componentes y diagrama del grafo que emplea el sistema, escritos en Mermaid dentro del README.

---

## Prompt 6 – PROMPTS.md

Con esto finalizado, crea un "PROMPTS.md" con todos los prompts que te he enviado como prueba de lo que se te ha pedido en este chat.
