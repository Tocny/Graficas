package mx.unam.ciencias.edd;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NoSuchElementException;

/**
 * <p>Clase genérica para listas doblemente ligadas.</p>
 *
 * <p>Las listas nos permiten agregar elementos al inicio o final de la lista,
 * eliminar elementos de la lista, comprobar si un elemento está o no en la
 * lista, y otras operaciones básicas.</p>
 *
 * <p>Las listas no aceptan a <code>null</code> como elemento.</p>
 *
 * @param <T> El tipo de los elementos de la lista.
 */
public class Lista<T> implements Coleccion<T> {

    /* Clase interna privada para nodos. */
    private class Nodo {
        /* El elemento del nodo. */
        private T elemento;
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nodo con un elemento. */
        private Nodo(T elemento) {
            this.elemento = elemento;
        }
    }

    /* Clase interna privada para iteradores. */
    private class Iterador implements IteradorLista<T> {
        /* El nodo anterior. */
        private Nodo anterior;
        /* El nodo siguiente. */
        private Nodo siguiente;

        /* Construye un nuevo iterador. */
        private Iterador() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Nos dice si hay un elemento siguiente. */
        @Override public boolean hasNext() {
            return siguiente!=null;
        }

        /* Nos da el elemento siguiente. */
        @Override public T next() {
            if(!hasNext()){
            throw new NoSuchElementException("No hay siguiente.");
            }

            anterior = siguiente;
            siguiente = siguiente.siguiente;
            return anterior.elemento;
        }

        /* Nos dice si hay un elemento anterior. */
        @Override public boolean hasPrevious() {
            return anterior!=null;
        }

        /* Nos da el elemento anterior. */
        @Override public T previous() {
            if(!hasPrevious()){
                throw new NoSuchElementException("No hay anterior");
            }

            siguiente = anterior;
            anterior = anterior.anterior;
            return siguiente.elemento;
        }

        /* Mueve el iterador al inicio de la lista. */
        @Override public void start() {
            anterior = null;
            siguiente = cabeza;
        }

        /* Mueve el iterador al final de la lista. */
        @Override public void end() {
            anterior = rabo;
            siguiente = null;
        }
    }

    /* Primer elemento de la lista. */
    private Nodo cabeza;
    /* Último elemento de la lista. */
    private Nodo rabo;
    /* Número de elementos en la lista. */
    private int longitud;

    /**
     * Regresa la longitud de la lista. El método es idéntico a {@link
     * #getElementos}.
     * @return la longitud de la lista, el número de elementos que contiene.
     */
    public int getLongitud() {
        return longitud;
    }

    /**
     * Regresa el número elementos en la lista. El método es idéntico a {@link
     * #getLongitud}.
     * @return el número elementos en la lista.
     */
    @Override public int getElementos() {
        return getLongitud();
    }

    /**
     * Nos dice si la lista es vacía.
     * @return <code>true</code> si la lista es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        return cabeza == null;
    }

    /**
     * Agrega un elemento a la lista. Si la lista no tiene elementos, el
     * elemento a agregar será el primero y último. El método es idéntico a
     * {@link #agregaFinal}.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    @Override public void agrega(T elemento) {
        agregaFinal(elemento);
    }

    /**
     * Agrega un elemento al final de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaFinal(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("No se admiten valores nulos");
        }

        Nodo n = new Nodo(elemento);

        if(cabeza == null){//Si la cabeza es nula, la lista es vacía, se actualiza la cabeza y el rabo a n.
            cabeza = n;
            rabo = n;
        } else {//Si la cabeza no es nula, se cuadra la posición de n a ser el siguiente del rabo y n será el nuevo rabo
            rabo.siguiente = n;
            n.anterior = rabo;
            rabo = n;
        }

        longitud++;
    }

    /**
     * Agrega un elemento al inicio de la lista. Si la lista no tiene elementos,
     * el elemento a agregar será el primero y último.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void agregaInicio(T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("No se admiten valores nulos");
        }

        Nodo n = new Nodo(elemento);

        if(cabeza == null){//Si la cabeza es nula, la lista es vacía, se actualiza la cabeza y el rabo a n.
            cabeza = n;
            rabo = n;
        } else {//Si la cabeza no es nula, se cuadra la posición de n a ser anterior de la cabeza y n será la nueva cabeza .
            cabeza.anterior = n;
            n.siguiente = cabeza;
            cabeza = n;
        }

        longitud++;
    }

    /**
     * Recorre la lista hasta llegar al iesimo nodo de la misma.
     * @param i El índice que se busca encontrar en la lista.
     * @return n, el iesimo nodo de la lista.
     */
    private Nodo getIesimo(int i){

        Nodo n = cabeza;
        //for para recorrer los elementos hasta que j=i
        for(int j = 0; j<i; j++){
            n = n.siguiente;
        }
        return n;
    }

