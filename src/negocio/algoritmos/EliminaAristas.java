package negocio.algoritmos;

import negocio.grafos.Arista;
import negocio.grafos.Grafo;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Set;

public class EliminaAristas {

    public static Grafo obtenerArbolConKRegiones(Grafo grafoOriginal, int k) {
        List<Arista> agmAristas = AlgoritmoPrim.ejecutar(grafoOriginal);
        Collections.sort(agmAristas, Collections.reverseOrder());
        
        Grafo arbolRegiones = new Grafo(grafoOriginal.tamano());
        
        for (int i = k - 1; i < agmAristas.size(); i++) {
            Arista a = agmAristas.get(i);
            arbolRegiones.agregarArista(a.getOrigen(), a.getDestino(), a.getPeso());
        }

        return arbolRegiones;
    }

    public static List<List<Integer>> encontrarRegiones(Grafo arbolRegiones, int k) {
        List<List<Integer>> componentes = new ArrayList<>();
        boolean[] visitadosGenerales = new boolean[arbolRegiones.tamano()];

        // Recorremos todas las provincias
        for (int i = 0; i < arbolRegiones.tamano(); i++) {
            // Si la provincia no pertenece a ninguna region calculada todabia:
            if (!visitadosGenerales[i]) {
                
                // Llamamos a BFS para descubrir toda la región
                Set<Integer> regionEncontrada = BFS.alcanzables(arbolRegiones, i);
                
                // Agregamos la región a nuestra lista final
                componentes.add(new ArrayList<>(regionEncontrada));
                
                // Marcamos todas las provincias de esta región como visitadas
                for (int vertice : regionEncontrada) {
                    visitadosGenerales[vertice] = true;
                }
            }
        }
        
        return componentes;
    }
}