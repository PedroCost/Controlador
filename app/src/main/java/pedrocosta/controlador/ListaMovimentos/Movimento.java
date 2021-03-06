package pedrocosta.controlador.ListaMovimentos;

import android.content.ContentValues;
import android.database.sqlite.SQLiteDatabase;

import java.util.HashMap;

import static java.security.AccessController.getContext;

public class Movimento {

    int ID, polegar, indicador, medio, anelar, minimo, rotacao;
    String nome;
    // HashMap<String, Integer> hashDedoValor = new HashMap<>();


    public Movimento(String nome, int polegar, int indicador, int medio, int anelar, int minimo, int rotacao) {
        this.ID = 1;
        this.nome = nome;
        this.polegar = polegar;
        this.indicador = indicador;
        this.medio = medio;
        this.anelar = anelar;
        this.minimo = minimo;
        this.rotacao = rotacao;
    }
    public Movimento(int id, String nome, int polegar, int indicador, int medio, int anelar, int minimo, int rotacao) {
        this.ID = id;
        this.nome = nome;
        this.polegar = polegar;
        this.indicador = indicador;
        this.medio = medio;
        this.anelar = anelar;
        this.minimo = minimo;
        this.rotacao = rotacao;
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

    public int getRotacao() {
        return rotacao;
    }

    public void setRotacao(int rotacao) {
        this.rotacao = rotacao;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public void setDedos(int polegar, int rotacao, int indicador, int medio, int anelar, int minimo) {
        this.polegar = polegar;
        this.rotacao = rotacao;
        this.indicador = indicador;
        this.medio = medio;
        this.anelar = anelar;
        this.minimo = minimo;
    }

    public String getTodosValores() {
        String str = String.valueOf(polegar) + "," + String.valueOf(indicador) + "," + String.valueOf(medio) + "," + String.valueOf(anelar) + "," + String.valueOf(minimo) + "," + String.valueOf(rotacao);
        return str;
    }
}
