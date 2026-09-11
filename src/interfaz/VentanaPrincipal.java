package interfaz;

import modelo.*;
import persistencia.Persistencia;
import reportes.GeneradorReportes;
import servicio.RefugioService;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.awt.event.WindowAdapter;
import java.awt.event.WindowEvent;
import java.io.File;

public class VentanaPrincipal extends JFrame {
    private final RefugioService servicio;
    private final Persistencia persistencia;
    private final Usuario usuario;
    private final GeneradorReportes reportes = new GeneradorReportes("reportes");

    private final DefaultTableModel modeloAnimales = noEditable(new String[]{"Código", "Nombre", "Especie", "Edad", "Clínico", "Adopción", "Ubicación"});
    private final DefaultTableModel modeloAdoptantes = noEditable(new String[]{"DPI", "Nombre", "Teléfono", "Correo", "Dirección"});
    private final DefaultTableModel modeloSolicitudes = noEditable(new String[]{"Código", "DPI", "Animal", "Fecha", "Estado"});
    private final DefaultTableModel modeloRescates = noEditable(new String[]{"Código", "Descripción", "Ubicación", "Prioridad", "Estado", "Fecha"});
    private final DefaultTableModel modeloUbicaciones = noEditable(new String[]{"Área/Jaula", "J1", "J2", "J3", "J4", "J5", "J6", "J7", "J8"});
    private final DefaultTableModel modeloBitacora = noEditable(new String[]{"Fecha/Hora", "Usuario", "Acción"});

    public VentanaPrincipal(RefugioService servicio, Persistencia persistencia, Usuario usuario) {
        this.servicio = servicio;
        this.persistencia = persistencia;
        this.usuario = usuario;
        construir();
        refrescarTodo();
    }

    private void construir() {
        setTitle("Centro de Rescate Animal - " + usuario.getUsuario() + " (" + usuario.getRol() + ")");
        setDefaultCloseOperation(JFrame.DO_NOTHING_ON_CLOSE);
        setSize(1180, 720);
        setLocationRelativeTo(null);

        JTabbedPane pestañas = new JTabbedPane();
        pestañas.addTab("Animales", panelAnimales());
        pestañas.addTab("Adoptantes", panelAdoptantes());
        pestañas.addTab("Solicitudes", panelSolicitudes());
        pestañas.addTab("Rescates", panelRescates());
        pestañas.addTab("Ubicaciones", panelUbicaciones());
        pestañas.addTab("Reportes", panelReportes());
        pestañas.addTab("Bitácora", panelBitacora());
        pestañas.addTab("Estudiante", panelEstudiante());
        add(pestañas, BorderLayout.CENTER);

        JPanel pie = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton guardar = new JButton("Guardar datos");
        JButton salir = new JButton("Guardar y salir");
        guardar.addActionListener(e -> guardar());
        salir.addActionListener(e -> cerrar());
        pie.add(new JLabel("Sesión: " + usuario.getUsuario() + "   "));
        pie.add(guardar);
        pie.add(salir);
        add(pie, BorderLayout.SOUTH);

        addWindowListener(new WindowAdapter() {
            @Override public void windowClosing(WindowEvent e) { cerrar(); }
        });
    }

    private JPanel panelAnimales() {
        JPanel p = basePanel();
        JTable tabla = new JTable(modeloAnimales);
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);

