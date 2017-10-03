package pedrocosta.controlador.ListaMovimentos;

import android.content.Intent;
import android.os.Bundle;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.Toast;

import java.util.ArrayList;

import pedrocosta.controlador.MainActivity;
import pedrocosta.controlador.R;

public class ListaMovimentos extends AppCompatActivity {


    ArrayList<Movimento> listaMovimentos;
    ListView listView;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_movimentos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        listView = (ListView)findViewById(R.id.listview_listaMovimentos);


        listaMovimentos = new ArrayList<>();

        listaMovimentos.add(new Movimento(0, "Polegar", 0,0,0,0,0,0));
        listaMovimentos.add(new Movimento(0, "Indicador", 0,0,0,0,0,0));
        listaMovimentos.add(new Movimento(0, "Medio", 0,0,0,0,0,0));
        listaMovimentos.add(new Movimento(0, "Anelar", 0,0,0,0,0,0));
        listaMovimentos.add(new Movimento(0, "Minimo", 0,0,0,0,0,0));


        adapter = new CustomAdapter(listaMovimentos, getApplicationContext());

        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Movimento movimento = listaMovimentos.get(position);
                Snackbar.make(view, "CLICK " + movimento.getNome() , Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();
            }
        });
        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movimento movimento = listaMovimentos.get(position);
                Snackbar.make(view, "LONG CLICK " + movimento.getNome() , Snackbar.LENGTH_LONG)
                        .setAction("No action", null).show();

                return true;
            }
        });
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.activity_definicoes_drawer, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            case R.id.action_criarMovimento:
                mostrarDialogCriarMovimento();
                return true;

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ListaMovimentos.this, MainActivity.class);
                intent.putExtra("Back Pressed",true);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    private void mostrarDialogCriarMovimento() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListaMovimentos.this);
        final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_criar_movimento, null);

        alertBuilder.setView(viewDialog);
        final AlertDialog alert = alertBuilder.create();

        Button viewConfirm = (Button) viewDialog.findViewById(R.id.button_dialogConfirmar);
        Button viewCancel = (Button) viewDialog.findViewById(R.id.button_dialogCancelar);

        viewConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(ListaMovimentos.this , "Confirmado", Toast.LENGTH_LONG).show();
            }
        });
        viewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Toast.makeText(ListaMovimentos.this , "Cancelado", Toast.LENGTH_LONG).show();
                alert.dismiss();
            }
        });


        alert.show();
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ListaMovimentos.this, MainActivity.class);
        intent.putExtra("Back Pressed",true);
        startActivity(intent);
    }
}