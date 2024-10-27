package mx.unam.ciencias.edd.pruebas;

import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.graficable.*;
import mx.unam.ciencias.edd.graficable.svg.*;
import java.util.*;

public class Simulacion {

    public static void main(String[] args) {
        // Crear el grafo dirigido
        GraficaDirigida<Estacion> grafica1 = new GraficaDirigida<>();
        grafica1.agrega(new Estacion(50, 50, ColorHex.VERDE, "Estación 1")); // Agregar vértice 1
        grafica1.agrega(new Estacion(100, 200, ColorHex.VERDE, "Estación 2")); // Agregar vértice 2
        grafica1.agrega(new Estacion(200, 50, ColorHex.VERDE, "Estación 3")); // Agregar vértice 3
        grafica1.conecta(new Estacion(50, 50, ColorHex.VERDE, "Estación 1"),
                         new Estacion(100, 200, ColorHex.VERDE, "Estación 2")); // 1 → 2
        grafica1.conecta(new Estacion(100, 200, ColorHex.VERDE, "Estación 2"),
                         new Estacion(200, 50, ColorHex.VERDE, "Estación 3")); // 2 → 3

        GraficaDirigida<Estacion> grafica2 = new GraficaDirigida<>();
        grafica2.agrega(new Estacion(200, 50, ColorHex.AMARILLO, "Estación 3")); // Agregar vértice 3
        grafica2.agrega(new Estacion(300, 50, ColorHex.AMARILLO, "Estación 4")); // Agregar vértice 4
        grafica2.agrega(new Estacion(250, 150, ColorHex.AMARILLO, "Estación 5")); // Agregar vértice 5
        grafica2.agrega(new Estacion(100, 100, ColorHex.AMARILLO, "Estación 6")); // Agregar vértice 6
        grafica2.conecta(new Estacion(200, 50, ColorHex.AMARILLO, "Estación 3"),
                         new Estacion(100, 100, ColorHex.AMARILLO, "Estación 6")); // 3 → 6
        grafica2.conecta(new Estacion(200, 50, ColorHex.AMARILLO, "Estación 3"),
                         new Estacion(300, 50, ColorHex.AMARILLO, "Estación 4")); // 3 → 4
        grafica2.conecta(new Estacion(300, 50, ColorHex.AMARILLO, "Estación 4"),
                         new Estacion(250, 150, ColorHex.AMARILLO, "Estación 5")); // 4 → 5

        GraficaDirigida<Estacion> grafica3 = new GraficaDirigida<>();
        grafica3.agrega(new Estacion(250, 150, ColorHex.MAGENTA, "Estación 5")); // Agregar vértice 5
        grafica3.agrega(new Estacion(50, 50, ColorHex.MAGENTA, "Estación 1"));//Agregamos la estacion 1.
        grafica3.agrega(new Estacion(100, 100, ColorHex.MAGENTA, "Estación 6")); // Agregar vértice 6
        grafica3.agrega(new Estacion(350, 200, ColorHex.MAGENTA, "Estación 7")); // Agregar vértice 7
        grafica3.conecta(new Estacion(250, 150, ColorHex.MAGENTA, "Estación 5"),
                         new Estacion(100, 100, ColorHex.MAGENTA, "Estación 6")); // 5 → 6
        grafica3.conecta(new Estacion(100, 100, ColorHex.MAGENTA, "Estación 6"),
                         new Estacion(350, 200, ColorHex.MAGENTA, "Estación 7")); // 6 → 7
        grafica3.conecta(new Estacion(100, 100, ColorHex.MAGENTA, "Estación 6"),
                         new Estacion(50, 50, ColorHex.MAGENTA, "Estación 1"));                 

        GraficaDirigida<Estacion> grafica4 = new GraficaDirigida<>();
        grafica4.agrega(new Estacion(50, 50, ColorHex.CYAN, "Estación 1")); // Agregar vértice 1
        grafica4.agrega(new Estacion(100, 200, ColorHex.CYAN, "Estación 2")); // Agregar vértice 2
        grafica4.agrega(new Estacion(350, 200, ColorHex.CYAN, "Estación 7")); // Agregar vértice 7
        grafica4.agrega(new Estacion(100, 100, ColorHex.CYAN, "Estación 6")); // Agregar vértice 6
        grafica4.agrega(new Estacion(250, 150, ColorHex.CYAN, "Estación 5")); // Agregar vértice 5
        grafica4.conecta(new Estacion(350, 200, ColorHex.CYAN, "Estación 7"),
                        new Estacion(50, 50, ColorHex.CYAN, "Estación 1"));
        grafica4.conecta(new Estacion(350, 200, ColorHex.CYAN, "Estación 7"),
                        new Estacion(100, 100, ColorHex.CYAN, "Estación 6"));
        grafica4.conecta(new Estacion(100, 200, ColorHex.CYAN, "Estación 2"),
                        new Estacion(250, 150, ColorHex.CYAN, "Estación 5"));

        // Combinar las gráficas
        List<GraficaDirigida<Estacion>> graficas = Arrays.asList(grafica1, grafica2, grafica3, grafica4);
        GraficaDirigida<Estacion> graficaCombinada = new GraficaDirigida<>();
        graficaCombinada = graficaCombinada.combinarGraficas(graficas);


        // Graficar en SVG
        Estacion estacion5 = new Estacion(250, 150, ColorHex.NEGRO, "Estación 5");
        Estacion estacion4 = new Estacion(300, 50, ColorHex.NEGRO, "Estación 4");
        //GraficadorGrafo<Estacion> graficador = new GraficadorGrafo<>(graficaCombinada, new TraductorSVG());

        GraficadorBuilderSVG<Estacion> graficador = new GraficadorBuilderSVG<>(graficaCombinada)
                                                    .setTrayectoria(graficaCombinada.dijkstraElementos(estacion5, estacion4));

        // Obtener la salida en SVG
        String resultadoSVG = graficador.graficar();
        
        // Imprimir o guardar el resultado en SVG
        System.out.println(resultadoSVG);
    }
}
