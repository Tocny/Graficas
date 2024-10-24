package mx.unam.ciencias.edd;

/**
 * Clase para métodos estáticos con dispersores de bytes.
 */
public class Dispersores {

    /* Constructor privado para evitar instanciación. */
    private Dispersores() {}

    /**
     * Función de dispersión XOR.
     * @param llave la llave a dispersar.
     * @return la dispersión de XOR de la llave.
     */
    public static int dispersaXOR(byte[] llave) {
        // Aquí va su código.
        //longitud de la llave.
        int l = llave.length;
        //acumulador de la dispersión.
        int r = 0;
        //índice para recorrer la llave.
        int i = 0;

        //Procedemos a hacer grupos de 4 posiciones de la llave.
        while(l>=4){
            r ^= combinaBE(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            i += 4;
            l -= 4;
        }

        //Procesamos bytes restantes, si es que hay.
        int s = 0;
        switch(l){
            case 3: s = combinaBE(llave[i], llave[i+1], llave[i+2], (byte)0);
                break;

            case 2: s = combinaBE(llave[i], llave[i+1], (byte)0, (byte)0);
                break;

            case 1: s = combinaBE(llave[i], (byte)0, (byte)0, (byte)0);
                break;
            
            default:
                break;
        }

        //Regresamos la última operación.
        return r^s;
    }

    /**
     * Función de dispersión de Bob Jenkins.
     * @param llave la llave a dispersar.
     * @return la dispersión de Bob Jenkins de la llave.
     */
    public static int dispersaBJ(byte[] llave) {
        // Aquí va su código.
        //Variables acumulativas:
        int a = 0x9E3779B9;
        int b = 0x9E3779B9;
        int c = 0xFFFFFFFF;

        //Longitud e indice.
        int l = llave.length;
        int i = 0;

        //Procedemos a formar grupos de 4 con los bytes de la llave.
        while(l>=12){
            //Acumulamos sobre a.
            a+= combinaLE(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            i += 4;
            l -= 4;

            //Acumulamos sobre b.
            b += combinaLE(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            i += 4;
            l -= 4;

            //Acumulamos sobre c.
            c += combinaLE(llave[i], llave[i+1], llave[i+2], llave[i+3]);
            i += 4;
            l -= 4;

            //Ejecutamos la mezcla y actualizamos el valor de los acumuladores.
            int[] mezcla = mezclaBJ(a, b, c);
            a = mezcla[0];
            b = mezcla[1];
            c = mezcla[2];
        }

        //Incrementamos a c el valor de la longitud.
        c += llave.length;

        //Switch para los valores de la longitud.
        switch (l) {
            //Grupos con c.
            case 11:
                c +=  mascara(llave[i+10]) << 24;
            case 10:
                c +=  mascara(llave[i+9]) << 16;
            case 9:
                c +=  mascara(llave[i+8]) << 8;
            
            //Grupos con b.
            case 8:
                b +=  mascara(llave[i+7]) << 24;
            case 7:
                b +=  mascara(llave[i+6]) << 16;
            case 6:
                b +=  mascara(llave[i+5]) << 8;
            case 5:
                b +=  mascara(llave[i+4]);

            //Grupos con a.
            case 4:
                a +=  mascara(llave[i+3]) << 24;
            case 3:
                a +=  mascara(llave[i+2]) << 16;
            case 2:
                a +=  mascara(llave[i+1]) << 8;
            case 1: 
                a +=  mascara(llave[i]);
            
            default: 
                break;
        }

        //Volvemos a mezclar los valores de los acumuladores.
        int[] mezcla = mezclaBJ(a, b, c);
        //Sacamos especificamente a c para regresarlo.
        c = mezcla[2];
        
        return c;
    }

    /**
     * Función de dispersión Daniel J. Bernstein.
     * @param llave la llave a dispersar.
     * @return la dispersión de Daniel Bernstein de la llave.
     */
    public static int dispersaDJB(byte[] llave) {
        // Aquí va su código.
        int h = 5381;
        
        //Procedemos con el ciclo para dispersar la llave.
        for (int i = 0; i < llave.length; i++) {
            h += (h << 5) + (llave[i] & 0xFF);
        }

        return h;
    }

    /**
     * Método mezcla para la función de Bob Jenkins.
     * @param a la variable acumulativa a.
     * @param b la variable acumulativa b.
     * @param c la variable acumulativa c.
     * @return un arreglo con las tres variables mixeadas.
     */
    private static int[] mezclaBJ(int a, int b, int c){
        //El mix de Jenkins :O
        a -= b; a -= c; a ^= (c >>> 13);
        b -= c; b -= a; b ^= (a << 8);
        c -= a; c -= b; c ^= (b >>> 13);
        a -= b; a -= c; a ^= (c >>> 12);
        b -= c; b -= a; b ^= (a << 16);
        c -= a; c -= b; c ^= (b >>> 5);
        a -= b; a -= c; a ^= (c >>> 3);
        b -= c; b -= a; b ^= (a << 10);
        c -= a; c -= b; c ^= (b >>> 15);

        //Metemos las entradas mixeadas a un arreglo
        int[] arreglo = new int[]{a, b, c};

        return arreglo;
    }

    /**
     * Método que combina 4 bytes en un entero empleando el esquema Big-endian.
     * @param a primer byte.
     * @param b segundo byte.
     * @param c tercer byte.
     * @param d cuarto byte.
     * @return un entero generado a partir de las 4 entradas.
     */
    private static int combinaBE(byte a, byte b, byte c, byte d){
        return (mascara(a) << 24) | (mascara(b) << 16) |
                (mascara(c) << 8) | mascara(d);
    }

    /**
     * Método que combina 4 bytes en un entero empleando el esquema Little-endian.
     * @param a primer byte.
     * @param b segundo byte.
     * @param c tercer byte.
     * @param d cuarto byte.
     * @return un entero generado a partir de las 4 entradas.
     */
    private static int combinaLE(byte a, byte b, byte c, byte d){
        return (mascara(d) << 24) | (mascara(c) << 16) |
                (mascara(b) << 8) | mascara(a);
    }

    /**
     * Método que aplica la máscara "0xFF" sobre un byte
     * para devolver un entero sin signo.
     * @param x un byte.
     * @return un entero sin signo a partir del byte x.
     */
    private static int mascara(byte x){
        return x & 0xFF;
    }
}
