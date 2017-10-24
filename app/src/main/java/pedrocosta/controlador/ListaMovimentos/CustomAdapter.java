package pedrocosta.controlador.ListaMovimentos;

import android.content.Context;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

import pedrocosta.controlador.DialogUtil;
import pedrocosta.controlador.R;

public class CustomAdapter extends ArrayAdapter<Movimento> implements View.OnClickListener{

    private ArrayList<Movimento> dataSet;
    Context mContext;
    DatabaseHelper db;

    // View lookup cache
    private static class ViewHolder {
        TextView textViewName;
        ImageView info;
    }



    public CustomAdapter(ArrayList<Movimento> data, Context context) {
        super(context, R.layout.row_item, data);
        this.dataSet = data;
        this.mContext = context;

    }


    @Override
    public void onClick(View v) {
        int position=(Integer) v.getTag();
        Object object= getItem(position);

        Movimento movimento = (Movimento)object;

        switch (v.getId())
        {
            case R.id.item_info:
                showEditDialog(movimento);
                break;
        }
    }


    private int lastPosition = -1;

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get the data item for this position
        Movimento movimento = getItem(position);
        // Check if an existing view is being reused, otherwise inflate the view
        ViewHolder viewHolder; // view lookup cache stored in tag

        final View result;

        if (convertView == null) {
            viewHolder = new ViewHolder();
            LayoutInflater inflater = LayoutInflater.from(getContext());
            convertView = inflater.inflate(R.layout.row_item, parent, false);
            viewHolder.textViewName = (TextView) convertView.findViewById(R.id.name);
            viewHolder.info = (ImageView) convertView.findViewById(R.id.item_info);

            result=convertView;

            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
            result=convertView;
        }

        Animation animation = AnimationUtils.loadAnimation(mContext, (position > lastPosition) ? R.anim.up_from_bottom : R.anim.down_from_top);
        result.startAnimation(animation);
        lastPosition = position;


        viewHolder.textViewName.setText(movimento.getNome());
        viewHolder.info.setOnClickListener(this);
        viewHolder.info.setTag(position);
        // Return the completed view to render on screen
        return convertView;
    }

    public void showEditDialog(Movimento movimento){
        db = new DatabaseHelper(getContext());
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(getContext());
        LayoutInflater inflater = (LayoutInflater) getContext().getSystemService( Context.LAYOUT_INFLATER_SERVICE );
        View viewDialog = inflater.inflate(R.layout.dialog_criar_movimento, null );
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

        final ListaMovimentos lm = new ListaMovimentos();

        viewConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.updateMovimento(new Movimento(nome.getText().toString(), sb_Polegar.getProgress() * 20,sb_Indicador.getProgress() * 20,sb_Medio.getProgress() * 20,sb_Anelar.getProgress() * 20,sb_Minimo.getProgress() * 20,sb_Rotacao.getProgress() * 20));
                ((ListaMovimentos)getContext()).fillList();
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