    /**
     * Inserta un elemento en un índice explícito.
     *
     * Si el índice es menor o igual que cero, el elemento se agrega al inicio
     * de la lista. Si el índice es mayor o igual que el número de elementos en
     * la lista, el elemento se agrega al fina de la misma. En otro caso,
     * después de mandar llamar el método, el elemento tendrá el índice que se
     * especifica en la lista.
     * @param i el índice dónde insertar el elemento. Si es menor que 0 el
     *          elemento se agrega al inicio de la lista, y si es mayor o igual
     *          que el número de elementos en la lista se agrega al final.
     * @param elemento el elemento a insertar.
     * @throws IllegalArgumentException si <code>elemento</code> es
     *         <code>null</code>.
     */
    public void inserta(int i, T elemento) {
        if(elemento == null){
            throw new IllegalArgumentException("No se admiten valores nulos");
        }

        if(i <= 0){
            agregaInicio(elemento);

        } else if (i >= getLongitud()){
            agregaFinal(elemento);

        } else {
            Nodo n = new Nodo(elemento);//Creación del nodo.
            Nodo s = getIesimo(i);//Obtención del iesimo nodo.
            Nodo a = s.anterior;//Creación del nodo siguiente a s.
            n.anterior = a;
            a.siguiente = n;
            n.siguiente = s;
            s.anterior = n;
            longitud++;
        }
    }

    /**
     * Elimina un elemento de la lista. Si el elemento no está contenido en la
     * lista, el método no la modifica.
     * @param elemento el elemento a eliminar.
     */
    @Override public void elimina(T elemento) {
        Nodo n = coincidencia(elemento);//genera el nodo a partir de la coincidencia en la lista
        
        if(n == null){//Si n es nulo, el elemento no está en la lista.
            //no hace nada
            
        } else if (cabeza == rabo && cabeza.elemento.equals(elemento)){//Si la lista tiene un solo elemento y es el que se recibió como argumento
            limpia();

        } else if(n == cabeza || n.anterior == null){//Si el elemento es la cabeza.
            eliminaPrimero();

        } else if(n == rabo || n.siguiente ==null){//Si el elemento es el rabo.
            eliminaUltimo();

        } else {//Si el elemento no es la cabeza ni el rabo.
            Nodo a = n.anterior;
            Nodo s = n.siguiente;
            a.siguiente = s;
            s.anterior = a;
            longitud--;
        }
    }

    /**
     * Recorre la lista hasta encontrar una coincidencia entre el 
     * parámetro y el elemento de un nodo.
     * @param elemento el elemento que se busca encontrar en la lista
     * @return n, el nodo que contiene la coincidencia.
     */
    private Nodo coincidencia(T elemento){
        if(!contiene(elemento)){//Si la lista no contiene el elemento
            return null;
        }

        Nodo n = cabeza;
        //Ciclo, se recorre la lista mientras n no sea null y no ocurra la igualdad entre elementos
        while(n!=null && !n.elemento.equals(elemento)){
            n = n.siguiente;
        }
        return n;
    }

    /**
     * Elimina el primer elemento de la lista y lo regresa.
     * @return el primer elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaPrimero() {
        if (cabeza == null){
            throw new NoSuchElementException("Lista vacía");
        }

        T e = cabeza.elemento;//Para almacenar el valor guardado en la cabeza
        cabeza = cabeza.siguiente;//Para actualizar el valor de la cabeza a su siguiente

        if(cabeza != null){//Si la nueva cabeza no es nula, se actualiza su anterior a nulo.
            cabeza.anterior = null;
        } else{//Si la nueva cabeza es nula, se actualiza el rabo a nulo.
            rabo = null;
        }

        longitud--;
        return e;
    }

    /**
     * Elimina el último elemento de la lista y lo regresa.
     * @return el último elemento de la lista antes de eliminarlo.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T eliminaUltimo() {
        if (esVacia()){
            throw new NoSuchElementException("Lista vacía");
        }

        T e = rabo.elemento;//para almacenar el elemento del rabo a eliminar.
        rabo = rabo.anterior;//actualizamos el rabo a su anterior.

        if(rabo != null){//Si el nuvo rabo no es nulo, entonces su siguiente es nulo.
            rabo.siguiente = null;
        } else {
            cabeza = null;//Si el nuevo rabo es nulo, entonces se actualiza el valor de la cabeza a nulo.
        }

        longitud--;
        return e;
    }

    /**
     * Nos dice si un elemento está en la lista.
     * @param elemento el elemento que queremos saber si está en la lista.
     * @return <code>true</code> si <code>elemento</code> está en la lista,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        return indiceDe(elemento) != -1;
    }

    /**
     * Regresa la reversa de la lista.
     * @return una nueva lista que es la reversa la que manda llamar el método.
     */
    public Lista<T> reversa() {
        Lista<T> r = new Lista<T>();

        Nodo n = rabo;//Se inicializa n en el rabo

        while(n!=null){
        //for(int i = 0; i < getLongitud() && n != null; i++){
            r.agrega(n.elemento);//Se agrega el anterior del nodo.
            n = n.anterior;//Se itera hacia el anterior de n.
        }
        
        return r;
    }

