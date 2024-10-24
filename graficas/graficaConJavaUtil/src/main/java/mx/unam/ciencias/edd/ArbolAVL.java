package mx.unam.ciencias.edd;

/**
 * <p>Clase para árboles AVL.</p>
 *
 * <p>Un árbol AVL cumple que para cada uno de sus vértices, la diferencia entre
 * la áltura de sus subárboles izquierdo y derecho está entre -1 y 1.</p>
 */
public class ArbolAVL<T extends Comparable<T>>
    extends ArbolBinarioOrdenado<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class VerticeAVL extends Vertice {

        /** La altura del vértice. */
        public int altura;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        public VerticeAVL(T elemento) {
            // Aquí va su código.
            super(elemento);
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return altura;
        }

        /**
         * Regresa una representación en cadena del vértice AVL.
         * @return una representación en cadena del vértice AVL.
         */
        @Override public String toString() {
            // Aquí va su código.
            return elemento.toString() + " " + altura + "/" + balance(this);
        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link VerticeAVL}, su elemento es igual al elemento de éste
         *         vértice, los descendientes de ambos son recursivamente
         *         iguales, y las alturas son iguales; <code>false</code> en
         *         otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") VerticeAVL vertice = (VerticeAVL)objeto;
            /*Nota: Para la fecha 4 de abril, este método no tenía el "Aquí va su código"
              aún así asumí que se debía modificar, pues no compilaba ni pasaba las pruebas*/
            return altura == vertice.altura && super.equals(objeto);
        }
            
    }
    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinarioOrdenado}.
     */
    public ArbolAVL() { super(); }

    /**
     * Construye un árbol AVL a partir de una colección. El árbol AVL tiene los
     * mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol AVL.
     */
    public ArbolAVL(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link VerticeAVL}.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    @Override protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        return new VerticeAVL(elemento);
    }

    /**
     * Agrega un nuevo elemento al árbol. El método invoca al método {@link
     * ArbolBinarioOrdenado#agrega}, y después balancea el árbol girándolo como
     * sea necesario.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        super.agrega(elemento);
        //Después de la anterior operación, el vértice agregado es el último agregado.
        VerticeAVL v = (VerticeAVL)super.ultimoAgregado;
        //rebalanceamos sobre el padre.
        rebalancea((VerticeAVL)v.padre);
    }
    
    /**
     * Elimina un elemento del árbol. El método elimina el vértice que contiene
     * el elemento, y gira el árbol como sea necesario para rebalancearlo.
     * @param elemento el elemento a eliminar del árbol.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        VerticeAVL v = (VerticeAVL)busca(elemento);

        if(v == null){
            return;
        }
        elementos--;

        //Si tiene dos hijos no nulos. Intercambiamos con el maximal.
        if(v.izquierdo!=null && v.derecho!=null){
            v = (VerticeAVL)intercambiaEliminable(v);
        }
        //Obtenemos el padre
        VerticeAVL p = (VerticeAVL)v.padre;
        //Eliminamos el vértice.
        eliminaVertice(v);
        //Rebalanceamos sobre el padre.
        rebalancea(p);


    }
    /**
     * Método de rebalanceo para después de agregar.
     * @param v un vértice AVL.
     */
    private void rebalancea(VerticeAVL v){
        //Caso de vértice vacío.
        if(v == null){
            return;
        }
        //Actualizamos la altura del vértice.
        actualizaAltura(v);
        
        //Si el balance de v es -2.
        if(balance(v) == -2){
            //Generamos referencias.
            VerticeAVL q = (VerticeAVL)v.derecho;
            VerticeAVL x = (VerticeAVL)q.izquierdo;

            if(balance(q) == 1){
                //Giramos a la derecha sobre q.
                super.giraDerecha(q);
                //Actualizamos alturas de los involucrados en el giro.
                actualizaAltura(x);
                actualizaAltura(q);
                //Actualizamos referencias.
                q = (VerticeAVL)v.derecho;
                x = (VerticeAVL)q.izquierdo;

            }
            //Independientemente de si hicimos el giro anterior o no:
            //Giramos a la izquierda sobre v.
            super.giraIzquierda(v);
            //Actualizamos alturas de los involucrados en el giro.
            actualizaAltura(q);
            actualizaAltura(v);
        }

        if(balance(v) == 2){
            //Generamos referencias.
            VerticeAVL p = (VerticeAVL)v.izquierdo;
            VerticeAVL y = (VerticeAVL)p.derecho;

            if(balance(p) == -1){
                //Giramos a la izquierda sobre p.
                super.giraIzquierda(p);
                //Actualizamos alturas de los involucrados en el giro.
                actualizaAltura(y);
                actualizaAltura(p);
                //Actualizamos referencias
                p = (VerticeAVL)v.izquierdo;
                y = (VerticeAVL)p.derecho;  
            }

            //Independientemente de si hicimos el giro anterior o no.
            //Giramos a al derecha sobre v.
            super.giraDerecha(v);
            //actualizamos alturas de los involucrados en el giro.
            actualizaAltura(p);
            actualizaAltura(v);
        }

        //Recursión sobre el padre.
        rebalancea((VerticeAVL)v.padre);

    }

    /**
     * Método privado encargado de realizar las operaciones de balance entre dos vértices.
     * @param v un vértice AVL.
     * @return la dieferencia de alturas de los hijos izquierdo y derecho de v
     */
    private int balance(VerticeAVL v){
        VerticeAVL vI = (VerticeAVL)v.izquierdo;
        VerticeAVL vD = (VerticeAVL)v.derecho;

        //Casos para el balance.
        if(vI != null && vD != null){//Tiene dos hijos.
            return vI.altura - vD.altura;

        } else if(vI != null && vD == null){//No tiene hijo derecho.
            return vI.altura + 1;

        } else if(vI == null && vD != null){//No tiene hijo izquierdo.
            return (-1) - vD.altura;

        } else{//No tiene hijos.
            return 0;
        }

    }

    /**
     * Método que recalcula la altura de un vértice basandose en los subárboles.
     * Modifica directament el valor del atributo altura de un vértice avl.
     * @param v un vértice avl.
     */
    private void actualizaAltura(VerticeAVL v){
        
        if(v == null){
            v.altura = -1;
        }else{
            //Vértices
            VerticeAVL vI = (VerticeAVL)v.izquierdo;
            VerticeAVL vD = (VerticeAVL)v.derecho;
            //Enteros para alturas.
            int vIH, vDH;
            //Para determinar la altura de vI
            if(vI == null){
                vIH = -1;
            }else{
                vIH = vI.altura;
            }
            //Para determinar la altura de vD.
            if(vD == null){
                vDH = -1;
            }else{
                vDH = vD.altura;
            }

            //Modificamos directamente la altura del vértice.
            v.altura = 1 + Math.max(vIH, vDH);
        }
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la derecha por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraDerecha(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la izquierda por el " +
                                                "usuario.");
    }

    /**
     * Lanza la excepción {@link UnsupportedOperationException}: los árboles AVL
     * no pueden ser girados a la izquierda por los usuarios de la clase, porque
     * se desbalancean.
     * @param vertice el vértice sobre el que se quiere girar.
     * @throws UnsupportedOperationException siempre.
     */
    @Override public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        throw new UnsupportedOperationException("Los árboles AVL no  pueden " +
                                                "girar a la derecha por el " +
                                                "usuario.");
    }
}
