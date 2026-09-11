package modelo;

public class Solicitud {
    private String codigo;
    private String dpiAdoptante;
    private String codigoAnimal;
    private String fecha;
    private String estado;

    public Solicitud(String codigo, String dpiAdoptante, String codigoAnimal, String fecha, String estado) {
        this.codigo = codigo;
        this.dpiAdoptante = dpiAdoptante;
        this.codigoAnimal = codigoAnimal;
        this.fecha = fecha;
        this.estado = estado;
    }

    public String getCodigo() { return codigo; }
    public String getDpiAdoptante() { return dpiAdoptante; }
    public String getCodigoAnimal() { return codigoAnimal; }
    public String getFecha() { return fecha; }
    public String getEstado() { return estado; }
    public void setEstado(String estado) { this.estado = estado; }

    public String toCSV() {
        return codigo + ";" + dpiAdoptante + ";" + codigoAnimal + ";" + fecha + ";" + estado;
    }
}
