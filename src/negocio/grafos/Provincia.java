package negocio.grafos;

public class Provincia {
    private int id;
    private String nombre;
    private double lat;
    private double lon;

    public Provincia(int id, String nombre, double lat, double lon) {
        this.id = id;
        this.nombre = nombre;
        this.lat = lat;
        this.lon = lon;
    }

    public int getId() {
        return id;
    }

    public String getNombre() {
        return nombre;
    }

    public double getLat() {
        return lat;
    }

    public double getLon() {
        return lon;
    }
}