    /**
     * Regresa una copia de la lista. La copia tiene los mismos elementos que la
     * lista que manda llamar el método, en el mismo orden.
     * @return una copiad de la lista.
     */
    public Lista<T> copia() {
        Lista<T> cp = new Lista<T>();

        Nodo n = cabeza;//Se inicializa n en la cabeza.

        while(n!=null){
        //for(int i = 0; i<getLongitud() && n!=null; i++){
            cp.agregaFinal(n.elemento);//Se agrega el elemento de n a la copia
            n = n.siguiente;//Se itera hacia el siguiente de n.
        }
        return cp;
    }

    /**
     * Limpia la lista de elementos, dejándola vacía.
     */
    @Override public void limpia() {
        cabeza = null;
        rabo = null;
        longitud = 0;
    }

    /**
     * Regresa el primer elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getPrimero() {
        if(esVacia()){
            throw new NoSuchElementException("Lista vacía");
        }
        return cabeza.elemento;
    }

    /**
     * Regresa el último elemento de la lista.
     * @return el primer elemento de la lista.
     * @throws NoSuchElementException si la lista es vacía.
     */
    public T getUltimo() {
        if(esVacia()){
            throw new NoSuchElementException("Lista vacía");
        }
        return rabo.elemento;
    }

    /**
     * Regresa el <em>i</em>-ésimo elemento de la lista.
     * @param i el índice del elemento que queremos.
     * @return el <em>i</em>-ésimo elemento de la lista.
     * @throws ExcepcionIndiceInvalido si <em>i</em> es menor que cero o mayor o
     *         igual que el número de elementos en la lista.
     */
    public T get(int i) {
        if(i < 0 || i >= getLongitud()){//En caso de índice fuera de rango
            throw new ExcepcionIndiceInvalido("Índice inválido");
        }

        Nodo n = getIesimo(i);//Se emplea el método auxiliar para obtener el iesimo nodo
        return n.elemento;
    }

    /**
     * Regresa el índice del elemento recibido en la lista.
     * @param elemento el elemento del que se busca el índice.
     * @return el índice del elemento recibido en la lista, o -1 si el elemento
     *         no está contenido en la lista.
     */
    public int indiceDe(T elemento) {
        Nodo n = cabeza;
        int i = 0;

        while(n!=null){
        //for(int j = 0; j<getLongitud(); j++){
            
            if(n.elemento.equals(elemento)){//En caso de darse la coincidencia
                return i;
            }
            //Se recorre al siguiente nodo y se incrementa el contador
            n = n.siguiente;
            i++;
        }
        return -1;
    }

    /**
     * Regresa una representación en cadena de la lista.
     * @return una representación en cadena de la lista.
     */
    @Override public String toString() {
        Nodo n = cabeza;
        StringBuilder s = new StringBuilder();
        s.append("[");//Abre corchete 

        if(n!=null){//Para verificar que la lista no es vacía
            
            while(n.siguiente!=null){//Mientras haya un siguiente nodo distinto de null, el ciclo continúa
                s.append(String.format("%s, ",n.elemento));
                n = n.siguiente;
            }
            s.append(String.format("%s",rabo.elemento));//En el ciclo quedó el rabo pendiente a ser adjuntado a s
        }

        s.append("]");//Cierra corchete
        return s.toString();
    }

    /**
     * Nos dice si la lista es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la lista es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") Lista<T> lista = (Lista<T>)objeto;

        if(getLongitud() != lista.getLongitud()){
            return false;
        }

        //Declaración de los nodos para cada lista.
        Nodo a = cabeza;
        Nodo b = lista.cabeza;

        while(a!=null){

            //If para verificar si los elementos son diferentes.
            if(!(a.elemento.equals(b.elemento))){
                return false;
            }

            //Recorre hacia los nodos siguientes.
            a = a.siguiente;
            b = b.siguiente;  
        }
        return true;
    }

    /**
     * Regresa un iterador para recorrer la lista en una dirección.
     * @return un iterador para recorrer la lista en una dirección.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Regresa un iterador para recorrer la lista en ambas direcciones.
     * @return un iterador para recorrer la lista en ambas direcciones.
     */
    public IteradorLista<T> iteradorLista() {
        return new Iterador();
    }

