package modelo;

public class Animal {
    private String codigo;
    private String nombre;
    private String especie;
    private int edadEstimada;
    private String estadoClinico;
    private String estadoAdopcion;
    private String ubicacion;
    private boolean activo;

    public Animal(String codigo, String nombre, String especie, int edadEstimada,
                  String estadoClinico, String estadoAdopcion, String ubicacion, boolean activo) {
        this.codigo = codigo;
        this.nombre = nombre;
        this.especie = especie;
        this.edadEstimada = edadEstimada;
        this.estadoClinico = estadoClinico;
        this.estadoAdopcion = estadoAdopcion;
        this.ubicacion = ubicacion;
        this.activo = activo;
    }

    public String getCodigo() { return codigo; }
    public String getNombre() { return nombre; }
    public String getEspecie() { return especie; }
    public int getEdadEstimada() { return edadEstimada; }
    public String getEstadoClinico() { return estadoClinico; }
    public String getEstadoAdopcion() { return estadoAdopcion; }
    public String getUbicacion() { return ubicacion; }
    public boolean isActivo() { return activo; }

    public void setNombre(String nombre) { this.nombre = nombre; }
    public void setEspecie(String especie) { this.especie = especie; }
    public void setEdadEstimada(int edadEstimada) { this.edadEstimada = edadEstimada; }
    public void setEstadoClinico(String estadoClinico) { this.estadoClinico = estadoClinico; }
    public void setEstadoAdopcion(String estadoAdopcion) { this.estadoAdopcion = estadoAdopcion; }
    public void setUbicacion(String ubicacion) { this.ubicacion = ubicacion; }
    public void setActivo(boolean activo) { this.activo = activo; }

    public String toCSV() {
        return codigo + ";" + nombre + ";" + especie + ";" + edadEstimada + ";" +
               estadoClinico + ";" + estadoAdopcion + ";" + ubicacion + ";" + activo;
    }
}
