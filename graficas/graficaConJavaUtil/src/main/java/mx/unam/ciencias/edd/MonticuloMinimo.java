package mx.unam.ciencias.edd;

import java.util.*;

/**
 * Clase para montículos mínimos (<i>min heaps</i>).
 */
public class MonticuloMinimo<T extends Comparable<T>>
    implements Coleccion<T>, MonticuloDijkstra<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Índice del iterador. */
        private int indice;

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return indice < elementos;
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            if(indice >= elementos){
                throw new NoSuchElementException("Iteración inválida.");
            }
            //Incrementamos el indice y regresamos el elemento.
            T elem = arbol[indice];//Guardamos el elemento antes de incrementar
            indice++;//Incrementamos
            return elem;
        }
    }

    /* El número de elementos en el arreglo. */
    private int elementos;
    /* Los índices. */
    private Map<T, Integer> indices;
    /* Usamos un truco para poder utilizar arreglos genéricos. */
    private T[] arbol;

    /* Truco para crear arreglos genéricos. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked") private T[] nuevoArreglo(int n) {
        return (T[])(new Comparable[n]);
    }

    /**
     * Constructor sin parámetros. Es más eficiente usar {@link
     * #MonticuloMinimo(Coleccion)} o {@link #MonticuloMinimo(Iterable,int)},
     * pero se ofrece este constructor por completez.
     */
    public MonticuloMinimo() {
        // Aquí va su código.
        arbol = nuevoArreglo(100);
        indices = new HashMap<>();
        elementos = 0;
    }

    /**
     * Constructor para montículo mínimo que recibe una colección. Es más barato
     * construir un montículo con todos sus elementos de antemano (tiempo
     * <i>O</i>(<i>n</i>)), que el insertándolos uno por uno (tiempo
     * <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param coleccion la colección a partir de la cuál queremos construir el
     *                  montículo.
     */
    public MonticuloMinimo(Coleccion<T> coleccion) {
        this(coleccion, coleccion.getElementos());
    }

    /**
     * Constructor para montículo mínimo que recibe un iterable y el número de
     * elementos en el mismo. Es más barato construir un montículo con todos sus
     * elementos de antemano (tiempo <i>O</i>(<i>n</i>)), que el insertándolos
     * uno por uno (tiempo <i>O</i>(<i>n</i> log <i>n</i>)).
     * @param iterable el iterable a partir de la cuál queremos construir el
     *                 montículo.
     * @param n el número de elementos en el iterable.
     */
    public MonticuloMinimo(Iterable<T> iterable, int n) {
        // Aquí va su código.
        arbol = nuevoArreglo(n);
        indices = new HashMap<>();
        elementos = 0;
        //Recorremos el iterable y vaciamos los elementos en el arreglo.
        for (T e : iterable) {
            arbol[elementos] = e;
            indices.put(e, elementos);
            elementos++;
        }
        //Acomodamos hacia abajo los elementos desde n/2 - 1 hasta 0.
        for (int i = elementos / 2 - 1; i >= 0; i--) {
            heapifyDown(i);
        }
    }

    /**
     * Método para realizar un acomodo hacia arriba en el monticulo.
     * @param i un índice del arreglo.
     */
    private void heapifyUp(int i){
        //Obtención del índice del padre.
        int p = (i - 1) / 2;

        if (i > 0 && arbol[i].compareTo(arbol[p]) < 0) {
            //Intercambia con su padre y hace recursión sobre él.
            intercambia(i, p);
            heapifyUp(p);
        }
    }

    /**
     * Metodo para realizar un acomodo hacia abajo en el monticulo.
     * @param i un índice del arreglo.
     */
    private void heapifyDown(int i){
        //Obtención de hijos.
        int hI = 2 * i + 1;
        int hD = 2 * i + 2;
        //Marcamos a un "menor" con valor inicial i.
        int menor = i;

        //Procedemos a buscar un hijo que sea menor que el "menor".
        //Si el hijo izquierdo existe y es menor que "menor"
        if (hI < elementos && arbol[hI].compareTo(arbol[menor]) < 0) {
            menor = hI;
        }
        //Si el hijo derecho existe y es menor que "menor"
        if (hD < elementos && arbol[hD].compareTo(arbol[menor]) < 0) {
            menor = hD;
        }

        //Si "menor" ha cambiado de valor.
        if (menor != i) {
            //Intercambiamos con i y hacemos recursión.
            intercambia(i, menor);
            heapifyDown(menor);
        }
    }

    /* Método auxiliar para intercambiar dos elementos en el arreglo. */
    private void intercambia(int i, int j) {
        //Hacemos el intercambio
        T temp = arbol[i];
        arbol[i] = arbol[j];
        arbol[j] = temp;
        //Agregamos al diccionario.
        indices.put(arbol[i], i);
        indices.put(arbol[j], j);
    }

    /**
     * Agrega un nuevo elemento en el montículo.
     * @param elemento el elemento a agregar en el montículo.
     */
    @Override public void agrega(T elemento) {
        // Aquí va su código.
        if (elementos == arbol.length) {
            ensanchaArreglo();
        }
        arbol[elementos] = elemento;
        indices.put(elemento, elementos);
        heapifyUp(elementos);
        elementos++;
    }

    /* Método auxiliar para incrementar el tamaño del arreglo. */
    private void ensanchaArreglo() {
        T[] nuevoArbol = nuevoArreglo(arbol.length * 2);
        for (int i = 0; i < elementos; i++) {
            nuevoArbol[i] = arbol[i];
        }
        arbol = nuevoArbol;
    }

    /**
     * Elimina el elemento mínimo del montículo.
     * @return el elemento mínimo del montículo.
     * @throws IllegalStateException si el montículo es vacío.
     */
    @Override public T elimina() {
        // Aquí va su código.
        if (elementos == 0) {
            throw new IllegalStateException("Montículo vacío.");
        }

        //Usamos el método auxiliar.
        return eliminaElemento(0);
    }

    /**
     * Elimina un elemento del montículo.
     * @param elemento a eliminar del montículo.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        if (esVacia()) {
            throw new IllegalStateException("El montículo está vacío");
        }
        
        //Sacamos su índice del diccionario.
        int indice = indices.get(elemento);
        
        //Eliminamos con el método auxiliar
        eliminaElemento(indice);
    }

    /* Elimina el elemento en el índice. */
    private T eliminaElemento(int indice) {
        if (indice < 0 || indice >= elementos) {
            throw new NoSuchElementException("Índice fuera de rango");
        }
        //Sacamos el elemento del índice, intercambiamos.
        T eliminado = arbol[indice];
        intercambia(indice, elementos - 1);
        //Eliminamos:
        arbol[elementos-1] = null;
        elementos--;
        //Reordenamos
        if (indice < elementos){
            heapifyUp(indice);
            heapifyDown(indice);;
        }
        //Eliminamos y regresamos el elemento.
        indices.remove(eliminado);
        return eliminado;
    }

    /**
     * Nos dice si un elemento está contenido en el montículo.
     * @param elemento el elemento que queremos saber si está contenido.
     * @return <code>true</code> si el elemento está contenido,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return indices.containsKey(elemento);
    }

    /**
     * Nos dice si el montículo es vacío.
     * @return <code>true</code> si ya no hay elementos en el montículo,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el montículo de elementos, dejándolo vacío.
     */
    @Override public void limpia() {
        // Aquí va su código.
        arbol = nuevoArreglo(100);
        elementos = 0;
        indices = new HashMap<>();
    }

   /**
     * Reordena un elemento en el árbol.
     * @param elemento el elemento que hay que reordenar.
     */
    @Override public void reordena(T elemento) {
        // Aquí va su código.
        int i = indices.get(elemento);
        heapifyUp(i);
        heapifyDown(i);

    }

    /* Acomoda hacia abajo (min-heapify); ve si el nodo actual (i) es mayor que
     * alguno de sus hijos (2*i+1, 2*i+2). Si así es, reemplaza el nodo con el
     * hijo menor, y recursivamente hace lo mismo con el hijo menor (que tiene
     * el valor del que era su padre). */
    private void acomodaAbajo(int i) {
        // Aquí va su código.
        heapifyDown(i);
    }

    /**
     * Regresa el número de elementos en el montículo mínimo.
     * @return el número de elementos en el montículo mínimo.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Regresa el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @param i el índice del elemento que queremos, en <em>in-order</em>.
     * @return el <i>i</i>-ésimo elemento del árbol, por niveles.
     * @throws NoSuchElementException si i es menor que cero, o mayor o igual
     *         que el número de elementos.
     */
    @Override public T get(int i) {
        // Aquí va su código.
        if(i < 0 || i >= elementos){
            throw new NoSuchElementException("Índice inválido.");
        }

        return arbol[i];

    }

    /**
     * Regresa una representación en cadena del montículo mínimo.
     * @return una representación en cadena del montículo mínimo.
     */
    @Override public String toString() {
        // Aquí va su código.
        StringBuilder sb = new StringBuilder();
        //Recorremos el arreglo en orden de los índices y adjuntamos al sb.
        for(int i = 0; i<elementos; i++){
            sb.append(arbol[i].toString() + ", ");
        }

        return sb.toString();
    }

    /**
     * Nos dice si el montículo mínimo es igual al objeto recibido.
     * @param objeto el objeto con el que queremos comparar el montículo mínimo.
     * @return <code>true</code> si el objeto recibido es un montículo mínimo
     *         igual al que llama el método; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") MonticuloMinimo<T> monticulo =
            (MonticuloMinimo<T>)objeto;
        // Aquí va su código.
        if(monticulo.getElementos() != this.getElementos()){//Si sus contadores de elementos difieren.
            return false;
        }
        //Para recorrer los arreglos.
        for(int i = 0; i < elementos; i++){
            //Itera hasta que se tope (o no) con una inconsistencia de elementos.
            if(monticulo.arbol[i].equals(arbol[i])){
                continue;
            }
            return false;
        }
        return true;
    }

    /**
     * Regresa un iterador para iterar el montículo mínimo. El montículo se
     * itera en orden BFS.
     * @return un iterador para iterar el montículo mínimo.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Ordena la colección usando HeapSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param coleccion la colección a ordenar.
     * @return una lista ordenada con los elementos de la colección.
     */
    public static <T extends Comparable<T>>
    Lista<T> heapSort(Coleccion<T> coleccion) {
        // Aquí va su código.
        MonticuloMinimo<T> monticulo = new MonticuloMinimo<>(coleccion);

        // Crear una lista para almacenar los elementos ordenados
        Lista<T> listaOrdenada = new Lista<>();

        // Extraer los elementos del montículo en orden ascendente
        while (!monticulo.esVacia()) {
            listaOrdenada.agregaFinal(monticulo.elimina());
        }
        
        return listaOrdenada;
    }
}
