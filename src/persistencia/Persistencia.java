package persistencia;

import modelo.*;
import servicio.RefugioService;

import java.io.*;

public class Persistencia {
    private final File carpetaDatos;

    public Persistencia(String ruta) {
        carpetaDatos = new File(ruta);
        if (!carpetaDatos.exists()) carpetaDatos.mkdirs();
    }

    public void prepararUsuarios() throws IOException {
        File f = new File(carpetaDatos, "usuarios.csv");
        if (!f.exists()) {
            try (PrintWriter pw = new PrintWriter(new FileWriter(f))) {
                pw.println("admin;admin123;ADMINISTRADOR");
                pw.println("auxiliar;aux123;AUXILIAR");
            }
        }
    }

    public Usuario autenticar(String usuario, String clave) throws IOException {
        prepararUsuarios();
        File f = new File(carpetaDatos, "usuarios.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1);
                if (p.length >= 3 && p[0].equals(usuario) && p[1].equals(clave)) return new Usuario(p[0], p[1], p[2]);
            }
        }
        return null;
    }

    public void cargarTodo(RefugioService s) throws IOException {
        cargarAnimales(s);
        cargarAdoptantes(s);
        cargarSolicitudes(s);
        cargarRescates(s);
        cargarBitacora(s);
        s.reconstruirMatrizDesdeAnimales();
    }

    public void guardarTodo(RefugioService s) throws IOException {
        guardarAnimales(s);
        guardarAdoptantes(s);
        guardarSolicitudes(s);
        guardarRescates(s);
        guardarBitacora(s);
    }

    private void cargarAnimales(RefugioService s) throws IOException {
        File f = new File(carpetaDatos, "animales.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                try {
                    String[] p = linea.split(";", -1);
                    if (p.length >= 8) s.agregarAnimalCargado(new Animal(p[0], p[1], p[2], Integer.parseInt(p[3]), p[4], p[5], p[6], Boolean.parseBoolean(p[7])));
                } catch (Exception ignored) { }
            }
        }
    }

    private void cargarAdoptantes(RefugioService s) throws IOException {
        File f = new File(carpetaDatos, "adoptantes.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1);
                if (p.length >= 6) s.agregarAdoptanteCargado(new Adoptante(p[0], p[1], p[2], p[3], p[4], Boolean.parseBoolean(p[5])));
            }
        }
    }

    private void cargarSolicitudes(RefugioService s) throws IOException {
        File f = new File(carpetaDatos, "solicitudes.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1);
                if (p.length >= 5) s.agregarSolicitudCargada(new Solicitud(p[0], p[1], p[2], p[3], p[4]));
            }
        }
    }

    private void cargarRescates(RefugioService s) throws IOException {
        File f = new File(carpetaDatos, "rescates.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", -1);
                if (p.length >= 6) s.agregarRescateCargado(new Rescate(p[0], p[1], p[2], p[3], p[4], p[5]));
            }
        }
    }

    private void cargarBitacora(RefugioService s) throws IOException {
        File f = new File(carpetaDatos, "bitacora.csv");
        if (!f.exists()) return;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String linea;
            while ((linea = br.readLine()) != null) {
                String[] p = linea.split(";", 3);
                if (p.length >= 3) s.agregarBitacoraCargada(new Bitacora(p[0], p[1], p[2]));
            }
        }
    }

    private void guardarAnimales(RefugioService s) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(carpetaDatos, "animales.csv")))) {
            for (int i = 0; i < s.getTotalAnimales(); i++) if (s.getAnimales()[i] != null) pw.println(s.getAnimales()[i].toCSV());
        }
    }

    private void guardarAdoptantes(RefugioService s) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(carpetaDatos, "adoptantes.csv")))) {
            for (int i = 0; i < s.getTotalAdoptantes(); i++) if (s.getAdoptantes()[i] != null) pw.println(s.getAdoptantes()[i].toCSV());
        }
    }

    private void guardarSolicitudes(RefugioService s) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(carpetaDatos, "solicitudes.csv")))) {
            for (int i = 0; i < s.getTotalSolicitudes(); i++) if (s.getSolicitudes()[i] != null) pw.println(s.getSolicitudes()[i].toCSV());
        }
    }

    private void guardarRescates(RefugioService s) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(carpetaDatos, "rescates.csv")))) {
            for (int i = 0; i < s.getTotalRescates(); i++) if (s.getRescates()[i] != null) pw.println(s.getRescates()[i].toCSV());
        }
    }

    private void guardarBitacora(RefugioService s) throws IOException {
        try (PrintWriter pw = new PrintWriter(new FileWriter(new File(carpetaDatos, "bitacora.csv")))) {
            for (int i = 0; i < s.getTotalBitacora(); i++) if (s.getBitacora()[i] != null) pw.println(s.getBitacora()[i].toCSV());
        }
    }
}
