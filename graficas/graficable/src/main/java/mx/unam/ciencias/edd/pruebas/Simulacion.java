package mx.unam.ciencias.edd.pruebas;

import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.graficable.*;
import mx.unam.ciencias.edd.graficable.svg.*;
import java.util.*;

public class Simulacion {

    public static void main(String[] args) {
        // Crear el grafo dirigido
        GraficaDirigida<Estacion> grafo = new GraficaDirigida<>();

        // Crear estaciones
        Estacion estacion1 = new Estacion(50, 50, ColorSVG.ROJO, "Estación 1");
        Estacion estacion2 = new Estacion(150, 150, ColorSVG.AZUL, "Estación 2");
        Estacion estacion3 = new Estacion(200, 50, ColorSVG.VERDE, "Estación 3");
        Estacion estacion4 = new Estacion(300, 200, ColorSVG.NEGRO, "Estación 4");

        // Agregar estaciones al grafo
        grafo.agrega(estacion1);
        grafo.agrega(estacion2);
        grafo.agrega(estacion3);
        grafo.agrega(estacion4);

        // Conectar estaciones (agregar aristas)
        grafo.conecta(estacion1, estacion2);
        grafo.conecta(estacion2, estacion3);
        grafo.conecta(estacion1, estacion3, 1000);
        grafo.conecta(estacion3, estacion4);


        // Graficar en SVG
        GraficadorGrafo<Estacion> graficador = new GraficadorGrafo<>(grafo, new TraductorSVG());
        graficador.graficaAristas();
        graficador.graficaCamino(grafo.dijkstraElementos(estacion1, estacion3));
        graficador.graficaVertices();

        // Mostrar el resultado en SVG
        System.out.println(graficador.toString());
    }
}
