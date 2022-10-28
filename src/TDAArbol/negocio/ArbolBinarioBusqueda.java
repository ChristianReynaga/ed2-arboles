package TDAArbol.negocio;

import java.util.LinkedList;
import java.util.List;
import java.util.Queue;
import java.util.Stack;

/**
 * Estructura arbol de busqueda binario,
 * Contiene una "raiz" NodoBinario<T>.
 * T hereda de la clase Comparable.
 * @author HP
 * @param <T>
 */
public class ArbolBinarioBusqueda< T extends Comparable<T> > implements IArbolBusqueda<T> {

    protected NodoBinario<T> raiz;

    @Override
    public boolean insertar(T datoAInsertar) {
        if (this.esArbolVacio()) {
            this.raiz = new NodoBinario<>(datoAInsertar);
            return true;
        }
        NodoBinario<T> nodoAnterior = NodoBinario.nodoVacio();
        NodoBinario<T> nodoActual = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoActual)) {
            if (datoAInsertar.compareTo(nodoActual.getDato()) == 0) {
                return false;
            }
            nodoAnterior = nodoActual;
            if (datoAInsertar.compareTo(nodoActual.getDato()) > 0) {
                nodoActual = nodoActual.getHijoDerecho();
            } else {
                nodoActual = nodoActual.getHijoIzquierdo();
            }
        }

        NodoBinario<T> nuevoNodo = new NodoBinario<>(datoAInsertar);
        if (datoAInsertar.compareTo(nodoAnterior.getDato()) > 0) {
            nodoAnterior.setHijoDerecho(nuevoNodo);
        } else {
            nodoAnterior.setHijoIzquierdo(nuevoNodo);
        }
        return true;
    }
    
    @Override
    public boolean eliminar(T datoAEliminar) {
        try{
            this.raiz = eliminar(this.raiz,datoAEliminar);
            return true;
        }catch(Exception ex){
            return false;
        }
    }
    
    private NodoBinario<T> eliminar(NodoBinario<T> nodoActual, T datoAEliminar)
        throws Exception{
            if(NodoBinario.esNodoVacio(nodoActual)){
                throw new Exception();
            }
            T datoDelNodoActual = nodoActual.getDato();
            if(datoAEliminar.compareTo(datoDelNodoActual) > 0){
                NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoAEliminar);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                return nodoActual;
            }
            if(datoAEliminar.compareTo(datoDelNodoActual) < 0){
                NodoBinario<T> nuevoHijoIzquierdo = eliminar(nodoActual.getHijoIzquierdo(),datoAEliminar);
                nodoActual.setHijoIzquierdo(nuevoHijoIzquierdo);
                return nodoActual;
            }
            //Caso 1
            if(nodoActual.esHoja()){
                return NodoBinario.nodoVacio();
            }
            //Caso 2
            if(nodoActual.esVacioHijoIzquierdo() &&
                    !nodoActual.esVacioHijoDerecho()){
                //return nodoActual.getHijoDerecho();
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoDerecho();
                nodoActual.setHijoDerecho(NodoBinario.nodoVacio());
                return (nuevoNodoActual);
            }
            if(!nodoActual.esVacioHijoIzquierdo() &&
                    nodoActual.esVacioHijoDerecho()){
//                return nodoActual.getHijoIzquierdo();
                NodoBinario<T> nuevoNodoActual = nodoActual.getHijoIzquierdo();
                nodoActual.setHijoIzquierdo(NodoBinario.nodoVacio());
                return (nuevoNodoActual);
            }
            //Caso 3
            T datoSucesor = buscarDatosSucesor(nodoActual.getHijoDerecho());
            NodoBinario<T> nuevoHijoDerecho = eliminar(nodoActual.getHijoDerecho(),datoSucesor);
                nodoActual.setHijoDerecho(nuevoHijoDerecho);
                nodoActual.setDato(datoSucesor);
                return nodoActual;       
    }
    
    protected T buscarDatosSucesor (NodoBinario<T> nodoActual){
        NodoBinario<T> nodoAnterior = nodoActual;
        while(!NodoBinario.esNodoVacio(nodoActual)){
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior.getDato();
    }
    
    ////////////////////////
    @Override
    public T buscar(T datoBuscado) {
        NodoBinario<T> nodoAuxiliar = this.raiz;

        while (!NodoBinario.esNodoVacio(nodoAuxiliar)) {
            if (datoBuscado.compareTo(nodoAuxiliar.getDato()) == 0) {
                return nodoAuxiliar.getDato();
            }
            if (datoBuscado.compareTo(nodoAuxiliar.getDato()) > 0) {
                nodoAuxiliar = nodoAuxiliar.getHijoDerecho();
            } else {
                nodoAuxiliar = nodoAuxiliar.getHijoIzquierdo();
            }
        }

        return null;
    }

    @Override
    public boolean contiene(T dato) {
        return this.buscar(dato) != null;
    }
//**********************************************////

    @Override
    public List<T> recorridoEnInOrden() {
        List<T> recorrido = new LinkedList<>();
        Stack< NodoBinario<T>> pilaDeNodos = new Stack<>();
        NodoBinario<T> NodoActual = raiz;

        meterEnPilaParaInOrden(NodoActual, pilaDeNodos);

        while (!pilaDeNodos.empty()) {
            NodoActual = pilaDeNodos.pop();
            recorrido.add(NodoActual.getDato());
            if (!NodoActual.esVacioHijoDerecho()) {
                NodoActual = NodoActual.getHijoDerecho();
                meterEnPilaParaInOrden(NodoActual, pilaDeNodos);
            }
        }

        return recorrido;
    }

    private void meterEnPilaParaInOrden(NodoBinario<T> NodoActual, Stack<NodoBinario<T>> pilaDeNodos) {
        while (!NodoBinario.esNodoVacio(NodoActual)) {
            pilaDeNodos.push(NodoActual);
            NodoActual = NodoActual.getHijoIzquierdo();
        }
    }
 
//*********************************************//
    public List<T> recorridoEnInOrdenRecursivo(){
        List<T> recorrido = new LinkedList();
        recorridoEnInOrdenRecursivo(this.raiz,recorrido);
        return recorrido;
    }
    private void recorridoEnInOrdenRecursivo(NodoBinario<T> nodoActual,List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorridoEnInOrdenRecursivo(nodoActual.getHijoIzquierdo(),recorrido);
        recorrido.add(nodoActual.getDato()); 
        recorridoEnInOrdenRecursivo(nodoActual.getHijoDerecho(),recorrido);
    }
    
//******************************************///

    @Override
    public List<T> recorridoEnPreOrden() {
        List<T> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Stack< NodoBinario<T>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(raiz);

        while (!pilaDeNodos.empty()) {
            NodoBinario<T> nodoActual = pilaDeNodos.pop();
            recorrido.add(nodoActual.getDato());

            if (!nodoActual.esVacioHijoDerecho()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }

        }

        return recorrido;
    }
   
//***************************////////
    
    public List<T> recorridoEnPreOrdenRecursivo(){
        List<T> recorrido = new LinkedList();
        recorridoEnPreOrdenRecursivo(this.raiz,recorrido);
        return recorrido;
    }
    
    private void recorridoEnPreOrdenRecursivo(NodoBinario<T> nodoActual, List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorrido.add(nodoActual.getDato());
        recorridoEnPreOrdenRecursivo(nodoActual.getHijoIzquierdo(),recorrido);
        recorridoEnPreOrdenRecursivo(nodoActual.getHijoDerecho(),recorrido);
    }
    
    /**+++++++++++++++++++++++++++++++++++++  
     * Recorrido en PostOrden
     * @return 
     **/
    @Override
    public List<T> recorridoEnPostOrden() {
       List<T> recorrido = new LinkedList();
       Stack< NodoBinario<T> > pilaDeNodos = new Stack();
       NodoBinario<T> nodoActual = this.raiz;
       
       while(!NodoBinario.esNodoVacio(nodoActual)){
           nodoActual =  meterEnPilaParaPostOrden(nodoActual,pilaDeNodos);
           nodoActual = nodoActual.getHijoDerecho();
       }
       
       while(!pilaDeNodos.empty()){
           if(pilaDeNodos.peek().getHijoDerecho() != nodoActual){
              nodoActual = pilaDeNodos.peek().getHijoDerecho();
              nodoActual =  meterEnPilaParaPostOrden(nodoActual,pilaDeNodos);
              if(!NodoBinario.esNodoVacio(nodoActual)){
                nodoActual = nodoActual.getHijoDerecho();
              }
           }else{
               nodoActual = pilaDeNodos.pop();
               recorrido.add(nodoActual.getDato());
           }
       }
        return recorrido;
    }
    /** recorre a la izquierda el arbol binario y añade a la pila cada Nodo visitado
     * se detiene antes de llegar a un NodoVacio 
     * retorna el ultimo nodo visitado
     **/
    private NodoBinario<T> meterEnPilaParaPostOrden(NodoBinario<T> nodoActual, Stack< NodoBinario<T> > pilaDeNodos ){
        NodoBinario<T> nodoAnterior = NodoBinario.nodoVacio();
        
        while(!NodoBinario.esNodoVacio(nodoActual) ){
            pilaDeNodos.push(nodoActual);
            nodoAnterior = nodoActual;
            nodoActual = nodoActual.getHijoIzquierdo();
        }
        return nodoAnterior;
    }
   
    ////////////////////////*************************************////////////////////////
    public List<T> recorridoEnPostOrdenRecursivo(){
        List<T> recorrido = new LinkedList();
        recorridoEnPostOrdenRecursivo(this.raiz,recorrido);
        return recorrido;
    }
    private void recorridoEnPostOrdenRecursivo(NodoBinario<T> nodoActual,List<T> recorrido){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return;
        }
        recorridoEnPostOrdenRecursivo(nodoActual.getHijoIzquierdo(),recorrido);
        recorridoEnPostOrdenRecursivo(nodoActual.getHijoDerecho(),recorrido);
        recorrido.add(nodoActual.getDato());
    }

    //***********************************************/////////
    @Override
    public List<T> recorridoPorNiveles() {
        List<T> recorrido = new LinkedList<>();
        if (this.esArbolVacio()) {
            return recorrido;
        }

        Queue< NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = colaDeNodos.poll();
            recorrido.add(nodoActual.getDato());

            if (!nodoActual.esVacioHijoIzquierdo()) {
                colaDeNodos.offer(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                colaDeNodos.offer(nodoActual.getHijoDerecho());
            }
        }
        return recorrido;
    }

    @Override
    public int size() { //sizeRecursivo
        return size(this.raiz);
    }
    
    private int size(NodoBinario<T> nodoActual){
       
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidadPorIzquierda = size(nodoActual.getHijoIzquierdo());
        int cantidadPorDerecha = size(nodoActual.getHijoDerecho());
        return cantidadPorIzquierda + cantidadPorDerecha + 1;
    }

    @Override
    public int altura() {
        return altura(this.raiz);
    }
    protected int altura(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int alturaPorIzquierda = altura(nodoActual.getHijoIzquierdo());
        int alturaPorDerecha = altura(nodoActual.getHijoDerecho());
        if(alturaPorIzquierda > alturaPorDerecha){
            return alturaPorIzquierda + 1;
        }
        return alturaPorDerecha + 1;
    }
    

    @Override
    public final void vaciar() {
        this.raiz = NodoBinario.nodoVacio();
    }

    @Override
    public final boolean esArbolVacio() {
        return NodoBinario.esNodoVacio(raiz);
    }

    @Override
    public int nivel() {
        return nivel(this.raiz);
    }
    
    private int nivel(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return -1;
        }
        int nivelMayorIzquierdo = nivel(nodoActual.getHijoIzquierdo());
        int nivelMayorDerecho = nivel(nodoActual.getHijoDerecho());
        if(nivelMayorIzquierdo > nivelMayorDerecho){
            return nivelMayorIzquierdo + 1;
        }
        return nivelMayorDerecho + 1;
    }

    public int cantidadHijosIzquierdos(){
        return cantidadHijosIzquierdos(raiz);
    }
    
    private int cantidadHijosIzquierdos(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        
        int cantidadXIzquierda = cantidadHijosIzquierdos(nodoActual.getHijoIzquierdo());
        int cantidadXDerecha = cantidadHijosIzquierdos(nodoActual.getHijoDerecho());
        
        if(!nodoActual.esVacioHijoIzquierdo()){
            return cantidadXIzquierda + cantidadXDerecha +1 ;
        }
        
        return  cantidadXIzquierda + cantidadXDerecha;
    }
    
   /**
    * funcion que devuleve verdadero si todos los nodos son hojas 
    * o si todos tienen ambos hijos
    * @return 
    */ 
    public boolean haySoloHojasCompletosRecursivo(){
        return haySoloHojasCompletosRecursivo(this.raiz);
    }
    
    private boolean haySoloHojasCompletosRecursivo(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return false;
        }
        if(nodoActual.esHoja()){
            return true;
        }
        
        if(haySoloHojasCompletosRecursivo(nodoActual.getHijoIzquierdo())){
            return haySoloHojasCompletosRecursivo(nodoActual.getHijoDerecho());
        }
        
        return false;
    }
    
     public boolean haySoloHojasCompletos() {
        
        if (this.esArbolVacio()) {
            return false;
        }

        Queue< NodoBinario<T>> colaDeNodos = new LinkedList<>();
        colaDeNodos.offer(raiz);

        while (!colaDeNodos.isEmpty()) {
            NodoBinario<T> nodoActual = colaDeNodos.poll();
            
                 if (!nodoActual.esVacioHijoIzquierdo()) {
                    colaDeNodos.offer(nodoActual.getHijoIzquierdo());
                }
                if (!nodoActual.esVacioHijoDerecho()) {
                    colaDeNodos.offer(nodoActual.getHijoDerecho());
                }
                
                if(!nodoActual.esHoja()){
                    if(nodoActual.esVacioHijoDerecho() || nodoActual.esVacioHijoIzquierdo()){
                        return false;
                    }
                }    
        }
        return true;
    }
     
    /**
     * metodo que retorne cuantos nodos solo tienen hijos derechos, 
     * por debajo del nivel "n"3
     * @param nivel
     * @return 
     */
    
    public int cantidadNodosConSoloHijoDerechoXNivel(int nivel){
        if (this.esArbolVacio()) {
            return 0;
        }
        
        Queue< NodoBinario<T> > colaDeNodos = new LinkedList<>();
        Stack<NodoBinario<T>> pilaDeNodos = new Stack<>();
        pilaDeNodos.push(this.raiz);
        
        while(nivel >= 0 ){
           while(!pilaDeNodos.empty()){
               colaDeNodos.offer(pilaDeNodos.pop());
           }
            MeterEnPilaHijosDeNodosDeCola(colaDeNodos, pilaDeNodos); 
            nivel--;
        }
        
        int cantidadHijosDerechos = 0;
        NodoBinario<T> nodoActual = NodoBinario.nodoVacio();
        while(!pilaDeNodos.empty()){
            nodoActual = pilaDeNodos.pop();
            cantidadHijosDerechos = cantidadHijosDerechos + cantidadNodosConSoloHijoDerecho(nodoActual);
        }
        return cantidadHijosDerechos;
        
    }

    private void MeterEnPilaHijosDeNodosDeCola(Queue<NodoBinario<T>> colaDeNodos, Stack<NodoBinario<T>> pilaDeNodos) {
        NodoBinario<T> nodoActual;
        while(!colaDeNodos.isEmpty()){
            nodoActual = colaDeNodos.poll();
            if (!nodoActual.esVacioHijoIzquierdo()) {
                pilaDeNodos.push(nodoActual.getHijoIzquierdo());
            }
            if (!nodoActual.esVacioHijoDerecho()) {
                pilaDeNodos.push(nodoActual.getHijoDerecho());
            }
        }
    }
   
    private int cantidadNodosConSoloHijoDerecho(NodoBinario<T> nodoActual){
        if(NodoBinario.esNodoVacio(nodoActual)){
            return 0;
        }
        int cantidadXIzquierda = cantidadNodosConSoloHijoDerecho(nodoActual.getHijoIzquierdo());
        int cantidadXDerecha = cantidadNodosConSoloHijoDerecho(nodoActual.getHijoDerecho());
        if(!nodoActual.esVacioHijoDerecho() && (nodoActual.esVacioHijoIzquierdo())){
            return cantidadXIzquierda + cantidadXDerecha + 1;
        }
        return cantidadXIzquierda + cantidadXDerecha;
    }
    
    /**
     * metodo que devuelva la cantidad de nodos que solo tienen hijo derecho
     * debajo del Nivel "n"
     * Recursivo
     * @param nivel se empezara debajo de el nivel
     * @return 
     */
    
   public int cantidadConSoloHijoDerechoBajoNivel(int nivel){
       return countWithAloneSonRightBelowXLevel(raiz, nivel);
   } 
   
   private int countWithAloneSonRightBelowXLevel(NodoBinario<T> nodoActual, int nivel){
       if(NodoBinario.esNodoVacio(nodoActual)){
           return 0;
       }
       int cantidadXIzquierda = countWithAloneSonRightBelowXLevel(nodoActual.getHijoIzquierdo(),Math.decrementExact(nivel));
       int cantidadXDerecha = countWithAloneSonRightBelowXLevel(nodoActual.getHijoDerecho(),Math.decrementExact(nivel));
       if(nivel < 0){
          if((nodoActual.esVacioHijoIzquierdo()) && (!nodoActual.esVacioHijoDerecho())){
             return cantidadXIzquierda + cantidadXDerecha + 1; 
          } 
       }
       return cantidadXIzquierda + cantidadXDerecha;
   }
  
   
   /***
    * metodo de reconstruccion de arboles
    * InOrden -- preOrden:
    * De una lista con el recorrido en inOrden y otra con el recorrido en preOrden de un Arbol
     * @param listaInOrden
     * @param listaPreOrden
    */
   public void reconstruccionInOrden_PreOrden( List<T> listaInOrden, List<T> listaPreOrden){
       raiz =  reconstruccionInOrden_PreOrdenRecursivo(listaInOrden, listaPreOrden);   
   }
   
   private NodoBinario<T> reconstruccionInOrden_PreOrdenRecursivo( List<T> listaInOrden, List<T> listaPreOrden){
       if(listaInOrden.isEmpty() && listaPreOrden.isEmpty()){
           return NodoBinario.nodoVacio();
       }
       NodoBinario<T> nodoActual = new NodoBinario(listaPreOrden.get(0));
       int posicionRaiz = listaInOrden.indexOf(nodoActual.getDato());
       //por izquierda
       List<T> inOrdenParaIzquierda = subLista(listaInOrden,0, posicionRaiz-1);
       List<T> preOrdenParaIzquierda = subLista(listaPreOrden,1, inOrdenParaIzquierda.size());
       nodoActual.setHijoIzquierdo(reconstruccionInOrden_PreOrdenRecursivo(inOrdenParaIzquierda, preOrdenParaIzquierda) );
       //por derecha
       List<T> inOrdenParaDerecha = subLista(listaInOrden,posicionRaiz+1, listaInOrden.size()-1);
       List<T> preOrdenParaDerecha = subLista(listaPreOrden,preOrdenParaIzquierda.size()+1, listaPreOrden.size()-1);
       nodoActual.setHijoDerecho(reconstruccionInOrden_PreOrdenRecursivo(inOrdenParaDerecha , preOrdenParaDerecha) );
       
       return nodoActual;
   }

   private List<T> subLista(List<T> lista,int fromIndex, int toIndex){
       List<T> SubLista = new LinkedList<>();
        while(fromIndex <= toIndex){
            SubLista.add(lista.get(fromIndex));
            fromIndex++;
        }
       return SubLista;
   }
   
    /***
    * metodo de reconstruccion de arboles
    * InOrden -- postOrden:
    * De una lista con el recorrido en inOrden y otra con el recorrido en postOrden de un Arbol
     * @param listaInOrden
     * @param listaPostOrden
    */
   public void reconstruccionInOrden_PostOrden(List<T> listaInOrden, List<T> listaPostOrden){
       raiz = reconstruccionInOrden_PostOrdenRecursivo(listaInOrden,listaPostOrden);
   }
   
   public NodoBinario<T> reconstruccionInOrden_PostOrdenRecursivo(List<T> listaInOrden, List<T> listaPostOrden){
       if(listaInOrden.isEmpty() || listaPostOrden.isEmpty()){
           return NodoBinario.nodoVacio();
       }
       NodoBinario nodoActual = new NodoBinario<>(listaPostOrden.get(listaPostOrden.size()-1));
       int posicionRaiz = listaInOrden.indexOf(nodoActual.getDato());
       
       List<T> inOrdenParaIzquierda = subLista(listaInOrden, 0, posicionRaiz-1);
       List<T> postOrdenParaIzquierda = subLista(listaPostOrden,0,inOrdenParaIzquierda.size()-1);
       nodoActual.setHijoIzquierdo(reconstruccionInOrden_PostOrdenRecursivo(inOrdenParaIzquierda,postOrdenParaIzquierda));
       
       List<T> inOrdenParaDerecha = subLista(listaInOrden,posicionRaiz+1,listaInOrden.size()-1);
       List<T> postOrdenParaDerecha = subLista(listaPostOrden,postOrdenParaIzquierda.size(),listaPostOrden.size()-2);
       nodoActual.setHijoDerecho(reconstruccionInOrden_PostOrdenRecursivo(inOrdenParaDerecha,postOrdenParaDerecha));
       
       return nodoActual;
   }
   
   //Metodo que retorne verdadero si un árbol binario está 
   // balanceado según las reglas del árbol AVL, falso en caso contrario
   public  boolean esBalanceado(){
       return esBalanceado(this.raiz);
   }
   
   private  boolean esBalanceado(NodoBinario nodoActual){
       if(NodoBinario.esNodoVacio(nodoActual)){
           return true;
       }
       final int rango_permitido = 1;
       int alturaXIzquierda = altura(nodoActual.getHijoIzquierdo());
       int alturaXDerecha = altura(nodoActual.getHijoDerecho());
       int diferencia = alturaXIzquierda - alturaXDerecha;
       if( ( diferencia > rango_permitido)
            || (diferencia < -rango_permitido) ){
           return false;
        }
        if(esBalanceado(nodoActual.getHijoIzquierdo())){
            return  esBalanceado(nodoActual.getHijoDerecho());
        }
        return false;
   }
   
   public T datoMayor(){
       return datoMayor(raiz);
   }
   
   private T datoMayor(NodoBinario<T> nodoActual){
       if(NodoBinario.esNodoVacio(nodoActual)){
           return (T) null;
       }
       T mayor = nodoActual.getDato();
       T datoMayorIzquierda = datoMayor(nodoActual.getHijoIzquierdo());
       T datoMayorDerecha = datoMayor(nodoActual.getHijoDerecho());       
           if(datoMayorIzquierda != null && datoMayorIzquierda.compareTo(mayor) > 0){
               mayor = datoMayorIzquierda;
           } if(datoMayorDerecha != null && datoMayorDerecha.compareTo(mayor) > 0){
               mayor = datoMayorDerecha;
           }
        return mayor;
   }
}
