package TDAArbol.negocio;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/**
 * Estructura arbol de busqueda multivias. Contiene una "raiz" NodoMVias<T> y
 * "orden" int T hereda de la clase Comparable.
 *
 * @author HP
 * @param <T>
 */
public class ArbolBusquedaMVias< T extends Comparable<T>> implements IArbolBusqueda<T> {

    protected NodoMVias<T> raiz;
    protected int orden;

    public ArbolBusquedaMVias() {
        this.orden = 3;
    }

    public ArbolBusquedaMVias(int orden) throws ExceptionArbolOrdenInvalido {
        if (orden < 3) {
            throw new ExceptionArbolOrdenInvalido("El orden debe ser mayor o igual a 3");
        }
        this.orden = orden;
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoMVias(this.orden, datoAInsertar);
            return true;
        }
        NodoMVias<T> nodoActual = this.raiz;
        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (existeDatoEnNodo(nodoActual, datoAInsertar)) {
                return false;// cuando el dato ya esta en el Nodo
            }
            if (nodoActual.esHoja()) { // solo se inserta en una hoja
//              if (existeDatoEnNodo(nodoActual, datoAInsertar)) {
//              return false;// cuando el dato ya esta en el Nodo
//              }
                if (nodoActual.estanTodosDatosLlenos()) { // si ya esta lleno el nodo, crea un hijo y lo inserta ahi
                    int posicionHijo = posicionHijoPorDondeBajar(nodoActual, datoAInsertar);
                    NodoMVias<T> nuevoHijo = new NodoMVias(this.orden, datoAInsertar);
                    nodoActual.setHijo(posicionHijo, nuevoHijo);
                } else {  //cuando el NodoActual tiene espacio para el datoAInsertar
                    insertarDatoEnNodo(nodoActual, datoAInsertar);
                }
                break;
            } else { // cuando el nodo no es hoja, debe recorrer
//              if (existeDatoEnNodo(nodoActual, datoAInsertar)) {
//              return false;// cuando el dato ya esta en el Nodo
//              }
                int posicionHijoPorBajar = posicionHijoPorDondeBajar(nodoActual, datoAInsertar);
                //el hijo de la posicion podria ser vacio o no
                if (!nodoActual.esHijoVacio(posicionHijoPorBajar)) { // si el hijo no es vacio se recorre por ese hijo
                    nodoActual = nodoActual.getHijo(posicionHijoPorBajar);
                } else {  // si el hijo es vacio ,crea un nuevo nodo y lo inserta ahi
                    NodoMVias<T> nuevoHijo = new NodoMVias(this.orden, datoAInsertar);
                    nodoActual.setHijo(posicionHijoPorBajar, nuevoHijo);
                    break;
                }
            }
        }
        return true;
    }

    /**
     * Devuelve TRUE si existe un T Dato válido (no Nulo) en el nodoActual
     * tambien retorna False si el dato no esta en el nodo o si solo tiene datos
     * vacios(nulos)
     *
     * @param nodoActual
     * @param dato
     * @return
     */
    protected boolean existeDatoEnNodo(NodoMVias<T> nodoActual, T dato) {
        for (int i = 0; i < (this.orden - 1); i++) {
            if (!nodoActual.esDatoVacio(i)) {
                if (nodoActual.getDato(i).compareTo(dato) == 0) {
                    return true;
                }
            }
        }
        return false;
    }

    /**
     * PreCondición: el nodoActual esta lleno de datos; Devuelve la posicion del
     * hijo por donde se debe bajar para insertar un dato en la posición
     * correcta del Arbol-MVias
     *
     * @param nodoActual
     * @param dato
     * @return
     */
    private int posicionHijoPorDondeBajar(NodoMVias<T> nodoActual, T dato) {
        for (int i = 0; i < (this.orden - 1); i++) {
            if (dato.compareTo(nodoActual.getDato(i)) < 0) {
                return i;
            }
        }
        return (this.orden - 1);
    }

    /**
     * 1.Precondición: tiene al menos un dato vacio(nulo) = espacio para un dato
     * valido mas. 2.Precondicion: el dato no existe en el nodoActual. Inserta
     * un dato en orden descedente en el NodoActual Orden sin exceso
     *
     * @param nodoActual
     * @param datoAInsertar
     */
    private void insertarDatoEnNodo(NodoMVias<T> nodoActual, T datoAInsertar) {
        for (int i = (this.orden - 2); i >= 0; i--) {
            if (!nodoActual.esDatoVacio(i)) {
                T datoActual = nodoActual.getDato(i);
                if (datoAInsertar.compareTo(datoActual) > 0) {
                    nodoActual.setDato(i + 1, datoAInsertar);
                    break;
                } else {
                    nodoActual.setDato(i + 1, datoActual);
                }
            }
            if (i == 0) {
                nodoActual.setDato(0, datoAInsertar);
            }
        }
    }

    @Override
    public boolean eliminar(T dato) {
        try {
            this.raiz = eliminar(raiz, dato);
            return true;
        } catch (Exception ex) {
            return false;
        }
    }

    private NodoMVias<T> eliminar(NodoMVias<T> nodoActual, T datoAEliminar) throws Exception {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            throw new Exception();
        }
        for (int index = 0; index < (orden - 1); index++) {
            if (nodoActual.esDatoVacio(index)) {
                throw new Exception();
            }
            T datoDePosicion = nodoActual.getDato(index);
            if (datoAEliminar.compareTo(datoDePosicion) == 0) { //si se encontro el dato
                if (nodoActual.esHoja()) {  //caso 1
                    eliminarDatoDelNodo(nodoActual, index);
                    /*elimina el dato de la posicion i
                     Pre : supone que el dato ya esta en el Nodo
                     aumenta un nodo vacio(nulo) */
                    if (nodoActual.estanTodosDatosVacios()) {
                        return NodoMVias.nodoVacio();
                    }
                    return nodoActual;
                }
                //Caso 2 o 3
                T datoSucesor = buscarDatoSucesor(nodoActual, index);
                /*este metodo verifica si hay hijos delante de la posicion , entonces retorna el sucesor inmediato
                 pero si no hay hijos delante de la posicion i, retorna datoVacio*/
                if (datoSucesor != NodoMVias.datoVacio()) {// caso 2
                    nodoActual = eliminar(nodoActual, datoSucesor);//si se modifica el valor de datoSucesor en el proceso afecta al orignal?
                    nodoActual.setDato(index, datoSucesor);
                    return nodoActual;
                }
                //caso 3
                T datoPredecesor = buscarDatoPredecesor(nodoActual, index);
                /* se supone que si o si hay predecesor*/
                nodoActual = eliminar(nodoActual, datoPredecesor);
                nodoActual.setDato(index, datoPredecesor);
                return nodoActual;
            } else { // si aun no se encontro el dato, hay que bajar en el arbol
                if (datoAEliminar.compareTo(datoDePosicion) < 0) {
                    NodoMVias<T> nuevoHijo = eliminar(nodoActual.getHijo(index), datoAEliminar);
                    nodoActual.setHijo(index, nuevoHijo);
                    return nodoActual;
                }
            }
        }
        NodoMVias<T> nuevoHijo = eliminar(nodoActual.getHijo(orden - 1), datoAEliminar);
        nodoActual.setHijo(orden - 1, nuevoHijo);
        return nodoActual;
    }

    /**
     * elimina el dato de la posicion indicada ¡válida!. Pre : supone que el
     * dato ya esta en el Nodo => aumenta un nodo vacio(nulo)
     *
     * @param nodoActual
     * @param posicion
     */
    protected void eliminarDatoDelNodo(NodoMVias<T> nodoActual, int posicion) {
        for (int i = posicion; i < orden - 2; i++) {
            nodoActual.setDato(i, nodoActual.getDato(i + 1));
        }
        nodoActual.setDato(orden - 2, (T) NodoMVias.datoVacio());
    }

    /**
     * este metodo verifica si hay hijos delante de la posicion , entonces
     * retorna el sucesor inmediato pero si no hay hijos delante de la posicion
     * i, retorna datoVacio
     */
    private T buscarDatoSucesor(NodoMVias<T> nodoActual, int posicion) {
        boolean hayHijo = false;
        for (int i = posicion + 1; i < (orden); i++) {
            if (!nodoActual.esHijoVacio(i)) {
                hayHijo = true;
            }
        }
        if (!hayHijo) {
            return (T) NodoMVias.datoVacio();
        }
        if (!nodoActual.esHijoVacio(posicion + 1)) {
            return nodoActual.getHijo(posicion + 1).getDato(0);
        }
        return nodoActual.getDato(posicion + 1);

    }

    /**
     * El nodoActual no es hoja , Supone que si o si hay predecesor
     *
     * @param nodoActual
     * @param i
     * @return
     */
    private T buscarDatoPredecesor(NodoMVias<T> nodoActual, int posicion) {
        if (!nodoActual.esHijoVacio(posicion)) {
            return nodoActual.getHijo(posicion).getDato(0);
        }
        return nodoActual.getDato(posicion - 1);
    }

    @Override
    public T buscar(T dato) {
        return buscar(this.raiz, dato);
    }

    private T buscar(NodoMVias<T> nodoActual, T datoABuscar) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return (T) NodoMVias.datoVacio(); // si es vacio retornamos dato vacio(nulo)
        }
        int cantidadDeDatos = nodoActual.cantidadDeDatosNoVacios();
        for (int i = 0; i < cantidadDeDatos; i++) {  // hasta (i < orden-1) si no hubiera datos nulos
            T datoActual = nodoActual.getDato(i);
            if (datoActual.compareTo(datoABuscar) == 0) {
                return datoActual;
            }
            if (datoABuscar.compareTo(datoActual) < 0) {
                return buscar(nodoActual.getHijo(i), datoABuscar);
            }
        } // si llega aqui el dato es Mayor que todos los anteriores
        return buscar(nodoActual.getHijo(cantidadDeDatos), datoABuscar);  // getHijo(orden-1) si no hubiera datos nulos
    }

    @Override
    public boolean contiene(T dato) {
        return buscar(dato) != NodoMVias.nodoVacio();
    }

    @Override
    public List<T> recorridoEnPreOrden() {
        List<T> recorrido = new LinkedList<>();
        recorridoEnPreOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPreOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }

        for (int i = 0; i < (orden - 1); i++) {
            if ((!nodoActual.esDatoVacio(i))) {
                recorrido.add(nodoActual.getDato(i));
            }
            recorridoEnPreOrden(nodoActual.getHijo(i), recorrido);
        }
        recorridoEnPreOrden(nodoActual.getHijo(orden - 1), recorrido);

    }

    @Override
    public List<T> recorridoEnInOrden() {
        List<T> recorrido = new LinkedList();
        recorridoEnInOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnInOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        int cantidad = nodoActual.cantidadDeDatosNoVacios();
        for (int i = 0; i < (cantidad); i++) {
            recorridoEnInOrden(nodoActual.getHijo(i), recorrido);
            if ((!nodoActual.esDatoVacio(i))) {
                recorrido.add(nodoActual.getDato(i));
            } else {
                break;
            }
        }

        recorridoEnInOrden(nodoActual.getHijo(cantidad), recorrido);
    }

    @Override
    public List<T> recorridoEnPostOrden() {
        List<T> recorrido = new LinkedList();
        recorridoEnPostOrden(this.raiz, recorrido);
        return recorrido;
    }

    private void recorridoEnPostOrden(NodoMVias<T> nodoActual, List<T> recorrido) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return;
        }
        recorridoEnPostOrden(nodoActual.getHijo(0), recorrido);

        for (int i = 0; i < (orden - 1); i++) {
            recorridoEnPostOrden(nodoActual.getHijo(i + 1), recorrido);
            if (!nodoActual.esDatoVacio(i)) {
                recorrido.add(nodoActual.getDato(i));
            }
        }
    }

    @Override
    public List<T> recorridoPorNiveles() {
        List<T> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }
        Queue< NodoMVias<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoMVias<T> nodoActual = colaDeNodos.poll();
            for (int i = 0; i < orden; i++) {
                if ((i < (orden - 1)) && !nodoActual.esDatoVacio(i)) { // verificar que el dato
                    recorrido.add(nodoActual.getDato(i));          // no sea Vacio(nulo)
                }
                if (!nodoActual.esHijoVacio(i)) {
                    colaDeNodos.offer(nodoActual.getHijo(i));
                }
            }
        }
        return recorrido;
    }

    @Override
    public int size() {
        return size(this.raiz);
    }

    private int size(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        for (int i = 0; i < orden; i++) {
            cantidad = cantidad + size(nodoActual.getHijo(i));
        }
        if (!NodoMVias.esNodoVacio(nodoActual)) {
            cantidad++;
        }
        return cantidad;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }

    private int altura(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int alturaMayor = 0;
        for (int i = 0; i < orden; i++) {
            int alturaHijo = altura(nodoActual.getHijo(i));
            if (alturaHijo > alturaMayor) {
                alturaMayor = alturaHijo;
            }
        }
        return alturaMayor + 1;
    }

    @Override
    public void vaciar() {
        this.raiz = NodoMVias.nodoVacio();
    }

    @Override
    public boolean esArbolVacio() {
        return NodoMVias.esNodoVacio(this.raiz);
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }

    private int nivel(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return -1;
        }
        int nivelMayor = -1;
        for (int i = 0; i < orden; i++) {
            int nivelHijo = nivel(nodoActual.getHijo(i));
            if (nivelHijo > nivelMayor) {
                nivelMayor = nivelHijo;
            }
        }
        return nivelMayor + 1;
    }

    //  *********   METODOS ADICIONALES   ****************///
    public int cantidadDeDatosVacios() {
        return cantidadDeDatosVacios(this.raiz);
    }

    private int cantidadDeDatosVacios(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidadDeVacios = 0;
        for (int i = 0; i < orden; i++) {
            if (i < orden - 1) {
                if (nodoActual.esDatoVacio(i)) {
                    cantidadDeVacios++;
                }
            }
            cantidadDeVacios = cantidadDeVacios + cantidadDeDatosVacios(nodoActual.getHijo(i));
        }
        return cantidadDeVacios;
    }

    public int cantidadDeHojas() {
        return cantidadDeHojas(this.raiz);
    }

    private int cantidadDeHojas(NodoMVias<T> nodoActual) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidadDeHojas = 0;
        if (nodoActual.esHoja()) {
            cantidadDeHojas++;
        }
        for (int i = 0; i < orden; i++) {
            cantidadDeHojas = cantidadDeHojas + cantidadDeHojas(nodoActual.getHijo(i));
        }
        return cantidadDeHojas;
    }

