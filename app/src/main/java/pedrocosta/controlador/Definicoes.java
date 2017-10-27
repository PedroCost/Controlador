package pedrocosta.controlador;


import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.IBinder;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import info.plux.pluxapi.Constants;


public class Definicoes extends AppCompatActivity {

    ServiceBitalino mService;
    boolean mBounded;
    Intent mIntent;
    BluetoothAdapter mBluetoothAdapter;
    private static final int REQUEST_ENABLE_BT = 1;

    EditText textConexao;

    TextView textEstado, textMACAddress;

    ImageView imageSearch;

    Button buttonLigar, buttonDefault, buttonDesligar;
    private Constants.States currentState = Constants.States.DISCONNECTED;

    SharedPreferences pref;
    SharedPreferences.Editor editor;
    Handler handler;

    String aux_lastState;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_definicoes);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        initElements();
        initUIElements();

        doBindService();

        String mac = pref.getString("MACAddress","");
        textConexao.setText(mac);



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

                if (!mBluetoothAdapter.isEnabled()) {
                    Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                    startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
                } else {
                    if (formatoMAC) {
                        mService.ligar(textConexao.getText().toString());
                        editor.putString("MACAddress",textConexao.getText().toString());
                        editor.commit();
                    }
                    else
                        Toast.makeText(getApplicationContext(), "MAC Address tem de estar no formato \"00:00:00:00:00:00\"", Toast.LENGTH_SHORT).show();
                }
            }
        });



        buttonDesligar.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                mService.desligar();
            }
        });

        imageSearch.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                Intent intent = new Intent(Definicoes.this, ScanActivity.class);
                startActivity(intent);
            }
        });

    }

    private void bitalinoInformacao() {
        String aux = mService.informacao();

        String[] informacao = aux.split("/");
        String bitalinoState = informacao[0];
        String bitalinoName = informacao[1];

        if(!bitalinoState.equals(aux_lastState)) {
            aux_lastState = bitalinoState;

            if (bitalinoState.equals("null")) {
                textEstado.setText("NOT_CONNECTED");
            } else {
                String[] bitalinoInfo = bitalinoState.split(" "); // String retornada: Device 20:15:05:29:21:77: CONNECTED
                textEstado.setText(bitalinoInfo[2]);
                textMACAddress.setText(bitalinoInfo[1].substring(0, bitalinoInfo[1].length() - 1));
            }
        }
    }

    private void initElements(){
        pref = getApplicationContext().getSharedPreferences("PrefMacAddress", MODE_PRIVATE);
        editor = pref.edit();
        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        mIntent = new Intent(this, ServiceBitalino.class);
        textConexao = (EditText)findViewById(R.id.editText_MACAdress);


        textEstado = (TextView) findViewById(R.id.textView_estado);
        textMACAddress = (TextView) findViewById(R.id.textView_macAddress);

        imageSearch = (ImageView) findViewById(R.id.imageView_search);

        buttonLigar = (Button) findViewById(R.id.button_ligar);
        buttonDesligar = (Button) findViewById(R.id.button_desligar);

        handler = new Handler();
        aux_lastState = "";
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
        handler.removeCallbacksAndMessages(null);
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
        onPause();
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