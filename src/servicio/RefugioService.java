package servicio;

import modelo.*;
import util.FechaUtil;

public class RefugioService {
    public static final int MAX_ANIMALES = 200;
    public static final int MAX_ADOPTANTES = 150;
    public static final int MAX_SOLICITUDES = 300;
    public static final int MAX_RESCATES = 200;
    public static final int MAX_BITACORA = 1000;
    public static final int FILAS_UBICACION = 4;
    public static final int COLUMNAS_UBICACION = 8;

    private final Animal[] animales = new Animal[MAX_ANIMALES];
    private final Adoptante[] adoptantes = new Adoptante[MAX_ADOPTANTES];
    private final Solicitud[] solicitudes = new Solicitud[MAX_SOLICITUDES];
    private final Rescate[] rescates = new Rescate[MAX_RESCATES];
    private final Bitacora[] bitacora = new Bitacora[MAX_BITACORA];
    private final String[][] ubicaciones = new String[FILAS_UBICACION][COLUMNAS_UBICACION];

    private int totalAnimales;
    private int totalAdoptantes;
    private int totalSolicitudes;
    private int totalRescates;
    private int totalBitacora;
    private String usuarioActual = "sistema";

    public RefugioService() {
        limpiarMatriz();
    }

    public void setUsuarioActual(String usuarioActual) { this.usuarioActual = usuarioActual; }

    public Animal[] getAnimales() { return animales; }
    public Adoptante[] getAdoptantes() { return adoptantes; }
    public Solicitud[] getSolicitudes() { return solicitudes; }
    public Rescate[] getRescates() { return rescates; }
    public Bitacora[] getBitacora() { return bitacora; }
    public String[][] getUbicaciones() { return ubicaciones; }
    public int getTotalAnimales() { return totalAnimales; }
    public int getTotalAdoptantes() { return totalAdoptantes; }
    public int getTotalSolicitudes() { return totalSolicitudes; }
    public int getTotalRescates() { return totalRescates; }
    public int getTotalBitacora() { return totalBitacora; }

    public String registrarAnimal(String codigo, String nombre, String especie, int edad, String estadoClinico) {
        if (vacio(codigo) || vacio(nombre) || vacio(especie) || vacio(estadoClinico)) return "Hay campos vacíos.";
        if (edad < 0 || edad > 40) return "Edad estimada inválida.";
        if (!especie.equalsIgnoreCase("Perro") && !especie.equalsIgnoreCase("Gato")) return "La especie debe ser Perro o Gato.";
        if (buscarAnimalPorCodigo(codigo) != null) return "Ya existe un animal con ese código.";
        if (totalAnimales >= MAX_ANIMALES) return "Se alcanzó la capacidad del arreglo de animales.";

        Animal animal = new Animal(codigo.trim(), nombre.trim(), normalizarEspecie(especie), edad,
                estadoClinico.trim(), "DISPONIBLE", "SIN_ASIGNAR", true);
        animales[totalAnimales++] = animal;
        registrarBitacora("Registró animal " + codigo);
        return "OK";
    }

