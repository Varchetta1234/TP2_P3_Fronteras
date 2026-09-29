package negocio.grafos;

import static org.junit.Assert.*;
import org.junit.Test;

public class EdicionDeAristasTest
{
	@Test(expected = IllegalArgumentException.class)
	public void primerVerticeNegativoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(-1, 3, 10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void primerVerticeExcedidoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(5, 2, 10);
	}

	@Test(expected = IllegalArgumentException.class)
	public void segundoVerticeNegativoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, -1, 10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void segundoVerticeExcedidoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 5, 10);
	}
	
	@Test(expected = IllegalArgumentException.class)
	public void agregarLoopTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 2, 10);
	}

	// TESTS PARA LOS PESOS

	@Test(expected = IllegalArgumentException.class)
	public void pesoCeroTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 0); 
	}

	@Test(expected = IllegalArgumentException.class)
	public void pesoNegativoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, -5); 
	}

	@Test
	public void aristaPesoCorrectoTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 42);
		
		assertEquals(42, grafo.pesoArista(2, 3));
		assertEquals(42, grafo.pesoArista(3, 2)); 
	}
	
	// TEST ARISTASS
	
	@Test
	public void aristaExistenteTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 10);
		assertTrue( grafo.existeArista(2, 3) );
	}

	@Test
	public void aristaOpuestaTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 10);
		assertTrue( grafo.existeArista(3, 2) );
	}

	@Test
	public void aristaInexistenteTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 10);
		assertFalse( grafo.existeArista(1, 4) );
	}

	@Test
	public void agregarAristaDosVecesTest()
	{		
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 3, 10);
		grafo.agregarArista(2, 3, 10);

		assertTrue( grafo.existeArista(2, 3) );
	}
	
	@Test
	public void eliminarAristaExistenteTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 4, 10);
		
		grafo.eliminarArista(2, 4);
		assertFalse( grafo.existeArista(2, 4) );
	}

	@Test
	public void eliminarAristaInexistenteTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.eliminarArista(2, 4);
		assertFalse( grafo.existeArista(2, 4) );
	}
	
	@Test
	public void eliminarAristaDosVecesTest()
	{
		Grafo grafo = new Grafo(5);
		grafo.agregarArista(2, 4, 10);
		
		grafo.eliminarArista(2, 4);
		grafo.eliminarArista(2, 4);
		assertFalse( grafo.existeArista(2, 4) );
	}
}