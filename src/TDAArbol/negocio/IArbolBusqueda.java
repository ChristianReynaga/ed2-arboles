package TDAArbol.negocio;

import java.util.List;

/**
 *Interfaz de la estructura Arbol de busqueda
 * @author HP
 * @param <T>
 */
public interface IArbolBusqueda< T extends Comparable<T> > {
    
    boolean insertar(T dato);
    
    boolean eliminar(T dato);
    
    T buscar(T dato);
    
    boolean contiene(T dato);
    
    List<T> recorridoEnInOrden();
    
    List<T> recorridoEnPreOrden();
    
    List<T> recorridoEnPostOrden();
    
    List<T> recorridoPorNiveles();
    
    int size();
    
    int altura();
    
    void vaciar();
    
    boolean esArbolVacio();
    
    int nivel();
}
