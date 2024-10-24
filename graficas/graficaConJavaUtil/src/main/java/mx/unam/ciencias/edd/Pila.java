package mx.unam.ciencias.edd;

/**
 * Clase para pilas genéricas.
 */
public class Pila<T> extends MeteSaca<T> {

    /**
     * Regresa una representación en cadena de la pila.
     * @return una representación en cadena de la pila.
     */
    @Override public String toString() {
        Nodo n = cabeza;
        StringBuilder s = new StringBuilder();

        while(n!=null){
            s.append(n.elemento.toString() + "\n");
            n = n.siguiente;
        }

        return s.toString();
    }

    /**
     * Agrega un elemento al tope de la pila.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void mete(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("No se admiten valores nulos");
        }
        
        Nodo n = new Nodo(elemento);

        if(cabeza == null){//Si la cabeza es nula, la pila es vacía, se actualiza la cabeza y el rabo a n.
            cabeza = n;
            rabo = n;
        } else {//Si la cabeza no es nula, se cuadra la posición de la cabeza a ser el siguiente de n, n será la nueva cabeza
            n.siguiente = cabeza;
            cabeza = n;
        }
    }
}