//    public int cantidadNodosIncompletosAPartirDelNivel(int nivel) {
//        return cantidadNodosIncompletosAPartirDelNivel(this.raiz, nivel);
//    }
//
//    private int cantidadNodosIncompletosAPartirDelNivel(NodoMVias<T> nodoActual, int nivel) {
//        if (NodoMVias.esNodoVacio(nodoActual)) {
//            return 0;
//        }
//        int cantidad = 0;
//        if (nivel <= 0) {
//            if (nodoActual.cantidadDeHijosNoVacios() < orden) {
//                cantidad++;
//            }
//            int n = nivel - 1;
//            for (int i = 0; i < orden; i++) {
//                int cantidadHijos = cantidadNodosIncompletosAPartirDelNivel(nodoActual.getHijo(i), (n));
//                cantidad += cantidadHijos;
//            }
//        }
//        return cantidad;
//    }
    public int cantidadNodosIncompletosHastaElNivel(int nivel) {
        return cantidadNodosIncompletosHastaElNivel(this.raiz, nivel);
    }

    private int cantidadNodosIncompletosHastaElNivel(NodoMVias<T> nodoActual, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        if (nivel >= 0) {

            if (nodoActual.cantidadDeHijosNoVacios() < orden) {
                cantidad++;
            }
            int n = nivel - 1;
            for (int i = 0; i < orden; i++) {
                int cantidadHijos = cantidadNodosIncompletosHastaElNivel(nodoActual.getHijo(i), (n));
                cantidad += cantidadHijos;
            }
        }
        return cantidad;
    }

    public boolean soloHayHojasAPartirDelNivel(int nivel) {
        if (!this.esArbolVacio()) {
            return soloHayHojasAPartirDelNivel(this.raiz, nivel);
        }
        return false;
    }

    private boolean soloHayHojasAPartirDelNivel(NodoMVias<T> nodoActual, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return true;
        }
        if (!nodoActual.esHoja() && nivel <= 0) {
            return false;
        }

        for (int i = 0; i < orden; i++) {
            if (!soloHayHojasAPartirDelNivel(nodoActual.getHijo(i), nivel - 1)) {
                return false;
            }
        }
        return true;
    }

    public int cantidadDeHojasAPartirDelNivel(int nivel) {
        return cantidadDeHojasAPartirDelNivel(raiz, nivel);
    }

    private int cantidadDeHojasAPartirDelNivel(NodoMVias<T> nodoActual, int nivel) {
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return 0;
        }
        int cantidad = 0;
        if (nodoActual.esHoja() && (nivel <= 0)) {
            cantidad++;
        }
        for (int i = 0; i < orden; i++) {            
            cantidad += cantidadDeHojasAPartirDelNivel(nodoActual.getHijo(i),nivel - 1);
        }

        return cantidad;
    }
}
