package pedrocosta.controlador;

/**
 * Created by Pedro-PC on 9/12/2017.
 */

public class Singleton {
    private static Singleton instance;
    private boolean bitalinoIniciado = false;


    private Singleton() { }


    public static Singleton getInstance( ) {
        if(instance == null) {
            instance = new Singleton();
        }
        return instance;
    }

    public void inicializarBitalino(){
        bitalinoIniciado = true;
    }

    public boolean estadoBitalino(){
        return bitalinoIniciado;
    }
}
