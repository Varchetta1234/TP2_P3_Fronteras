package negocio.algoritmos;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.Arrays;
import java.util.List;
import negocio.grafos.Grafo;

public class EliminaAristasTest {

    @Test
    public void unaSolaRegionTest() {
        
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(1, 2, 20);
        g.agregarArista(2, 3, 15);

        Grafo arbolK = EliminaAristas.obtenerArbolConKRegiones(g, 1);
        List<List<Integer>> regiones = EliminaAristas.encontrarRegiones(arbolK, 1);

        // Debe devolver exactamente 1 región
        assertEquals(1, regiones.size());
        // Esa unica regin debe contener a los 4 veertices
        assertEquals(4, regiones.get(0).size());
    }

    @Test
    public void maximaCantidadDeRegionesTest() {
        // Caso Borde: K = V (cantidad de vertices). 
    	//Debe borrar todas las aristas del AGM.
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(1, 2, 20);
        g.agregarArista(2, 3, 15);

        Grafo arbolK = EliminaAristas.obtenerArbolConKRegiones(g, 4);
        List<List<Integer>> regiones = EliminaAristas.encontrarRegiones(arbolK, 4);

        // Debe devolver exactamente 4 regiones
        assertEquals(4, regiones.size());
        
        // Cada region debe tener exactamente 1 provincia (quedaron todos aislados)
        for (List<Integer> region : regiones) {
            assertEquals(1, region.size());
        }
    }

    @Test
    public void divisionDosRegionesTest() {
        // Armamos dos grupos unidos por una arista pesada (1-2)
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 5);   // grupo A (barato)
        g.agregarArista(2, 3, 8);   // Grupo B (barato)
        g.agregarArista(1, 2, 100); // Arista a eliminar

        Grafo arbolK = EliminaAristas.obtenerArbolConKRegiones(g, 2);
        List<List<Integer>> regiones = EliminaAristas.encontrarRegiones(arbolK, 2);

        // Verificamos que haya devuelto 2 regiones
        assertEquals(2, regiones.size());

        // Verificamos la agrupación exacta. 
        // Como no sabemos si la lista 0 es el grupo A o el B, comprobamos las dos opciones.
        List<Integer> region1 = regiones.get(0);
        List<Integer> region2 = regiones.get(1);

        boolean gruposSeparadosCorrectamente = 
            (region1.containsAll(Arrays.asList(0, 1)) && region2.containsAll(Arrays.asList(2, 3))) ||
            (region1.containsAll(Arrays.asList(2, 3)) && region2.containsAll(Arrays.asList(0, 1)));

        assertTrue("Los grupos no se separaron por la arista más pesada", gruposSeparadosCorrectamente);
    }
    
    @Test
    public void tresRegionesTest() {
        // Grafo de 5 nodos. 
        // Nodos 0, 1, 2 muy juntos (pesos bajos).
        // Nodos 3 y 4 lejos de ese grupo y lejos entre sí (pesos muy altos).
        Grafo g = new Grafo(5);
        g.agregarArista(0, 1, 2);
        g.agregarArista(1, 2, 3);
        g.agregarArista(2, 3, 50); // Puente al 3
        g.agregarArista(3, 4, 80); // Puente al 4
        
        // Pedimos 3 regiones. Deberia borrar las dos de mayor peso (50 y 80).
        Grafo arbolK = EliminaAristas.obtenerArbolConKRegiones(g, 3);
        List<List<Integer>> regiones = EliminaAristas.encontrarRegiones(arbolK, 3);
        
        assertEquals(3, regiones.size());
    }
}