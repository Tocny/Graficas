package mx.unam.ciencias.edd.pruebas;

import mx.unam.ciencias.edd.graficable.svg.ColorHex;
import mx.unam.ciencias.edd.graficable.VerticeCoordenado;
import java.util.Objects;


/**
 * Clase que representa una estación, implementando la interfaz VerticeCoordenado.
 */
public class Estacion implements VerticeCoordenado {
    
    /** Coordenada X de la estación. */
    private double coordX;
    
    /** Coordenada Y de la estación. */
    private double coordY;
    
    /** Color de la estación. */
    private ColorHex colorVertice;

    /** Descripción textual de la estación. */
    private String descripcion;

    /**
     * Constructor de la clase Estacion.
     * 
     * @param coordX la coordenada X de la estación.
     * @param coordY la coordenada Y de la estación.
     * @param colorVertice el color de la estación.
     * @param descripcion la descripción de la estación.
     */
    public Estacion(double coordX, double coordY, ColorHex colorVertice, String descripcion) {
        this.coordX = coordX;
        this.coordY = coordY;
        this.colorVertice = colorVertice;
        this.descripcion = descripcion;
    }

    @Override
    public double getCoordX() {
        return coordX;
    }

    @Override
    public double getCoordY() {
        return coordY;
    }

    @Override
    public ColorHex getColorVertice() {
        return colorVertice;
    }

    @Override
    public String descripcion() {
        return descripcion; // Retorna la descripción pasada por el constructor
    }

    @Override
    public boolean equals(Object obj) {
        if (this == obj) return true; // Comparar referencia
        if (!(obj instanceof Estacion)) return false; // Verificar si es del mismo tipo
        Estacion other = (Estacion) obj; // Hacer el casting

        // Comparar todos los atributos relevantes
        return Double.compare(coordX, other.getCoordX()) == 0 &&
               Double.compare(coordY, other.getCoordY()) == 0 &&
               descripcion.equals(other.descripcion());
    }

    @Override
    public int hashCode() {
        return Objects.hash(coordX, coordY, descripcion);
    }
}
