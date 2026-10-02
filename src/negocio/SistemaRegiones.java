package negocio;

import negocio.grafos.Grafo;
import negocio.grafos.Provincia;
import negocio.grafos.CargadorDeDatos; 
import negocio.algoritmos.EliminaAristas;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class SistemaRegiones {
    
    private Grafo grafo;
    private Grafo grafoResultante;
    private Map<Integer, Provincia> mapaProvincias;

    public SistemaRegiones() {
        this.mapaProvincias = new HashMap<>(); 
        try {
            this.grafo = CargadorDeDatos.cargarDesdeArchivo("provincias.json", this); 
        } catch (Exception e) {
            e.printStackTrace();
            this.grafo = new Grafo(24);
        }
    }

    public void agregarProvincia(int id, String nombre, double lat, double lon) {
        mapaProvincias.put(id, new Provincia(id, nombre, lat, lon));
    }

    public void registrarFrontera(int id1, int id2, int similaridad) {
        grafo.agregarArista(id1, id2, similaridad);
    }
    
    
    public int cantidadProvincias() {
        return (this.grafo != null) ? this.grafo.tamano() : 24;
    }

    
    public boolean existeFrontera(int i, int j) {
        if (this.grafo == null) return false;
        return this.grafo.existeArista(i, j);
    }

    
    public boolean existeFronteraResultante(int i, int j) {
        if (this.grafoResultante == null) return false;
        return this.grafoResultante.existeArista(i, j);
    }

    public List<List<Integer>> generarRegiones(int k) {
        if (k <= 0 || k > grafo.tamano()) {
            throw new IllegalArgumentException("Cantidad de regiones K invalida");
        }
        
        // 1. Obtener y guardar el grafo (bosque) resultante con las aristas ya eliminadas
        this.grafoResultante = EliminaAristas.obtenerArbolConKRegiones(this.grafo, k);
        
        // 2. Extraer las componentes conexas a partir de ese grafo resultante
        return EliminaAristas.encontrarRegiones(this.grafoResultante, k);
    }

    public Provincia obtenerProvincia(int id) {
        return mapaProvincias.get(id);
    }

    public void reiniciarAristas() {
        this.grafo = new Grafo(24);
    }

    public void recargarDesdeJSON() {
        try {
            this.grafo = CargadorDeDatos.cargarDesdeArchivo("provincias.json", this); 
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}