    public Animal buscarAnimalPorCodigo(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < totalAnimales; i++) {
            if (animales[i] != null && animales[i].getCodigo().equalsIgnoreCase(codigo.trim())) return animales[i];
        }
        return null;
    }

    public int buscarIndiceAnimal(String codigo) {
        for (int i = 0; i < totalAnimales; i++) {
            if (animales[i] != null && animales[i].getCodigo().equalsIgnoreCase(codigo.trim())) return i;
        }
        return -1;
    }

    public String editarAnimal(String codigo, String nombre, String especie, int edad, String estadoClinico, String estadoAdopcion) {
        Animal animal = buscarAnimalPorCodigo(codigo);
        if (animal == null || !animal.isActivo()) return "Animal no encontrado o eliminado.";
        if (vacio(nombre) || vacio(especie) || vacio(estadoClinico) || vacio(estadoAdopcion)) return "Hay campos vacíos.";
        if (edad < 0 || edad > 40) return "Edad estimada inválida.";
        if (!especie.equalsIgnoreCase("Perro") && !especie.equalsIgnoreCase("Gato")) return "La especie debe ser Perro o Gato.";

        animal.setNombre(nombre.trim());
        animal.setEspecie(normalizarEspecie(especie));
        animal.setEdadEstimada(edad);
        animal.setEstadoClinico(estadoClinico.trim());
        animal.setEstadoAdopcion(estadoAdopcion.trim().toUpperCase());
        registrarBitacora("Editó animal " + codigo);
        return "OK";
    }

    public String eliminarAnimalLogico(String codigo) {
        Animal animal = buscarAnimalPorCodigo(codigo);
        if (animal == null || !animal.isActivo()) return "Animal no encontrado o ya eliminado.";
        liberarUbicacionDeAnimal(codigo);
        animal.setActivo(false);
        animal.setEstadoAdopcion("ELIMINADO");
        registrarBitacora("Eliminó lógicamente animal " + codigo);
        return "OK";
    }

    public Animal[] buscarAnimales(String texto, String criterio) {
        Animal[] resultados = new Animal[MAX_ANIMALES];
        int c = 0;
        String t = texto == null ? "" : texto.trim().toLowerCase();
        for (int i = 0; i < totalAnimales; i++) {
            Animal a = animales[i];
            if (a == null || !a.isActivo()) continue;
            boolean coincide = false;
            if (criterio.equals("Código")) coincide = a.getCodigo().toLowerCase().contains(t);
            else if (criterio.equals("Nombre")) coincide = a.getNombre().toLowerCase().contains(t);
            else if (criterio.equals("Especie")) coincide = a.getEspecie().toLowerCase().contains(t);
            else if (criterio.equals("Estado")) coincide = a.getEstadoAdopcion().toLowerCase().contains(t) || a.getEstadoClinico().toLowerCase().contains(t);
            else coincide = true;
            if (coincide) resultados[c++] = a;
        }
        return resultados;
    }

    public String registrarAdoptante(String dpi, String nombre, String telefono, String correo, String direccion) {
        if (vacio(dpi) || vacio(nombre) || vacio(telefono) || vacio(correo) || vacio(direccion)) return "Hay campos vacíos.";
        if (buscarAdoptante(dpi) != null) return "Ya existe un adoptante con ese DPI.";
        if (totalAdoptantes >= MAX_ADOPTANTES) return "Se alcanzó la capacidad del arreglo de adoptantes.";
        adoptantes[totalAdoptantes++] = new Adoptante(dpi.trim(), nombre.trim(), telefono.trim(), correo.trim(), direccion.trim(), true);
        registrarBitacora("Registró adoptante " + dpi);
        return "OK";
    }

    public Adoptante buscarAdoptante(String dpi) {
        if (dpi == null) return null;
        for (int i = 0; i < totalAdoptantes; i++) {
            if (adoptantes[i] != null && adoptantes[i].getDpi().equalsIgnoreCase(dpi.trim())) return adoptantes[i];
        }
        return null;
    }

    public String editarAdoptante(String dpi, String nombre, String telefono, String correo, String direccion) {
        Adoptante a = buscarAdoptante(dpi);
        if (a == null || !a.isActivo()) return "Adoptante no encontrado.";
        if (vacio(nombre) || vacio(telefono) || vacio(correo) || vacio(direccion)) return "Hay campos vacíos.";
        a.setNombre(nombre.trim());
        a.setTelefono(telefono.trim());
        a.setCorreo(correo.trim());
        a.setDireccion(direccion.trim());
        registrarBitacora("Editó adoptante " + dpi);
        return "OK";
    }

    public String registrarSolicitud(String codigo, String dpi, String codigoAnimal) {
        if (vacio(codigo) || vacio(dpi) || vacio(codigoAnimal)) return "Hay campos vacíos.";
        if (buscarSolicitud(codigo) != null) return "Ya existe una solicitud con ese código.";
        Adoptante adoptante = buscarAdoptante(dpi);
        Animal animal = buscarAnimalPorCodigo(codigoAnimal);
        if (adoptante == null || !adoptante.isActivo()) return "El adoptante no existe.";
        if (animal == null || !animal.isActivo()) return "El animal no existe.";
        if (!animal.getEstadoAdopcion().equalsIgnoreCase("DISPONIBLE")) return "El animal no está disponible para adopción.";
        if (totalSolicitudes >= MAX_SOLICITUDES) return "Se alcanzó la capacidad del arreglo de solicitudes.";
        solicitudes[totalSolicitudes++] = new Solicitud(codigo.trim(), dpi.trim(), codigoAnimal.trim(), FechaUtil.fechaActual(), "PENDIENTE");
        registrarBitacora("Registró solicitud " + codigo + " para animal " + codigoAnimal);
        return "OK";
    }

    public Solicitud buscarSolicitud(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < totalSolicitudes; i++) {
            if (solicitudes[i] != null && solicitudes[i].getCodigo().equalsIgnoreCase(codigo.trim())) return solicitudes[i];
        }
        return null;
    }

    public String cambiarEstadoSolicitud(String codigo, String nuevoEstado) {
        Solicitud s = buscarSolicitud(codigo);
        if (s == null) return "Solicitud no encontrada.";
        if (!nuevoEstado.equalsIgnoreCase("PENDIENTE") && !nuevoEstado.equalsIgnoreCase("APROBADA") && !nuevoEstado.equalsIgnoreCase("RECHAZADA")) {
            return "Estado no permitido.";
        }
        if (!s.getEstado().equalsIgnoreCase("PENDIENTE") && !nuevoEstado.equalsIgnoreCase(s.getEstado())) {
            return "La solicitud ya fue atendida.";
        }
        s.setEstado(nuevoEstado.toUpperCase());
        if (nuevoEstado.equalsIgnoreCase("APROBADA")) {
            Animal a = buscarAnimalPorCodigo(s.getCodigoAnimal());
            if (a == null || !a.isActivo()) return "El animal asociado ya no está disponible.";
            a.setEstadoAdopcion("ADOPTADO");
            liberarUbicacionDeAnimal(a.getCodigo());
            rechazarOtrasSolicitudes(a.getCodigo(), s.getCodigo());
        }
        registrarBitacora("Cambió solicitud " + codigo + " a " + nuevoEstado.toUpperCase());
        return "OK";
    }

    private void rechazarOtrasSolicitudes(String codigoAnimal, String solicitudAprobada) {
        for (int i = 0; i < totalSolicitudes; i++) {
            Solicitud s = solicitudes[i];
            if (s != null && s.getCodigoAnimal().equalsIgnoreCase(codigoAnimal)
                    && !s.getCodigo().equalsIgnoreCase(solicitudAprobada)
                    && s.getEstado().equalsIgnoreCase("PENDIENTE")) {
                s.setEstado("RECHAZADA");
            }
        }
    }

    public String registrarRescate(String codigo, String descripcion, String ubicacionReporte, String prioridad) {
        if (vacio(codigo) || vacio(descripcion) || vacio(ubicacionReporte) || vacio(prioridad)) return "Hay campos vacíos.";
        if (buscarRescate(codigo) != null) return "Ya existe un rescate con ese código.";
        if (!prioridadValida(prioridad)) return "Prioridad válida: BAJA, MEDIA, ALTA o CRITICA.";
        if (totalRescates >= MAX_RESCATES) return "Se alcanzó la capacidad del arreglo de rescates.";
        rescates[totalRescates++] = new Rescate(codigo.trim(), descripcion.trim(), ubicacionReporte.trim(), prioridad.toUpperCase(), "ACTIVO", FechaUtil.fechaActual());
        registrarBitacora("Registró rescate " + codigo + " prioridad " + prioridad.toUpperCase());
        return "OK";
    }

    public Rescate buscarRescate(String codigo) {
        if (codigo == null) return null;
        for (int i = 0; i < totalRescates; i++) {
            if (rescates[i] != null && rescates[i].getCodigo().equalsIgnoreCase(codigo.trim())) return rescates[i];
        }
        return null;
    }

    public String atenderRescate(String codigo) {
        Rescate r = buscarRescate(codigo);
        if (r == null) return "Rescate no encontrado.";
        if (!r.getEstado().equalsIgnoreCase("ACTIVO")) return "El rescate ya fue atendido.";
        r.setEstado("ATENDIDO");
        registrarBitacora("Atendió rescate " + codigo);
        return "OK";
    }

    public String asignarUbicacion(String codigoAnimal, int fila, int columna) {
        Animal a = buscarAnimalPorCodigo(codigoAnimal);
        if (a == null || !a.isActivo()) return "Animal no encontrado.";
        if (fila < 0 || fila >= FILAS_UBICACION || columna < 0 || columna >= COLUMNAS_UBICACION) return "Ubicación inválida.";
        if (!ubicaciones[fila][columna].equals("LIBRE")) return "El espacio ya está ocupado.";
        if (!a.getUbicacion().equals("SIN_ASIGNAR")) return "El animal ya tiene una ubicación asignada.";
        ubicaciones[fila][columna] = a.getCodigo();
        a.setUbicacion(codigoUbicacion(fila, columna));
        registrarBitacora("Asignó animal " + codigoAnimal + " a " + a.getUbicacion());
        return "OK";
    }

    public String liberarUbicacion(int fila, int columna) {
        if (fila < 0 || fila >= FILAS_UBICACION || columna < 0 || columna >= COLUMNAS_UBICACION) return "Ubicación inválida.";
        if (ubicaciones[fila][columna].equals("LIBRE")) return "El espacio ya está libre.";
        String codigoAnimal = ubicaciones[fila][columna];
        Animal a = buscarAnimalPorCodigo(codigoAnimal);
        if (a != null) a.setUbicacion("SIN_ASIGNAR");
        ubicaciones[fila][columna] = "LIBRE";
        registrarBitacora("Liberó espacio " + codigoUbicacion(fila, columna));
        return "OK";
    }

    public void liberarUbicacionDeAnimal(String codigoAnimal) {
        for (int f = 0; f < FILAS_UBICACION; f++) {
            for (int c = 0; c < COLUMNAS_UBICACION; c++) {
                if (ubicaciones[f][c].equalsIgnoreCase(codigoAnimal)) {
                    ubicaciones[f][c] = "LIBRE";
                }
            }
        }
        Animal a = buscarAnimalPorCodigo(codigoAnimal);
        if (a != null) a.setUbicacion("SIN_ASIGNAR");
    }

    public int contarEspaciosOcupados() {
        int ocupados = 0;
        for (int f = 0; f < FILAS_UBICACION; f++) {
            for (int c = 0; c < COLUMNAS_UBICACION; c++) {
                if (!ubicaciones[f][c].equals("LIBRE")) ocupados++;
            }
        }
        return ocupados;
    }

    public int capacidadTotal() { return FILAS_UBICACION * COLUMNAS_UBICACION; }

    public void reconstruirMatrizDesdeAnimales() {
        limpiarMatriz();
        for (int i = 0; i < totalAnimales; i++) {
            Animal a = animales[i];
            if (a == null || !a.isActivo()) continue;
            String u = a.getUbicacion();
            if (u != null && u.matches("A[1-4]-J[1-8]")) {
                int fila = Integer.parseInt(u.substring(1, 2)) - 1;
                int col = Integer.parseInt(u.substring(4)) - 1;
                if (ubicaciones[fila][col].equals("LIBRE")) ubicaciones[fila][col] = a.getCodigo();
                else a.setUbicacion("SIN_ASIGNAR");
            }
        }
    }

    public void agregarAnimalCargado(Animal a) { if (a != null && totalAnimales < MAX_ANIMALES) animales[totalAnimales++] = a; }
    public void agregarAdoptanteCargado(Adoptante a) { if (a != null && totalAdoptantes < MAX_ADOPTANTES) adoptantes[totalAdoptantes++] = a; }
    public void agregarSolicitudCargada(Solicitud s) { if (s != null && totalSolicitudes < MAX_SOLICITUDES) solicitudes[totalSolicitudes++] = s; }
    public void agregarRescateCargado(Rescate r) { if (r != null && totalRescates < MAX_RESCATES) rescates[totalRescates++] = r; }
    public void agregarBitacoraCargada(Bitacora b) { if (b != null && totalBitacora < MAX_BITACORA) bitacora[totalBitacora++] = b; }

    public void registrarBitacora(String accion) {
        if (totalBitacora >= MAX_BITACORA) return;
        bitacora[totalBitacora++] = new Bitacora(FechaUtil.fechaHoraActual(), usuarioActual, limpiarTexto(accion));
    }

    public String codigoUbicacion(int fila, int columna) {
        return "A" + (fila + 1) + "-J" + (columna + 1);
    }

    private void limpiarMatriz() {
        for (int f = 0; f < FILAS_UBICACION; f++) {
            for (int c = 0; c < COLUMNAS_UBICACION; c++) ubicaciones[f][c] = "LIBRE";
        }
    }

    private boolean vacio(String s) { return s == null || s.trim().isEmpty(); }
    private String normalizarEspecie(String e) { return e.equalsIgnoreCase("Perro") ? "Perro" : "Gato"; }
    private boolean prioridadValida(String p) {
        return p.equalsIgnoreCase("BAJA") || p.equalsIgnoreCase("MEDIA") || p.equalsIgnoreCase("ALTA") || p.equalsIgnoreCase("CRITICA");
    }
    private String limpiarTexto(String s) { return s == null ? "" : s.replace(';', ',').replace('\n', ' '); }
}
