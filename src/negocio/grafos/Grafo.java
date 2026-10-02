package negocio.grafos;

import java.util.HashSet;
import java.util.Set;

//Mismo grafo que el trabajado en clase pero con pesos
public class Grafo {
    private boolean[][] A;
    private int[][] pesos;

    public Grafo(int vertices) {
        A = new boolean[vertices][vertices];
        pesos = new int[vertices][vertices];
    }

    public void agregarArista(int i, int j, int peso) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);
        
        if (peso <= 0) {
            throw new IllegalArgumentException("El peso debe ser estrictamente mayor a 0.");
        }
        
        A[i][j] = true;
        A[j][i] = true;
        pesos[i][j] = peso;
        pesos[j][i] = peso;        
    }

    public void eliminarArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);
        A[i][j] = false;
        A[j][i] = false;
        pesos[i][j] = 0;
        pesos[j][i] = 0;
    }

    public boolean existeArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        verificarDistintos(i, j);
        return A[i][j];
    }

    public int pesoArista(int i, int j) {
        verificarVertice(i);
        verificarVertice(j);
        return pesos[i][j];
    }

    public int tamano() {
        return A.length;
    }

    public Set<Integer> vecinos(int i) {
        verificarVertice(i);
        Set<Integer> ret = new HashSet<>();
        for (int j = 0; j < this.tamano(); ++j) {
            if (i != j && this.existeArista(i, j)) {
                ret.add(j);
            }
        }
        return ret;
    }

    private void verificarVertice(int i) {
        if (i < 0) throw new IllegalArgumentException("Vertice negativo: " + i);
        if (i >= A.length) throw new IllegalArgumentException("Vertice excedido: " + i);
    }

    private void verificarDistintos(int i, int j) {
        if (i == j) throw new IllegalArgumentException("Loops no permitidos: " + i);
    }
}
