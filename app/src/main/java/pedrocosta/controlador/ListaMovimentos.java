package pedrocosta.controlador;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.ArrayList;

public class ListaMovimentos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_lista_movimentos);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        String[] movimentos = {"Mindinho","Anelar","Médio","Indicador","Polegar"};
        ListView listaMovimentos = (ListView) findViewById(R.id.listview_listaMovimentos);

        final ArrayList<String> list = new ArrayList<String>();
        for (int i = 0; i < movimentos.length; ++i) {
            list.add(movimentos[i]);
        }

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_list_item_1, list);
        listaMovimentos.setAdapter(adapter);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {

            // Respond to the action bar's Up/Home button
            case android.R.id.home:
                Intent intent = new Intent(ListaMovimentos.this, MainActivity.class);
                intent.putExtra("Back Pressed",true);
                startActivity(intent);
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed(){
        Intent intent = new Intent(ListaMovimentos.this, MainActivity.class);
        intent.putExtra("Back Pressed",true);
        startActivity(intent);
    }
}