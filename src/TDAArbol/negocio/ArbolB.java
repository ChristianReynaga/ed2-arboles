package TDAArbol.negocio;

import java.util.Stack;

/**
 * Estructura arbol de busqueda multivias balanceado.
 * Contiene una "raiz" NodoMVias<T>, "orden", nroMinDatos, nroMinDeHijos, nroMaxDeDatos int.
 * T hereda de la clase Comparable.
 * @author HP
 * @author HP
 * @param <T>
 */
public class ArbolB< T extends Comparable<T>> extends ArbolBusquedaMVias<T> {

    private int nroMinDeDatos = 1;
    private int nroMinDeHijos = 2;
    private int nroMaxDeDatos = 2;

    public ArbolB() {
        // constructor por defecto -> inicia el arbol 2-3
    }

    /**
     * Constructor para arbol b. Parametro Orden.
     *
     * @param orden
     * @throws Exception
     */
    public ArbolB(int orden) throws Exception {
        super(orden);

        this.nroMaxDeDatos = orden - 1;
        this.nroMinDeDatos = this.nroMaxDeDatos / 2;
        this.nroMinDeHijos = nroMinDeDatos + 1;
    }

    @Override
    public boolean insertar(T datoAInsertar) {
        if (NodoMVias.esNodoVacio(raiz)) {
            super.raiz = new NodoMVias<>(orden + 1, datoAInsertar);
            return true;
        }
        Stack< NodoMVias<T>> pilaDePadres = new Stack<>();
        NodoMVias<T> nodoActual = raiz;

        while (!NodoMVias.esNodoVacio(nodoActual)) {
            if (nodoActual.esHoja()) {
                //this.existeDatoEnNodo => no considera el espacio exceso, No es necesario
                if (this.existeDatoEnNodo(nodoActual, datoAInsertar)) {
                    return false; // el dato ya existe
                }
                //1.PreCondicion: tiene espacio para un dato valido mas.( incluyendo el Exceso)
                //2.PreCondicion: el dato a insertar no esta en el nodoActual
                this.insertarDatoEnNodo(nodoActual, datoAInsertar, this.orden + 1);
                if (nodoActual.cantidadDeDatosNoVacios() <= this.nroMaxDeDatos) {
                    break; //Arbol-Ok
                } else {
                    this.dividir(nodoActual, pilaDePadres);
                    break;
                }
            } else { // cuando no es hoja
                if (this.existeDatoEnNodo(nodoActual, datoAInsertar)) {
                    return false;
                }
                NodoMVias<T> nodoAux = nodoActual;
                int nroDatosNodoActual = nodoActual.cantidadDeDatosNoVacios();

                for (int i = 0; i < nroDatosNodoActual && nodoAux == nodoActual; i++) {
                    T datoenTurno = nodoActual.getDato(i);
                    if (datoAInsertar.compareTo(datoenTurno) < 0) {
                        nodoAux = nodoActual.getHijo(i);
                    }
                }
                if (nodoAux == nodoActual) {
                    nodoAux = nodoActual.getHijo(nroDatosNodoActual);
                }
                //si cambio de nodo, el nodoActual es padre del nodoAux
                pilaDePadres.push(nodoActual);
                nodoActual = nodoAux;
            }//fin else
        }//fin while

        return true;
    }

    /**
     * 1.Precondición: Tiene al menos un dato vacio(nulo) = espacio para un dato
     * valido mas. 2.Precondicion: el dato no existe en el nodoActual. Inserta
     * un dato en orden ascendente en el NodoActual. Orden con exceso -> orden +
     * 1. Retorna la posicion donde insertó el dato.
     *
     * @param nodoActual
     * @param datoAInsertar
     */
    private int insertarDatoEnNodo(NodoMVias<T> nodoActual, T datoAInsertar, int ordenConExceso) {
        for (int i = (ordenConExceso - 2); i >= 0; i--) {
            if (!nodoActual.esDatoVacio(i)) {
                T datoActual = nodoActual.getDato(i);
                if (datoAInsertar.compareTo(datoActual) > 0) {
                    nodoActual.setDato(i + 1, datoAInsertar);
                    return i + 1;
                } else {
                    nodoActual.setDato(i + 1, datoActual);
                }
            }
            if (i == 0) {
                nodoActual.setDato(0, datoAInsertar);
            }
        }
        return 0;
    }

