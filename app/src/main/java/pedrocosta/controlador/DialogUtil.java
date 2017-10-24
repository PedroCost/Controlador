package pedrocosta.controlador;

import android.content.Context;
import android.provider.ContactsContract;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.SeekBar;
import android.widget.TextView;

import java.util.List;

import pedrocosta.controlador.ListaMovimentos.DatabaseHelper;
import pedrocosta.controlador.ListaMovimentos.ListaMovimentos;
import pedrocosta.controlador.ListaMovimentos.Movimento;

import static java.security.AccessController.getContext;

/**
 * Created by Pedro-PC on 10/17/2017.
 */

public class DialogUtil {

    Context mContext;
    DatabaseHelper db;

    public DialogUtil(Context context){
        this.mContext = context;
    }

    public void EditDialog(Movimento movimento) {
        db = new DatabaseHelper(mContext);

        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(mContext);
        LayoutInflater inflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View viewDialog = inflater.inflate(R.layout.dialog_criar_movimento, null);
        //final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_criar_movimento, null);

        alertBuilder.setView(viewDialog);
        final AlertDialog alert = alertBuilder.create();

        Button viewConfirm = (Button) viewDialog.findViewById(R.id.button_dialogConfirmar);
        Button viewCancel = (Button) viewDialog.findViewById(R.id.button_dialogCancelar);

        TextView titulo = (TextView) viewDialog.findViewById(R.id.textView_criarMovimento);
        titulo.setText("Editar Movimento");


        final EditText nome = (EditText) viewDialog.findViewById(R.id.editText_criarNomeMovimento);
        final SeekBar sb_Polegar = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarPolegar);
        final SeekBar sb_Rotacao = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarRotacao);
        final SeekBar sb_Indicador = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarIndicador);
        final SeekBar sb_Medio = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarMedio);
        final SeekBar sb_Anelar = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarAnelar);
        final SeekBar sb_Minimo = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarMinimo);
        nome.setText(movimento.getNome());
        sb_Polegar.setProgress(movimento.getPolegar() / 20);
        sb_Rotacao.setProgress(movimento.getRotacao() / 20);
        sb_Indicador.setProgress(movimento.getIndicador() / 20);
        sb_Medio.setProgress(movimento.getMedio() / 20);
        sb_Anelar.setProgress(movimento.getAnelar() / 20);
        sb_Minimo.setProgress(movimento.getMinimo() / 20);


        viewConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.updateMovimento(new Movimento(nome.getText().toString(), sb_Polegar.getProgress() * 20,sb_Indicador.getProgress() * 20,sb_Medio.getProgress() * 20,sb_Anelar.getProgress() * 20,sb_Minimo.getProgress() * 20,sb_Rotacao.getProgress() * 20));
                alert.dismiss();
            }
        });
        viewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        });
    alert.show();
    }
}
