package pedrocosta.controlador;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import static java.security.AccessController.getContext;

/**
 * Created by Pedro-PC on 9/5/2017.
 */

public class Movimento {

    int ID;
    String nome;
    HashMap<String, Integer> hashDedoValor = new HashMap<>();

    public Movimento(int ID, String nome, HashMap<String, Integer> hashDedoValor) {
        this.ID = ID;
        this.nome = nome;
        this.hashDedoValor = hashDedoValor;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public HashMap<String, Integer> getHashDedoValor() {
        return hashDedoValor;
    }

    public void setHashDedoValor(HashMap<String, Integer> hashDedoValor) {
        this.hashDedoValor = hashDedoValor;
    }

    public int getValorDedo(String dedo){
        return hashDedoValor.get(dedo);
    }



}
