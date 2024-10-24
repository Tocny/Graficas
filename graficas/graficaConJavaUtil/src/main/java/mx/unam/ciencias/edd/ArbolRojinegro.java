package mx.unam.ciencias.edd;

/**
 * Clase para árboles rojinegros. Un árbol rojinegro cumple las siguientes
 * propiedades:
 *
 * <ol>
 *  <li>Todos los vértices son NEGROS o ROJOS.</li>
 *  <li>La raíz es NEGRA.</li>
 *  <li>Todas las hojas (<code>null</code>) son NEGRAS (al igual que la raíz).</li>
 *  <li>Un vértice ROJO siempre tiene dos hijos NEGROS.</li>
 *  <li>Todo camino de un vértice a alguna de sus hojas descendientes tiene el
 *      mismo número de vértices NEGROS.</li>
 * </ol>
 *
 * Los árboles rojinegros se autobalancean.
 */
public class ArbolRojinegro<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeRojinegro extends Vertice {

        /** El color del vértice. */
        public Color color;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeRojinegro(T elemento) {
            // Aquí va su código.
            super(elemento);
            color = Color.NINGUNO;
        }

        /**
         * Regresa una representación en cadena del vértice rojinegro.
         * @return una representación en cadena del vértice rojinegro.
         */
        @Override public String toString() {
            // Aquí va su código.
            if(color == color.ROJO){
                return "R{" + elemento.toString() + "}";
            } else{
                return "N{" + elemento.toString() + "}";
            }
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeRojinegro}, su elemento es igual al elemento de
         *         éste vértice, los descendientes de ambos son recursivamente
         *         iguales, y los colores son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked")
                VerticeRojinegro vertice = (VerticeRojinegro)objeto;
            // Aquí va su código.

            return (color == vertice.color && super.equals(objeto));
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolRojinegro() { super(); }