    /**
     * Regresa una copia de la lista, pero ordenada. Para poder hacer el
     * ordenamiento, el método necesita una instancia de {@link Comparator} para
     * poder comparar los elementos de la lista.
     * @param comparador el comparador que la lista usará para hacer el
     *                   ordenamiento.
     * @return una copia de la lista, pero ordenada.
     */
    public Lista<T> mergeSort(Comparator<T> comparador) {
        if(longitud<=1){//Clausula de escape, genera solo una copia.
            return this.copia();
        }
        //Declaración de las dos listas.
        Lista<T> l1 = new Lista<T>();
        Lista<T> l2 = new Lista<T>();
        Nodo n = cabeza;

        //Mitad de la longitud.
        int half = longitud/2;
        
        //Generamos la primera lista.
        for(int i = 0; i<half; i++){
            l1.agregaFinal(n.elemento);
            n = n.siguiente;
        }

        //Generamos la segunda lista.
        while(n!=null){
            l2.agregaFinal(n.elemento);
            n = n.siguiente;
        }

        //Llamadas recursivas del método
        l1 = l1.mergeSort(comparador);
        l2 = l2.mergeSort(comparador);
        
        //Mezcla de las dos listas
        return mezcla(comparador, l1, l2);
    }

    /**
     * Regresa la mezcla de dos listas ordenadas.
     * @param comparador el comparador que la lista usará para devolver una lista ordenada.
     * @param a una lista.
     * @param b otra lista.
     */
    private Lista<T> mezcla(Comparator<T> comparador, Lista<T> a, Lista<T> b) {
        Lista<T> l = new Lista<T>();
        Nodo i = a.cabeza;
        Nodo j = b.cabeza;
        
        //Ciclo para llenar la primera parte de la lista. Se llena primero con la lista menor.
        while(i!=null && j!=null){//Termina cuando se acaben los elementos de alguna lista
            if(comparador.compare(i.elemento,j.elemento)<=0){//Si i<=j.
                l.agregaFinal(i.elemento);
                i = i.siguiente;
                
            } else{//Si i>j
                l.agregaFinal(j.elemento);
                j = j.siguiente;
            }
        }
        
        //Quedaron pendientes de agregar los elementos de la otra lista:
        while(i!=null){//En caso de que los elementos pendientes sean de a.
            l.agregaFinal(i.elemento);
            i = i.siguiente;
        }
        while(j!=null){//En caso de que los elementos pendientes sean de b.
            l.agregaFinal(j.elemento);
            j = j.siguiente;
        }
        
       return l;
    }

    /**
     * Regresa una copia de la lista recibida, pero ordenada. La lista recibida
     * tiene que contener nada más elementos que implementan la interfaz {@link
     * Comparable}.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista que se ordenará.
     * @return una copia de la lista recibida, pero ordenada.
     */
    public static <T extends Comparable<T>>
    Lista<T> mergeSort(Lista<T> lista) {
        return lista.mergeSort((a, b) -> a.compareTo(b));
    }

    /**
     * Busca un elemento en la lista ordenada, usando el comparador recibido. El
     * método supone que la lista está ordenada usando el mismo comparador.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador con el que la lista está ordenada.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public boolean busquedaLineal(T elemento, Comparator<T> comparador) {
        Nodo n = cabeza;
        //El ciclo termina si se termina de iterar el arreglo o resulta que elemento es mayor que n.elemento
        while(n!=null && comparador.compare(n.elemento, elemento)<=0){//si n.elemento <= elemento
            if(elemento.equals(n.elemento)){
                return true;
            }
            n = n.siguiente;
        }
        return false;
    }

    /**
     * Busca un elemento en una lista ordenada. La lista recibida tiene que
     * contener nada más elementos que implementan la interfaz {@link
     * Comparable}, y se da por hecho que está ordenada.
     * @param <T> tipo del que puede ser la lista.
     * @param lista la lista donde se buscará.
     * @param elemento el elemento a buscar.
     * @return <code>true</code> si el elemento está contenido en la lista,
     *         <code>false</code> en otro caso.
     */
    public static <T extends Comparable<T>>
    boolean busquedaLineal(Lista<T> lista, T elemento) {
        return lista.busquedaLineal(elemento, (a, b) -> a.compareTo(b));
    }
}
