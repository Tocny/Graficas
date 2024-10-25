package mx.unam.ciencias.edd.graficable.svg;

/**Enumeración para los colores en su valor hexadecimal. */
public enum ColorHex {
    /**Color: rojo. */
    ROJO("#FF0000"),
    /**Color: verde. */
    VERDE("#00FF00"),
    /**Color: azul. */
    AZUL("#0000FF"),
    /**Color: amarillo. */
    AMARILLO("#FFFF00"),
    /**Color: narnaja. */
    NARANJA("#FFA500"),
    /**Color: negro. */
    NEGRO("#000000"),
    /**Color: blanco. */
    BLANCO("#FFFFFF"),
    /**Color: gris. */
    GRIS("#808080"),
    /** Color: magenta. */
    MAGENTA("#FF00FF"),
    /** Color: púrpura. */
    PURPURA("#800080"),
    /** Color: cian. */
    CYAN("#00FFFF");

    /**Constante codigoColor para los objetos. */
    private final String codigoColor;

    /**
     * Constructor de la enumeracion, asigna el atributo codigoColor.
     * @param codigoColor cadena correspondiente al codigo del color.
     */
    ColorHex(String codigoColor) {
        this.codigoColor = codigoColor;
    }

    /**
     * Getter del codigo de un objeto de la enumeración.
     * @return codigoColor.
     */
    public String getCodigoColor() {
        return codigoColor;
    }
}