        JPanel acciones = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registrar = new JButton("Registrar");
        JButton editar = new JButton("Editar");
        JButton eliminar = new JButton("Eliminar lógico");
        JButton buscar = new JButton("Buscar");
        JButton listar = new JButton("Listar todos");
        registrar.addActionListener(e -> registrarAnimal());
        editar.addActionListener(e -> editarAnimal());
        eliminar.addActionListener(e -> eliminarAnimal());
        buscar.addActionListener(e -> buscarAnimales());
        listar.addActionListener(e -> refrescarAnimales(null));
        acciones.add(registrar); acciones.add(editar); acciones.add(eliminar); acciones.add(buscar); acciones.add(listar);
        p.add(acciones, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelAdoptantes() {
        JPanel p = basePanel();
        p.add(new JScrollPane(new JTable(modeloAdoptantes)), BorderLayout.CENTER);
        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registrar = new JButton("Registrar");
        JButton editar = new JButton("Editar");
        JButton buscar = new JButton("Buscar DPI");
        registrar.addActionListener(e -> registrarAdoptante());
        editar.addActionListener(e -> editarAdoptante());
        buscar.addActionListener(e -> buscarAdoptante());
        a.add(registrar); a.add(editar); a.add(buscar);
        p.add(a, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelSolicitudes() {
        JPanel p = basePanel();
        p.add(new JScrollPane(new JTable(modeloSolicitudes)), BorderLayout.CENTER);
        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registrar = new JButton("Registrar solicitud");
        JButton aprobar = new JButton("Aprobar");
        JButton rechazar = new JButton("Rechazar");
        registrar.addActionListener(e -> registrarSolicitud());
        aprobar.addActionListener(e -> cambiarSolicitud("APROBADA"));
        rechazar.addActionListener(e -> cambiarSolicitud("RECHAZADA"));
        a.add(registrar); a.add(aprobar); a.add(rechazar);
        p.add(a, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelRescates() {
        JPanel p = basePanel();
        p.add(new JScrollPane(new JTable(modeloRescates)), BorderLayout.CENTER);
        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton registrar = new JButton("Registrar rescate");
        JButton atender = new JButton("Marcar atendido");
        registrar.addActionListener(e -> registrarRescate());
        atender.addActionListener(e -> atenderRescate());
        a.add(registrar); a.add(atender);
        p.add(a, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelUbicaciones() {
        JPanel p = basePanel();
        JTable tabla = new JTable(modeloUbicaciones);
        tabla.setRowHeight(35);
        p.add(new JScrollPane(tabla), BorderLayout.CENTER);
        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JButton asignar = new JButton("Asignar animal");
        JButton liberar = new JButton("Liberar espacio");
        JButton refrescar = new JButton("Refrescar");
        asignar.addActionListener(e -> asignarUbicacion());
        liberar.addActionListener(e -> liberarUbicacion());
        refrescar.addActionListener(e -> refrescarUbicaciones());
        a.add(asignar); a.add(liberar); a.add(refrescar);
        p.add(a, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelReportes() {
        JPanel p = new JPanel(new GridBagLayout());
        GridBagConstraints g = new GridBagConstraints();
        g.gridx = 0; g.fill = GridBagConstraints.HORIZONTAL; g.insets = new Insets(8, 8, 8, 8);
        JButton animales = new JButton("Generar reporte de animales");
        JButton adopciones = new JButton("Generar reporte de adopciones");
        JButton ocupacion = new JButton("Generar reporte de ocupación");
        JButton bitacora = new JButton("Generar reporte de bitácora");
        animales.addActionListener(e -> generarReporte(1));
        adopciones.addActionListener(e -> generarReporte(2));
        ocupacion.addActionListener(e -> generarReporte(3));
        bitacora.addActionListener(e -> generarReporte(4));
        g.gridy = 0; p.add(new JLabel("Los reportes HTML se guardan en la carpeta reportes/"), g);
        g.gridy = 1; p.add(animales, g);
        g.gridy = 2; p.add(adopciones, g);
        g.gridy = 3; p.add(ocupacion, g);
        g.gridy = 4; p.add(bitacora, g);
        return p;
    }

    private JPanel panelBitacora() {
        JPanel p = basePanel();
        p.add(new JScrollPane(new JTable(modeloBitacora)), BorderLayout.CENTER);
        JButton refrescar = new JButton("Refrescar");
        refrescar.addActionListener(e -> refrescarBitacora());
        JPanel a = new JPanel(new FlowLayout(FlowLayout.LEFT)); a.add(refrescar); p.add(a, BorderLayout.NORTH);
        return p;
    }

    private JPanel panelEstudiante() {
        JPanel p = new JPanel(new GridLayout(6, 1, 8, 8));
        p.setBorder(BorderFactory.createEmptyBorder(50, 120, 50, 120));
        JLabel t = new JLabel("Datos del estudiante", SwingConstants.CENTER);
        t.setFont(new Font("SansSerif", Font.BOLD, 24));
        p.add(t);
        p.add(new JLabel("Nombre: EDITAR EN VentanaPrincipal.java"));
        p.add(new JLabel("Carné: EDITAR EN VentanaPrincipal.java"));
        p.add(new JLabel("Sección: EDITAR EN VentanaPrincipal.java"));
        p.add(new JLabel("Curso: Introducción a la Programación y Computación 1"));
        p.add(new JLabel("Proyecto: Centro de Rescate Animal: Gestión de Refugio y Adopciones"));
        return p;
    }

    private void registrarAnimal() {
        JTextField codigo = new JTextField(); JTextField nombre = new JTextField();
        JComboBox<String> especie = new JComboBox<>(new String[]{"Perro", "Gato"});
        JTextField edad = new JTextField(); JTextField clinico = new JTextField("ESTABLE");
        Object[] campos = {"Código:", codigo, "Nombre:", nombre, "Especie:", especie, "Edad estimada:", edad, "Estado clínico:", clinico};
        if (JOptionPane.showConfirmDialog(this, campos, "Registrar animal", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            String r = servicio.registrarAnimal(codigo.getText(), nombre.getText(), String.valueOf(especie.getSelectedItem()), Integer.parseInt(edad.getText()), clinico.getText());
            resultado(r); refrescarTodo();
        } catch (NumberFormatException ex) { error("La edad debe ser un número entero."); }
    }

    private void editarAnimal() {
        String codigo = pedir("Código del animal a editar:"); if (codigo == null) return;
        Animal x = servicio.buscarAnimalPorCodigo(codigo);
        if (x == null || !x.isActivo()) { error("Animal no encontrado."); return; }
        JTextField nombre = new JTextField(x.getNombre());
        JComboBox<String> especie = new JComboBox<>(new String[]{"Perro", "Gato"}); especie.setSelectedItem(x.getEspecie());
        JTextField edad = new JTextField(String.valueOf(x.getEdadEstimada()));
        JTextField clinico = new JTextField(x.getEstadoClinico());
        JComboBox<String> adopcion = new JComboBox<>(new String[]{"DISPONIBLE", "EN_PROCESO", "ADOPTADO"}); adopcion.setSelectedItem(x.getEstadoAdopcion());
        Object[] campos = {"Nombre:", nombre, "Especie:", especie, "Edad:", edad, "Estado clínico:", clinico, "Estado adopción:", adopcion};
        if (JOptionPane.showConfirmDialog(this, campos, "Editar animal", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            resultado(servicio.editarAnimal(codigo, nombre.getText(), String.valueOf(especie.getSelectedItem()), Integer.parseInt(edad.getText()), clinico.getText(), String.valueOf(adopcion.getSelectedItem())));
            refrescarTodo();
        } catch (NumberFormatException ex) { error("La edad debe ser un número entero."); }
    }

    private void eliminarAnimal() {
        String codigo = pedir("Código del animal a eliminar lógicamente:"); if (codigo == null) return;
        if (JOptionPane.showConfirmDialog(this, "¿Confirma la eliminación lógica?", "Confirmar", JOptionPane.YES_NO_OPTION) == JOptionPane.YES_OPTION) {
            resultado(servicio.eliminarAnimalLogico(codigo)); refrescarTodo();
        }
    }

    private void buscarAnimales() {
        JComboBox<String> criterio = new JComboBox<>(new String[]{"Código", "Nombre", "Especie", "Estado"});
        JTextField texto = new JTextField();
        Object[] campos = {"Criterio:", criterio, "Texto:", texto};
        if (JOptionPane.showConfirmDialog(this, campos, "Buscar animales", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            Animal[] r = servicio.buscarAnimales(texto.getText(), String.valueOf(criterio.getSelectedItem()));
            refrescarAnimales(r);
        }
    }

    private void registrarAdoptante() {
        JTextField dpi = new JTextField(); JTextField nombre = new JTextField(); JTextField tel = new JTextField(); JTextField correo = new JTextField(); JTextField dir = new JTextField();
        Object[] c = {"DPI:", dpi, "Nombre:", nombre, "Teléfono:", tel, "Correo:", correo, "Dirección:", dir};
        if (JOptionPane.showConfirmDialog(this, c, "Registrar adoptante", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            resultado(servicio.registrarAdoptante(dpi.getText(), nombre.getText(), tel.getText(), correo.getText(), dir.getText())); refrescarTodo();
        }
    }

    private void editarAdoptante() {
        String dpi = pedir("DPI del adoptante:"); if (dpi == null) return;
        Adoptante x = servicio.buscarAdoptante(dpi); if (x == null || !x.isActivo()) { error("Adoptante no encontrado."); return; }
        JTextField nombre = new JTextField(x.getNombre()); JTextField tel = new JTextField(x.getTelefono()); JTextField correo = new JTextField(x.getCorreo()); JTextField dir = new JTextField(x.getDireccion());
        Object[] c = {"Nombre:", nombre, "Teléfono:", tel, "Correo:", correo, "Dirección:", dir};
        if (JOptionPane.showConfirmDialog(this, c, "Editar adoptante", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            resultado(servicio.editarAdoptante(dpi, nombre.getText(), tel.getText(), correo.getText(), dir.getText())); refrescarTodo();
        }
    }

    private void buscarAdoptante() {
        String dpi = pedir("DPI a buscar:"); if (dpi == null) return;
        Adoptante a = servicio.buscarAdoptante(dpi);
        if (a == null || !a.isActivo()) error("No encontrado.");
        else JOptionPane.showMessageDialog(this, "DPI: "+a.getDpi()+"\nNombre: "+a.getNombre()+"\nTeléfono: "+a.getTelefono()+"\nCorreo: "+a.getCorreo()+"\nDirección: "+a.getDireccion());
    }

    private void registrarSolicitud() {
        JTextField codigo = new JTextField(); JTextField dpi = new JTextField(); JTextField animal = new JTextField();
        Object[] c = {"Código solicitud:", codigo, "DPI adoptante:", dpi, "Código animal:", animal};
        if (JOptionPane.showConfirmDialog(this, c, "Registrar solicitud", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            resultado(servicio.registrarSolicitud(codigo.getText(), dpi.getText(), animal.getText())); refrescarTodo();
        }
    }

    private void cambiarSolicitud(String estado) {
        String codigo = pedir("Código de la solicitud:"); if (codigo == null) return;
        resultado(servicio.cambiarEstadoSolicitud(codigo, estado)); refrescarTodo();
    }

    private void registrarRescate() {
        JTextField codigo = new JTextField(); JTextField desc = new JTextField(); JTextField ubi = new JTextField(); JComboBox<String> prioridad = new JComboBox<>(new String[]{"BAJA", "MEDIA", "ALTA", "CRITICA"});
        Object[] c = {"Código:", codigo, "Descripción:", desc, "Ubicación del reporte:", ubi, "Prioridad:", prioridad};
        if (JOptionPane.showConfirmDialog(this, c, "Registrar rescate", JOptionPane.OK_CANCEL_OPTION) == JOptionPane.OK_OPTION) {
            resultado(servicio.registrarRescate(codigo.getText(), desc.getText(), ubi.getText(), String.valueOf(prioridad.getSelectedItem()))); refrescarTodo();
        }
    }

    private void atenderRescate() {
        String codigo = pedir("Código del rescate:"); if (codigo == null) return;
        resultado(servicio.atenderRescate(codigo)); refrescarTodo();
    }

    private void asignarUbicacion() {
        JTextField animal = new JTextField(); JTextField fila = new JTextField(); JTextField columna = new JTextField();
        Object[] c = {"Código animal:", animal, "Área (1-4):", fila, "Jaula (1-8):", columna};
        if (JOptionPane.showConfirmDialog(this, c, "Asignar ubicación", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            resultado(servicio.asignarUbicacion(animal.getText(), Integer.parseInt(fila.getText()) - 1, Integer.parseInt(columna.getText()) - 1)); refrescarTodo();
        } catch (NumberFormatException ex) { error("Área y jaula deben ser números."); }
    }

    private void liberarUbicacion() {
        JTextField fila = new JTextField(); JTextField columna = new JTextField();
        Object[] c = {"Área (1-4):", fila, "Jaula (1-8):", columna};
        if (JOptionPane.showConfirmDialog(this, c, "Liberar ubicación", JOptionPane.OK_CANCEL_OPTION) != JOptionPane.OK_OPTION) return;
        try {
            resultado(servicio.liberarUbicacion(Integer.parseInt(fila.getText()) - 1, Integer.parseInt(columna.getText()) - 1)); refrescarTodo();
        } catch (NumberFormatException ex) { error("Área y jaula deben ser números."); }
    }

    private void generarReporte(int tipo) {
        try {
            guardar();
            File f;
            if (tipo == 1) f = reportes.reporteAnimales(servicio);
            else if (tipo == 2) f = reportes.reporteAdopciones(servicio);
            else if (tipo == 3) f = reportes.reporteOcupacion(servicio);
            else f = reportes.reporteBitacora(servicio);
            servicio.registrarBitacora("Generó reporte HTML: " + f.getName());
            guardar();
            JOptionPane.showMessageDialog(this, "Reporte generado:\n" + f.getAbsolutePath());
        } catch (Exception ex) { error("No se pudo generar el reporte: " + ex.getMessage()); }
    }

    private void refrescarTodo() {
        refrescarAnimales(null); refrescarAdoptantes(); refrescarSolicitudes(); refrescarRescates(); refrescarUbicaciones(); refrescarBitacora();
    }

    private void refrescarAnimales(Animal[] filtro) {
        modeloAnimales.setRowCount(0);
        if (filtro != null) {
            for (int i = 0; i < filtro.length; i++) { Animal a = filtro[i]; if (a != null && a.isActivo()) agregarFilaAnimal(a); }
        } else {
            for (int i = 0; i < servicio.getTotalAnimales(); i++) { Animal a = servicio.getAnimales()[i]; if (a != null && a.isActivo()) agregarFilaAnimal(a); }
        }
    }

    private void agregarFilaAnimal(Animal a) { modeloAnimales.addRow(new Object[]{a.getCodigo(), a.getNombre(), a.getEspecie(), a.getEdadEstimada(), a.getEstadoClinico(), a.getEstadoAdopcion(), a.getUbicacion()}); }

    private void refrescarAdoptantes() {
        modeloAdoptantes.setRowCount(0);
        for (int i = 0; i < servicio.getTotalAdoptantes(); i++) { Adoptante a = servicio.getAdoptantes()[i]; if (a != null && a.isActivo()) modeloAdoptantes.addRow(new Object[]{a.getDpi(), a.getNombre(), a.getTelefono(), a.getCorreo(), a.getDireccion()}); }
    }

    private void refrescarSolicitudes() {
        modeloSolicitudes.setRowCount(0);
        for (int i = 0; i < servicio.getTotalSolicitudes(); i++) { Solicitud s = servicio.getSolicitudes()[i]; if (s != null) modeloSolicitudes.addRow(new Object[]{s.getCodigo(), s.getDpiAdoptante(), s.getCodigoAnimal(), s.getFecha(), s.getEstado()}); }
    }

    private void refrescarRescates() {
        modeloRescates.setRowCount(0);
        for (int i = 0; i < servicio.getTotalRescates(); i++) { Rescate r = servicio.getRescates()[i]; if (r != null) modeloRescates.addRow(new Object[]{r.getCodigo(), r.getDescripcion(), r.getUbicacionReporte(), r.getPrioridad(), r.getEstado(), r.getFecha()}); }
    }

    private void refrescarUbicaciones() {
        modeloUbicaciones.setRowCount(0);
        for (int f = 0; f < RefugioService.FILAS_UBICACION; f++) {
            Object[] fila = new Object[RefugioService.COLUMNAS_UBICACION + 1];
            fila[0] = "Área " + (f + 1);
            for (int c = 0; c < RefugioService.COLUMNAS_UBICACION; c++) fila[c + 1] = servicio.getUbicaciones()[f][c];
            modeloUbicaciones.addRow(fila);
        }
    }

    private void refrescarBitacora() {
        modeloBitacora.setRowCount(0);
        for (int i = 0; i < servicio.getTotalBitacora(); i++) { Bitacora b = servicio.getBitacora()[i]; if (b != null) modeloBitacora.addRow(new Object[]{b.getFechaHora(), b.getUsuario(), b.getAccion()}); }
    }

    private void guardar() {
        try { persistencia.guardarTodo(servicio); }
        catch (Exception ex) { error("No se pudieron guardar los datos: " + ex.getMessage()); }
    }

    private void cerrar() {
        servicio.registrarBitacora("Cierre de sesión");
        guardar();
        dispose();
        System.exit(0);
    }

    private static DefaultTableModel noEditable(String[] columnas) {
        return new DefaultTableModel(columnas, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
    }

    private JPanel basePanel() { JPanel p = new JPanel(new BorderLayout(8, 8)); p.setBorder(BorderFactory.createEmptyBorder(8, 8, 8, 8)); return p; }
    private String pedir(String mensaje) { return JOptionPane.showInputDialog(this, mensaje); }
    private void resultado(String r) { if ("OK".equals(r)) JOptionPane.showMessageDialog(this, "Operación realizada correctamente."); else error(r); }
    private void error(String mensaje) { JOptionPane.showMessageDialog(this, mensaje, "Aviso", JOptionPane.WARNING_MESSAGE); }
}
