package mx.unam.ciencias.edd;

import java.util.Comparator;

/**
 * Clase para ordenar y buscar arreglos genéricos.
 */
public class Arreglos {

    /* Constructor privado para evitar instanciación. */
    private Arreglos() {}

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     */
    public static <T> void
    quickSort(T[] arreglo, Comparator<T> comparador) {
        //Llamada al método privado, considerando los extremos primordiales del arreglo.
        quickSort(arreglo, comparador, 0, arreglo.length-1);
    }

    /**
     * Ordena el arreglo proporcionando el limite inferior
     * y el límite superior del arreglo. 
     * Nota: Se hace por a parte para facilitar las llamadas recursivas del método.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordenar el arreglo.
     * @param min el límite inferior del arreglo, siempre será 0 en su llamada inicial.
     * @param max el límite superior del arreglo, siempre será longitud-1 en su llamada inicial.
     */
    private static <T> void quickSort(T[] arreglo, Comparator<T> comparador, int min, int max){
        if(max<=min){
            return;
        }

        //Declaración de los apuntadores:
        int leftP = min+1;
        int rightP = max;
        //Enteros que serán utilizados para comparar elementos.
        int cmprA,cmprB;

        while(leftP < rightP){
            //Comparadores, se almacena el valor de la operación compare para su uso en los if.
            cmprA = comparador.compare(arreglo[leftP], arreglo[min]);//diferencia entre leftP y min.
            cmprB = comparador.compare(arreglo[rightP], arreglo[min]);//diferencia entre rightP y min.

            if(cmprA>0 && cmprB<=0){//si arr[leftP]>arr[min] y arr[rightP]<=arr[min].
                intercambia(arreglo, leftP, rightP);
                leftP++;
                rightP--;
            } else if(cmprA <= 0){//Si arr[leftP]<=arr[min]
                leftP++;
            } else{
                rightP--;
            }
        }

        if(comparador.compare(arreglo[leftP], arreglo[min])>0){//si arr[leftP]>arr[min]
            leftP--;
        }
        intercambia(arreglo, min, leftP);//Genera el intercambio entre el minimo y el apuntador izquierdo
        //Llamadas recursivas al método
        quickSort(arreglo, comparador, min, leftP-1);
        quickSort(arreglo, comparador, leftP+1, max);
    }

    /**
     * Ordena el arreglo recibido usando QickSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    quickSort(T[] arreglo) {
        quickSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo a ordenar.
     * @param comparador el comparador para ordernar el arreglo.
     */
    public static <T> void
    selectionSort(T[] arreglo, Comparator<T> comparador) {
        for(int i = 0; i<arreglo.length-1 ; i++){
            int m = i;
            for(int j = i+1; j<arreglo.length; j++){//Para encontrar el mínimo del resto del arreglo
                if(comparador.compare(arreglo[j], arreglo[m]) < 0){//Si j<m se actualiza el valor de m a j
                    m = j;
                }
            }
            intercambia(arreglo, i, m);
        }
    }

    /**
     * Intercambia los valores del arreglo proporcionados por dos índices.
     * @param arr el arreglo sobre el que se aplica el intercambio de valores
     * @param i el i-ésimo elemento del arreglo (current item).
     * @param m el m-ésimo elemento del arreglo (current minimum).
     */
    private static <T> void intercambia(T[] arr, int i, int m){
        T x = arr[i];
        arr[i] = arr[m];
        arr[m] = x;
    }

    /**
     * Ordena el arreglo recibido usando SelectionSort.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     */
    public static <T extends Comparable<T>> void
    selectionSort(T[] arreglo) {
        selectionSort(arreglo, (a, b) -> a.compareTo(b));
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T> int
    busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador) {
        //Llamada al método privado, considerando los extremos primordiales del arreglo.
        return busquedaBinaria(arreglo, elemento, comparador, 0, arreglo.length-1);
    }

    /**
     * Busca el índice del elemento solicitado en el arreglo o -1 si no se encuentra.
     * Nota: el método se hace a parte para facilitar las llamadas recursivas.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo el arreglo dónde buscar.
     * @param elemento el elemento a buscar.
     * @param comparador el comparador para hacer la búsqueda.
     * @param min el límite inferior sobre el cual buscar.
     * @param max el límite superior del arreglo en el cual buscar.
     * @return el índice del elemento en el arreglo (o -1)
     */
    private static <T> int busquedaBinaria(T[] arreglo, T elemento, Comparator<T> comparador, int min, int max){
        if(max<min){//Clausula de escape. Si resulta que el índice superior es mayor al menor.
            return -1;
        }
        
        int midIndex = min + (max - min)/2;//Variable del indice de en medio
        T midElem = arreglo[midIndex];//Variable para el elemento dentro del índice en medio

        if(elemento.equals(midElem)){//Si los elementos coinciden, se retorna el indice de enmedio.
            return midIndex;
        } 
        
        if(comparador.compare(elemento, midElem)<0){//Si elemento < midElem, se hace recursión en [min,midIndex-1]
            return busquedaBinaria(arreglo, elemento, comparador, min, midIndex-1);
        } else { //En otro caso (midElem < elemento) se hace recursión en [midIndex+1, max]
            return busquedaBinaria(arreglo, elemento, comparador, midIndex+1, max);
        }
    }

    /**
     * Hace una búsqueda binaria del elemento en el arreglo. Regresa el índice
     * del elemento en el arreglo, o -1 si no se encuentra.
     * @param <T> tipo del que puede ser el arreglo.
     * @param arreglo un arreglo cuyos elementos son comparables.
     * @param elemento el elemento a buscar.
     * @return el índice del elemento en el arreglo, o -1 si no se encuentra.
     */
    public static <T extends Comparable<T>> int
    busquedaBinaria(T[] arreglo, T elemento) {
        return busquedaBinaria(arreglo, elemento, (a, b) -> a.compareTo(b));
    }
}