    /**
     * Si la pilaDePadres esta vacia -> la raiz sobrepaso el nroMaxDeDatos y no
     * tiene padre -> Hay que crear un nuevoNodoPadre donde sube el dato del
     * centro del nodoActual(pos = nroMinDeDatos). Si la pilaDePadres no esta
     * vacia: 1.Pre: cada nodo Padre tiene al menos un espacio para un agregar
     * un dato más. 2.Pre: el dato a subir del hijo no está en el padre. Sube el
     * dato del centro(pos=nroMinDeDatos)del nodoActual a un nodoPadre(nuevo si
     * no hay) Y divide el nodoActual en dos partes y los conecta al
     * nodoPadre,la 1era. parte "a la izq. del dato que subió" y la 2da. parte
     * "a la derecha del dato que subió". "Realiza el proceso para cada Nodo de
     * la pilaDePadres si también sobrepasan el nroMaxDeDatos".
     *
     * @param nodoActual
     * @param pilaDePadres
     */
    private void dividir(NodoMVias<T> nodoActual, Stack< NodoMVias<T>> pilaDePadres) {

        while (!pilaDePadres.empty()
                && nodoActual.cantidadDeDatosNoVacios() > this.nroMaxDeDatos) {
            NodoMVias<T> nodoHijoIzquierdoDelDatoASubir = new NodoMVias<>(this.orden + 1);
            NodoMVias<T> nodoHijoDerechoDelDatoASubir = new NodoMVias<>(this.orden + 1);
            T datoASubir = nodoActual.getDato(nroMinDeDatos);
            NodoMVias<T> nodoPadre = pilaDePadres.pop();
            int posicionDeSubida = insertarDatoEnNodo(nodoPadre, datoASubir, this.orden + 1);

            enviarHijosYDatosEntreNodos(nodoActual, nodoHijoDerechoDelDatoASubir, nroMinDeDatos + 1, orden - 1);
            enviarHijosYDatosEntreNodos(nodoActual, nodoHijoIzquierdoDelDatoASubir, 0, nroMinDeDatos - 1);

            //nodoActual = new NodoMVias<>(orden+1); // para eliminar el nodoActual, y no siga enlazado
            //recorrer los hijos una posicion a la derecha para hacer espacio para el nodoHijoDerecho
            for (int i = orden; i >= posicionDeSubida + 1; i--) {
                if (!nodoPadre.esHijoVacio(i)) {
                    nodoPadre.setHijo(i + 1, nodoPadre.getHijo(i));
                }
            }
            nodoPadre.setHijo(posicionDeSubida, nodoHijoIzquierdoDelDatoASubir);
            nodoPadre.setHijo(posicionDeSubida + 1, nodoHijoDerechoDelDatoASubir);
            nodoActual = nodoPadre;
        }

        if (nodoActual.cantidadDeDatosNoVacios() > this.nroMaxDeDatos) {
            NodoMVias<T> nodoHijoIzquierdoDelDatoASubir = new NodoMVias<>(this.orden + 1);
            NodoMVias<T> nodoHijoDerechoDelDatoASubir = new NodoMVias<>(this.orden + 1);
            T datoASubir = nodoActual.getDato(nroMinDeDatos);
            NodoMVias<T> nuevoNodoPadre = new NodoMVias<>(this.orden + 1, datoASubir);
            enviarHijosYDatosEntreNodos(nodoActual, nodoHijoDerechoDelDatoASubir, nroMinDeDatos + 1, orden - 1);
            enviarHijosYDatosEntreNodos(nodoActual, nodoHijoIzquierdoDelDatoASubir, 0, nroMinDeDatos - 1);
            nodoActual = new NodoMVias<>(orden + 1); // para eliminar el nodoActual, y no siga enlazado
            nuevoNodoPadre.setHijo(0, nodoHijoIzquierdoDelDatoASubir);
            nuevoNodoPadre.setHijo(1, nodoHijoDerechoDelDatoASubir);
            this.raiz = nuevoNodoPadre;
        }
    }

