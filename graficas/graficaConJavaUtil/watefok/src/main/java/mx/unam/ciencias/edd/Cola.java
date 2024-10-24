package mx.unam.ciencias.edd;

/**
 * Clase para colas genéricas.
 */
public class Cola<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la cola.
     * @return una representación en cadena de la cola.
     */
    @Override public String toString() {
        Nodo n = cabeza;
        StringBuilder s = new StringBuilder();

        while(n!=null){
            s.append(n.elemento.toString() + ",");
            n = n.siguiente;
        }

        return s.toString();
    }

    /**
     * Agrega un elemento al final de la cola.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        // Aquí va su código.
        if(elemento == null){
            throw new IllegalArgumentException("No se admiten valores nulos");
        }

        Nodo n = new Nodo(elemento);

        if(cabeza == null){//Si la cabeza es nula, la cola es vacía, se actualiza la cabeza y el rabo a n.
            cabeza = n;
            rabo = n;
        } else {//Si la cabeza no es nula, se asigna n a ser el siguiente del rabo y n será el nuevo rabo de la cola.
            rabo.siguiente = n;
            rabo = n;
        }
    }
}
