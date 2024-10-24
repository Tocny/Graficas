package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * <p>Clase abstracta para árboles binarios genéricos.</p>
 *
 * <p>La clase proporciona las operaciones básicas para árboles binarios, pero
 * deja la implementación de varias en manos de las subclases concretas.</p>
 */
public abstract class ArbolBinario<T> implements Coleccion<T> {

    /**
     * Clase interna protegida para vértices.
     */
    protected class Vertice implements VerticeArbolBinario<T> {

        /** El elemento del vértice. */
        protected T elemento;
        /** El padre del vértice. */
        protected Vertice padre;
        /** El izquierdo del vértice. */
        protected Vertice izquierdo;
        /** El derecho del vértice. */
        protected Vertice derecho;

        /**
         * Constructor único que recibe un elemento.
         * @param elemento el elemento del vértice.
         */
        protected Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
        }

        /**
         * Nos dice si el vértice tiene un padre.
         * @return <code>true</code> si el vértice tiene padre,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayPadre() {
            // Aquí va su código.
            return padre != null;
        }

        /**
         * Nos dice si el vértice tiene un izquierdo.
         * @return <code>true</code> si el vértice tiene izquierdo,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayIzquierdo() {
            // Aquí va su código.
            return izquierdo!=null;
        }

        /**
         * Nos dice si el vértice tiene un derecho.
         * @return <code>true</code> si el vértice tiene derecho,
         *         <code>false</code> en otro caso.
         */
        @Override public boolean hayDerecho() {
            // Aquí va su código.
            return derecho!=null;
        }

        /**
         * Regresa el padre del vértice.
         * @return el padre del vértice.
         * @throws NoSuchElementException si el vértice no tiene padre.
         */
        @Override public VerticeArbolBinario<T> padre() {
            // Aquí va su código.
            if(padre == null){
                throw new NoSuchElementException("No hay padre");
            }
            return padre;
        }

        /**
         * Regresa el izquierdo del vértice.
         * @return el izquierdo del vértice.
         * @throws NoSuchElementException si el vértice no tiene izquierdo.
         */
        @Override public VerticeArbolBinario<T> izquierdo() {
            // Aquí va su código.
            if(izquierdo == null){
                throw new NoSuchElementException("No hay izquierdo");
            }
            return izquierdo;
        }

        /**
         * Regresa el derecho del vértice.
         * @return el derecho del vértice.
         * @throws NoSuchElementException si el vértice no tiene derecho.
         */
        @Override public VerticeArbolBinario<T> derecho() {
            // Aquí va su código.
            // Aquí va su código.
            if(derecho == null){
                throw new NoSuchElementException("No hay derecho");
            }
            return derecho;
        }

        /**
         * Regresa la altura del vértice.
         * @return la altura del vértice.
         */
        @Override public int altura() {
            // Aquí va su código.
            return altura(this);
        }

        private int altura(Vertice v){
            if(v == null){
                return -1;
            }

            int leftH = altura(v.izquierdo);
            int rightH = altura(v.derecho);

            if(leftH <= rightH){
                return 1 + rightH;
            } else{
                return 1 + leftH;
            }
        }

        /**
         * Regresa la profundidad del vértice.
         * @return la profundidad del vértice.
         */
        @Override public int profundidad() {
            // Aquí va su código.
            return profundidad(this);
        }

        private int profundidad(Vertice v){
            if(v.padre==null){
                return 0;
            }
            return 1 + profundidad(v.padre);
        }

        /**
         * Regresa el elemento al que apunta el vértice.
         * @return el elemento al que apunta el vértice.
         */
        @Override public T get() {
            // Aquí va su código.
            return this.elemento;

        }

        /**
         * Compara el vértice con otro objeto. La comparación es
         * <em>recursiva</em>. Las clases que extiendan {@link Vertice} deben
         * sobrecargar el método {@link Vertice#equals}.
         * @param objeto el objeto con el cual se comparará el vértice.
         * @return <code>true</code> si el objeto es instancia de la clase
         *         {@link Vertice}, su elemento es igual al elemento de éste
         *         vértice, y los descendientes de ambos son recursivamente
         *         iguales; <code>false</code> en otro caso.
         */
        @Override public boolean equals(Object objeto) {
            if (objeto == null || getClass() != objeto.getClass())
                return false;
            @SuppressWarnings("unchecked") Vertice vertice = (Vertice)objeto;
            // Aquí va su código.
            
            //Llamada al método auxiliar.
            return equals(raiz, vertice);
        }