    /**
     * Envía datos de un NodoEmisor a un NodoReceptor,indicando las posiciones
     * inicial y final del rango de datos que se enviara del nodoEmisor, Además
     * envia los hijos que corresponden a las posiciones de los datos(+1) del
     * nodoEmisor.
     *
     * @param nodoEmisor
     * @param nodoReceptor
     * @param posInicial
     * @param posFinal
     */
    private void enviarHijosYDatosEntreNodos(NodoMVias<T> nodoEmisor, NodoMVias<T> nodoReceptor,
            int posInicial, int posFinal) {
        int j = 0;// j para poner datos en el inicio del receptor 
        for (int i = posInicial; i <= posFinal + 1; i++, j++) { //+1 para enviar hijos
            if (i <= posFinal) {
                nodoReceptor.setDato(j, nodoEmisor.getDato(i));
            }
            nodoReceptor.setHijo(j, nodoEmisor.getHijo(i));
        }
    }

    @Override
    public boolean eliminar(T datoAEliminar) {
        Stack< NodoMVias<T>> pilaDePadres = new Stack<>();
        // devuelve nodoVacio si el dato no esta en el arbol
        NodoMVias<T> nodoActual = buscarNodoConElDato(datoAEliminar, pilaDePadres);
        if (NodoMVias.esNodoVacio(nodoActual)) {
            return false;
        }
        //obtenemos el indice donde esta el dato en el nodoActual
        int indice = getPosicionDelDato(nodoActual, datoAEliminar);

        if (nodoActual.esHoja()) { //nodoActual es hoja 
            eliminarDatoDelNodo(nodoActual, indice);  //no es necesario considerar el exceso
            NodoMVias<T> nodoPadre = pilaDePadres.isEmpty() ? NodoMVias.nodoVacio() : pilaDePadres.peek();
            if (!NodoMVias.esNodoVacio(nodoPadre)
                    && nodoActual.cantidadDeDatosNoVacios() < this.nroMinDeDatos) {
                //realiza las operaciones de prestamo o fusión considerando 
                //los casos de una hoja. Además de prestarse o fusionar llaves, también 
                //van en el prestamo los hijos en caso de no ser hoja
                prestarOFusionar(nodoActual, pilaDePadres);
            } else if (NodoMVias.esNodoVacio(nodoPadre)
                    && nodoActual.cantidadDeDatosNoVacios() == 0) {
                //si la raiz no tiene llaves dejar el arbol vacio 
                this.vaciar();
            }
        } else {
            //nodo que no es hoja, hay que buscar quien reemplaza a la llave que se borra.
            //EL reemplazo estará en alguna hoja siguiente a la rama del hijo que esta
            // en la misma posición de la llave que se elimina.
            pilaDePadres.push(nodoActual);
            NodoMVias<T> hijoIzquierdoDeDatoAEliminar = nodoActual.getHijo(indice);
            NodoMVias<T> nodoDelPredecesor = buscarNodoDelPredecesor(pilaDePadres, hijoIzquierdoDeDatoAEliminar);
            //el nodoDelPredecesor será si o si una hoja
            T datoDeReemplazo = nodoDelPredecesor.getDato(nodoDelPredecesor.cantidadDeDatosNoVacios() - 1);// llavemayorDelNodoDelPredecesor
            //eliminar el dato predecesor del nodoDelPredecesor
            nodoDelPredecesor.setDato(nodoDelPredecesor.cantidadDeDatosNoVacios()-1,(T) NodoMVias.datoVacio());
            //this.eliminarLlaveMayorDelNodo(nodoDelPredecesor); //siempre es el dato Mayor en ese nodo
            nodoActual.setDato(indice, datoDeReemplazo);
            //si el nodo del que se sacó el reemplazo queda con pocos datos, será necesario prestarse o fusionar
            NodoMVias<T> nodoPadre = pilaDePadres.isEmpty() ? NodoMVias.nodoVacio() : pilaDePadres.peek();
            if (!NodoMVias.esNodoVacio(nodoPadre)
                    && nodoDelPredecesor.cantidadDeDatosNoVacios() < this.nroMinDeDatos) {
                prestarOFusionar(nodoDelPredecesor, pilaDePadres);

            }

        }
        return true;
    }
    
    /**
     * Retorna el nodo del arbol que contenga el datoABuscar si no se encuentra
     * se retorna nodoVacio. Carga en la pilaDePadres todos los nodos que se
     * recorren hasta llegar al nodo con el dato
     *
     * @param datoAEliminar
     * @param pilaDePadres
     * @return
     */ //getpsicionDeDato retorna -1 si no hay el dato
    private NodoMVias<T> buscarNodoConElDato(T datoABuscar, Stack<NodoMVias<T>> pilaDePadres) {
        return buscarNodoConElDato(this.raiz, datoABuscar, pilaDePadres);
    }

