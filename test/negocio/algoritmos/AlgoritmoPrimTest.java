package negocio.algoritmos;

import static org.junit.Assert.*;
import org.junit.Test;
import java.util.List;
import negocio.grafos.Arista;
import negocio.grafos.Grafo;

public class AlgoritmoPrimTest {

    @Test
    public void grafoVacioTest() {
        Grafo g = new Grafo(0);
        
        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        assertEquals(0, agm.size());
    }

    @Test
    public void grafoUnVerticeTest() {
        
        Grafo g = new Grafo(1);
        
        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        assertEquals(0, agm.size());
    }

    @Test
    public void grafoInconexoTest() {
        // Dos islas separadas: {0,1} y {2,3}
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(2, 3, 20);

        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        // Como el grafo está desconectado, Prim no podrá encontrar V-1 aristas.
        // Solo conectará lo que alcance desde su nodo de inicio.
        assertTrue(agm.size() < 3);
    }
    
    @Test
    public void grafoCaminoSimpleTest() {
        // Grafo en forma de línea recta: 0 - 1 - 2 - 3
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 10);
        g.agregarArista(1, 2, 20);
        g.agregarArista(2, 3, 30);

        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        // V=4, debe tener 3 aristas.
        assertEquals(3, agm.size());
        // El único camino posible suma 60 (10 + 20 + 30)
        assertEquals(60, calcularPesoTotal(agm));
    }

    @Test
    public void grafoCompletoTest() {
        // Todos conectados con todos. V=4
        Grafo g = new Grafo(4);
        g.agregarArista(0, 1, 1);
        g.agregarArista(0, 2, 4);
        g.agregarArista(0, 3, 3);
        g.agregarArista(1, 2, 2);
        g.agregarArista(1, 3, 5);
        g.agregarArista(2, 3, 6);

        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        assertEquals(3, agm.size());
        // Las aristas más baratas que conectan a todos son: 
        //(0,1)=1, (1,2)=2, (0,3)=3. Total = 6.
        assertEquals(6, calcularPesoTotal(agm));
    }

    @Test
    public void grafoConMultiplesCaminosTest() {
        // Un grafo normal donde el algoritmo debe elegir el desvío más barato
        Grafo g = new Grafo(5);
        g.agregarArista(0, 1, 10);
        g.agregarArista(0, 2, 20);
        g.agregarArista(1, 3, 50); // Camino muy caro, Prim lo debe evitar
        g.agregarArista(2, 3, 20); // Debe ir por acá
        g.agregarArista(2, 4, 33); // Camino caro
        g.agregarArista(3, 4, 10); // Debe ir por acá para conectar el 4

        List<Arista> agm = AlgoritmoPrim.ejecutar(g);
        
        assertEquals(4, agm.size());
        // El arbol optimo es: 0-1 (10), 0-2 (20), 2-3 (20), 3-4 (10). Total = 60.
        assertEquals(60, calcularPesoTotal(agm));
    }

    // metodo auxiliar 
    private int calcularPesoTotal(List<Arista> agm) {
        int total = 0;
        for (Arista a : agm) {
            total += a.getPeso();
        }
        return total;
    }
}
