package pedrocosta.controlador.ListaMovimentos;

import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.design.widget.Snackbar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ListView;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Iterator;

import pedrocosta.controlador.MainActivity;
import pedrocosta.controlador.R;
import pedrocosta.controlador.ServiceBitalino;

public class ListaMovimentos extends AppCompatActivity {


    ArrayList<Movimento> listaMovimentos;
    ListView listView;
    private CustomAdapter adapter;
    DatabaseHelper db;
    Context context;
    ServiceBitalino mService;
    boolean mBounded, listaAtiva;
    Intent intentServiceBitalino;
    Handler handler;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_movimentos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
        initUIElements();

        fillList();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        adapter = new CustomAdapter(listaMovimentos, getApplicationContext());
        doBindService();

    }

    private void initUIElements() {
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                final Movimento movimento = listaMovimentos.get(position);

                if(listaAtiva)
                    Snackbar.make(view, "A realizar: " + movimento.getNome() + " " + movimento.getTodosValores() , Snackbar.LENGTH_LONG).setAction("No action", null).show();
                else {
                    Toast.makeText(getApplicationContext(), "A realizar ...", Toast.LENGTH_SHORT).show();
                    return;
                }

                // listView.setEnabled(false);
                final int[] cont = {0};
                handler.postDelayed(new Runnable() {
                    public void run() {
                        if(cont[0] == 7) {
                            handler.removeCallbacksAndMessages(null);
                            // reEnableList();
                            listaAtiva = true;
                        } else {
                            listaAtiva = false;
                            cont[0] += 1;
                            controlaMotores(cont[0],movimento);
                            handler.postDelayed(this, 1500);
                        }
                    }
                }, 1000);


            }
        });


        listView.setOnItemLongClickListener(new AdapterView.OnItemLongClickListener() {

            public boolean onItemLongClick(AdapterView<?> parent, View view, int position, long id) {
                Movimento movimento = listaMovimentos.get(position);
                showDeleteDialog(movimento);
                return true;
            }
        });
    }

    private void reEnableList() {
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    Thread.sleep(500);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                ListaMovimentos.this.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        listView.setEnabled(true);
                    }
                });
            }
        }).start();
    }

    private void initElements() {
        listaMovimentos = new ArrayList<>();
        adapter = new CustomAdapter(listaMovimentos, getApplicationContext());
        db = new DatabaseHelper(this);
        intentServiceBitalino = new Intent(this, ServiceBitalino.class);
        context = this;
        listView = (ListView)findViewById(R.id.listview_listaMovimentos);
        handler = new Handler();
        listaAtiva = true;
    }

    public void fillList() {
        listView.setAdapter(null);
        listaMovimentos.clear();
        for (Iterator<Movimento> i = db.getAllMovimentos().iterator(); i.hasNext();) {
            Movimento item = i.next();
            listaMovimentos.add(item);
        }
        listView.setAdapter(new CustomAdapter(listaMovimentos, this));
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
                showCreateDialog();
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

    private void showCreateDialog() {
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListaMovimentos.this);
        final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_criar_movimento, null);

        alertBuilder.setView(viewDialog);
        final AlertDialog alert = alertBuilder.create();

        Button viewConfirm = (Button) viewDialog.findViewById(R.id.button_dialogConfirmar);
        Button viewCancel = (Button) viewDialog.findViewById(R.id.button_dialogCancelar);

        final TextView nome = (TextView) viewDialog.findViewById(R.id.editText_criarNomeMovimento);

        final SeekBar sb_Polegar = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarPolegar);
        final SeekBar sb_Rotacao = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarRotacao);
        final SeekBar sb_Indicador = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarIndicador);
        final SeekBar sb_Medio = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarMedio);
        final SeekBar sb_Anelar = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarAnelar);
        final SeekBar sb_Minimo = (SeekBar) viewDialog.findViewById(R.id.seekBar_criarMinimo);


        viewConfirm.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                if(nome.getText().equals("") || nome.getText().equals(null) ){
                    Toast.makeText(ListaMovimentos.this , "Nome tem de estar preenchido" , Toast.LENGTH_SHORT).show();
                } else {
                    db.addMovimento(new Movimento(nome.getText().toString(), sb_Polegar.getProgress() * 20,sb_Indicador.getProgress() * 20,sb_Medio.getProgress() * 20,sb_Anelar.getProgress() * 20,sb_Minimo.getProgress() * 20,sb_Rotacao.getProgress() * 20));
                    Toast.makeText(ListaMovimentos.this, "Movimento \"" + " " + nome.getText() + "\" adicionado",
                                Toast.LENGTH_LONG).show();
                    alert.dismiss();
                    fillList();
                }

            }
        });
        viewCancel.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                alert.dismiss();
            }
        });


        alert.show();
    }

    private void showDeleteDialog(final Movimento movimento){
        final AlertDialog.Builder alertBuilder = new AlertDialog.Builder(ListaMovimentos.this);
        final View viewDialog = getLayoutInflater().inflate(R.layout.dialog_eliminar_movimento, null);

        alertBuilder.setView(viewDialog);
        final AlertDialog alert = alertBuilder.create();

        Button buttonConfirmar = (Button) viewDialog.findViewById(R.id.button_dialogEliminarConfirmar);
        final Button buttonCancelar = (Button) viewDialog.findViewById(R.id.button_dialogEliminarCancelar);
        TextView textView = (TextView) viewDialog.findViewById(R.id.textView_eliminarMovimento);
        textView.setText("Tem a certeza que pretende eliminar o movimento: " + movimento.getNome() + "?");

        buttonConfirmar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                db.deleteMovimento(movimento);
                alert.dismiss();
                fillList();
            }
        });
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
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


    ServiceConnection mConnection = new ServiceConnection() {
        public void onServiceDisconnected(ComponentName name) {
            mBounded = false;
            mService = null;
        }

        public void onServiceConnected(ComponentName name, IBinder service) {
            mBounded = true;
            ServiceBitalino.LocalBinder mLocalBinder = (ServiceBitalino.LocalBinder)service;
            mService = mLocalBinder.getServerInstance();
        }
    };

    public void doBindService() {
        bindService(intentServiceBitalino, mConnection, BIND_AUTO_CREATE);
        mBounded = true;
    }

    public void doUnbindService() {
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

    public void controlaMotores(int i, Movimento movimento) {
        if (i == 1) {
            mService.trigger(1, 0);
            mService.pwm(movimento.getPolegar());
        }
        if (i == 2){
            mService.trigger(1, 1);
            mService.pwm(movimento.getRotacao() + 120);
        }
        if (i == 3){
            mService.trigger(1, 0);
            mService.pwm(movimento.getIndicador() + 120);
        }
        if(i == 4) {
            mService.trigger(1, 1);
            mService.pwm(movimento.getMedio());
        }
        if(i == 5) {
            mService.trigger(0, 1);
            mService.pwm(movimento.getAnelar() + 120);
        }
        if(i == 6) {
            mService.trigger(0, 1);
            mService.pwm(movimento.getPolegar());
        }
    }

}
