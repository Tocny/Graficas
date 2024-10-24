package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios ordenados. Los árboles son genéricos, pero
 * acotados a la interfaz {@link Comparable}.</p>
 *
 * <p>Un árbol instancia de esta clase siempre cumple que:</p>
 * <ul>
 *   <li>Cualquier elemento en el árbol es mayor o igual que todos sus
 *       descendientes por la izquierda.</li>
 *   <li>Cualquier elemento en el árbol es menor o igual que todos sus
 *       descendientes por la derecha.</li>
 * </ul>
 */
public class ArbolBinarioOrdenado<T extends Comparable<T>>
    extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Pila para recorrer los vértices en DFS in-order. */
        private Pila<Vertice> pila;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            pila = new Pila<Vertice>();

            if(raiz == null){//En caso de raiz nula.
                return;
            }
            
            //Metemos la raiz a la pila.
            Vertice v = raiz;
            pila.mete(v);

            //Metemos los izquierdos a la pila.
            while (v.izquierdo != null) {
                pila.mete(v.izquierdo);
                v = v.izquierdo;
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !pila.esVacia();
        }

        /* Regresa el siguiente elemento en orden DFS in-order. */
        @Override public T next() {
            // Aquí va su código.
            Vertice v = pila.saca();//Sacamos el vértice dze la pila.
            T e = v.elemento;//Guardamos el elemento.

            //Verificamos que el derecho no sea null
            if(v.derecho != null){

                //Metemos el derecho.
                v = v.derecho;
                pila.mete(v);

                //Metemos los izquierdos del derecho.
                while(v.izquierdo != null){
                    v = v.izquierdo;
                    pila.mete(v);
                }
            }
            return e;
            
        }
    }

    /**
     * El vértice del último elemento agegado. Este vértice sólo se puede
     * garantizar que existe <em>inmediatamente</em> después de haber agregado
     * un elemento al árbol. Si cualquier operación distinta a agregar sobre el
     * árbol se ejecuta después de haber agregado un elemento, el estado de esta
     * variable es indefinido.
     */
    protected Vertice ultimoAgregado;

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioOrdenado() { super(); }

    /**
     * Construye un árbol binario ordenado a partir de una colección. El árbol
     * binario ordenado tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario ordenado.
     */
    public ArbolBinarioOrdenado(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un nuevo elemento al árbol. El árbol conserva su orden in-order.
     * @param elemento el elemento a agregar.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if(elemento == null){
            throw new IllegalArgumentException("Nulo");
        }

        Vertice v = nuevoVertice(elemento);//Creamos el vertice.

        ultimoAgregado = v;//Actualizamos el valor del último.
        elementos++;//Incrementamos el contador de elementos.

        //En caso de raiz nula.
        if(raiz == null){
            raiz = v;
            return;
        }

        //Uso del método auxiliar.
        agrega(raiz, v);
    }

    /**
     * Método auxiliar para agregar un elemento al arbol
     * dados dos vértices comparables.
     * @param actual un vértice de referencia.
     * @param nuevo el vértice que queremos agregar.
     */
    private void agrega(Vertice actual, Vertice nuevo){

        if(nuevo.elemento.compareTo(actual.elemento) <= 0){//Si nuevo<=actual
            if(actual.izquierdo == null){//Si el izquierdo es nulo
                actual.izquierdo = nuevo;
                nuevo.padre = actual;
                return;
            }
            agrega(actual.izquierdo, nuevo);//Llamada recursiva.

        } else{//Si nuevo>actual
            if(actual.derecho == null){//Si el derecho es nulo.
                actual.derecho = nuevo;
                nuevo.padre = actual;
                return;
            }
            agrega(actual.derecho, nuevo);//Llamada recursiva.
        }
    }

    /**
     * Elimina un elemento. Si el elemento no está en el árbol, no hace nada; si
     * está varias veces, elimina el primero que encuentre (in-order). El árbol
     * conserva su orden in-order.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        Vertice v = (Vertice)busca(elemento);
        
        //Si no se encontró el elemento.
        if(v == null){
            return;
        }
        elementos--;

        //Casos para los cuales el vértice tiene entre uno y ningún hijo
        if(v.izquierdo == null && v.derecho == null){//v es hoja.
            eliminaVertice(v);
            return;
        } else if(v.izquierdo != null && v.derecho ==  null){//v tiene izquierdo pero no derecho.
            eliminaVertice(v);
            return;
        } else if(v.izquierdo == null && v.derecho != null){//v tiene derecho pero no izquierdo.
            eliminaVertice(v);
            return;
        }

        //Si el elemento tiene dos hijos.
        if(v.izquierdo != null && v.derecho != null){
            v = intercambiaEliminable(v);//Le aplicamos el intercambio con el maximal.
            eliminaVertice(v);//Aplicamos eliminaVertice. Pues ahora tiene a lo más un hijo.
        }
    }
    
    /**
     * Método encargado de buscar el maximo dado un vértice.
     * @param v el vértice sobre el cual se realizará la busqueda.
     * @return el máximo del subárbol.
     */
    private Vertice maximoEnSubarbol(Vertice v){
        if(v.derecho == null){
            return v;
        }
        return maximoEnSubarbol(v.derecho);
    }

    /**
     * Intercambia el elemento de un vértice con dos hijos distintos de
     * <code>null</code> con el elemento de un descendiente que tenga a lo más
     * un hijo.
     * @param vertice un vértice con dos hijos distintos de <code>null</code>.
     * @return el vértice descendiente con el que vértice recibido se
     *         intercambió. El vértice regresado tiene a lo más un hijo distinto
     *         de <code>null</code>.
     */
    protected Vertice intercambiaEliminable(Vertice vertice) {
        // Aquí va su código.
        Vertice max = maximoEnSubarbol(vertice.izquierdo);

        //Intercambio;
        T e = vertice.elemento;//Guardamos el elemento.
        vertice.elemento = max.elemento;//Reasignamos el máximo.
        max.elemento = e;//Asignamos el valor de e al máximo,

        return max;
    }

    /**
     * Elimina un vértice que a lo más tiene un hijo distinto de
     * <code>null</code> subiendo ese hijo (si existe).
     * @param vertice el vértice a eliminar; debe tener a lo más un hijo
     *                distinto de <code>null</code>.
     */
    protected void eliminaVertice(Vertice vertice) {
        // Aquí va su código.
        Vertice p = vertice.padre;//Obtención del padre del vertice

        //bolenano para determinar si el vertice tiene padre y es el izquierdo de su padre.
        boolean esIzquierdo = (p!=null) && (p.izquierdo == vertice);

        //Obtención de u, el hijo no null del vértice.
        Vertice u = null;
        if(vertice.izquierdo != null){//Si el izquierdo es el no null
            u = vertice.izquierdo;
        } else{//En otro caso, si el derecho es el no null.
            u = vertice.derecho;
        }

        //If para alinear a u con el padre del vértice. Para quitar las referencias a v.
        if(p!=null){
            if(esIzquierdo){//Si el vertice es izquierdo.
                p.izquierdo = u;
            } else{//Si el vertice es derecho.
                p.derecho = u;
            }
        } else {//De otro modo, entonce p era nulo, entonce el vertice era la raiz.
            raiz = u;
        }

        //Actualizamos el padre de u al padre del vertice.
        if(u != null){
            u.padre = p;
        }
    }

    /**
     * Busca un elemento en el árbol recorriéndolo in-order. Si lo encuentra,
     * regresa el vértice que lo contiene; si no, regresa <code>null</code>.
     * @param elemento el elemento a buscar.
     * @return un vértice que contiene al elemento buscado si lo
     *         encuentra; <code>null</code> en otro caso.
     */
    @Override public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return busca(raiz, elemento);
    }

    /**
     * Método auxiliar de busqueda, compara un vertide dado con un elemento.
     * Se hizo por a parte para facilitar las recursiones sobre los vértices.
     * @param v el vértice a comparar.
     * @param elemento el elemento que se busca.
     * @return el vértice donde se dé la coincidencia.
     */
    private VerticeArbolBinario<T> busca(Vertice v, T elemento){
        
        if(v == null){//Si el vértice es nulo
            return null;
        }
        
        //Si el elemento = v.elemento
        if(elemento.compareTo(v.elemento) == 0){
            return v;
        }

        //Si elemento < v.elemento
        if(elemento.compareTo(v.elemento) < 0){
            return busca(v.izquierdo, elemento);
        }

        //En otro caso. elemento > v.elemento.
        //Si elemento < v.elemento
        if(elemento.compareTo(v.elemento) > 0){
            return busca(v.derecho, elemento);
        }

        return null;

    }

    /**
     * Regresa el vértice que contiene el último elemento agregado al
     * árbol. Este método sólo se puede garantizar que funcione
     * <em>inmediatamente</em> después de haber invocado al método {@link
     * agrega}. Si cualquier operación distinta a agregar sobre el árbol se
     * ejecuta después de haber agregado un elemento, el comportamiento de este
     * método es indefinido.
     * @return el vértice que contiene el último elemento agregado al árbol, si
     *         el método es invocado inmediatamente después de agregar un
     *         elemento al árbol.
     */
    public VerticeArbolBinario<T> getUltimoVerticeAgregado() {
        return ultimoAgregado;
    }

    /**
     * Gira el árbol a la derecha sobre el vértice recibido. Si el vértice no
     * tiene hijo izquierdo, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraDerecha(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.

        //Vertice "v" casteado:
        Vertice v = (Vertice)vertice;

        //En caso de no tener izquierdo.
        if(v.izquierdo == null){
            return;
        }

        Vertice izq = v.izquierdo;

        /*Pasa el subarbol derecho del izquierdo de v
          a ser el subarbol izquierdo de v*/
        v.izquierdo = izq.derecho;
        //Se enlaza el subarbol derecho del izquierdo de v con v.
        if(izq.derecho != null){
            izq.derecho.padre = v;
        }

        //Se actualiza el padre del izquierdo para asociar el padre de v al padre de izq.
        izq.padre = v.padre;

        //Si resulta que v no tenía padre, era la raíz. Entoncez izq es la nueva raíz.
        if(v.padre == null){
            raiz = izq;
        //Si v era derecho, su nuevo valor es izq.
        } else if(v == v.padre.derecho){
            v.padre.derecho = izq;
        //Si v era izquierdo, su nuevo valor es izq
        } else{
            v.padre.izquierdo = izq;
        }

        //Finalmente, el derecho de izq es v y del padre de v es izq.
        izq.derecho = v;
        v.padre = izq;
    }

    /**
     * Gira el árbol a la izquierda sobre el vértice recibido. Si el vértice no
     * tiene hijo derecho, el método no hace nada.
     * @param vertice el vértice sobre el que vamos a girar.
     */
    public void giraIzquierda(VerticeArbolBinario<T> vertice) {
        // Aquí va su código.

        //Vertice "v" casteado:
        Vertice v = (Vertice)vertice;

        //En caso de no tener derecho.
        if(v.derecho == null){
            return;
        }
        Vertice der = v.derecho;

        /*Pasa el subarbol izquierdo del derecho de v
          a ser el subarbol derecho de v*/
        v.derecho = der.izquierdo;
        //Se enlaza el subarbol izquierdo del derecho de v con v.
        if(der.izquierdo != null){
            der.izquierdo.padre = v;
        }

        //Se actualiza el padre del derecho para asociar el padre de v al padre de der.
        der.padre = v.padre;

        //Si resulta que v no tenía padre, era la raíz. Entonces der es la nueva raíz.
        if(v.padre == null){
            raiz = der;
        //Si v era derecho, su nuevo valor es der.
        } else if(v == v.padre.derecho){
            v.padre.derecho = der;
        //Si v era izquierdo, su nuevo valor es der.
        } else{
            v.padre.izquierdo = der;
        }

        //Finalmente, el izquierdo del derecho de v guarda el valor de v y el padre de v el valor del derecho de v.
        der.izquierdo = v;
        v.padre = der;

        
    }

    /**
     * Realiza un recorrido DFS <em>pre-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPreOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPreOrder(accion, raiz);
        
    }

    /**
     * Auxiliar de dfsPreOrder para recorrer recursivamente el árbol.
     * Se hace por a parte para facilitar las llamadas recursivas.
     * @param accion una instancia de AccionVerticeArbolBinario<T> para actuar sobre los vertices.
     * @param v un vértice de partida para recorrer el árbol.
     */
    private void dfsPreOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
        if(v == null){
            return;
        }
        accion.actua(v);//Acción sobre el vértice;
        dfsPreOrder(accion, v.izquierdo);//Recursión sobre el izquierdo.
        dfsPreOrder(accion, v.derecho);//Recursión sobre el derecho.
    }

    /**
     * Realiza un recorrido DFS <em>in-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsInOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsInOrder(accion, raiz);

    }

    /**
     * Auxiliar de dfsInOrder para recorrer recursivamente el árbol.
     * Se hace por a parte para facilitar las llamadas recursivas.
     * @param accion una instancia de AccionVerticeArbolBinario<T> para actuar sobre los vertices.
     * @param v un vértice de partida para recorrer el árbol.
     */
    private void dfsInOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
        if(v == null){
            return;
        }
        dfsInOrder(accion, v.izquierdo);//Recursión sobre el izquierdo.
        accion.actua(v);//Acción sobre el vértice;
        dfsInOrder(accion, v.derecho);//Recursión sobre el derecho.
    }

    /**
     * Realiza un recorrido DFS <em>post-order</em> en el árbol, ejecutando la
     * acción recibida en cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void dfsPostOrder(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        dfsPostOrder(accion, raiz);
    }

    /**
     * Auxiliar de dfsPostOrder para recorrer recursivamente el árbol.
     * Se hace por a parte para facilitar las llamadas recursivas.
     * @param accion una instancia de AccionVerticeArbolBinario<T> para actuar sobre los vertices.
     * @param v un vértice de partida para recorrer el árbol.
     */
    private void dfsPostOrder(AccionVerticeArbolBinario<T> accion, Vertice v){
        if(v == null){
            return;
        }
        dfsPostOrder(accion, v.izquierdo);//Recursión sobre el izquierdo.
        dfsPostOrder(accion, v.derecho);//Recursión sobre el derecho.
        accion.actua(v);//Acción sobre el vértice;
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
