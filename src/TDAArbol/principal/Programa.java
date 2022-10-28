/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package TDAArbol.principal;

import TDAArbol.negocio.ArbolAVL;
import TDAArbol.negocio.ArbolBinarioBusqueda;
import java.util.List;

/**
 *
 * @author HP
 */
public class Programa {
    public static void main(String[] arg) {
        ArbolBinarioBusqueda<Integer> Arbol = new ArbolBinarioBusqueda<>();
        Arbol.insertar(10);
        Arbol.insertar(20);
        Arbol.insertar(3);
        Arbol.insertar(9);
        Arbol.insertar(15);
        Arbol.insertar(25);
        Arbol.insertar(1);
        Arbol.insertar(4);
        Arbol.insertar(7);
        Arbol.insertar(6);
        Arbol.insertar(8);
        Arbol.insertar(30);
        Arbol.insertar(2);
        Arbol.insertar(30);
        Arbol.insertar(20);
        Arbol.insertar(40);
        Arbol.insertar(10);
        Arbol.insertar(35);
        Arbol.insertar(45);
        Arbol.insertar(50);
        Arbol.insertar(9);
        Arbol.insertar(26);
        Arbol.insertar(28);
        Arbol.insertar(30);
        boolean es = Arbol.esBalanceado();
        System.out.println(es);
        //
        // List<Integer> postorden = Arbol.recorridoEnPostOrdenRecursivo();
        // List<Integer> inorden = Arbol.recorridoEnInOrdenRecursivo();
        //
        // System.out.println("InOrden= " + inorden);
        // System.out.println("--------------------");
        // System.out.println("PostOrden = " + postorden);
        // System.out.println("--------------------");
        //
        // Arbol.vaciar();
        // Arbol.reconstruccionInOrden_PostOrden(inorden, postorden);
        // inorden = Arbol.recorridoEnInOrdenRecursivo();
        // postorden = Arbol.recorridoEnPostOrdenRecursivo();
        // System.out.println("InOrden= " + inorden);
        // System.out.println("--------------------");
        // System.out.println("PostOrden = " + postorden);
        // System.out.println("--------------------");

        // System.out.println(Arbol.recorridoPorNiveles());
        // Arbol.eliminar(26);
        // Arbol.eliminar(20);
        // Arbol.eliminar(21);
        System.out.println(Arbol.recorridoEnInOrden());
    }
}
