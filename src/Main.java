import interfaz.VentanaLogin;
import persistencia.Persistencia;
import servicio.RefugioService;

import javax.swing.*;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                RefugioService servicio = new RefugioService();
                Persistencia persistencia = new Persistencia("datos");
                persistencia.prepararUsuarios();
                persistencia.cargarTodo(servicio);
                new VentanaLogin(persistencia, servicio).setVisible(true);
            } catch (Exception e) {
                JOptionPane.showMessageDialog(null, "No se pudo iniciar el sistema: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
            }
        });
    }
}
