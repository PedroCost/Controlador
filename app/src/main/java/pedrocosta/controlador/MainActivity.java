package pedrocosta.controlador;

import android.bluetooth.BluetoothAdapter;
import android.content.ComponentName;
import android.content.Intent;
import android.content.ServiceConnection;
import android.os.Bundle;
import android.os.IBinder;
import android.support.design.widget.NavigationView;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.SeekBar;
import android.widget.TextView;

import com.afollestad.materialdialogs.MaterialDialog;

import java.util.ArrayList;
import java.util.List;

import info.plux.pluxapi.bitalino.BITalinoCommunication;

public class MainActivity extends AppCompatActivity
        implements NavigationView.OnNavigationItemSelectedListener {

    private Dedo dedoPolegar, dedoIndicador, dedoMedio, dedoAnelar, dedoMinimo, dedo;

    private final String TAG = this.getClass().getSimpleName();

    private boolean isBITalino2 = false;
    private BITalinoCommunication bitalino;

    final int[] digitalChannels = new int[2];
    private static final int REQUEST_ENABLE_BT = 1;

    ServiceBitalino mService;
    boolean mBounded;
    boolean serviceBounded;
    TextView text;
    Button buttonPWM;
    ComponentName service = null;
    SeekBar seekBar;
    TextView textView_SeekBarValue;
    ImageView imageViewMao;
    Intent intentServiceBitalino;
    List<Dedo> listaDedos = new ArrayList<Dedo>();


    public static boolean flag = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(
                this, drawer, toolbar, R.string.navigation_drawer_open, R.string.navigation_drawer_close);
        drawer.setDrawerListener(toggle);
        toggle.syncState();
        NavigationView navigationView = (NavigationView) findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);



        BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        initElements();
        initUIElements();


        if(flag==false) {  // Primeira vez que onCreate é corrido. Começa o serviço e pede o para ligar o bluetooth
            flag=true;
            service = startService(new Intent(getBaseContext(), ServiceBitalino.class));
            if (!mBluetoothAdapter.isEnabled()) {
                Intent enableBtIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
                startActivityForResult(enableBtIntent, REQUEST_ENABLE_BT);
            }
        }


        doBindService();
    }

    private void initUIElements() {

        // Zona de click da Imagem
        imageViewMao.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    System.out.println("Location: " + event.getX() + " , " + event.getY());
                    trataDedos(event.getX(), event.getY());
                }
                return true;
            }
        });

        // Métodos da seekbar
        seekBar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                textView_SeekBarValue.setText(String.valueOf(progress*20));
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                // TODO Auto-generated method stub
            }
        });

        // Botao PWM
        buttonPWM.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                estadoDedos();
                if(dedoIndicador.isAtivo() || dedoAnelar.isAtivo())
                    mService.pwm(seekBar.getProgress()*20 + 120);
                else
                    mService.pwm(seekBar.getProgress()*20);
            }
        });
    }

    private void initElements() {
        seekBar = (SeekBar) findViewById(R.id.seekBar);
        textView_SeekBarValue = (TextView) findViewById(R.id.textView2);
        imageViewMao = (ImageView) findViewById(R.id.imageMao);
        buttonPWM = (Button) findViewById(R.id.button_pwn) ;
        intentServiceBitalino = new Intent(this, ServiceBitalino.class);



        dedoPolegar = new Dedo("Polear", 0, ((ImageView) findViewById(R.id.imagePolegar)));         listaDedos.add(dedoPolegar);
        dedoIndicador = new Dedo("Indicador", 1, ((ImageView) findViewById(R.id.imageIndicador)));  listaDedos.add(dedoIndicador);
        dedoMedio = new Dedo("Medio", 2, ((ImageView) findViewById(R.id.imageMedio)));              listaDedos.add(dedoMedio);
        dedoAnelar = new Dedo("Anelar", 3, ((ImageView) findViewById(R.id.imageAnelar)));           listaDedos.add(dedoAnelar);
        dedoMinimo = new Dedo("Minimo", 4, ((ImageView) findViewById(R.id.imageMinimo)));           listaDedos.add(dedoMinimo);

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

    @Override
    protected void onDestroy() {
        super.onDestroy();
        System.out.println("OnDestroy Main");
        mService.desligar();
    }

    @Override
    protected void onResume() {
        super.onResume();
        System.out.println("onResume Main");
    }

    @Override
    protected void onPause() {
        super.onPause();
        System.out.println("OnPause Main Activity");
        doUnbindService();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            Intent i = new Intent(getApplicationContext(), Definicoes.class);
            startActivity(i);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @SuppressWarnings("StatementWithEmptyBody")
    @Override
    public boolean onNavigationItemSelected(MenuItem item) {
        // Handle navigation view item clicks here.
        int id = item.getItemId();
        if (id == R.id.nav_definicoes) {
            Intent i = new Intent(getApplicationContext(), Definicoes.class);
            startActivity(i);
        }
        if (id == R.id.nav_listaMovimentos) {
            Intent i = new Intent(getApplicationContext(), ListaMovimentos.class);
            startActivity(i);
        } else if (id == R.id.nav_sair) {
            System.exit(0);
        }

        DrawerLayout drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        drawer.closeDrawer(GravityCompat.START);
        return true;
    }

    public void trataDedos(float x, float y) {
        // POLEGAR
        if (x > 3 && x < 364 && y > 429 && y < 689) {
            dedoPolegar.mudaEstado();
            if(dedoPolegar.isAtivo()) {
                desativaDedos(dedoPolegar.getPosicao());
                mService.trigger(1, 0);
            }
            else
                mService.trigger(0,0);
            // INDICADOR
        } else if (x > 190 && x < 415 && y > 70 && y < 400) {
            dedoIndicador.mudaEstado();
            if(dedoIndicador.isAtivo()){
                desativaDedos(dedoIndicador.getPosicao());
                mService.trigger(1,0);
            }
            else
                mService.trigger(0,0);
            // MEDIO
        } else if (x > 420 && x < 550 && y > 0 && y < 368) {
            dedoMedio.mudaEstado();
            if (dedoMedio.isAtivo()){
                desativaDedos(dedoMedio.getPosicao());
                mService.trigger(1, 1);
            }
            else
                mService.trigger(0,0);
            // ANELAR
        } else if (x > 600 && x < 770 && y > 40 && y < 430) {
            dedoAnelar.mudaEstado();
            if(dedoAnelar.isAtivo()) {
                desativaDedos(dedoAnelar.getPosicao());
                mService.trigger(0, 1);
            }
            else
                mService.trigger(0,0);
            // MINIMO
        } else if ((x > 700 && x < 830 && y > 380 && y < 550) || (x > 770 && x < 880 && y > 260 && y < 380)) {
            dedoMinimo.mudaEstado();
            if(dedoMinimo.isAtivo()) {
                desativaDedos(dedoMinimo.getPosicao());
                mService.trigger(0, 1);
            }
            else
                mService.trigger(0,0);
        }
    }

    public void desativaDedos(int posicao){
        for (Dedo elemento : listaDedos) {
            if(elemento.getPosicao() != posicao)
                elemento.desativa();
        }
    }

    public void estadoDedos(){
        System.out.println(dedoPolegar.toString());
        System.out.println(dedoIndicador.toString());
        System.out.println(dedoMedio.toString());
        System.out.println(dedoAnelar.toString());
        System.out.println(dedoMinimo.toString());
    }

}
