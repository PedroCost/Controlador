package pedrocosta.controlador;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class About extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_about);

        TextView tv = (TextView) findViewById(R.id.textView_texto);
        tv.setText("Projeto desenvolvido em conjunto com o Escola Superior de Tecnologia do Politecnico de Setubal com objectivo de demonstrar o que é desenvolvido na nossa escola e desta maneira ficar em exposição ao novos alunos.");
    }
}
