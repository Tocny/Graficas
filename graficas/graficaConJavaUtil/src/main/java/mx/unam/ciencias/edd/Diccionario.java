package mx.unam.ciencias.edd;

import java.lang.reflect.Array;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * Clase para diccionarios (<em>hash tables</em>). Un diccionario generaliza el
 * concepto de arreglo, mapeando un conjunto de <em>llaves</em> a una colección
 * de <em>valores</em>.
 */
public class Diccionario<K, V> implements Iterable<V> {

    /* Clase interna privada para entradas. */
    private class Entrada {

        /* La llave. */
        private K llave;
        /* El valor. */
        private V valor;

        /* Construye una nueva entrada. */
        private Entrada(K llave, V valor) {
            // Aquí va su código.
            this.llave = llave;
            this.valor = valor;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador {

        /* En qué lista estamos. */
        private int indice;
        /* Iterador auxiliar. */
        private Iterator<Entrada> iterador;

        /* Construye un nuevo iterador, auxiliándose de las listas del
         * diccionario. */
        private Iterador() {
            // Aquí va su código.
            indice = 0;
            mueveIterador();
        }

        /* Nos dice si hay una siguiente entrada. */
        public boolean hasNext() {
            return iterador != null;
        }

        /* Regresa la siguiente entrada. */
        protected Entrada siguiente() {
            // Aquí va su código.
            if(iterador == null){//Si el iterador es nulo.
                throw new NoSuchElementException("No hay siguiente.");
            }

            //Guaradamos la entrada siguiente del iterador.
            Entrada siguiente = iterador.next();

            //Si ya no hay siguiente, buscamos la siguiente entrada no nula.
            if(!iterador.hasNext()){
                mueveIterador();
            }

            return siguiente;
        }

        /* Mueve el iterador a la siguiente entrada válida. */
        private void mueveIterador() {
            // Aquí va su código.
            //Recorremos las entradas.
            for (int i = indice; i < entradas.length; i++) {
                //Incrementamos el índice.
                indice++;
                //Si la entrada es no nula, actualizamos el iterador.
                if (entradas[i] != null) {
                    iterador = entradas[i].iterator();
                    return;
                }
            }

            //Si llega a este punto, el iterador será nulo.
            iterador = null;
        }
    }

    /* Clase interna privada para iteradores de llaves. */
    private class IteradorLlaves extends Iterador
        implements Iterator<K> {

        /* Regresa el siguiente elemento. */
        @Override public K next() {
            // Aquí va su código.
            return siguiente().llave;
        }
    }

    /* Clase interna privada para iteradores de valores. */
    private class IteradorValores extends Iterador
        implements Iterator<V> {

        /* Regresa el siguiente elemento. */
        @Override public V next() {
            // Aquí va su código.
            return siguiente().valor;
        }
    }

    /** Máxima carga permitida por el diccionario. */
    public static final double MAXIMA_CARGA = 0.72;

    /* Capacidad mínima; decidida arbitrariamente a 2^6. */
    private static final int MINIMA_CAPACIDAD = 64;

    /* Dispersor. */
    private Dispersor<K> dispersor;
    /* Nuestro diccionario. */
    private Lista<Entrada>[] entradas;
    /* Número de valores. */
    private int elementos;

    /* Truco para crear un arreglo genérico. Es necesario hacerlo así por cómo
       Java implementa sus genéricos; de otra forma obtenemos advertencias del
       compilador. */
    @SuppressWarnings("unchecked")
    private Lista<Entrada>[] nuevoArreglo(int n) {
        return (Lista<Entrada>[])Array.newInstance(Lista.class, n);
    }

    /**
     * Construye un diccionario con una capacidad inicial y dispersor
     * predeterminados.
     */
    public Diccionario() {
        this(MINIMA_CAPACIDAD, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial definida por el
     * usuario, y un dispersor predeterminado.
     * @param capacidad la capacidad a utilizar.
     */
    public Diccionario(int capacidad) {
        this(capacidad, (K llave) -> llave.hashCode());
    }

    /**
     * Construye un diccionario con una capacidad inicial predeterminada, y un
     * dispersor definido por el usuario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(Dispersor<K> dispersor) {
        this(MINIMA_CAPACIDAD, dispersor);
    }

    /**
     * Construye un diccionario con una capacidad inicial y un método de
     * dispersor definidos por el usuario.
     * @param capacidad la capacidad inicial del diccionario.
     * @param dispersor el dispersor a utilizar.
     */
    public Diccionario(int capacidad, Dispersor<K> dispersor) {
        // Aquí va su código.
        this.dispersor = dispersor;
        
        if(capacidad < MINIMA_CAPACIDAD){
            capacidad = MINIMA_CAPACIDAD;
        }

        capacidad = buscaPotenciaDos(2*capacidad);
        entradas = nuevoArreglo(capacidad);
    }

    /**
     * Método que busca una potencia de dos mayor o gual a un parámetro dado.
     * @param n un entero sobre el cual buscar la potencia.
     * @return Una potencia de 2 mayor o igual que n.
     */
    private int buscaPotenciaDos(int n){
        //Procedemos a correr bits desde la potencia 2^0, o sea el 1.
        int p = 1;
        //En tanto la potencia sea menor a n, corremos p una posición.
        while(p < n){
            p <<= 1;
        }
        return p; 
    }

    /**
     * Agrega un nuevo valor al diccionario, usando la llave proporcionada. Si
     * la llave ya había sido utilizada antes para agregar un valor, el
     * diccionario reemplaza ese valor con el recibido aquí.
     * @param llave la llave para agregar el valor.
     * @param valor el valor a agregar.
     * @throws IllegalArgumentException si la llave o el valor son nulos.
     */
    public void agrega(K llave, V valor) {
        // Aquí va su código.
        if(llave == null){
            throw new IllegalArgumentException("Llave inválida.");
        }

        if(valor == null){
            throw new IllegalArgumentException("Valor inválido.");
        }

        //Sacamos la dispersión de la llave con la mascara "entradas.length - 1".
        int i = dispersor.dispersa(llave) & (entradas.length - 1);

        //Generamos la entrada nueva.
        Entrada e = new Entrada(llave, valor);

        //Si la iesima posición del arreglo es nula.
        if(entradas[i] == null){
            //Le creamos la lista, le agregamamos la entrada e incrementamos el contador de elementos.
            entradas[i] = new Lista<Entrada>();
            entradas[i].agrega(e);
            elementos++;

        }else{//En otro caso.
            //Buscamos una colisión.
            Entrada c = buscaEntrada(i, llave);

            if(c != null){//Si hay colisión, reemplazamos el valor.
                c.valor = valor;

            } else{//Si no hay colisión solo agregamos.
                entradas[i].agrega(e);
                elementos++;
            }
        }

        //En caso de tener que duplicar la capacidad del diccionario.
        if(carga() >= MAXIMA_CARGA){
            duplicaCapacidad();
        }
    }

    /**
     * Método para buscar una entrada en el arreglo de entradas
     * dada una llave.
     * @param i un índice para buscar en una posición del arreglo particular.
     * @param llave una llave para comparar.
     * @return la entrada coincidencia.
     */
    private Entrada buscaEntrada(int i, K llave){
        //En caso valores nulos.
        if(entradas[i] == null){
            return null;
        }

        //Procedemos a recorrer las entradas hasta dar con la coincidencia.
        for(Entrada e: entradas[i]){
            if(llave.equals(e.llave)){
                return e;
            }
        }

        //Si llegamos a este punto entonces no se encontró.
        return null;
    }

    /**
     * Método que duplica la capacidad del diccionario.
     */
    private void duplicaCapacidad(){
        //Creamos el nuevo arreglo.
        Lista<Entrada>[] nuevoArreglo = nuevoArreglo(2*entradas.length);

        //Nos apoyamos del iterador (que ya está dado a partir de las listas del arreglo).
        Iterador iterador = new Iterador();

        //Procedemos a recorrer el iterador.
        while(iterador.hasNext()){
            //Sacamos entrada y dispersión.
            Entrada e = iterador.siguiente();
            int i = dispersor.dispersa(e.llave) & (nuevoArreglo.length - 1);

            //Si en la posición no cuenta con una lista entonces se la creamos para evitar NullPointerException's.
            if(nuevoArreglo[i] == null){
                nuevoArreglo[i] = new Lista<Entrada>();
            }

            //Agregamos la entrada a la lista.
            nuevoArreglo[i].agrega(e);

        }
        
        //Actualizamos el arreglo.
        entradas = nuevoArreglo;
    }

    /**
     * Regresa el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor.
     * @return el valor correspondiente a la llave.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no está en el diccionario.
     */
    public V get(K llave) {
        // Aquí va su código.
        if(llave == null){
            throw new IllegalArgumentException("Llave inválida.");
        }

        //Generamos la dispersión y buscamos la entrada.
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        Entrada e = buscaEntrada(i, llave);

        //Si al buscar la entrada nos dió algo distinto de null, regresamos el valor.
        if(e != null){
            return e.valor;

        } else{//Si no, tiramos la excepción.
            throw new NoSuchElementException("No hay entrada para la llave: " + llave);
        }
    }

    /**
     * Nos dice si una llave se encuentra en el diccionario.
     * @param llave la llave que queremos ver si está en el diccionario.
     * @return <code>true</code> si la llave está en el diccionario,
     *         <code>false</code> en otro caso.
     */
    public boolean contiene(K llave) {
        // Aquí va su código.
        if(llave == null){
            return false;
        }

        //Generamos la dispersión.
        int i = dispersor.dispersa(llave) & (entradas.length - 1);

        //Retornamos que la busqueda no proporcione un valor nulo.
        return buscaEntrada(i, llave) != null;
    }

    /**
     * Elimina el valor del diccionario asociado a la llave proporcionada.
     * @param llave la llave para buscar el valor a eliminar.
     * @throws IllegalArgumentException si la llave es nula.
     * @throws NoSuchElementException si la llave no se encuentra en
     *         el diccionario.
     */
    public void elimina(K llave) {
        // Aquí va su código.
        if(llave == null){
            throw new IllegalArgumentException("Llave inválida.");
        }

        //Generamos la dispersión y buscamos la entrada.
        int i = dispersor.dispersa(llave) & (entradas.length - 1);
        Entrada e = buscaEntrada(i, llave);

        //En caso de, al buscar, sacar una entrada nula.
        if(e == null){
            throw new NoSuchElementException("Valor no contenido.");
        }

        //Decrementamos el contador de elementos.
        elementos--;

        //Eliminamos la entrada de la lista.
        entradas[i].elimina(e);

        //En caso de que la lista ya no cuente con elementos, nulificamos la posición del arreglo.
        if(entradas[i].getElementos() == 0){
            entradas[i] = null;
        }
    }

    /**
     * Nos dice cuántas colisiones hay en el diccionario.
     * @return cuántas colisiones hay en el diccionario.
     */
    public int colisiones() {
        // Aquí va su código.
        //Inicializamos el acumulador.
        int colisiones = 0;
        
        //Recorremos el arreglo de entradas para acumular las longitudes de las listas.
        for(Lista<Entrada> l : entradas){
            if(l != null){
                colisiones += l.getElementos();
            }
        }

        //Procedemos a regresar el contador
        if(colisiones == 0){
            return colisiones;

        } else{
            return colisiones-1;
        }
    }

    /**
     * Nos dice el máximo número de colisiones para una misma llave que tenemos
     * en el diccionario.
     * @return el máximo número de colisiones para una misma llave.
     */
    public int colisionMaxima() {
        // Aquí va su código.
        //Generamos un máximo que iremos intercambiando.
        int max = 0;
        
        //Recorremos el arreglo de entradas para buscar el máximo.
        for(Lista<Entrada> l : entradas){
            if((l != null) && (max < l.getElementos())){
                max = l.getElementos();
            }
        }

        //Regresamos el máximo.
        return max - 1;
    }

    /**
     * Nos dice la carga del diccionario.
     * @return la carga del diccionario.
     */
    public double carga() {
        // Aquí va su código.
        return (double) elementos/entradas.length;
    }

    /**
     * Regresa el número de entradas en el diccionario.
     * @return el número de entradas en el diccionario.
     */
    public int getElementos() {
        // Aquí va su código.
        return elementos;
    }

    /**
     * Nos dice si el diccionario es vacío.
     * @return <code>true</code> si el diccionario es vacío, <code>false</code>
     *         en otro caso.
     */
    public boolean esVacia() {
        // Aquí va su código.
        return elementos == 0;
    }

    /**
     * Limpia el diccionario de elementos, dejándolo vacío.
     */
    public void limpia() {
        // Aquí va su código.
        entradas = nuevoArreglo(entradas.length);
        elementos = 0;
    }

    /**
     * Regresa una representación en cadena del diccionario.
     * @return una representación en cadena del diccionario.
     */
    @Override public String toString() {
        // Aquí va su código.
        //Generamos el iterador.
        Iterador iterador = new Iterador();

        //En caso de que sea vacía.
        if(esVacia()){
            return "{}";
        }

        //Generamos el string builder.
        StringBuilder sb = new StringBuilder();
        sb.append("{ ");

        //Recorremos el diccionario.
        while(iterador.hasNext()){
            Entrada e = iterador.siguiente();
            sb.append(String.format("'%d': '%d', ", e.llave, e.valor));
        }

        sb.append("}");

        return sb.toString();
    }

    /**
     * Nos dice si el diccionario es igual al objeto recibido.
     * @param o el objeto que queremos saber si es igual al diccionario.
     * @return <code>true</code> si el objeto recibido es instancia de
     *         Diccionario, y tiene las mismas llaves asociadas a los mismos
     *         valores.
     */
    @Override public boolean equals(Object o) {
        if (o == null || getClass() != o.getClass())
            return false;
        @SuppressWarnings("unchecked") Diccionario<K, V> d =
            (Diccionario<K, V>)o;
        // Aquí va su código.
        //Si no cuentan con la misma cantidad de elementos.
        if(this.getElementos() != d.getElementos()){
            return false;
        }

        Iterador iterador = new Iterador();

        while(iterador.hasNext()){
            Entrada e = iterador.siguiente();

            //Si 'd' no cuenta con la llave de 'e'
            if(!d.contiene(e.llave)){
                return false;
            }

            //Si las llaves son iguales pero sus valores son distintos.
            if(!this.get(e.llave).equals(d.get(e.llave))){
                return false;
            }
        }

        //Llegado a este punto necesariamente es verdadero.
        return true;
    }

    /**
     * Regresa un iterador para iterar las llaves del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar las llaves del diccionario.
     */
    public Iterator<K> iteradorLlaves() {
        return new IteradorLlaves();
    }

    /**
     * Regresa un iterador para iterar los valores del diccionario. El
     * diccionario se itera sin ningún orden específico.
     * @return un iterador para iterar los valores del diccionario.
     */
    @Override public Iterator<V> iterator() {
        return new IteradorValores();
    }
}
