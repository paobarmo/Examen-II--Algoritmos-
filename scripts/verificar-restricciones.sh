#!/usr/bin/env bash
# Busca en el código de la solución (java/src/main y csharp/src) usos que el
# examen no permite. Es informativo: siempre termina con código 0.

cd "$(dirname "$0")/.." || exit 0

echo "Las coincidencias son indicativas y deben revisarse manualmente (por ejemplo, una clase propia con un nombre parecido)."
echo

total=0

buscar() {
    # $1 = carpeta, $2 = patrón de archivos, $3 = expresión regular extendida
    local carpeta="$1" archivos="$2" expr="$3"
    [ -d "$carpeta" ] || return 0
    local salida
    salida=$(grep -rnE --include="$archivos" -e "$expr" "$carpeta" 2>/dev/null)
    if [ -n "$salida" ]; then
        printf '%s\n' "$salida"
        total=$((total + $(printf '%s\n' "$salida" | wc -l)))
    fi
}

JAVA_EXPR='java\.util\.(ArrayList|LinkedList|HashMap|HashSet|TreeMap|TreeSet|PriorityQueue|ArrayDeque|Stack|Vector)|import[[:space:]]+java\.util\.\*|Collections\.sort|Arrays\.sort|List\.sort|\.sorted\(|List\.of\(|Map\.of\(|Set\.of\('
CS_EXPR='using[[:space:]]+System\.Linq|using[[:space:]]+System\.Collections\.Generic|List<|LinkedList<|Dictionary<|HashSet<|SortedDictionary|SortedSet|SortedList|PriorityQueue<|Queue<|Stack<|Array\.Sort|\.Sort\(|OrderBy|ThenBy'

echo "== Java (java/src/main)"
buscar "java/src/main" "*.java" "$JAVA_EXPR"
echo
echo "== C# (csharp/src)"
buscar "csharp/src" "*.cs" "$CS_EXPR"
echo

echo "Total de coincidencias: $total"
exit 0
