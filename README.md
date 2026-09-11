# Centro de Rescate Animal - Proyecto IPC1 2S2026

Proyecto Java Swing construido manualmente con código, usando arreglos estáticos y matrices.

## Requisitos
- Java 17 o superior
- Visual Studio Code con Extension Pack for Java

## Ejecutar en VS Code
1. Abra esta carpeta completa en VS Code.
2. Abra `src/Main.java`.
3. Presione **Run Java**.
4. También puede compilar desde terminal:

```bash
javac -d out src/Main.java src/modelo/*.java src/servicio/*.java src/persistencia/*.java src/reportes/*.java src/interfaz/*.java src/util/*.java
java -cp out Main
```

## Usuarios iniciales
Se crean automáticamente en `datos/usuarios.csv`:
- Administrador: `admin` / `admin123`
- Auxiliar: `auxiliar` / `aux123`

Puede cambiarlos directamente en el archivo `datos/usuarios.csv`.

## Datos del estudiante
Edite el método `panelEstudiante()` en `src/interfaz/VentanaPrincipal.java` y sustituya los textos de nombre, carné y sección.

## Persistencia
Los datos se guardan en:
- `datos/animales.csv`
- `datos/adoptantes.csv`
- `datos/solicitudes.csv`
- `datos/rescates.csv`
- `datos/bitacora.csv`
- `datos/usuarios.csv`

## Reportes
Los HTML se generan dentro de `reportes/` e incluyen fecha y hora en el nombre.

## Importante
La lógica principal no utiliza `ArrayList`, `LinkedList`, `HashMap`, `List`, `Queue`, `Stack` ni `Vector`.
