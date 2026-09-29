package negocio.algoritmos;

import negocio.grafos.Arista;
import negocio.grafos.Grafo;
import java.util.ArrayList;
import java.util.List;


//implementacion de Prim con arreglo simple y matris de adyacencia 
public class AlgoritmoPrim {
    
    public static List<Arista> ejecutar(Grafo grafo) {
  
    	int n = grafo.tamano();
        boolean[] visitados = new boolean[n];
        double[] distancias = new double[n];
        int[] padres = new int[n];
        List<Arista> agm = new ArrayList<>();
        
        if (n <= 1)
            return agm;

        for (int i = 0; i < n; i++) {
            distancias[i] = Double.MAX_VALUE;
            padres[i] = -1;
        }

        distancias[0] = 0.0;

        for (int i = 0; i < n - 1; i++) {
            int u = minKey(distancias, visitados, n);
            if (u == -1) break;
            
            visitados[u] = true;

            for (int v : grafo.vecinos(u)) {
                double peso = grafo.pesoArista(u, v);
                if (!visitados[v] && peso < distancias[v]) {
                    padres[v] = u;
                    distancias[v] = peso;
                }
            }
        }

        for (int i = 1; i < n; i++) {
            if (padres[i] != -1) {
                agm.add(new Arista(padres[i], i, grafo.pesoArista(padres[i], i)));
            }
        }
        return agm;
    }

    private static int minKey(double[] distancias, boolean[] visitados, int n) {
        double min = Double.MAX_VALUE;
        int minIndex = -1;
        for (int v = 0; v < n; v++) {
            if (!visitados[v] && distancias[v] < min) {
                min = distancias[v];
                minIndex = v;
            }
        }
        return minIndex;
    }
}