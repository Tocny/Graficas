package mx.unam.ciencias.edd.graficable;

import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.graficable.svg.ColorSVG;
import java.util.*;

public class GraficadorGrafo<V extends VerticeCoordenado> {

    /**La grafica dirigida que vamos a graficar. */
    private GraficaDirigida<V> grafo; 
    /**Ancho del lienzo */
    private double anchoLienzo = 20;
    /**Alto del lienzo. */
    private double altoLienzo = 20;
    /**Traductor intercambiable. */
    private TraductorLenguaje traductor;
    /**StringBuilder que almacena los datos. */
    private StringBuilder sb;

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

            if (coordX > anchoLienzo) {
                anchoLienzo = coordX;
            }
            if (coordY > altoLienzo) {
                altoLienzo = coordY;
            }

            anchoLienzo += 50;
            altoLienzo += 50;
        }
    }

    /**
     * Método que agrega los vértices al sb.
     */
    public void graficaVertices(){
        int radio = 10; 
        ColorSVG colorBorde = ColorSVG.NEGRO; 
        int gruesoBorde = 2; 
        ColorSVG colorFigura = ColorSVG.BLANCO; 
        int tamFuente = 12; // Tamaño de la fuente para el texto
        ColorSVG colorTexto = ColorSVG.NEGRO; // Color del texto

        for (V vertice : grafo.obtenerElementos()) {
            double cx = vertice.getCoordX();
            double cy = vertice.getCoordY();
            
            // Dibuja el círculo
            sb.append(traductor.dibujaCirculo(cx, cy, radio, colorBorde, gruesoBorde, colorFigura));
            
            sb.append(traductor.dibujaTexto(cx, cy - 15, vertice.descripcion(), tamFuente, colorTexto)); // Ajustar 'cy' para que el texto esté arriba
        }
    }

    /**
     * Método para graficar las aristas.
     */
    public void graficaAristas() {
        ColorSVG colorLinea = ColorSVG.NEGRO; 
        int anchoLinea = 2; 

        for(V vertice : grafo.obtenerElementos()){
            for(V vecino: grafo.obtenerElementos()){ // Asegúrate de que obtenerVecinos use el tipo correcto
                double x1 = vertice.getCoordX();
                double y1 = vertice.getCoordY();
                double x2 = vecino.getCoordX();
                double y2 = vecino.getCoordY();

                sb.append(traductor.dibujaLinea(x1, y1, x2, y2, colorLinea, anchoLinea));
            }
        }
    }

    /**
     * Método para graficar un camino.
     * 
     * @param camino una lista de vertices.
     */
    public void graficaCamino(List<V> camino){
        ColorSVG colorCamino = ColorSVG.ROJO; // Puedes cambiar el color del camino
        int anchoCamino = 3; // Grosor de la línea del camino

        for (int i = 0; i < camino.size() - 1; i++) {
            V verticeActual = camino.get(i);
            V siguienteVertice = camino.get(i + 1);
            double x1 = verticeActual.getCoordX();
            double y1 = verticeActual.getCoordY();
            double x2 = siguienteVertice.getCoordX();
            double y2 = siguienteVertice.getCoordY();

            sb.append(traductor.dibujaLinea(x1, y1, x2, y2, colorCamino, anchoCamino));
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