    private NodoMVias<T> buscarNodoConElDato(NodoMVias<T> nodoDeTurno, T datoABuscar, Stack< NodoMVias<T>> pilaDePadres) {
        if (NodoMVias.esNodoVacio(nodoDeTurno)) {
            return NodoMVias.nodoVacio();
        }
        int cantidadDeDatos = nodoDeTurno.cantidadDeDatosNoVacios();
        for (int i = 0; i < cantidadDeDatos; i++) {
            T datoDeTurno = nodoDeTurno.getDato(i);
            if (datoDeTurno.compareTo(datoABuscar) == 0) {
                return nodoDeTurno;
            }
            if (datoABuscar.compareTo(datoDeTurno) < 0) {
                pilaDePadres.push(nodoDeTurno);
                return buscarNodoConElDato(nodoDeTurno.getHijo(i), datoABuscar, pilaDePadres);
            }
        }
        pilaDePadres.push(nodoDeTurno);
        return buscarNodoConElDato(nodoDeTurno.getHijo(cantidadDeDatos), datoABuscar, pilaDePadres);
    }

    /**
     * retorna la posición del dato en el nodoDeTurno si no lo encuentra retorna
     * -1 No se considera el exceso.
     *
     * @param nodoDeTurno
     * @param datoABuscar
     * @return
     */
    private int getPosicionDelDato(NodoMVias<T> nodoDeTurno, T datoABuscar) {
        int cantidadDeDatosNoNulos = nodoDeTurno.cantidadDeDatosNoVacios();
        for (int i = 0; i < orden - 1; i++) {
            if (nodoDeTurno.esDatoVacio(i)) {
                break;
            }
            if (nodoDeTurno.getDato(i).compareTo(datoABuscar) == 0) {
                return i;
            }
        }
        return -1;
    }


    /**
     * Realiza prestamo o fusión(según sea el caso) si la cantidad de
     * datos(validos) del nodoActual es menor al nroMinDeDatos. " Realiza el
     * proceso para cada nodo de la pilaDePadres si también tienen menor
     * cant.Datos. que el nroMinDeDatos. Pre: nodoActual = raiz, no entra
     * directamente en este metodo.
     *
     * @param nodoActual
     * @param pilaDePadres
     */
    private void prestarOFusionar(NodoMVias<T> nodoActual, Stack<NodoMVias<T>> pilaDePadres) {

        NodoMVias nodoPadre = (pilaDePadres.isEmpty()) ? NodoMVias.nodoVacio() : pilaDePadres.pop();
        if (NodoMVias.esNodoVacio(nodoPadre)) {
            if (nodoActual.cantidadDeDatosNoVacios() == 0) {
                this.raiz = nodoActual.getHijo(0);
                return;
            }
        }
        if (nodoActual.cantidadDeDatosNoVacios() < nroMinDeDatos) {
            //el nodoActual si o si esta en el nodoPadre
            int posicionDelHijo = getPosicionDeHijoEnNodo(nodoPadre, nodoActual);
            if (!prestarseDelNodoHVD(nodoPadre, posicionDelHijo)) {
                if(!prestarseDelNodoHVI(nodoPadre, posicionDelHijo)){
                   fusionar(nodoPadre, posicionDelHijo); 
                }   
            } 
            prestarOFusionar(nodoPadre, pilaDePadres);
        }

    }

    /**
     * retorna la posición del nodoHijo en el nodoPadre, si no existe en el
     * nodoPadre retorna -1.
     *
     * @param nodoPadre
     * @param nodoHijo
     * @return
     */
    private int getPosicionDeHijoEnNodo(NodoMVias<T> nodoPadre, NodoMVias<T> nodoHijo) {
        for (int i = 0; i < orden; i++) {
            if (nodoHijo.equals(nodoPadre.getHijo(i))) {
                return i;
            }
        }
        return -1;
    }   
    /**
     * De un NodoPadre, el hijo De la Pos. indiceDeHijo se presta un dato de su
     * hermanoVecinoDerecho. si no es posible retorna false.
     *
     * @param nodoPadre
     * @param indiceDeHijo
     * @return
     */
    public boolean prestarseDelNodoHVD(NodoMVias<T> nodoPadre, int indiceDeHijo) {
        if (indiceDeHijo < nodoPadre.cantidadDeHijosNoVacios() - 1) {
            NodoMVias<T> hermanoVecinoDerecho = nodoPadre.getHijo(indiceDeHijo + 1);
            int cantidadDeDatos = hermanoVecinoDerecho.cantidadDeDatosNoVacios();
            if (cantidadDeDatos - 1 >= nroMinDeDatos) {
                NodoMVias<T> hijoQueSePresta = nodoPadre.getHijo(indiceDeHijo);
                T datoQuePrestaElPadre = nodoPadre.getDato(indiceDeHijo);
                hijoQueSePresta.setDato(nroMinDeDatos - 1, datoQuePrestaElPadre);

                T datoQuePrestaElHVD = hermanoVecinoDerecho.getDato(0);
                nodoPadre.setDato(indiceDeHijo, datoQuePrestaElHVD);
                this.eliminarDatoDelNodo(hermanoVecinoDerecho, 0);
                return true;
            }
        }
        return false;
    }

