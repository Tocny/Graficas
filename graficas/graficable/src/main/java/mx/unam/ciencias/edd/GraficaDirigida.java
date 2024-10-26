package mx.unam.ciencias.edd;

import java.util.*;

/**
 * Clase para gráficas. Una gráfica es un conjunto de vértices y aristas, tales
 * que las aristas son un subconjunto del producto cruz de los vértices.
 */
public class GraficaDirigida<T> implements Coleccion<T> {

    /* Clase interna privada para iteradores. */
    private class Iterador implements Iterator<T> {

        /* Iterador auxiliar. */
        private Iterator<Vertice> iterador;

        /* Construye un nuevo iterador, auxiliándose de la lista de vértices. */
        public Iterador() {
            // Aquí va su código.
            iterador = vertices.values().iterator();
        }

        /* Nos dice si hay un siguiente elemento. */
        @Override public boolean hasNext() {
            // Aquí va su código.
            return iterador.hasNext();
        }

        /* Regresa el siguiente elemento. */
        @Override public T next() {
            // Aquí va su código.
            return iterador.next().elemento;
        }
    }

    /* Clase interna privada para vértices. */
    private class Vertice implements VerticeGrafica<T>, Comparable<Vertice> {

        /* El elemento del vértice. */
        private T elemento;
        /* La distancia del vértice. */
        private double distancia;
        /* El diccionario de vecinos del vértice. */
        private Map<T, Vecino> vecinos;
        /**Diccionario de vecinos apuntdores. */
        private Map<T, Vecino> apuntadores;

        /* Crea un nuevo vértice a partir de un elemento. */
        public Vertice(T elemento) {
            // Aquí va su código.
            this.elemento = elemento;
            vecinos = new HashMap<>();
            apuntadores = new HashMap<>();
        }

        /* Regresa el elemento del vértice. */
        @Override public T get() {
            // Aquí va su código.
            return elemento;
        }

        /* Regresa el grado del vértice. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecinos.size();
        }

        /**Regresa el número de vertices que apuntan al vertice. */
        public int getGradoApuntadores(){
            return apuntadores.size();
        }

        /* Regresa un iterable para los vecinos. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            List<VerticeGrafica<T>> vecinosList = new ArrayList<>();
            for (Vecino vecino : vecinos.values()) {
                vecinosList.add(vecino);  // Asegúrate de que Vecino implemente VerticeGrafica
            }
            return vecinosList;
        }

        /* Compara dos vértices por distancia. */
        @Override public int compareTo(Vertice vertice) {
            // Aquí va su código.
            if(this.distancia < vertice.distancia){
                return -1;

            } else if (this.distancia > vertice.distancia){
                return 1;

            } else if(this.distancia == vertice.distancia){
                return 0;
            }

            return 0; 
        }
    }

    /* Clase interna privada para vértices vecinos. */
    private class Vecino implements VerticeGrafica<T> {

        /* El vértice vecino. */
        public Vertice vecino;
        /* El peso de la arista conectando al vértice con su vértice vecino. */
        public double peso;

        /* Construye un nuevo vecino con el vértice recibido como vecino y el
         * peso especificado. */
        public Vecino(Vertice vecino, double peso) {
            // Aquí va su código.
            this.vecino = vecino;
            this.peso = peso;
        }

        /* Regresa el elemento del vecino. */
        @Override public T get() {
            // Aquí va su código.
            return vecino.elemento;
        }

        /* Regresa el grado del vecino. */
        @Override public int getGrado() {
            // Aquí va su código.
            return vecino.getGrado();
        }

        /* Regresa un iterable para los vecinos del vecino. */
        @Override public Iterable<? extends VerticeGrafica<T>> vecinos() {
            // Aquí va su código.
            List<VerticeGrafica<T>> vecinosList = new ArrayList<>();
    
            for (Vecino vecino : this.vecino.vecinos.values()) {
                vecinosList.add(vecino);  // Agregamos el vecino a la lista
            }
            
            return vecinosList;
        }
    }

