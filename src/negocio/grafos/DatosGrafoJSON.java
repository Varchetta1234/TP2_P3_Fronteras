package negocio.grafos;

import java.util.List;

public class DatosGrafoJSON {
    private List<ProvinciaJSON> provincias;
    private List<FronteraJSON> fronteras;
    
    public List<ProvinciaJSON> getProvincias() { 
        return provincias; 
    }
    
    public List<FronteraJSON> getFronteras() { 
        return fronteras; 
    }
}