package mx.unam.ciencias.edd.graficable;

import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.graficable.svg.ColorHex;
import java.util.*;

public class GraficadorGrafo<V extends VerticeCoordenado> {

    /** La grafica dirigida que vamos a graficar. */
    private GraficaDirigida<V> grafo; 
    /** Ancho del lienzo. */
    private double anchoLienzo = 200;
    /** Alto del lienzo. */
    private double altoLienzo = 20;
    /**Maxima coordenada X. */
    private double maxCoordX = 0;
    /**Máxima coordenada Y. */
    private double maxCoordY = 0;
    /** Traductor intercambiable. */
    private TraductorLenguaje traductor;
    /** StringBuilder que almacena los datos. */
    private StringBuilder sb;

    /** Constantes privadas para dimensiones y estilos */
    private static final int RADIO_VERTICE = 10; 
    private static final ColorHex COLOR_BORDE = ColorHex.NEGRO; 
    private static final int GRUESO_BORDE = 2; 
    private static final ColorHex COLOR_FIGURA = ColorHex.BLANCO; 
    private static final int TAM_FUENTE = 12; // Tamaño de la fuente para el texto
    private static final ColorHex COLOR_TEXTO = ColorHex.NEGRO; // Color del texto
    private static final int ANCHO_LINEA = 2; 
    private static final int TAMANIO_APUNTADOR = 5;

    /**
     * Constructor de la clase, asigna atributos.
     * 
     * @param grafo un grafo dirigido.
     */
    public GraficadorGrafo(GraficaDirigida<V> grafo, TraductorLenguaje traductor){
        this.grafo = grafo;
        this.traductor = traductor;
        this.sb = new StringBuilder(); // Inicializa StringBuilder
        calculaDimensiones();
        sb.append(traductor.start(anchoLienzo, altoLienzo));
    }

    /**
     * Método que calcula las dimensiones del grafo.
     */
    private void calculaDimensiones(){
        if (grafo == null || grafo.obtenerElementos().isEmpty()) {
            throw new IllegalArgumentException("El grafo está vacío o no inicializado.");
        }
        
        for (V vertice : grafo.obtenerElementos()) {
            double coordX = vertice.getCoordX();
            double coordY = vertice.getCoordY();

            if (coordX > maxCoordX) {
                maxCoordX = coordX;
            }
            if (coordY > maxCoordY) {
                maxCoordY = coordY;
            }
        }

        anchoLienzo += maxCoordX;
        altoLienzo += maxCoordY;
    }

    /**
     * Método que agrega los vértices al sb.
     */
    public void graficaVertices(){
        for (V vertice : grafo.obtenerElementos()) {
            double cx = vertice.getCoordX();
            double cy = vertice.getCoordY();
            
            // Dibuja el círculo
            sb.append(traductor.dibujaCirculo(cx, cy, RADIO_VERTICE, COLOR_BORDE, GRUESO_BORDE, COLOR_FIGURA));
            sb.append(traductor.dibujaTexto(cx, cy - 15, vertice.descripcion(), TAM_FUENTE, COLOR_TEXTO)); // Ajustar 'cy' para que el texto esté arriba
        }
    }

    /**
     * Método para graficar las aristas.
     */
    public void graficaAristas() {

        for (V vertice : grafo.obtenerElementos()) {
            for (V vecino : grafo.obtenerElementos()) { // Asegúrate de que obtenerVecinos use el tipo correcto
                double x1 = vertice.getCoordX();
                double y1 = vertice.getCoordY();
                double x2 = vecino.getCoordX();
                double y2 = vecino.getCoordY();

                sb.append(traductor.dibujaLinea(x1, y1, x2, y2, vertice.getColorVertice(), ANCHO_LINEA));

            }
        }
    }

    /**
     * Método para graficar un camino.
     * 
     * @param camino una lista de vertices.
     */
    public void graficaCamino(List<V> camino){

        if(camino.isEmpty()){
            System.err.println("NOMAMES");
        }

        ColorHex colorCamino = ColorHex.ROJO; // Puedes cambiar el color del camino

        for (int i = 0; i < camino.size() - 1; i++) {
            V verticeActual = camino.get(i);
            V siguienteVertice = camino.get(i + 1);
            double x1 = verticeActual.getCoordX();
            double y1 = verticeActual.getCoordY();
            double x2 = siguienteVertice.getCoordX();
            double y2 = siguienteVertice.getCoordY();

            sb.append(traductor.dibujaLinea(x1, y1, x2, y2, colorCamino, ANCHO_LINEA*2));
        }

        graficaDescripciones(camino);
    }

    /**
     * Método para graficar una lista con información de los vértices de un camin
     * a la derecha del lienzo
     */
    private void graficaDescripciones(List<V> camino) {
        double posicionX = maxCoordX + 30; // Margen a la derecha del lienzo
        double posicionY = 10; // Inicia en la parte superior

        for (V vertice : camino) {
            posicionY += TAM_FUENTE + 5; // Espaciado vertical entre descripciones
            sb.append(traductor.dibujaTexto(posicionX, posicionY, vertice.descripcion(), TAM_FUENTE, vertice.getColorVertice()));
        }
    }

    /**
     * Método toString, regresa el sb acumulado.
     * @return método toString del stringBuilder.
     */
    @Override public String toString(){
        sb.append(traductor.end());
        return sb.toString();
    }
}
