package modelo;

public class Bitacora {
    private String fechaHora;
    private String usuario;
    private String accion;

    public Bitacora(String fechaHora, String usuario, String accion) {
        this.fechaHora = fechaHora;
        this.usuario = usuario;
        this.accion = accion;
    }

    public String getFechaHora() { return fechaHora; }
    public String getUsuario() { return usuario; }
    public String getAccion() { return accion; }

    public String toCSV() {
        return fechaHora + ";" + usuario + ";" + accion;
    }
}