    /**
     * Construye un árbol rojinegro a partir de una colección. El árbol
     * rojinegro tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        rojinegro.
     */
    public ArbolRojinegro(Coleccion<T> coleccion) {
        // Aquí va su código.
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link
     * VerticeRojinegro}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice rojinegro con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeRojinegro(elemento);
    }

    /**
     * Regresa el color del vértice rojinegro.
     * @param vertice el vértice del que queremos el color.
     * @return el color del vértice rojinegro.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         VerticeRojinegro}.
     */
    public Color getColor(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.
        VerticeRojinegro a = (VerticeRojinegro)vertice;
        return a.color;
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol recoloreando
     * vértices y girando el árbol como sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);//Usamos el método de la clase padre.
        VerticeRojinegro v = (VerticeRojinegro)ultimoAgregado;//Agarramos el último agregado.
        v.color = Color.ROJO;
        agregaRebalancea(v);
    }
        
    /**
     * Método de rebalanceo después de agregar.
     * @param v un vértice rojinegro.
     */
    private void agregaRebalancea(VerticeRojinegro v){

        //Obtenemos el padre.
        VerticeRojinegro padre = (VerticeRojinegro)v.padre;
        
        //Padre nulo.
        if(padre == null){
            v.color = Color.NEGRO;
            return;
        } 
        //Padre negro.
        if(esNegro(padre)){
            return;
        }

        //Obtención del abuelo.
        VerticeRojinegro abuelo = (VerticeRojinegro)padre.padre;
        //Obtención del tio.
        VerticeRojinegro tio = hermano(padre, abuelo);

        //Si el tío es rojo.
        if(esRojo(tio)){
            //Colorea al tio y al padre de negro y al abuelo de rojo..
            tio.color = Color.NEGRO;
            padre.color = Color.NEGRO;
            abuelo.color = Color.ROJO;
            //Recursión sobre el abuelo.
            agregaRebalancea(abuelo);
            return;
        }

        //Si están cruzados.
        if(esDerecho(padre) && esIzquierdo(v)){//v izquierdo, p derecho.
            //gira sobre el padre a la derecha.
            super.giraDerecha(padre);
            //Realizamos un intercambio.
            VerticeRojinegro x = v;
            v = padre;
            padre = x;

        } else if(esIzquierdo(padre) && esDerecho(v)){//v derecho, p izquierdo.
            //gira sobre el padre a la izquierda.
            super.giraIzquierda(padre);
            //Realizamos un intercambio.
            VerticeRojinegro x = v;
            v = padre;
            padre = x;
        }

        //En otro caso: Si no están cruzados. No hay necesidad de verificar nada.
        //Coloreamos al padre de negro y al abuelo de rojo.
        padre.color = Color.NEGRO;
        abuelo.color = Color.ROJO;
        //giramos sobre el abuelo en la dirección contraria al vértice.
        if(esIzquierdo(v)){
            super.giraDerecha(abuelo);
        } else{
            super.giraIzquierda(abuelo);
        }

    }

    /**
     * Método destinado a obtener el hermano dados dos referencias: un vértice y un padre.
     * @param v un vértice rojinegro.
     * @param padre el padre del vértice.
     * @return el hermano del vértice 'v'.
     */
    private VerticeRojinegro hermano(VerticeRojinegro v, VerticeRojinegro padre){
        if(esIzquierdo(v)){
            return (VerticeRojinegro)padre.derecho;
        } else{
            return (VerticeRojinegro)padre.izquierdo;
        }
    }

    /**
     * Método para determinar si un vértice es izquierdo;
     * @param v un vértice rojinegro.
     * @return si el vértice es izquierdo.
     */
    private Boolean esIzquierdo(VerticeRojinegro v){
        return v.padre.izquierdo == v;
    }

    /**
     * Método para determinar si un vértice es derecho;
     * @param v un vértice rojinegro.
     * @return si el vértice es derecho.
     */
    private Boolean esDerecho(VerticeRojinegro v){
        return !esIzquierdo(v);
    }

    /**
     * Método para determinar si un vértice es rojo.
     * @param v un vértce rojinegro.
     * @return si es rojo.
     */
    private Boolean esRojo(VerticeRojinegro v){
        return (v != null && v.color == Color.ROJO);
    }

     /**
     * Método para determinar si un vértice es negro.
     * @param v un vértce rojinegro.
     * @return si es negro, o bien, si es nulo.
     */
    private Boolean esNegro(VerticeRojinegro v){
        return (v == null || v.color == Color.NEGRO);
    }


    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y recolorea y gira el árbol como sea necesario para
     * rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        //Busca el vértice.
        VerticeRojinegro v = (VerticeRojinegro)busca(elemento);
        //Vertice hijo y vértice fantasma.
        VerticeRojinegro h = null;
        VerticeRojinegro spook = null;

        //Si el elemento no estaba termina.
        if(v == null){
            return;
        }

        //Si tiene dos hijos no nulos. Intercambiamos con el maximal.
        if(v.izquierdo!=null && v.derecho!=null){
            v = (VerticeRojinegro)intercambiaEliminable(v);
        }

        //Para obtener el hijo h.
        if(v.izquierdo == null && v.derecho == null){//Dos hijos nulos, metemos a fantasma.
            //Generamos el vértice fantasma.
            spook = (VerticeRojinegro)nuevoVertice(null);
            spook.color = Color.NEGRO;

            //Conectamos al fantasma con el vértice.
            spook.padre = v;
            v.izquierdo = spook;

            //El vértice h es es el fantasma.
            h = spook;

        } else if(v.izquierdo != null && v.derecho == null){//Izquierdo no nulo.
            h = (VerticeRojinegro)v.izquierdo;

        } else if(v.izquierdo == null && v.derecho != null){//Derecho no nulo.
            h = (VerticeRojinegro)v.derecho;
        }

        //Usamos el auxiliar para desconectar el vértice y trepar al hijo.
        eliminaVertice(v);

        //Los tres casos para los colores de v y h.
        if(esRojo(h) && esNegro(v)){//Hermano rojo: colorea al hijo de negro.
            h.color = Color.NEGRO;

        } else if(esRojo(v) && esNegro(h)){//el vértice era rojo y el hijo negro.
            //No hace nada XD.

        } else if(esNegro(v) && esNegro(h)){//el vértice y el hijo negros: rebalancea.
            eliminaRebalancea(h);
        }

        //Regresando del algoritmo de rebalanceo, elimina a fantasma.
        if(spook != null){
            eliminaVertice(spook);
        }

        //Decrementamos el número de elementos.
        elementos--;
    }

    /**
     * Método para rebalancear un árbol después de eliminar un vértice.
     * @param v un vértice rojinegro.
     */
    private void eliminaRebalancea(VerticeRojinegro v){

        //Generamos el padre.
        VerticeRojinegro p = (VerticeRojinegro)v.padre;
        
        //Papá nulo.
        if(p == null){
            return;
        }

        //Generamos el hermano.
        VerticeRojinegro h = hermano(v, p);

        //El hermano es rojo.
        if(esRojo(h)){
            //Coloreamos al padre y al hermano.
            p.color = Color.ROJO;
            h.color = Color.NEGRO;

            //Giro del padre en dirección del vertice.
            if(esIzquierdo(v)){
                super.giraIzquierda(p);
            } else{
                super.giraDerecha(p);
            }

            //Actualizamos referencias de los parientes.
            p = (VerticeRojinegro)v.padre;
            h = hermano(v, p);
        }

        //Generamos los sobrinos.
        VerticeRojinegro hI = (VerticeRojinegro)h.izquierdo;
        VerticeRojinegro hD = (VerticeRojinegro)h.derecho;

        //Todo el mundo es negro.
        if(esNegro(p) && esNegro(h) && esNegro(hI) && esNegro(hD)){
            //colorea al hermano de rojo y hace recursión sobre el padre.
            h.color = Color.ROJO;
            eliminaRebalancea(p);
            return;
        }

        //El padre es rojo pero el hermano y sobrinos son negros.
        if(esRojo(p) && esNegro(h) && esNegro(hI) && esNegro(hD)){
            //Coloreamos al hermano de rojo y al padre de negro.
            h.color = Color.ROJO;
            p.color = Color.NEGRO;
            return;
        }

        //Sobrino rojo en la misma dirección que el vértice.
        if(esIzquierdo(v) && esRojo(hI) && esNegro(hD)){
            //Coloreamos al hermano de rojo y al sobrino rojo de negro.
            h.color = Color.ROJO;
            hI.color = Color.NEGRO;

            //Giramos al hermano. en la dirección contraria a v.
            super.giraDerecha(h);

            //Actualizamos referencias de los parientes.
            h = hermano(v, p);
            hI = (VerticeRojinegro)h.izquierdo;
            hD = (VerticeRojinegro)h.derecho;

        } else if(esDerecho(v) && esNegro(hI) && esRojo(hD)){
            //Coloreamos al hermano de rojo y al sobrino rojo de negro.
            h.color = Color.ROJO;
            hD.color = Color.NEGRO;

            //Giramos al hermano en dirección contraria al vértice
            super.giraIzquierda(h);

            //Actualizamos referencias de los parientes.
            h = hermano(v, p);
            hI = (VerticeRojinegro)h.izquierdo;
            hD = (VerticeRojinegro)h.derecho;
        }

        //En otro caso, no hay que verificar nada.
        //Colorea al hermano y al padre.
        h.color = p.color;
        p.color = Color.NEGRO;
        //Cambia el color del sobrino de dirección contraria al vértice y gira sobre el padre.
        if(esIzquierdo(v)){
            hD.color = Color.NEGRO;
            super.giraIzquierda(p);
        } else{
            hI.color = Color.NEGRO;
            super.giraDerecha(p);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la izquierda por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la izquierda " +
                                                "por el usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles
     * rojinegros no pueden ser girados a la derecha por los usuarios de la
     * clase, porque se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles rojinegros no " +
                                                "pueden girar a la derecha " +
                                                "por el usuario.");
    }
}
