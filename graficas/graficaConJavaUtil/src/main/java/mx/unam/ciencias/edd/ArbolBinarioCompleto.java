package mx.unam.ciencias.edd;

import java.util.Iterator;

/**
 * <p>Clase para árboles binarios completos.</p>
 *
 * <p>Un árbol binario completo agrega y elimina elementos de tal forma que el
 * árbol siempre es lo más cercano posible a estar lleno.</p>
 */
public class ArbolBinarioCompleto<T> extends ArbolBinario<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Cola para recorrer los vértices en BFS. */
        private Cola<Vertice> cola;

        /* Inicializa al iterador. */
        private Iterador() {
            // Aquí va su código.
            cola = new Cola<Vertice>();
            if(raiz!=null){
                cola.mete(raiz);
            }
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return !cola.esVacia();
        }

        /* Regresa el siguiente elemento en orden BFS. */
        @Override public T next() {
            // Aquí va su código.
            
            Vertice v = cola.saca();
            if(v.izquierdo != null){
                cola.mete(v.izquierdo);
            }
            if(v.derecho != null){
                cola.mete(v.derecho);
            }
            return v.elemento;
        }
    }

    /**
     * Constructor sin parámetros. Para no perder el constructor sin parámetros
     * de {@link ArbolBinario}.
     */
    public ArbolBinarioCompleto() { super(); }

    /**
     * Construye un árbol binario completo a partir de una colección. El árbol
     * binario completo tiene los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario completo.
     */
    public ArbolBinarioCompleto(Coleccion<T> coleccion) {
        super(coleccion);
    }

    /**
     * Agrega un elemento al árbol binario completo. El nuevo elemento se coloca
     * a la derecha del último nivel, o a la izquierda de un nuevo nivel.
     * @param elemento el elemento a agregar al árbol.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elemento == null){//Verifica que el elemento no sea nulo.
            throw new IllegalArgumentException("elemento vacío");
        }

        //Actualiza información.
        Vertice nuevo = nuevoVertice(elemento);
        elementos++;

        //En caso de que el árbol sea vacío.
        if(raiz == null){
            raiz = nuevo;
            return;
        }

        //Inicializamos la cola.
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);

        //Recorrido en bfs. Hasta encontrar un hoyo.
        while(!cola.esVacia()){
            Vertice v = cola.saca();

            //Si el izquierdo de v tiene hoyo, metemos el nuevo.
            if(v.izquierdo == null){
                nuevo.padre = v;
                v.izquierdo = nuevo;
                return;
            }

            //Si el derecho de v tiene hoyo, metemos el nuevo.
            if(v.derecho == null){
                nuevo.padre = v;
                v.derecho = nuevo;
                return;
            }
            
            //Si no pasa lo anterior. Metemos el izquierdo y derecho de v.
            cola.mete(v.izquierdo);
            cola.mete(v.derecho);
        }
        
    }

    /**
     * Elimina un elemento del árbol. El elemento a eliminar cambia lugares con
     * el último elemento del árbol al recorrerlo por BFS, y entonces es
     * eliminado.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        //Vertice del árbol con el elemento solicitado.
        Vertice vElim = (Vertice)busca(elemento);

        //En caso de que sea null, no está en el árbol.
        if(vElim == null){
            return;
        }
        
        //Decremento
        elementos--;
        if(elementos == 0){//Si el número de elementos es 0, se limpia el árbol.
            limpia();
            return;
        }

        //Recorrido del árbol en bfs. Para encontrar el último vértice.
        Vertice ultimo = null;
        Vertice v = null;
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);

        while(!cola.esVacia()){
            v = cola.saca();

            if(v.izquierdo != null){
                cola.mete(v.izquierdo);
            }
            if(v.derecho != null){
                cola.mete(v.derecho);
            }
            ultimo = v;//A medida que recorremos el árbol se actualiza el valor del último.
        }
    
        //Intercambio:
        T elem = vElim.elemento;//Almacenamos el valor del vertice a eliminar.
        vElim.elemento = ultimo.elemento;//El valor del eliminado es el del último.
        ultimo.elemento = elem;//El ultimo es el valor del eliminado.

        //Para determinar si el último es izquierdo o derecho.
        if(ultimo.padre.izquierdo == ultimo){
            ultimo.padre.izquierdo = null;
        } else{
            ultimo.padre.derecho = null;
        }
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol binario completo
     * siempre es ⌊log<sub>2</sub><em>n</em>⌋.
     * @return la altura del árbol.
     */
    @Override public int altura() {
        // Aquí va su código.
        if(raiz == null){
            return -1;
        }
        //Uso de la clase math para el logaritmo.
        return (int)Math.floor(Math.log(elementos)/Math.log(2));
    }

    /**
     * Realiza un recorrido BFS en el árbol, ejecutando la acción recibida en
     * cada elemento del árbol.
     * @param accion la acción a realizar en cada elemento del árbol.
     */
    public void bfs(AccionVerticeArbolBinario<T> accion) {
        // Aquí va su código.
        
        if(raiz == null){//Si el árbol era vacío, no hacemos nada.
            return;
        }

        //Empezamos la cola.
        Cola<Vertice> cola = new Cola<Vertice>();
        cola.mete(raiz);
        Vertice v = null;

        //Recorrido bfs
        while(!cola.esVacia()){
            //v es lo que sacamos de la cola.
            v = cola.saca();
            accion.actua(v);//Procesamos el vertice con la acción.

            //Metemos izquierdo
            if(v.izquierdo != null){
                cola.mete(v.izquierdo);
            }

            //Metemos derecho.
            if(v.derecho != null){
                cola.mete(v.derecho);
            }

        }
        
    }

    /**
     * Regresa un iterador para iterar el árbol. El árbol se itera en orden BFS.
     * @return un iterador para iterar el árbol.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }
}
