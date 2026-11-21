# Repositorio base – Examen 2, Algoritmos y Estructuras de Datos I

## Cómo empezar

1. Haga un **fork** de este repositorio (o use el botón **Use this template**).
2. Clone su copia:
   ```bash
   git clone https://github.com/<su-usuario>/<su-repositorio>.git
   ```
3. Elija **Java** o **C#** y **borre la carpeta del lenguaje que no va a usar** (`java/` o `csharp/`).

## Requisitos

- Java: JDK 17 o superior y Maven.
- C#: .NET 8 SDK.

## Compilar, ejecutar y probar

### Java

```bash
cd java
mvn compile                                    # compilar
java -cp target/classes cr.ac.tec.examen2.Main # ejecutar
mvn test                                       # pruebas
```

### C#

```bash
cd csharp
dotnet build                          # compilar
dotnet run --project src/Examen2      # ejecutar
dotnet test                           # pruebas
```

## Verificación de restricciones

```bash
./scripts/verificar-restricciones.sh
```

El resultado es informativo; revise cada coincidencia manualmente.

## Carpeta `datos/`

Contiene los archivos CSV de cada versión (`version-a/` y `version-b/`).

---

Este README debe ser reemplazado por el README de su solución.