    /* Interface para poder usar lambdas al buscar el elemento que sigue al
     * reconstruir un camino. */
    @FunctionalInterface
    private interface BuscadorCamino<T> {
        /* Regresa true si el vértice se sigue del vecino. */
        public boolean seSiguen(GraficaDirigida<T>.Vertice v, GraficaDirigida<T>.Vecino a);
    }

    /* Vértices. */
    private Map<T, Vertice> vertices;
    /* Número de aristas. */
    private int aristas;

    /**
     * Constructor único.
     */
    public GraficaDirigida() {
        // Aquí va su código.
        vertices = new HashMap<>();
    }

    /**
     * Regresa el número de elementos en la gráfica. El número de elementos es
     * igual al número de vértices.
     * @return el número de elementos en la gráfica.
     */
    @Override public int getElementos() {
        // Aquí va su código.
        return vertices.size();
    }

    /**
     * Regresa el número de aristas.
     * @return el número de aristas.
     */
    public int getAristas() {
        // Aquí va su código.
        return aristas;
    }

    /**
     * Agrega un nuevo elemento a la gráfica.
     * @param elemento el elemento a agregar.
     * @throws IllegalArgumentException si el elemento ya había sido agregado a
     *         la gráfica.
     */
    @Override public void agrega(T elemento) {
        //System.out.println("LOL");
        // Aquí va su código.
        if(elemento == null){//Elemento nulo.
            throw new IllegalArgumentException("Elemento nulo.");
        }

        if(contiene(elemento)){//Elemento ya en el conjunto.
            throw new IllegalArgumentException("Ya se encuentra en el conjunto de vértices: " + elemento);
        }

        //Creamos el vértice y lo agregamos a la lista de vértices.
        Vertice v = new Vertice(elemento);
        vertices.put(elemento, v);
        //System.out.println("LOLLOL");
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica. El peso de la arista que conecte a los elementos será 1.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, o si a es
     *         igual a b.
     */
    public void conecta(T a, T b) {
        // Aquí va su código.
        //Excepciones
        if(a.equals(b)){//Elementos iguales
            throw new IllegalArgumentException("Elementos iguales, no admitimos lazos.");
        }

        //Vertices de los elementos.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);

        if(sonVecinos(a, b)){//Elementos ya vecinos.
            throw new IllegalArgumentException("Los vértices ya están conectados.");
        }

        //Incrementamos el contador de aristas.
        aristas++;

        //Creamos la instancia de vecinos con peso 1.0
        Vecino vcA = new Vecino(vA, 1.0);
        Vecino vcB = new Vecino(vB, 1.0);

        //Agregamos a sus listas de adyacencias.
        vA.vecinos.put(b, vcB);
        vB.apuntadores.put(a, vcA);
    }

    /**
     * Conecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica.
     * @param a el primer elemento a conectar.
     * @param b el segundo elemento a conectar.
     * @param peso el peso de la nueva vecino.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b ya están conectados, si a es
     *         igual a b, o si el peso es no positivo.
     */
    public void conecta(T a, T b, double peso) {
        // Aquí va su código.
        //Excepciones
        if(a.equals(b)){//Elementos iguales
            throw new IllegalArgumentException("Elementos iguales, no admitimos lazos.");
        }

        if(peso <= 0){
            throw new IllegalArgumentException("No se admiten pesos menores a 0.");
        }

        //Vertices de los elementos.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);

        if(sonVecinos(a, b)){//Elementos ya vecinos.
            throw new IllegalArgumentException("Los vértices ya están conectados.");
        }

        //Incrementamos el contador de aristas.
        aristas++;

        //Creamos la instancia de vecinos con peso 1.0
        Vecino vcA = new Vecino(vA, peso);
        Vecino vcB = new Vecino(vB, peso);

        //Agregamos a sus listas de adyacencias.
        vA.vecinos.put(b, vcB);
        vB.apuntadores.put(a, vcA);
    }

    /**
     * Desconecta dos elementos de la gráfica. Los elementos deben estar en la
     * gráfica y estar conectados entre ellos.
     * @param a el primer elemento a desconectar.
     * @param b el segundo elemento a desconectar.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public void desconecta(T a, T b) {
        // Aquí va su código.
        //Vertices de los elementos.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);

        if(!sonVecinos(a, b)){//Elementos ya vecinos.
            throw new IllegalArgumentException("Los vértices no están conectados.");
        }

        //Decrementamos el contador de aristas.
        aristas--;

        vA.vecinos.remove(b);
        vB.apuntadores.remove(a);
    }

    /**
     * Nos dice si el elemento está contenido en la gráfica.
     * @return <code>true</code> si el elemento está contenido en la gráfica,
     *         <code>false</code> en otro caso.
     */
    @Override public boolean contiene(T elemento) {
        // Aquí va su código.
        return vertices.containsKey(elemento);
    }

    /**
     * Elimina un elemento de la gráfica. El elemento tiene que estar contenido
     * en la gráfica.
     * @param elemento el elemento a eliminar.
     * @throws NoSuchElementException si el elemento no está contenido en la
     *         gráfica.
     */
    @Override public void elimina(T elemento) {
        // Aquí va su código.
        throw new UnsupportedOperationException("Operación no soportada");

        /*
        //Obtenemos el vértice del elemento.
        Vertice v = (Vertice) vertice(elemento);

        // Almacenamos los vecinos en una lista para evitar ConcurrentModificationException
        List<T> vecinosAEliminar = new ArrayList<>(v.vecinos.keySet());

        for (T vecinoElemento : vecinosAEliminar) {
            desconecta(v.elemento, vecinoElemento);
        }

        // Almacenamos los apuntadores (los que apuntan a él) también para evitar ConcurrentModificationException.
        List<T> apuntadoresAEliminar = new ArrayList<>(v.apuntadores.keySet());

        // Eliminamos todas las aristas que entran en el vértice (apuntador -> v).
        for (T apuntadorElemento : apuntadoresAEliminar) {
            desconecta(apuntadorElemento, v.elemento);
        }

        // Eliminar el vértice del mapa
        vertices.remove(elemento);
        */
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinos(T a, T b) {
        // Aquí va su código.
        //Vertices de los elementos.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);
        
        //Perificamos si los dos son vecinos entre sí.
        return vA.vecinos.containsKey(b) && vB.apuntadores.containsKey(a);
    }

    /**
     * Nos dice si dos elementos de la gráfica están conectados. Los elementos
     * deben estar en la gráfica.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return <code>true</code> si a y b son vecinos, <code>false</code> en otro caso.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     */
    public boolean sonVecinosApuntador(T a, T b) {
        // Aquí va su código.
        return sonVecinos(b, a);
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPeso(T a, T b) {
        // Aquí va su código.
        if(!sonVecinos(a, b)){//Si no son vecinos.
            throw new IllegalArgumentException("No son vécinos: " + a + ", " + b);
        }

        //Obtenemos los vértices.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);

        //tomamos el peso de "a" de los vecinos de b.
        return vB.apuntadores.get(a).peso;
    }

    /**
     * Regresa el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @return el peso de la arista que comparten los vértices que contienen a
     *         los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados.
     */
    public double getPesoApuntador(T a, T b) {
        // Aquí va su código.
        return getPeso(b, a);
    }

    /**
     * Define el peso de la arista que comparten los vértices que contienen a
     * los elementos recibidos.
     * @param a el primer elemento.
     * @param b el segundo elemento.
     * @param peso el nuevo peso de la arista que comparten los vértices que
     *        contienen a los elementos recibidos.
     * @throws NoSuchElementException si a o b no son elementos de la gráfica.
     * @throws IllegalArgumentException si a o b no están conectados, o si peso
     *         es menor o igual que cero.
     */
    public void setPeso(T a, T b, double peso) {
        // Aquí va su código.
        if(!(sonVecinos(a, b) || sonVecinos(b, a))){//Si no son vecinos.
            throw new IllegalArgumentException("No son vécinos: " + a + ", " + b);
        }

        if(peso <= 0){//Si el peso dado es menor o igual a 0.
            throw new IllegalArgumentException("Peso inválido");
        }

        //Obtenemos los vértices.
        Vertice vA = (Vertice) vertice(a);
        Vertice vB = (Vertice) vertice(b);

        //Asignamos pesos.
        vA.vecinos.get(b).peso = peso;
        vB.apuntadores.get(a).peso = peso;
    }

    /**
     * Regresa el vértice correspondiente el elemento recibido.
     * @param elemento el elemento del que queremos el vértice.
     * @throws NoSuchElementException si elemento no es elemento de la gráfica.
     * @return el vértice correspondiente el elemento recibido.
     */
    public VerticeGrafica<T> vertice(T elemento) {
        // Aquí va su código.
        //Verificamos que el diccionario contenga el elemento.
        if(vertices.containsKey(elemento)){
            return vertices.get(elemento);
        }

        //En otro caso, tiramos la excepción.
        throw new NoSuchElementException("No se encuentra el elemento: " + elemento);

    }

    /**
     * Método que regresa una lista de los elementos (llaves del map).
     * 
     * @return una lista de los elementos (llaves).
     */
    public List<T> obtenerElementos() {
        return new ArrayList<>(vertices.keySet());
    }

    /**
     * Método que regresa todos los elementos de los vecinos de un vértice.
     * 
     * @param elemento el elemento que se va a buscar en los vértices.
     * @return una lista de vecinos del elemento.
     */
    public List<T> obtenerVecinos(T elemento){

        Vertice vertice = (Vertice) vertice(elemento);

        return new ArrayList<>(vertice.vecinos.keySet());

    }

    /**
     * Nos dice si la gráfica es conexa.
     * @return <code>true</code> si la gráfica es conexa, <code>false</code> en
     *         otro caso.
     */
    public boolean esConexa() {
        // Aquí va su código.
        
        Iterator<T> iterador = vertices.keySet().iterator();
        Vertice primero = (Vertice) vertice(iterador.next());

        //Comparamos el número de vértices coloreados de negro con el número de vértices en el conjunto.
        return dfsInt(primero.get(), x -> {}) == vertices.size();
    }

    /**
     * Realiza la acción recibida en cada uno de los vértices de la gráfica, en
     * el orden en que fueron agregados.
     * @param accion la acción a realizar.
     */
    public void paraCadaVertice(AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        //Recorremos la lista de vértices y aplicamos la acción.
        for(Vertice v : vertices.values()){
            accion.actua(v);
        }
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por BFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void bfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        if (!contiene(elemento)) {
            throw new NoSuchElementException("No está el elemento: " + elemento);
        }
        
        // Inicializar la cola para BFS
        Queue<Vertice> cola = new LinkedList<>();
        Vertice v = (Vertice) vertice(elemento);
        
        // Marca los vértices visitados
        Map<Vertice, Boolean> visitados = new HashMap<>();
        visitados.put(v, true);
        
        // Añadir el vértice inicial a la cola
        cola.add(v);
        
        // Realizar el recorrido
        while (!cola.isEmpty()) {
            Vertice u = cola.poll();
            accion.actua(u);
            
            // Procesar vecinos
            for (Vecino ve : u.vecinos.values()) {
                if (!visitados.containsKey(ve.vecino)) {
                    visitados.put(ve.vecino, true);
                    cola.add(ve.vecino);
                }
            }
        }
    }
    
    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public int dfsInt(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        if (!contiene(elemento)) {
            throw new NoSuchElementException("No está el elemento: " + elemento);
        }

        // Inicializar la pila para DFS
        Stack<Vertice> pila = new Stack<>();
        Vertice v = (Vertice) vertice(elemento);
        
        // Marca los vértices visitados
        Map<Vertice, Boolean> visitados = new HashMap<>();
        visitados.put(v, true);
        
        // Añadir el vértice inicial a la pila
        pila.push(v);
        
        // Realizar el recorrido
        while (!pila.isEmpty()) {
            Vertice u = pila.pop();
            accion.actua(u);
            
            // Procesar vecinos
            for (Vecino ve : u.vecinos.values()) {
                if (!visitados.containsKey(ve.vecino)) {
                    visitados.put(ve.vecino, true);
                    pila.push(ve.vecino);
                }
            }
        }
        return visitados.size();
    }

    /**
     * Realiza la acción recibida en todos los vértices de la gráfica, en el
     * orden determinado por DFS, comenzando por el vértice correspondiente al
     * elemento recibido.
     * @param elemento el elemento sobre cuyo vértice queremos comenzar el
     *        recorrido.
     * @param accion la acción a realizar.
     * @throws NoSuchElementException si el elemento no está en la gráfica.
     */
    public void dfs(T elemento, AccionVerticeGrafica<T> accion) {
        // Aquí va su código.
        if (!contiene(elemento)) {
            throw new NoSuchElementException("No está el elemento: " + elemento);
        }

        // Inicializar la pila para DFS
        Stack<Vertice> pila = new Stack<>();
        Vertice v = (Vertice) vertice(elemento);
        
        // Marca los vértices visitados
        Map<Vertice, Boolean> visitados = new HashMap<>();
        visitados.put(v, true);
        
        // Añadir el vértice inicial a la pila
        pila.push(v);
        
        // Realizar el recorrido
        while (!pila.isEmpty()) {
            Vertice u = pila.pop();
            accion.actua(u);
            
            // Procesar vecinos
            for (Vecino ve : u.vecinos.values()) {
                if (!visitados.containsKey(ve.vecino)) {
                    visitados.put(ve.vecino, true);
                    pila.push(ve.vecino);
                }
            }
        }
    }

    /**
     * Nos dice si la gráfica es vacía.
     * @return <code>true</code> si la gráfica es vacía, <code>false</code> en
     *         otro caso.
     */
    @Override public boolean esVacia() {
        // Aquí va su código.
        return vertices.isEmpty();
    }

    /**
     * Limpia la gráfica de vértices y aristas, dejándola vacía.
     */
    @Override public void limpia() {
        // Aquí va su código.
        vertices.clear();
        aristas = 0;
    }

    /**
     * Regresa una representación en cadena de la gráfica.
     * @return una representación en cadena de la gráfica.
     */
    @Override public String toString() {
        // Aquí va su código.
        StringBuilder sb = new StringBuilder();
        //Abrimos corchete para los vértices
        sb.append("{");
        
        for(Vertice v : vertices.values()){
            sb.append(v.elemento.toString() + ", ");
        }
        //Cerramos corchete de vértices y abrimos el de adyacencias
        sb.append("}, {");
        
        //Lista que acumulará los vértices que ya hayamos adjuntado al sb y evitar repetir adyacencias.
        List<T> acumulados = new ArrayList<T>();
        //Recorrido para todas las adyacencias de los vértices.
        for(Vertice v : vertices.values()){
            for(Vecino vc : v.vecinos.values()){
                //Si ya se ha acumuladoel vecino de v, entonces saltamos a la siguiente iteración.
                if(acumulados.contains(vc.vecino.get())){
                    continue;
                }
                //Adjuntamos los elementos de v y su vecino no adjuntado.
                sb.append("(" + v.elemento + ", " + vc.vecino.get() + "), ");
            }
            //Acumulamos en la lista al vértice del que revisamos adyacencias.
            acumulados.add(v.elemento);
        }
        //Cerramos corchete de adyacencias.
        sb.append("}");

        return sb.toString();
    }

    /**
     * Nos dice si la gráfica es igual al objeto recibido.
     * @param objeto el objeto con el que hay que comparar.
     * @return <code>true</code> si la gráfica es igual al objeto recibido;
     *         <code>false</code> en otro caso.
     */
    @Override public boolean equals(Object objeto) {
        if (objeto == null || getClass() != objeto.getClass())
            return false;
        @SuppressWarnings("unchecked") GraficaDirigida<T> grafica = (GraficaDirigida<T>)objeto;
        // Aquí va su código.

        //Si no tienen el mismo numero de elementos.
        if(getElementos() != grafica.getElementos()){
            return false;
        }

        //Si no tienen el mismo numero de aristas.
        if(aristas != grafica.aristas){
            return false;
        }

        //Procedemos a checar los vértices.
        for (Vertice v : this.vertices.values()) {
            //Si el objeto no contiene el elemento de v.
            if (!grafica.contiene(v.elemento))
                return false;

            //Sacamos el equivalente a v en el objeto recibido.
            Vertice vG = (Vertice) grafica.vertice(v.elemento);

            //Si no tienen la misma cantidad de elementos.
            if (v.vecinos.size() != vG.vecinos.size()){
                return false;
            }

            //Procedemos a verificar que tengan los mismos vecinos.
            for (Vecino vc : v.vecinos.values()) {
                if (!vG.vecinos.containsKey(vc.vecino.elemento)){
                    return false;
                }
            }
        }

        //Para este punto ya son equivalentes.
        return true;
    }

    /**
     * Regresa un iterador para iterar la gráfica. La gráfica se itera en el
     * orden en que fueron agregados sus elementos.
     * @return un iterador para iterar la gráfica.
     */
    @Override public Iterator<T> iterator() {
        return new Iterador();
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public List<VerticeGrafica<T>> trayectoriaMinima(T origen, T destino) {
        // Aquí va su código.
        //Los vértices origen y destino.
        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);

        //Lista para almacenar los elementos.
        List<VerticeGrafica<T>> trayectoria = new ArrayList<>();

        //Si el origen y el destino son iguales.
        if(s.get().equals(t.get())){
            trayectoria.add(s);
            return trayectoria;
        }

        //Hacemos las distancias infinitas.
        final double INFINITO = Double.POSITIVE_INFINITY;
        distanciasInfinitas(s, INFINITO);

        //Creamos la cola con el origen.
        Queue<Vertice> cola = new LinkedList<>();
        cola.add(s);

        //Procedemos al while para calcular distancias.
        while(!cola.isEmpty()){
            //Marcamos "u" como el vértice que sacamos de la cola.
            Vertice u = cola.poll();

            //Recorremos todos su vecinos.
            for(Vecino vc : u.vecinos.values()){
                //Si la distancia del vecino es infinita, la incrementamos en 1 con respecto a la de u.
                if(vc.vecino.distancia == INFINITO){
                    vc.vecino.distancia = u.distancia + 1;
                    cola.add(vc.vecino);
                }
            }
        }

        //Si para este punto la distancia de t es infinita, regresamos la lista (vacía).
        if(t.distancia == INFINITO){
            return trayectoria;
        }

        //Procedemos con la construcción de la trayectoria.
        Vertice u = t;//Partimos del vértice destino.
        while(u != s){//Hasta que u sea el origen.
            for(Vecino vc : u.apuntadores.values()){
                //Si damos con un vecino que tal que d(u)-1 = d(vc.vecino)
                if(u.distancia - 1 == vc.vecino.distancia){
                    //Metemos a 'u' y actualizamos su valor al del vértice siguiente.
                    trayectoria.add(0, u);
                    u = vc.vecino;
                    break;
                }
            }
        }
        //Finalmente agregamos al vértice origen.
        trayectoria.add(0, s);

        return trayectoria;
    }

    /**
     * Calcula una trayectoria de distancia mínima entre dos vértices.
     * @param origen el vértice de origen.
     * @param destino el vértice de destino.
     * @return Una lista con vértices de la gráfica, tal que forman una
     *         trayectoria de distancia mínima entre los vértices <code>a</code> y
     *         <code>b</code>. Si los elementos se encuentran en componentes conexos
     *         distintos, el algoritmo regresa una lista vacía.
     */
    public List<T> trayectoriaMinimaElementos(T origen, T destino){
        List<VerticeGrafica<T>> listaVertices = trayectoriaMinima(origen, destino);
        List<T> trayectoria = new ArrayList<>();

        for(VerticeGrafica<T> vertice: listaVertices){
            trayectoria.add(vertice.get());
        }

        return trayectoria;
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * @throws NoSuchElementException si alguno de los dos elementos no está en
     *         la gráfica.
     */
    public List<VerticeGrafica<T>> dijkstra(T origen, T destino) {
        // Aquí va su código.
        //Los vértices origen y destino.
        Vertice s = (Vertice) vertice(origen);
        Vertice t = (Vertice) vertice(destino);

        //Lista para almacenar los elementos.
        List<VerticeGrafica<T>> trayectoria = new ArrayList<>();

        //Si el origen y el destino son iguales.
        if(s.get().equals(t.get())){
            trayectoria.add(s);
            return trayectoria;
        }

        //Hacemos las distancias infinitas.
        final double INFINITO = Double.POSITIVE_INFINITY;
        distanciasInfinitas(s, INFINITO);

        //Procedemos a determinar el tipo de monticulo para el algoritmo.
        MonticuloDijkstra<Vertice> m;//Nos apoyaremos de la interfaz.
        int n = vertices.size();
        
        /*
        if(aristas > ((n*(n - 1))/2 - n)){//Cantidades cuadráticas de vértices para el número de aristas.
            m = new MonticuloArreglo<Vertice>(vertices.values(), vertices.size());
        }else{//Cantidades lineales de vértices para el número de aristas.
            m = new MonticuloMinimo<Vertice>(vertices.values(), vertices.size());
        }*/
        m = new MonticuloArreglo<Vertice>(vertices.values(), vertices.size());

        //Procedemos con el algoritmo de Djikstra.
        while(!m.esVacia()){
            Vertice u = m.elimina();
            //Actualizamos las distancias de los vecinos
            for(Vecino v : u.vecinos.values()){
                //si d(v) > d(u) + peso(v, u)
                if(v.vecino.distancia > u.distancia + v.peso){
                    //Entonces d(v) = d(u) + peso(u, v).
                    v.vecino.distancia = u.distancia + v.peso;
                    m.reordena(v.vecino);
                }
            }
        }

        //Si para este punto la distancia de t es infinita, regresamos la lista (vacía).
        if(t.distancia == INFINITO){
            return trayectoria;
        }

        //Procedemos con la construcción de la trayectoria.
        Vertice u = t;//Partimos del vértice destino.
        while(u != s){//Hasta que u sea el origen.
            for(Vecino vc : u.apuntadores.values()){
                //Si damos con un vecino que tal que d(u)-1 = d(vc.vecino)
                if(u.distancia - vc.peso == vc.vecino.distancia){
                    //Metemos a 'u' y actualizamos su valor al del vértice siguiente.
                    trayectoria.add(0, u);
                    u = vc.vecino;
                    break;
                }
            }
        }
        //Finalmente agregamos al vértice origen.
        trayectoria.add(0, s);

        return trayectoria;
    }

    /**
     * Calcula la ruta de peso mínimo entre el elemento de origen y el elemento
     * de destino. Esta lista se genera sobre los elementos.
     * @param origen el vértice origen.
     * @param destino el vértice destino.
     * @return una trayectoria de peso mínimo entre el vértice <code>origen</code> y
     *         el vértice <code>destino</code>. Si los vértices están en componentes
     *         conexas distintas, regresa una lista vacía.
     * 
     */
    public List<T> dijkstraElementos(T origen, T destino){
        List<VerticeGrafica<T>> listaVertices = dijkstra(origen, destino);
        List<T> trayectoria = new ArrayList<>();

        for(VerticeGrafica<T> vertice: listaVertices){
            trayectoria.add(vertice.get());
        }

        return trayectoria;
    }

    /**
     * Método que vuelve infinitas las distancias de todos los vértices
     * exceptuando un vértices particular 's' (Que será el origen).
     * Claro Este método puede aplicarse sobre cualquier cota superior que se considere "infinito".
     * @param s un vértice particular al que nulificaremos su distancia.
     * @param INFINITO una cota superior que será considerada el infinito.
     */
    private void distanciasInfinitas(Vertice s, Double INFINITO){
        //Hacemos todas las distancias "infinitas" excepto 's'.
        for(Vertice v : vertices.values()){
            //Si el vértice es el origen, distancia 0.
            if(v.get().equals(s.get())){
                s.distancia = 0;
                continue;
            }
            //En otro caso, se hace infinita.
            v.distancia = INFINITO;
        }
    }

    /**
     * Combina una lista de gráficas dirigidas en una sola.
     * Los vértices que se intersecan se consideran uno solo.
     * 
     * @param graficas la lista de gráficas a combinar.
     * @return una nueva gráfica que contiene todos los vértices y aristas de las gráficas dadas.
     */
    public GraficaDirigida<T> combinarGraficas(List<GraficaDirigida<T>> graficas) {
        // Nueva gráfica combinada
        GraficaDirigida<T> graficaCombinada = new GraficaDirigida<>();

        for (GraficaDirigida<T> grafica : graficas) {
            agregarVertices(grafica, graficaCombinada);
        }

        for (GraficaDirigida<T> grafica : graficas) {
            agregarConexiones(grafica, graficaCombinada);
        }

        return graficaCombinada;
    }

    /**
     * Agrega los vértices de la gráfica dada a la gráfica combinada.
     * 
     * @param grafica la gráfica de la que se agregarán los vértices.
     * @param graficaCombinada la gráfica en la que se agregarán los vértices.
     */
    private void agregarVertices(GraficaDirigida<T> grafica, GraficaDirigida<T> graficaCombinada) {
        for (T elemento : grafica.vertices.keySet()) {
            // Verificar si el vértice ya fue agregado usando el método contiene
            if (!graficaCombinada.contiene(elemento)) {
                // Agregar el nuevo vértice a la gráfica combinada
                graficaCombinada.agrega(elemento);
            }
        }
        
    }

    /**
     * Agrega las conexiones de los vértices de la gráfica dada a la gráfica combinada.
     * 
     * @param grafica la gráfica de la que se agregarán las conexiones.
     * @param graficaCombinada la gráfica en la que se agregarán las conexiones.
     */
    private void agregarConexiones(GraficaDirigida<T> grafica, GraficaDirigida<T> graficaCombinada) {
        for (T elemento : grafica.vertices.keySet()) {
            Vertice verticeActual = grafica.vertices.get(elemento); // Obtenemos el vértice original

            // Agregar conexiones de los vecinos
            for (T vecino : verticeActual.vecinos.keySet()) {
                Vecino verticeVecino = verticeActual.vecinos.get(vecino);
                if (!graficaCombinada.sonVecinos(elemento, vecino)) {
                    graficaCombinada.conecta(elemento, vecino, grafica.getPeso(elemento, vecino)); // Conectar desde el elemento actual al vecino
                }
            }

        }
    }

}
