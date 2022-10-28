/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDAArbol.principal;

import TDAArbol.negocio.ArbolBusquedaMVias;
import TDAArbol.negocio.ExceptionArbolOrdenInvalido;
import TDAArbol.negocio.NodoMVias;

/**
 *
 * @author HP
 */
public class programaMVias {

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws ExceptionArbolOrdenInvalido {
        // TODO code application logic here
        ArbolBusquedaMVias<Integer> a = new ArbolBusquedaMVias<>(4);
        a.insertar(10);
        a.insertar(30);
        a.insertar(20);
        a.insertar(40);
        a.insertar(18);
        a.insertar(12);
        a.insertar(14);
        a.insertar(22);
        a.insertar(28);
        a.insertar(24);
        a.insertar(13);
        a.insertar(9);
        System.out.println(a.recorridoPorNiveles());
        System.out.println(a.size());
        System.out.println(a.cantidadDeHojasAPartirDelNivel(2));
        
    }
}