    /**
     * De un NodoPadre, el hijo De la Pos. indiceDeHijo se presta un dato de su
     * hermanoVecinoIzquierdo. si no es posible retorna false.
     *
     * @param nodoPadre
     * @param indiceDeHijo
     * @return
     */
    private boolean prestarseDelNodoHVI(NodoMVias<T> nodoPadre, int indiceDeHijo) {
        if (indiceDeHijo > 0) {
            NodoMVias<T> hermanoVecinoIzquierdo = nodoPadre.getHijo(indiceDeHijo - 1);
            int cantidadDeDatosHVI = hermanoVecinoIzquierdo.cantidadDeDatosNoVacios();
            if (cantidadDeDatosHVI - 1 >= nroMinDeDatos) {
                NodoMVias<T> hijoQueSePresta = nodoPadre.getHijo(indiceDeHijo);
                T datoQuePrestaElPadre = nodoPadre.getDato(indiceDeHijo - 1);
                this.insertarDatoEnNodo(hijoQueSePresta, datoQuePrestaElPadre, orden);

                T datoQuePrestaElHVI = hermanoVecinoIzquierdo.getDato(cantidadDeDatosHVI - 1);
                nodoPadre.setDato(indiceDeHijo - 1, datoQuePrestaElHVI);
                this.eliminarDatoDelNodo(hermanoVecinoIzquierdo, cantidadDeDatosHVI - 1);
                return true;
            }
        }
        return false;
    }

    /**
     * fusiona el padre con el hijo anterior o siguiente al hijo del indice
     * indicado.
     *
     * @param nodoPadre
     * @param indiceDeHijo
     */
    private void fusionar(NodoMVias<T> nodoPadre, int indiceDeHijo) {
            int cantidadDeDatos = nodoPadre.cantidadDeDatosNoVacios();            
            if ((indiceDeHijo == 0) || cantidadDeDatos > indiceDeHijo) {
                fusion(nodoPadre, indiceDeHijo, indiceDeHijo + 1); 
            }else{
                fusion(nodoPadre, indiceDeHijo-1,indiceDeHijo);         
        }
    }
    
