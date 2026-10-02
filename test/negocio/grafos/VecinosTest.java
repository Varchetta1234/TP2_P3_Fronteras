package negocio.grafos;

import static org.junit.Assert.*;
import org.junit.Test;

public class VecinosTest{
	
	@Test(expected = IllegalArgumentException.class)
	public void verticeNegativoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.vecinos(-1);
	}

	@Test(expected = IllegalArgumentException.class)
	public void verticeExcedidoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.vecinos(5); // El grafo de tamaño 5 tiene vertices del 0 al 4
	}

	@Test
	public void todosAisladosTest()
	{
		Grafo grafo = new Grafo(5);
		assertEquals(0, grafo.vecinos(2).size());
	}
	
	@Test
	public void verticeUniversalTest()
	{
		Grafo grafo = new Grafo(4);
		
		grafo.agregarArista(1, 0, 10);
		grafo.agregarArista(1, 2, 10);
		grafo.agregarArista(1, 3, 10);
		
		int[] esperado = {0, 2, 3};
		AssertAuxiliar.iguales(esperado, grafo.vecinos(1));
	}
	
	@Test
	public void verticeNormalTest()
	{
		Grafo grafo = new Grafo(5);

		grafo.agregarArista(1, 3, 10);
		grafo.agregarArista(2, 3, 10);
		grafo.agregarArista(2, 4, 10);
		
		int[] esperados = {1, 2};
		AssertAuxiliar.iguales(esperados, grafo.vecinos(3));
	}
}