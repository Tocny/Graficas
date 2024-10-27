package mx.unam.ciencias.edd.graficable;

import mx.unam.ciencias.edd.GraficaDirigida;
import mx.unam.ciencias.edd.graficable.svg.TraductorSVG;
import java.util.List;

public class GraficadorBuilderSVG<T extends VerticeCoordenado> {

    /** Grafo dirigido. */
    private GraficaDirigida<T> grafo;
    /** Trayectoria a resaltar en el grafo. */
    private List<T> trayectoria;
    /** Graficador de gráficos */
    private GraficadorGrafo<T> graficador;

    /**
     * Constructor de BuildGraficador.
     * 
     * @param grafo Grafo dirigido de vértices que implementan VerticeCoordenado.
     */
    public GraficadorBuilderSVG(GraficaDirigida<T> grafo) {
        this.grafo = grafo;
        this.graficador = new GraficadorGrafo<>(grafo, new TraductorSVG());
    }

    /**
     * Configura la trayectoria en el grafo.
     * 
     * @param trayectoria La lista de vértices que representan el camino o trayectoria.
     * @return La instancia actual de BuildGraficador para encadenamiento.
     */
    public GraficadorBuilderSVG<T> setTrayectoria(List<T> trayectoria) {
        this.trayectoria = trayectoria;
        return this;
    }

    /**
     * Genera la representación gráfica en formato SVG.
     * 
     * @return La cadena SVG que representa el grafo con la trayectoria, si se configuró.
     */
    public String graficar() {
        if (grafo == null || grafo.obtenerElementos().isEmpty()) {
            throw new IllegalArgumentException("El grafo no contiene elementos.");
        }

        // Graficar las aristas del grafo
        graficador.graficaAristas();

        // Si se ha establecido una trayectoria, graficarla
        if (trayectoria != null && !trayectoria.isEmpty()) {
            graficador.graficaCamino(trayectoria);
            graficador.graficaDescripciones(trayectoria);
        }

        //Al ultimo los vértices
        graficador.graficaVertices();

        // Retornar el resultado SVG generado por GraficadorGrafo
        return graficador.toString();
    }
}
