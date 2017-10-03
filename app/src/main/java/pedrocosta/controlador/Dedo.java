package pedrocosta.controlador;


import android.view.View;
import android.widget.ImageView;

/**
 * Created by Pedro-PC.
 */

public class Dedo {
    private String nome;
    private int valor, pwm;
    private int posicao;
    private boolean ativo;
    private ImageView imagem;

    public Dedo(String nome, int posicao, ImageView imagem) {
        this.nome = nome;
        this.valor = 0;
        this.posicao = posicao;
        this.ativo = false;
        this.imagem = imagem;
        this.pwm = 0;
    }
    public Dedo() {
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public int getValor() {
        return valor;
    }

    public void setValor(int valor) {
        this.valor = valor;
    }

    public int getPosicao() {
        return posicao;
    }

    public void setPosicao(int posicao) {
        this.posicao = posicao;
    }

    public boolean isAtivo() {
        return ativo;
    }

    public void setAtivo(boolean ativo) {
        this.ativo = ativo;
    }

    public int getPwm() {
        return pwm;
    }

    public void setPwm(int pwm) {
        this.pwm = pwm;
    }

    public void mudaEstado(){
        this.ativo = !ativo;
        if (this.imagem.getVisibility() == View.VISIBLE){
            this.imagem.setVisibility(View.INVISIBLE);
        } else {
            this.imagem.setVisibility(View.VISIBLE);
        }
    }

    public void desativa(){
        this.ativo = false;
        this.imagem.setVisibility(View.INVISIBLE);
    }

    @Override
    public String toString() {
        return "Dedo{" +
                "nome='" + nome + '\'' +
                ", valor=" + valor +
                ", posicao=" + posicao +
                ", ativo=" + ativo +
                ", pwm=" + pwm +
                '}';
    }
}
