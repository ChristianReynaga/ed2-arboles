package TDAArbol.negocio;

/**
 *Excepcion para la clase ArbolBusquedaMVias cuando recibe un "orden" invalido.
 * @author HP
 */
public class ExceptionArbolOrdenInvalido extends Exception {

    /**
     * Creates a new instance of <code>ExceptionArbolOrdenInvalido</code>
     * without detail message.
     */
    public ExceptionArbolOrdenInvalido() {
    }

    /**
     * Constructs an instance of <code>ExceptionArbolOrdenInvalido</code> with
     * the specified detail message.
     *
     * @param msg the detail message.
     */
    public ExceptionArbolOrdenInvalido(String msg) {
        super(msg);
    }
}
