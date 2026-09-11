package reportes;

import modelo.*;
import servicio.RefugioService;
import util.FechaUtil;

import java.io.*;

public class GeneradorReportes {
    private final File carpeta;

    public GeneradorReportes(String ruta) {
        carpeta = new File(ruta);
        if (!carpeta.exists()) carpeta.mkdirs();
    }

    public File reporteAnimales(RefugioService s) throws IOException {
        File f = archivo("animales");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            inicio(pw, "Reporte de Animales");
            pw.println("<h1>Animales rescatados</h1><table><tr><th>Código</th><th>Nombre</th><th>Especie</th><th>Edad</th><th>Clínico</th><th>Adopción</th><th>Ubicación</th><th>Activo</th></tr>");
            for (int i = 0; i < s.getTotalAnimales(); i++) {
                Animal a = s.getAnimales()[i];
                if (a != null) pw.println("<tr><td>"+e(a.getCodigo())+"</td><td>"+e(a.getNombre())+"</td><td>"+e(a.getEspecie())+"</td><td>"+a.getEdadEstimada()+"</td><td>"+e(a.getEstadoClinico())+"</td><td>"+e(a.getEstadoAdopcion())+"</td><td>"+e(a.getUbicacion())+"</td><td>"+a.isActivo()+"</td></tr>");
            }
            fin(pw);
        }
        return f;
    }

    public File reporteAdopciones(RefugioService s) throws IOException {
        File f = archivo("adopciones");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            inicio(pw, "Reporte de Adopciones");
            pw.println("<h1>Solicitudes de adopción</h1><table><tr><th>Código</th><th>DPI</th><th>Animal</th><th>Fecha</th><th>Estado</th></tr>");
            for (int i = 0; i < s.getTotalSolicitudes(); i++) {
                Solicitud x = s.getSolicitudes()[i];
                if (x != null) pw.println("<tr><td>"+e(x.getCodigo())+"</td><td>"+e(x.getDpiAdoptante())+"</td><td>"+e(x.getCodigoAnimal())+"</td><td>"+e(x.getFecha())+"</td><td>"+e(x.getEstado())+"</td></tr>");
            }
            fin(pw);
        }
        return f;
    }

    public File reporteOcupacion(RefugioService s) throws IOException {
        File f = archivo("ocupacion");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            inicio(pw, "Reporte de Ocupación");
            int ocupados = s.contarEspaciosOcupados();
            pw.println("<h1>Ocupación del refugio</h1><p>Ocupados: "+ocupados+" de "+s.capacidadTotal()+"</p><table><tr><th>Área / Jaula</th>");
            for (int c = 0; c < RefugioService.COLUMNAS_UBICACION; c++) pw.println("<th>J"+(c+1)+"</th>");
            pw.println("</tr>");
            for (int r = 0; r < RefugioService.FILAS_UBICACION; r++) {
                pw.println("<tr><th>Área "+(r+1)+"</th>");
                for (int c = 0; c < RefugioService.COLUMNAS_UBICACION; c++) pw.println("<td>"+e(s.getUbicaciones()[r][c])+"</td>");
                pw.println("</tr>");
            }
            fin(pw);
        }
        return f;
    }

    public File reporteBitacora(RefugioService s) throws IOException {
        File f = archivo("bitacora");
        try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
            inicio(pw, "Bitácora de Acciones");
            pw.println("<h1>Bitácora de acciones</h1><table><tr><th>Fecha/Hora</th><th>Usuario</th><th>Acción</th></tr>");
            for (int i = 0; i < s.getTotalBitacora(); i++) {
                Bitacora b = s.getBitacora()[i];
                if (b != null) pw.println("<tr><td>"+e(b.getFechaHora())+"</td><td>"+e(b.getUsuario())+"</td><td>"+e(b.getAccion())+"</td></tr>");
            }
            fin(pw);
        }
        return f;
    }

    private File archivo(String nombre) { return new File(carpeta, nombre + "_" + FechaUtil.selloArchivo() + ".html"); }

    private void inicio(PrintWriter pw, String titulo) {
        pw.println("<!DOCTYPE html><html><head><meta charset='UTF-8'><title>"+e(titulo)+"</title><style>body{font-family:Arial;margin:30px;background:#f5f7fa}h1{color:#25476a}table{border-collapse:collapse;width:100%;background:white}th,td{border:1px solid #bbb;padding:8px;text-align:left}th{background:#dde8f2}</style></head><body>");
        pw.println("<p>Generado: " + e(FechaUtil.fechaHoraActual()) + "</p>");
    }

    private void fin(PrintWriter pw) { pw.println("</table></body></html>"); }

    private String e(String s) {
        if (s == null) return "";
        return s.replace("&", "&amp;").replace("<", "&lt;").replace(">", "&gt;").replace("\"", "&quot;");
    }
}
