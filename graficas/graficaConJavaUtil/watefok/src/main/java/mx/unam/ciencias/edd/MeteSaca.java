package mx.unam.ciencias.edd;

import java.util.NoSuchElementException;

/**
 * Clase abtracta para estructuras lineales restringidas a operaciones
 * mete/saca/mira.
 */
public abstract class MeteSaca<T> {

    /**
     * Clase interna protegida para nodos.
     */
    protected class Nodo {
        /** El elemento del nodo. */
        public T elemento;
        /** El siguiente nodo. */
        public Nodo siguiente;

        /**
         * Construye un nodo con un elemento.
         * @param elemento el elemento del nodo.
         */
        public Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /** La cabeza de la estructura. */
    protected Nodo cabeza;
    /** El rabo de la estructura. */
    protected Nodo rabo;

    /**
     * Agrega un elemento al extremo de la estructura.
     * @param elemento el elemento a agregar.
     */
    public abstract void mete(T elemento);

    /**
     * Elimina el elemento en un extremo de la estructura y lo regresa.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T saca() {
        if(cabeza == null){//En caso estructura vacía
            throw new NoSuchElementException("No hay cabeza");
        }
        T e = cabeza.elemento;//Almacena el elemento antes de ser eliminado
        cabeza = cabeza.siguiente;//Itera la cabeza al siguiente nodo.
        return e;
    }

    /**
     * Nos permite ver el elemento en un extremo de la estructura, sin sacarlo
     * de la misma.
     * @return el elemento en un extremo de la estructura.
     * @throws NoSuchElementException si la estructura está vacía.
     */
    public T mira() {
        if(cabeza == null){//En caso de estructura vacía.
            throw new NoSuchElementException("No hay cabeza");
        }
        return cabeza.elemento;
    }

    /**
     * Nos dice si la estructura está vacía.
     * @return <code>true</code> si la estructura no tiene elementos,
     *         <code>false</code> en otro caso.
     */
    public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Compara la estructura con un objeto.
     * @param object el objeto con el que queremos comparar la estructura.
     * @return <code>true</code> si el objeto recibido es una instancia de la
     *         misma clase que la estructura, y sus elementos son iguales en el
     *         mismo orden; <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object object) {
        if (object == null || getClass() != object.getClass())
            return false;
        @SuppressWarnings("unchecked") MeteSaca<T> m = (MeteSaca<T>)object;
        // Aquí va su código.
        Nodo a = cabeza;
        Nodo b = m.cabeza;

        while(a!=null){
            //If para verificar si los elementos son diferentes.
            /** Como no se lleva control de la longitud, debemos verificar
             *  que b no sea null para continuar con las comparaciones
             *  para asegurar que seguimos contando con elementos comparables
             */
            if(b == null || !a.elemento.equals(b.elemento)){
                return false;
            }

            //Recorre hacia los nodos siguientes.
            a = a.siguiente;
            b = b.siguiente;
        }

        //En caso de sigan habiendo elementos en m (b no nulo).
        if(b != null){
            return false;
        }

        return true;
    }
}