    private void  fusion(NodoMVias<T> nodoPadre, int posicionHijoDestino, int posicionHijoSiguiente){
        NodoMVias<T> nodoDestino = nodoPadre.getHijo(posicionHijoDestino);
        NodoMVias<T> nodoSiguiente = nodoPadre.getHijo(posicionHijoSiguiente);
        T datoPadre = nodoPadre.getDato(posicionHijoDestino); // luego hay que eliminar este dato
        int base = this.insertarDatoEnNodo(nodoDestino, datoPadre, orden+1) + 1;
        
        int cantDeDatosNodoSiguiente = nodoSiguiente.cantidadDeDatosNoVacios();        
        for(int i = 0; i < cantDeDatosNodoSiguiente; i++){
            nodoDestino.setDato(base + i, nodoSiguiente.getDato(i));            
        }       
        if(!nodoSiguiente.esHoja()){
            int cantDeHijosNodoSiguiente = nodoSiguiente.cantidadDeHijosNoVacios();
            for(int i = 0 ; i < cantDeHijosNodoSiguiente; i++){
                nodoDestino.setHijo(base + i, nodoSiguiente.getHijo(i));
            }
                
        }
        contraer(nodoPadre,posicionHijoSiguiente);
        //si era hoja elimina el sguiete  contraer(padre, posHSig);posicionHijoDestino+1
    }
   /**
    * remueve el hijo de la posicion index, y recorre los hijos siguientes si existen.
    * remueve el datode la posicion index -1 del padre, y recorre los datos sig. si existen
    * @param nodoPadre
    * @param indexHijo 
    */
    private void contraer(NodoMVias<T> nodoPadre, int indexHijo) {
        int cantidadDeDatos = nodoPadre.cantidadDeDatosNoVacios();    
        for(int i = indexHijo; i <= cantidadDeDatos; i++){
            nodoPadre.setDato(i -1, nodoPadre.getDato(i));
            nodoPadre.setHijo(i, nodoPadre.getHijo(i+1));
        }        
     
    }
    /**
     * retorna el nodo que tiene el dato Mayor, este nodo siempre es hoja.
     * @param pilaDePadres
     * @param nodoActual
     * @return 
     */
    private NodoMVias<T> buscarNodoDelPredecesor(Stack<NodoMVias<T>> pilaDePadres,NodoMVias<T> nodoActual) {
        while (!nodoActual.esHoja()) {
            pilaDePadres.push(nodoActual);
            nodoActual = nodoActual.getHijo(nodoActual.cantidadDeDatosNoVacios());
        }
        return nodoActual;
    }

    
    ///******************    METODOS ADICIONALES     **************************////
    public int cantidadDeNodosConMinDeHijos(){
        return cantidadDeNodosConMinDeHijos(super.raiz);
    }
    public int cantidadDeNodosConMinDeHijos(NodoMVias<T> nodoActual){
        if(NodoMVias.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidadDeLosHijos = 0;
        int cantidadDeNodoActual = 0;
        for(int i = 0; i < super.orden; i++){
            if(!nodoActual.esHijoVacio(i)){
                cantidadDeNodoActual++;
                cantidadDeLosHijos += this.cantidadDeNodosConMinDeHijos(nodoActual.getHijo(i));
            }
        }
        return (cantidadDeNodoActual <= this.nroMinDeHijos)? 
                  cantidadDeLosHijos + 1 : cantidadDeLosHijos;
    }
    
    public int nivelDeDatoMayor(){
        return nivelDeDatoMayor(this.raiz);
    }
    
    private int nivelDeDatoMayor(NodoMVias<T> nodoActual){
        if(NodoMVias.esNodoVacio(nodoActual)){
            return -1;
        }
        int cantDatos = nodoActual.cantidadDeDatosNoVacios();
        int nivelHijo = nivelDeDatoMayor(nodoActual.getHijo(cantDatos-1));
        return nivelHijo+1;
    }
    
    public static void main(String[] args) throws Exception {
        ArbolB<Integer> a = new ArbolB<>(3);
//        NodoMVias<Integer> n = new NodoMVias<>(4);
//        n.setDato(0, 4);
//        n.setDato(1, 7);
//        n.setDato(2, 5);
//        NodoMVias<Integer> h = new NodoMVias<>(4);
//        n.setHijo(3, h);
////        n.setDato(3, 9);
//        System.out.println(a.getPosicionDelDato(n, 5));
//        System.out.println(a.getPosicionDeHijoEnNodo(n, h));
//        a.insertar(200);
//        a.insertar(100);
//        a.insertar(10);
//        a.insertar(350);
//        a.insertar(75);
//        a.insertar(250);
//
//        a.insertar(15);
//        a.insertar(450);
//        a.insertar(300);
//        a.insertar(1);
//        a.insertar(76);
//        a.insertar(77);
//        a.insertar(78);
//        a.insertar(79);
//        a.insertar(80);
//        a.insertar(81);
//        a.insertar(82);

        for (int i = 1; i <= 20; i++) {
            a.insertar(i);
        }
        for (int i = 40; i >= 21; i--) {
            a.insertar(i);
        }
        System.out.println(a.recorridoPorNiveles());
        System.out.println(a.nivelDeDatoMayor());
//        System.out.println(a.recorridoEnInOrden());
//        a.eliminar(28);
//        a.eliminar(27);
//        a.eliminar(29);
//        a.eliminar(26);
//        a.eliminar(8);
//        a.eliminar(4);
//        System.out.println(a.recorridoPorNiveles());
//        a.eliminar(3);
//        System.out.println(a.recorridoPorNiveles());
//        System.out.println(a.nivelDeDatoMayor());
    }

}
