package negocio;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;

public class SistemaRegionesTest {

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConBucleTest() {
        SistemaRegiones sistema = new SistemaRegiones(); 
        sistema.registrarFrontera(1, 1, 10);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConPesoNegativoTest() {
        SistemaRegiones sistema = new SistemaRegiones();
        sistema.registrarFrontera(0, 1, -15);
    }

    @Test(expected = IllegalArgumentException.class)
    public void fronteraConPesoCeroTest() {
        SistemaRegiones sistema = new SistemaRegiones();
        sistema.registrarFrontera(0, 1, 0);
    }

    @Test
    public void integracionGenerarRegionesTest() {
        SistemaRegiones sistema = new SistemaRegiones();
        
        // Creamos una línea recta: 0 - 1 - 2 - 3
        sistema.registrarFrontera(0, 1, 10);
        sistema.registrarFrontera(1, 2, 100); // peso 100
        sistema.registrarFrontera(2, 3, 10);
        
        // Pedimos dividir el país en 2, se tendria que eliminar la de 100
        List<List<Integer>> regiones = sistema.generarRegiones(2);
        
        assertEquals(2, regiones.size());
        
        // Verificamos que no haya perdido ninguna provincia en el proceso: dudoso a continuacion
        int totalProvincias = 0;
        for (List<Integer> region : regiones) {
            totalProvincias += region.size();
        }
        assertEquals(24, totalProvincias);
    }
}
