package negocio.grafos;

import com.google.gson.Gson;
import negocio.SistemaRegiones;
import java.io.BufferedReader;
import java.io.FileReader;

public class CargadorDeDatos {

    public static Grafo cargarDesdeArchivo(String rutaArchivo, SistemaRegiones sistema) {
        Grafo grafo = new Grafo(24); 
        Gson gson = new Gson();

        try {
            BufferedReader br = new BufferedReader(new FileReader(rutaArchivo));
            DatosGrafoJSON datos = gson.fromJson(br, DatosGrafoJSON.class);
            br.close();

            // 1.Cargar los nombres en el mapa del sistema
            for (ProvinciaJSON p : datos.getProvincias()) {
                sistema.agregarProvincia(p.id, p.nombre,p.lat,p.lon);
            }

            // 2. Cargar las aristas en la matriz de adyacencia
            for (FronteraJSON f : datos.getFronteras()) {
                grafo.agregarArista(f.origen, f.destino, f.peso);
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
        return grafo;
    }
}