package pedrocosta.controlador.ListaMovimentos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import static java.security.AccessController.getContext;

/**
 * Created by Pedro-PC on 9/5/2017.
 */

public class Movimento {

    int ID, polegar, indicador, medio, anelar, minimo;
    String nome;
    // HashMap<String, Integer> hashDedoValor = new HashMap<>();


    public Movimento(int ID, String nome, int polegar, int indicador, int medio, int anelar, int minimo) {
        this.ID = ID;
        this.nome = nome;
        this.polegar = polegar;
        this.indicador = indicador;
        this.medio = medio;
        this.anelar = anelar;
        this.minimo = minimo;
    }

    public int getID() {
        return ID;
    }

    public void setID(int ID) {
        this.ID = ID;
    }

    public int getPolegar() {
        return polegar;
    }

    public void setPolegar(int polegar) {
        this.polegar = polegar;
    }

    public int getIndicador() {
        return indicador;
    }

    public void setIndicador(int indicador) {
        this.indicador = indicador;
    }

    public int getMedio() {
        return medio;
    }

    public void setMedio(int medio) {
        this.medio = medio;
    }

    public int getAnelar() {
        return anelar;
    }

    public void setAnelar(int anelar) {
        this.anelar = anelar;
    }

    public int getMinimo() {
        return minimo;
    }

    public void setMinimo(int minimo) {
        this.minimo = minimo;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getTodosValores() {
        String str = String.valueOf(polegar) + "," + String.valueOf(indicador) + "," + String.valueOf(medio) + "," + String.valueOf(anelar) + "," + String.valueOf(minimo);
        return str;
    }
}
