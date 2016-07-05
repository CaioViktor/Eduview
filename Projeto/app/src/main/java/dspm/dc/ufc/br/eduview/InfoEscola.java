package dspm.dc.ufc.br.eduview;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.widget.GridLayout;
import android.widget.ScrollView;
import android.widget.TextView;

public class InfoEscola extends AppCompatActivity {

    private Escola escola;
    private String[] tag = {"Nome","Rua","Numero","Bairro","Cep","DDD","Telefone","Email",
            "Situação","Prédio próprio","Acessibilidade","Rede","Atendimento Especializado ",
            "Refeitório","Auditório","Laboratório Informatica","Laboratório Ciências",
            "Quadra coberta","Quadra descoberta","Pátio coberto","Pátio descoberto",
            "Parque infantil","Biblioteca","Número de salas","Alimentação","Água","Energia",
            "Internet","Quantidade de computadores "};


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_escola);

        ScrollView sv = (ScrollView) findViewById(R.id.scrollViewInfo);
        GridLayout gl = new GridLayout(this);
        sv.addView(gl);


        Intent intent = getIntent();

        if(intent != null){
            String json = intent.getStringExtra("json");
            escola = new Escola(json);

            String[] info = escola.getAll();

            for(int i=0; i<tag.length; i++){
                for(int j=0; j<2; j++){
                    GridLayout.Spec linha = GridLayout.spec(i);
                    GridLayout.Spec coluna = GridLayout.spec(j);
                    GridLayout.LayoutParams lp = new GridLayout.LayoutParams(linha, coluna);

                    if (j == 0) {
                        TextView tv = new TextView(this);
                        tv.setText(tag[i]);
                        gl.addView(tv, lp);
                    }

                    if (j == 1) {
                        TextView tv = new TextView(this);
                        tv.setText(info[i]);
                        gl.addView(tv, lp);
                    }
                }
            }
        }
    }
}
