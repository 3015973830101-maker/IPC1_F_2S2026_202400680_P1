package interfaz;

import modelo.Usuario;
import persistencia.Persistencia;
import servicio.RefugioService;

import javax.swing.*;
import java.awt.*;

public class VentanaLogin extends JFrame {
    private final Persistencia persistencia;
    private final RefugioService servicio;
    private final JTextField txtUsuario = new JTextField(18);
    private final JPasswordField txtClave = new JPasswordField(18);

    public VentanaLogin(Persistencia persistencia, RefugioService servicio) {
        this.persistencia = persistencia;
        this.servicio = servicio;
        construir();
    }

    private void construir() {
        setTitle("Centro de Rescate Animal - Inicio de sesión");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(430, 280);
        setLocationRelativeTo(null);
        setResizable(false);

        JPanel principal = new JPanel(new BorderLayout(10, 10));
        principal.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titulo = new JLabel("Centro de Rescate Animal", SwingConstants.CENTER);
        titulo.setFont(new Font("SansSerif", Font.BOLD, 22));
        principal.add(titulo, BorderLayout.NORTH);

        JPanel formulario = new JPanel(new GridLayout(4, 1, 5, 5));
        formulario.add(new JLabel("Usuario:"));
        formulario.add(txtUsuario);
        formulario.add(new JLabel("Contraseña:"));
        formulario.add(txtClave);
        principal.add(formulario, BorderLayout.CENTER);

        JButton btnIngresar = new JButton("Ingresar");
        btnIngresar.addActionListener(e -> ingresar());
        principal.add(btnIngresar, BorderLayout.SOUTH);
        getRootPane().setDefaultButton(btnIngresar);
        add(principal);
    }

    private void ingresar() {
        String usuario = txtUsuario.getText().trim();
        String clave = new String(txtClave.getPassword());
        if (usuario.isEmpty() || clave.isEmpty()) {
            JOptionPane.showMessageDialog(this, "Ingrese usuario y contraseña.", "Validación", JOptionPane.WARNING_MESSAGE);
            return;
        }
        try {
            Usuario u = persistencia.autenticar(usuario, clave);
            if (u == null) {
                JOptionPane.showMessageDialog(this, "Credenciales incorrectas.", "Acceso denegado", JOptionPane.ERROR_MESSAGE);
                return;
            }
            servicio.setUsuarioActual(u.getUsuario());
            servicio.registrarBitacora("Inicio de sesión como " + u.getRol());
            VentanaPrincipal vp = new VentanaPrincipal(servicio, persistencia, u);
            vp.setVisible(true);
            dispose();
        } catch (Exception ex) {
            JOptionPane.showMessageDialog(this, "Error al leer usuarios: " + ex.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
}
