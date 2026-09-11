package modelo;

public class Rescate {
    private String codigo;
    private String descripcion;
    private String ubicacionReporte;
    private String prioridad;
    private String estado;
    private String fecha;

    public Rescate(String codigo, String descripcion, String ubicacionReporte,
                   String prioridad, String estado, String fecha) {
        this.codigo = codigo;
        this.descripcion = descripcion;
        this.ubicacionReporte = ubicacionReporte;
        this.prioridad = prioridad;
        this.estado = estado;
        this.fecha = fecha;
    }

    public String getCodigo() { return codigo; }
    public String getDescripcion() { return descripcion; }
    public String getUbicacionReporte() { return ubicacionReporte; }
    public String getPrioridad() { return prioridad; }
    public String getEstado() { return estado; }
    public String getFecha() { return fecha; }
    public void setPrioridad(String prioridad) { this.prioridad = prioridad; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toCSV() {
        return codigo + ";" + descripcion + ";" + ubicacionReporte + ";" + prioridad + ";" + estado + ";" + fecha;
    }
}
