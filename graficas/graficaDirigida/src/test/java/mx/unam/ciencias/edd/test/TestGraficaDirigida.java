package mx.unam.ciencias.edd.test;

import java.util.NoSuchElementException;
import java.util.Random;
import mx.unam.ciencias.edd.AccionVerticeGrafica;
import mx.unam.ciencias.edd.Grafica;
import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.VerticeGrafica;
import org.junit.Assert;
import org.junit.Rule;
import org.junit.Test;
import org.junit.rules.Timeout;
import java.util.*;

/**
 * Clase para pruebas unitarias de la clase {@link Grafica}.
 */
public class TestGraficaDirigida {

    /** Expiración para que ninguna prueba tarde más de 5 segundos. */
    @Rule public Timeout expiracion = Timeout.seconds(5);

    /* Generador de números aleatorios. */
    private Random random;
    /* Número total de elementos. */
    private int total;
    /* La gráfica. */
    private GraficaDirigida<Integer> grafica;

    /**
     * Crea una gráfica para cada prueba.
     */
    public TestGraficaDirigida() {
        random = new Random();
        total = 2 + random.nextInt(100);
        grafica = new GraficaDirigida<Integer>();
    }

    /**
     * Prueba unitaria para {@link Grafica#Grafica}.
     */
    @Test public void testConstructor() {
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#getElementos}.
     */
    @Test public void testGetElementos() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.getElementos() == i+1);
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#getAristas}.
     */
    @Test public void testGetAristas() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int cont = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                cont++;
                Assert.assertTrue(grafica.getAristas() == cont);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#agrega}.
     */
    @Test public void testAgrega() {
        try {
            grafica.agrega(null);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);  // Aquí parece estar funcionando bien
            Assert.assertTrue(grafica.contiene(i));  // Verifica si este método se cuelga
        }
        try {
            grafica.agrega(total/2);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta}.
     */
    @Test public void testConecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getAristas() == ++c);
                Assert.assertTrue(grafica.getPeso(i, j) == 1.0);
                //Assert.assertTrue(grafica.getPeso(j, i) == 1.0);
            }
        }
        try {
            grafica.conecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.conecta(0, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#conecta(Object,Object,double)}.
     */
    @Test public void testConectaConPeso() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                double peso = random.nextDouble() * total;
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j, peso);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getAristas() == ++c);
                Assert.assertTrue(grafica.getPeso(i, j) == peso);
                //Assert.assertTrue(grafica.getPeso(j, i) == peso);
            }
        }
        try {
            grafica.conecta(0, 0, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.conecta(-1, -2, 1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.conecta(0, 1, 1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        grafica.agrega(total);
        try {
            grafica.conecta(0, total, -1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#desconecta}.
     */
    @Test public void testDesconecta() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int c = (total * (total-1)) / 2;
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertTrue(grafica.getAristas() == c--);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                grafica.desconecta(i, j);
                Assert.assertFalse(grafica.sonVecinos(i, j));
            }
        }
        Assert.assertTrue(grafica.getAristas() == 0);
        try {
            grafica.desconecta(0, 0);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
        try {
            grafica.desconecta(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#contiene}.
     */
    @Test public void testContiene() {
        for (int i = 0; i < total; i++) {
            grafica.agrega(i);
            Assert.assertTrue(grafica.contiene(i));
        }
        Assert.assertFalse(grafica.contiene(-1));
    }

    /**
     * Prueba unitaria para {@link Grafica#sonVecinos}.
     */
    @Test public void testSonVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                Assert.assertFalse(grafica.sonVecinos(i, j));
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
            }
        }
        try {
            grafica.sonVecinos(-1, -2);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#getPeso}.
     */
    @Test public void testGetPeso() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                try {
                    grafica.getPeso(i, j);
                    Assert.fail();
                } catch (IllegalArgumentException iae) {}
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getPeso(i, j) == 1.0);
                //Assert.assertTrue(grafica.getPesoApuntador(j, i) == 1.0);
            }
        }
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                double peso = random.nextDouble() * total;
                Assert.assertTrue(grafica.getPeso(i, j) == 1.0);
                //Assert.assertTrue(grafica.getPesoApuntador(j, i) == 1.0);
                grafica.setPeso(i, j, peso);
                Assert.assertTrue(grafica.getPeso(i, j) == peso);
                //Assert.assertTrue(grafica.getPeso(j, i) == peso);
            }
        }
        try {
            grafica.getPeso(-1, 0);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.getPeso(0, -1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.getPeso(-1, -1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
    }

    /**
     * Prueba unitaria para {@link Grafica#setPeso}.
     */
    @Test public void testSetPeso() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                grafica.conecta(i, j);
                Assert.assertTrue(grafica.sonVecinos(i, j));
                Assert.assertTrue(grafica.getPeso(i, j) == 1.0);
                //Assert.assertTrue(grafica.getPeso(j, i) == 1.0);
                double peso = random.nextDouble() * total;
                grafica.setPeso(i, j, peso);
                Assert.assertTrue(grafica.getPeso(i, j) == peso);
                //Assert.assertTrue(grafica.getPeso(j, i) == peso);
            }
        }
        try {
            grafica.setPeso(-1, 0, 1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.setPeso(0, -1, 1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        try {
            grafica.setPeso(-1, -1, 1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        grafica.agrega(total);
        grafica.conecta(0, total);
        try {
            grafica.setPeso(0, total, -1);
            Assert.fail();
        } catch (IllegalArgumentException iae) {}
    }

   /**
     * Prueba unitaria para {@link Grafica#vertice}.
     */
    @Test public void testVertice() {
        try {
            grafica.vertice(-1);
            Assert.fail();
        } catch (NoSuchElementException nsee) {}
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        VerticeGrafica<Integer> vertice = grafica.vertice(0);
        Assert.assertTrue(vertice.get() == 0);
        Assert.assertTrue(vertice.getGrado() == total - 1);
        int v = 1;
        List<Integer> vecinos = new ArrayList<Integer>();
        for (int i = 1; i < total; i++)
            vecinos.add(i);
        for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
            Assert.assertTrue(vecinos.contains(vecino.get()));
            vecinos.remove(vecino.get());
        }
        Assert.assertTrue(vecinos.isEmpty());
        Assert.assertTrue(vecinos.size() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#esConexa}.
     */
    @Test public void testEsConexa() {
        if (total < 6)
            total += 6;
        grafica.agrega(0);
        for (int i = 1; i < total; i++) {
            grafica.agrega(i);
            Assert.assertFalse(grafica.esConexa());
            grafica.conecta(i-1, i);
            Assert.assertTrue(grafica.esConexa());
        }
        grafica.limpia();
        for (int i = 0; i < total/2; i++)
            grafica.agrega(i);
        for (int i = 0; i < total/2; i++)
            for (int j = i+1; j < total/2; j++)
                grafica.conecta(i, j);
        Assert.assertTrue(grafica.esConexa());
        for (int i = total/2; i < total; i++)
            grafica.agrega(i);
        for (int i = total/2; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Assert.assertFalse(grafica.esConexa());
        grafica.conecta(total/2-1, total/2);
        Assert.assertTrue(grafica.esConexa());
    }

   /**
     * Prueba unitaria para {@link Grafica#paraCadaVertice}.
     */
    @Test public void testParaCadaVertice() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        int[] cont = { 0 };
        grafica.paraCadaVertice(v -> { cont[0]++; });
        Assert.assertTrue(cont[0] == total);
    }

   /**
     * Prueba unitaria para {@link Grafica#bfs}.
     */
    @Test public void testBfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 1, 2, 3, 4, 5, 6 };
        grafica.bfs(0, v -> Assert.assertTrue(v.get() == a[c[0]++]));
        grafica = new GraficaDirigida<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        List<Integer> lista = new ArrayList<Integer>();
        grafica.bfs(0, v -> lista.add(v.get()));
        List<Integer> vertices = new ArrayList<Integer>();
        for (int i = 0; i < total; i++)
            vertices.add(i);
        Assert.assertTrue(lista.size() == vertices.size());

        // Usamos un índice para iterar y verificar los elementos
        while (!lista.isEmpty()) {
            Integer elemento = lista.remove(0); // Removemos el primer elemento
            Assert.assertTrue(vertices.contains(elemento));
        }     
    }

    /**
     * Prueba unitaria para {@link Grafica#dfs}.
     */
    @Test public void testDfs() {
        for (int i = 0; i < 7; i++)
            grafica.agrega(i);
        grafica.conecta(0, 1);
        grafica.conecta(0, 2);
        grafica.conecta(1, 3);
        grafica.conecta(1, 4);
        grafica.conecta(3, 5);
        grafica.conecta(3, 6);
        int[] c = { 0 };
        int[] a = { 0, 2, 1, 4, 3, 6, 5 };
        grafica.dfs(0, v -> Assert.assertTrue(v.get() == a[c[0]++]));
        grafica = new GraficaDirigida<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        List<Integer> lista = new ArrayList<Integer>();
        grafica.dfs(0, v -> lista.add(v.get()));
        List<Integer> vertices = new ArrayList<Integer>();
        for (int i = 0; i < total; i++)
            vertices.add(i);
        Assert.assertTrue(lista.size() == vertices.size());

        
        // Utilizamos un índice para iterar sobre `lista`
        while (!lista.isEmpty()) {
            Integer elemento = lista.remove(0); // Removemos el primer elemento
            Assert.assertTrue(vertices.contains(elemento));
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#esVacia}.
     */
    @Test public void testEsVacio() {
        Assert.assertTrue(grafica.esVacia());
        grafica.agrega(0);
        Assert.assertFalse(grafica.esVacia());
    }

    /**
     * Prueba unitaria para {@link Grafica#limpia}.
     */
    @Test public void testLimpia() {
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        Assert.assertFalse(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == total);
        grafica.limpia();
        Assert.assertTrue(grafica.esVacia());
        Assert.assertTrue(grafica.getElementos() == 0);
        Assert.assertTrue(grafica.getAristas() == 0);
    }

    /**
     * Prueba unitaria para {@link Grafica#toString}.
     */
    @Test public void testToString() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++){
                grafica.conecta(i, j);
                grafica.conecta(j, i);   
            }
        String s = "{";
        for (int i = 0; i < total; i++)
            s += String.format("%d, ", i);
        s += "}, {";
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                s += String.format("(%d, %d), ", i, j);
        s += "}";
        Assert.assertTrue(grafica.toString().equals(s));
    }

    /**
     * Prueba unitaria para {@link Grafica#equals}.
     */
    @Test public void testEquals() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++){
                grafica.conecta(i, j);
                grafica.conecta(j, i);
            }
        GraficaDirigida<Integer> otra = new GraficaDirigida<Integer>();
        for (int i = total - 1; i > -1; i--)
            otra.agrega(i);
        Assert.assertFalse(grafica.equals(otra));
        for (int i = total - 1; i > -1; i--)
            for (int j = 0; j < i; j++){
                otra.conecta(i, j);
                otra.conecta(j, i);
            }
        Assert.assertTrue(grafica.equals(otra));
        grafica = new GraficaDirigida<Integer>();
        otra = new GraficaDirigida<Integer>();
        for (int i = 0; i < 4; i++) {
            grafica.agrega(i);
            otra.agrega(i);
        }
        grafica.conecta(0, 1);
        grafica.conecta(2, 3);
        otra.conecta(0, 2);
        otra.conecta(1, 3);
        Assert.assertFalse(grafica.equals(otra));
    }

    /**
     * Prueba unitaria para {@link Grafica#iterator}.
     */
    @Test public void testIterator() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        int c = 0;
        for (Integer i : grafica)
            Assert.assertTrue(i == c++);
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#get} a
     * través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGet() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(i == vertice.get());
        }
    }

    /**
     * Prueba unitaria para la implementación {@link VerticeGrafica#getGrado}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeGetGrado() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++){
                grafica.conecta(i, j);
                grafica.conecta(j, i);
            }
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            Assert.assertTrue(vertice.getGrado() == total - 1);
        }
    }

    /**
     * Prueba unitaria para la implementación de {@link VerticeGrafica#vecinos}
     * a través del método {@link Grafica#vertice}.
     */
    @Test public void testVerticeVecinos() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        for (int i = 0; i < total; i++)
            for (int j = i+1; j < total; j++)
                grafica.conecta(i, j);
        for (int i = 0; i < total; i++) {
            VerticeGrafica<Integer> vertice = grafica.vertice(i);
            int c = 0;
            for (VerticeGrafica<Integer> vecino : vertice.vecinos()) {
                int e = vecino.get();
                Assert.assertTrue(e >= 0);
                Assert.assertTrue(e < total);
                Assert.assertFalse(e == i);
                c++;
            }
            Assert.assertTrue(vertice.getGrado() == c);
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#trayectoriaMinima}.
     */
    @Test public void testTrayectoriaMinima() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        List<VerticeGrafica<Integer>> lista =
            new ArrayList<VerticeGrafica<Integer>>();
        lista.add(grafica.vertice(0));
        List<VerticeGrafica<Integer>> tm =
                grafica.trayectoriaMinima(0, 0);
        Assert.assertTrue(lista.equals(tm));
        for (int i = 1; i < total; i++) {
            grafica.conecta(i-1, i);
            lista.add(grafica.vertice(i));
            tm = grafica.trayectoriaMinima(0, i);
            Assert.assertTrue(lista.equals(tm));
        }
    }

    /**
     * Prueba unitaria para {@link Grafica#dijkstra}.
     */
    @Test public void testDijkstra() {
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        List<VerticeGrafica<Integer>> lista =
            new ArrayList<VerticeGrafica<Integer>>();
        for (int i = 0; i < total; i++) {
            for (int j = i+1; j < total; j++) {
                double peso = (i + 1 == j) ? 1 :
                    total * 5.0 + random.nextDouble() * total * 5.0;
                grafica.conecta(i, j, peso);
            }
        }
        for (int i = 0; i < total; i++) {
            lista.add(grafica.vertice(i));
            List<VerticeGrafica<Integer>> dijkstra =
                grafica.dijkstra(0, i);
            Assert.assertTrue(lista.equals(dijkstra));
        }
        total = 500 + random.nextInt(500);
        grafica = new GraficaDirigida<Integer>();
        for (int i = 0; i < total; i++)
            grafica.agrega(i);
        lista = new ArrayList<VerticeGrafica<Integer>>();
        for (int i = 0; i < total; i++) {
            int m = Math.min(i+4, total);
            for (int j = i+1; j < m; j++) {
                double peso = (i + 1 == j) ? 1 :
                    total * 5.0 + random.nextDouble() * total * 5.0;
                grafica.conecta(i, j, peso);
            }
        }
        for (int i = 0; i < total; i++) {
            lista.add(grafica.vertice(i));
            List<VerticeGrafica<Integer>> dijkstra =
                grafica.dijkstra(0, i);
            Assert.assertTrue(lista.equals(dijkstra));
        }
    }

    @Test public void testCombinarGraficas() {
        // Crear gráficas individuales
        GraficaDirigida<Integer> grafica1 = new GraficaDirigida<>();
        grafica1.agrega(1); // Agregar vértice 1
        grafica1.agrega(2); // Agregar vértice 2
        grafica1.agrega(3); // Agregar vértice 3
        grafica1.conecta(1, 2); // 1 → 2
        grafica1.conecta(2, 3); // 2 → 3

        GraficaDirigida<Integer> grafica2 = new GraficaDirigida<>();
        grafica2.agrega(3); // Agregar vértice 3
        grafica2.agrega(4); // Agregar vértice 4
        grafica2.agrega(5);
        grafica2.agrega(2);
        grafica2.conecta(3, 2); //3 -> 2
        grafica2.conecta(3, 4); // 3 → 4
        grafica2.conecta(4, 5); // 4 → 5

        GraficaDirigida<Integer> grafica3 = new GraficaDirigida<>();
        grafica3.agrega(5); // Agregar vértice 5
        grafica3.agrega(6); // Agregar vértice 6
        grafica3.agrega(1); // Agregar vértice 1
        grafica3.conecta(5, 6); // 5 → 6
        grafica3.conecta(6, 1); // 6 → 1

        GraficaDirigida<Integer> grafica4 = new GraficaDirigida<>();
        grafica4.agrega(6); // Agregar vértice 6
        grafica4.agrega(7); // Agregar vértice 7
        grafica4.agrega(1);
        grafica4.agrega(2);
        grafica4.agrega(5);
        grafica4.conecta(7, 1); // 7 → 1
        grafica4.conecta(7, 6); // 7 → 6
        grafica4.conecta(2, 5); // 2 → 5 (conexión directa desde grafica1)

        // Combinar las gráficas
        List<GraficaDirigida<Integer>> graficas = Arrays.asList(grafica1, grafica2, grafica3, grafica4);
        GraficaDirigida<Integer> graficaCombinada = new GraficaDirigida<>();
        graficaCombinada = graficaCombinada.combinarGraficas(graficas);

        // Comprobar los vértices
        Assert.assertTrue("La gráfica combinada debe contener el vértice 1", graficaCombinada.contiene(1));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 2", graficaCombinada.contiene(2));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 3", graficaCombinada.contiene(3));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 4", graficaCombinada.contiene(4));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 5", graficaCombinada.contiene(5));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 6", graficaCombinada.contiene(6));
        Assert.assertTrue("La gráfica combinada debe contener el vértice 7", graficaCombinada.contiene(7));

        // Comprobar las conexiones utilizando sonVecinos
        Assert.assertTrue("La gráfica combinada debe contener la conexión 1 → 2", graficaCombinada.sonVecinos(1, 2));        
        Assert.assertTrue("La gráfica combinada debe contener la conexión 2 → 3", graficaCombinada.sonVecinos(2, 3));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 1 → 2", graficaCombinada.sonVecinos(3, 2));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 3 → 4", graficaCombinada.sonVecinos(3, 4));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 4 → 5", graficaCombinada.sonVecinos(4, 5));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 5 → 6", graficaCombinada.sonVecinos(5, 6));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 6 → 7", graficaCombinada.sonVecinos(7, 6));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 1 → 2", graficaCombinada.sonVecinos(6, 1));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 7 → 1", graficaCombinada.sonVecinos(7, 1));
        Assert.assertTrue("La gráfica combinada debe contener la conexión 2 → 5", graficaCombinada.sonVecinos(2, 5));

        // Comprobar conexiones que no deben existir
        Assert.assertFalse("La gráfica combinada no debe contener la conexión 1 → 3", graficaCombinada.sonVecinos(1, 3));
        Assert.assertFalse("La gráfica combinada no debe contener la conexión 5 → 4", graficaCombinada.sonVecinos(5, 4));
    }

}