        /**
         * Método para comparar dos vértices.
         * Es auxiliar para facilitar llamadas recursivas.
         * @param a un vértice.
         * @param b otro vértice.
         * @return si los dos vértices son equivalentes.
         */
        private boolean equals(Vertice a, Vertice b){

            //Caso para vertices vacíos.
            if(a == null && b == null){//Ambos vértices son vacíos, son iguales.
                return true;

            } else if(a!=null && b==null){//a es no vacío pero b no, no son iguales.
                return false;

            } else if(a==null && b!=null){//b es no vacío pero a no, no son iguales.
                return false;
            }

            //Procedemos a hacer las comparaciones recursivas.
            boolean b1 = a.elemento.equals(b.elemento);//Compara elemento.
            boolean b2 = equals(a.izquierdo, b.izquierdo);//Compara izquierdos.
            boolean b3 = equals(a.derecho, b.derecho);//Compara derechos.

            //Comparamos las tres comparaciones.
            return b1 && b2 && b3;

        }

        /**
         * Regresa una representación en cadena del vértice.
         * @return una representación en cadena del vértice.
         */
        @Override public String toString() {
            // Aquí va su código.
            return elemento.toString();
        }
    }

    /** La raíz del árbol. */
    protected Vertice raiz;
    /** El número de elementos */
    protected int elementos;

    /**
     * Constructor sin parámetros. Tenemos que definirlo para no perderlo.
     */
    public ArbolBinario() {}

    /**
     * Construye un árbol binario a partir de una colección. El árbol binario
     * tendrá los mismos elementos que la colección recibida.
     * @param coleccion la colección a partir de la cual creamos el árbol
     *        binario.
     */
    public ArbolBinario(Coleccion<T> coleccion) {
        // Aquí va su código.
        for(T e : coleccion){
            this.agrega(e);
        }
    }

    /**
     * Construye un nuevo vértice, usando una instancia de {@link Vertice}. Para
     * crear vértices se debe utilizar este método en lugar del operador
     * <code>new</code>, para que las clases herederas de ésta puedan
     * sobrecargarlo y permitir que cada estructura de árbol binario utilice
     * distintos tipos de vértices.
     * @param elemento el elemento dentro del vértice.
     * @return un nuevo vértice con el elemento recibido dentro del mismo.
     */
    protected Vertice nuevoVertice(T elemento) {
        // Aquí va su código.
        Vertice v = new Vertice(elemento);
        return v;
    }

    /**
     * Regresa la altura del árbol. La altura de un árbol es la altura de su
     * raíz.
     * @return la altura del árbol.
     */
    public int altura() {
        // Aquí va su código.
        return altura(raiz);
    }

    /**
     * Método auxiliar para obtener la altura de un vértice recursivamente;
     * @param v el vértice el que habremos de obtener su altura.
     * @return la altura del vértice.
     */
    private int altura(Vertice v){
        if(v == null){
            return -1;
        }
        return 1 + max(altura(v.izquierdo), altura(v.derecho));
    }

    /**
     * Método auxiliar para determinar el maximo entre dos enteros.
     * @param a un entero.
     * @param b otro entero.
     * @return el máximo entre a y b.
     */
    private int max(int a, int b){
        if(a<=b){
            return b;
        }
        return a;
    }

    /**
     * Regresa el número de elementos que se han agregado al árbol.
     * @return el número de elementos en el árbol.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si un elemento está en el árbol binario.
     * @param elemento el elemento que queremos comprobar si está en el árbol.
     * @return <code>true</code> si el elemento está en el árbol;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return busca(raiz, elemento) != null;
    }

    /**
     * Busca el vértice de un elemento en el árbol. Si no lo encuentra regresa
     * <code>null</code>.
     * @param elemento el elemento para buscar el vértice.
     * @return un vértice que contiene el elemento buscado si lo encuentra;
     *         <code>null</code> en otro caso.
     */
    public VerticeArbolBinario<T> busca(T elemento) {
        // Aquí va su código.
        return busca(raiz, elemento);
    }

    /**
     * Método auxiliar para buscar un vértice en base a un elemento.
     * @param v un vértice de inicio.
     * @param elemento un elemento para comparar.
     * @return el vértice coincidencia, si es que hay.
     */
    private VerticeArbolBinario<T> busca(Vertice v, T elemento){
        if(elemento == null){
            return null;
        }
        if(v == null){
            return null;
        }

        //Si coincide con el vertice actual, retornamos v.
        if(v.get().equals(elemento)){
            return v;
        }

        //Busqueda recursiva sobre el subárbol izquierdo.
        VerticeArbolBinario<T> izq = busca(v.izquierdo, elemento);
        if(izq != null){
            return izq;
        }

        //Busqueda recursiva sobre el subárbol derecho.
        VerticeArbolBinario<T> der = busca(v.derecho, elemento);
        if(der != null){
            return der;
        }

        //Si de plano no se encontró, pues retornamos null.
        return null;
    }

    /**
     * Regresa el vértice que contiene la raíz del árbol.
     * @return el vértice que contiene la raíz del árbol.
     * @throws NoSuchElementException si el árbol es vacío.
     */
    public VerticeArbolBinario<T> raiz() {
        // Aquí va su código.
        if(raiz == null){
            throw new NoSuchElementException("Árbol vacío");
        }
        return raiz;
    }

    /**
     * Nos dice si el árbol es vacío.
     * @return <code>true</code> si el árbol es vacío, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return raiz == null;
    }

    /**
     * Limpia el árbol de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        raiz = null;
        elementos = 0;
    }

    /**
     * Compara el árbol con un objeto.
     * @param objeto el objeto con el que queremos comparar el árbol.
     * @return <code>true</code> si el objeto recibido es un árbol binario y los
     *         árboles son iguales; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked")
            ArbolBinario<T> arbol = (ArbolBinario<T>)objeto;
        // Aquí va su código.

        return esVacia() || raiz.equals(arbol.raiz);

    }


    /**
     * Regresa una representación en cadena del árbol.
     * @return una representación en cadena del árbol.
     */
    @Override public String toString() {
        // Aquí va su código.
        return toString(raiz);
        
    }

    /**
     * Método central para hacer la llamada a los otros métodos del toString.
     * @param t un árbol binario.
     * @return el árbol en una cadena.
     */
    private String toString(Vertice v){
        if(v == null){
            return "";
        }

        int altura = altura();
        int[] a = new int[altura+1];

        for(int i = 0; i < altura+1; i++){
            a[i] = 0;
        }
        String t = toString(v, 0, a);
        //zSystem.out.print(t);
        return t;
    }

    /**
     * Método que imprime los espacios para el toString.
     * @param l un entero.
     * @param a un arreglo binario.
     * @return una cadena de espacios.
     */
    private String dibujaEspacios(int l, int[] a){
        StringBuilder s = new StringBuilder();
        s.append("");

        for(int i = 0; i<=l-1; i++){
            if(a[i] == 1){
                s.append("│  ");
            } else{
                s.append("   ");
            }
        }

        return s.toString();
    }
    
    /**
     * Método encargado de generar la cadena para imprimir el árbol.
     * @param v un vértice.
     * @param l un entero.
     * @param a un arreglo binario.
     * @return la cadena mediante la cual se imprime el árbol.
     */
    private String toString(Vertice v, int l, int[] a){
        StringBuilder s = new StringBuilder();
        s.append(v.toString() + "\n");
        
        a[l] = 1;

        if(v.izquierdo != null && v.derecho != null){
            s.append(dibujaEspacios(l, a));
            s.append("├─›");
            s.append(toString(v.izquierdo, l+1, a));
            s.append(dibujaEspacios(l, a));
            s.append("└─»");
            a[l] = 0;
            s.append(toString(v.derecho, l+1, a));

        } else if(v.izquierdo != null){
            s.append(dibujaEspacios(l, a));
            s.append("└─›");
            a[l] = 0;
            s.append(toString(v.izquierdo, l+1, a));

        } else if(v.derecho != null){
            s.append(dibujaEspacios(l, a));
            s.append("└─»");
            a[l] = 0;
            s.append(toString(v.derecho, l+1, a));
        }

        return s.toString();
    }

    /**
     * Convierte el vértice (visto como instancia de {@link
     * VerticeArbolBinario}) en vértice (visto como instancia de {@link
     * Vertice}). Método auxiliar para hacer esta audición en un único lugar.
     * @param vertice el vértice de árbol binario que queremos como vértice.
     * @return el vértice recibido visto como vértice.
     * @throws ClassCastException si el vértice no es instancia de {@link
     *         Vertice}.
     */
    protected Vertice vertice(VerticeArbolBinario<T> vertice) {
        return (Vertice)vertice;
    }
}
