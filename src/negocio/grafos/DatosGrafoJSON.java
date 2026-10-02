package negocio.grafos;

import java.util.List;
//import java.util.ArrayList; Consultar al profe!tema encapsulamiento ...


public class DatosGrafoJSON {
    private List<ProvinciaJSON> provincias;
    private List<FronteraJSON> fronteras;
    
    public List<ProvinciaJSON> getProvincias() { 
        return provincias; 
    }
    
//    public List<ProvinciaJSON> getProvincias() { 
//        return new ArrayList<>(provincias); 
//    }

    
    public List<FronteraJSON> getFronteras() { 
        return fronteras; 
    }
}