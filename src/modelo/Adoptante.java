package modelo;

public class Adoptante {
    private String dpi;
    private String nombre;
    private String telefono;
    private String correo;
    private String direccion;
    private boolean activo;

    public Adoptante(String dpi, String nombre, String telefono, String correo, String direccion, boolean activo) {
        this.dpi = dpi;
        this.nombre = nombre;
        this.telefono = telefono;
        this.correo = correo;
        this.direccion = direccion;
        this.activo = activo;
    }

    public String getDpi() { return dpi; }
    public String getNombre() { return nombre; }
    public String getTelefono() { return telefono; }
    public String getCorreo() { return correo; }
    public String getDireccion() { return direccion; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setTelefono(String telefono) { this.telefono = telefono; }
    public void setCorreo(String correo) { this.correo = correo; }
    public void setDireccion(String direccion) { this.direccion = direccion; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String toCSV() {
        return dpi + ";" + nombre + ";" + telefono + ";" + correo + ";" + direccion + ";" + activo;
    }
}
