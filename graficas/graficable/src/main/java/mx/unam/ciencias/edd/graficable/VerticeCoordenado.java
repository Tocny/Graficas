package mx.unam.ciencias.edd.graficable;

import mx.unam.ciencias.edd.graficable.svg.ColorSVG;

/**
 * Interfaz que define el comportamiento de un vértice en un sistema gráfico 
 * con coordenadas y color. Los vértices pueden tener una representación en SVG 
 * y ser descritos de manera textual.
 */
public interface VerticeCoordenado {

    /**
     * Obtiene la coordenada X del vértice.
     * 
     * @return la coordenada X del vértice.
     */
    public double getCoordX();

    /**
     * Obtiene la coordenada Y del vértice.
     * 
     * @return la coordenada Y del vértice.
     */
    public double getCoordY();

    /**
     * Obtiene el color del vértice representado como una instancia de {@link ColorSVG}.
     * 
     * @return el color del vértice.
     */
    public ColorSVG getColorVertice();

    /**
     * Proporciona una descripción textual del vértice, la cual puede incluir 
     * información como sus coordenadas y su color.
     * 
     * @return una cadena con la descripción del vértice.
     */
    public String descripcion();
}
