package pedrocosta.controlador;


import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import info.plux.pluxapi.Constants;


public class Definicoes extends AppCompatActivity {

    ServiceBitalino mService;
    boolean mBounded;
    Intent mIntent;
    TextView text;
    Button button;

    EditText textConexao;

    TextView textEstado;
    TextView textMACAddress;
    TextView textInformacao;

    Button buttonLigar;
    Button buttonDefault;
    Button buttonDesligar;
    Button buttonState;
    private Constants.States currentState = Constants.States.DISCONNECTED;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
        initUIElements();

        doBindService();


        final Handler handler = new Handler();
        final int delay = 500; //milliseconds

        handler.postDelayed(new Runnable(){
            public void run(){
                bitalinoInformacao();
                handler.postDelayed(this, delay);
            }
        }, delay);

    }

    private void initUIElements() {
        buttonLigar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                boolean formatoMAC = textConexao.getText().toString().matches("\\d\\d:\\d\\d:\\d\\d:\\d\\d:\\d\\d:\\d\\d");
                if(formatoMAC)
                    mService.ligar(textConexao.getText().toString());
                else
                    Toast.makeText(getApplicationContext(), "MAC Address tem de estar no formato \"00:00:00:00:00:00\"", Toast.LENGTH_SHORT).show();
            }
        });


        buttonDefault.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mService.ligar();
            }
        });

        buttonDesligar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mService.desligar();
            }
        });

        buttonState.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                bitalinoInformacao();
            }
        });

        /*
        buttonState.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mService.informacao();
            }
        });
        */
    }

    private void bitalinoInformacao() {
        String aux = mService.informacao();

        String[] informacao = aux.split("/");
        String bitalinoState = informacao[0];
        String bitalinoName = informacao[1];


        if(bitalinoState.equals("null")){
            textEstado.setText("NOT_CONNECTED");
        }
        else {
            String[] bitalinoInfo = bitalinoState.split(" "); // String retornada: Device 20:15:05:29:21:77: CONNECTED
            textEstado.setText(bitalinoInfo[2]);
            textMACAddress.setText(bitalinoInfo[1]);
        }

    }

    private void initElements(){
        mIntent = new Intent(this, ServiceBitalino.class);
        textConexao = (EditText)findViewById(R.id.editText_MACAdress);


        textEstado = (TextView) findViewById(R.id.textView_estado);
        textMACAddress = (TextView) findViewById(R.id.textView_macAddress);

        buttonLigar = (Button) findViewById(R.id.button_ligar);
        buttonDesligar = (Button) findViewById(R.id.button_desligar);
        buttonDefault = (Button) findViewById(R.id.button_default);
        buttonState = (Button) findViewById(R.id.button_state);
    }



    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("OnResume Definicoes");
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("onDestroy Definicoes");
    }

    @Override
    protected void onPause(){
        super.onPause();
        doUnbindService();
        System.out.println("OnPause Definicoes");
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(Definicoes.this, MainActivity.class);
                intent.putExtra("Back Pressed",true);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(Definicoes.this, MainActivity.class);
        intent.putExtra("Back Pressed",true);
        startActivity(intent);
        finish();

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
        bindService(mIntent, mConnection, BIND_AUTO_CREATE);
        mBounded = true;
    }

    public void doUnbindService() {
        if (mBounded) {
            unbindService(mConnection);
            mBounded = false;
        }
    }

}