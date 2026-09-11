package util;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class FechaUtil {
    private static final DateTimeFormatter FECHA = DateTimeFormatter.ofPattern("dd/MM/yyyy");
    private static final DateTimeFormatter FECHA_HORA = DateTimeFormatter.ofPattern("dd/MM/yyyy HH:mm:ss");
    private static final DateTimeFormatter ARCHIVO = DateTimeFormatter.ofPattern("yyyyMMdd_HHmmss");

    public static String fechaActual() {
        return LocalDateTime.now().format(FECHA);
    }

    public static String fechaHoraActual() {
        return LocalDateTime.now().format(FECHA_HORA);
    }

    public static String selloArchivo() {
        return LocalDateTime.now().format(ARCHIVO);
    }
}
