package dspm.dc.ufc.br.eduview;

import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import android.view.View;

import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.Switch;
import android.widget.TextView;
import android.widget.Toast;


import java.util.List;
import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoEscola extends AppCompatActivity {

    private Escola escola;
    private boolean escolaMarcada;
    EscolaStorageHelper esh;

    private String[] tag = {"Nome","Rua","Numero","Bairro","Cep","DDD","Telefone","Email",
            "Situação","Prédio próprio","Acessibilidade","Rede","Atendimento Especializado ",
            "Refeitório","Auditório","Laboratório Informatica","Laboratório Ciências",
            "Quadra coberta","Quadra descoberta","Pátio coberto","Pátio descoberto",
            "Parque infantil","Biblioteca","Número de salas","Alimentação","Água","Energia",
            "Internet","Quantidade de computadores "};

    public AvaliarFragment avaliarFragment;
    public RatingBar rb;
    EditText avaliarT;

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_escola);

        esh = new EscolaStorageHelper(this);

        //server = new Server(this);



        ScrollView sv = (ScrollView) findViewById(R.id.scrollViewInfo);
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        GridLayout gl = new GridLayout(this);
        hsv.addView(gl);


        Intent intent = getIntent();

        if(intent != null){
            String json = intent.getStringExtra("json");
            escola = new Escola(json);
            escolaMarcada = isEscolaMarcada();

            ((Switch)findViewById(R.id.marcadorSwitch)).setChecked(escolaMarcada);

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

    private boolean isEscolaMarcada(){

        List<EscolaStorageItem> escolasResult = esh.getAllEscolas();

        for(EscolaStorageItem esi: escolasResult){
            if((escola.getPk_escola() == esi.getEscola().getPk_escola()) && esi.isNotificacoes()){
                return true;
            }
        }


        return false;
    }

    public void clickSwitchMarcar(View v){
        escolaMarcada = !escolaMarcada;
        esh.setEscolaMarcada(escola,escolaMarcada);
    }


    public void avaliarEscola(View view) {
        SharedPreferences preferences = getSharedPreferences(getString(R.string.app_name), 0);
        if(preferences.getString(MainActivity.USUARIO,null) != null){
            Usuario usuario = new Usuario(preferences.getString(MainActivity.USUARIO,null));
            avaliarFragment = new AvaliarFragment(this,escola,usuario);
            avaliarFragment.show(getFragmentManager(),"Avalie aqui!");
        }else{
            Toast.makeText(getApplicationContext(),"Você deve estar logado.",Toast.LENGTH_SHORT).show();
        }


    }

    public void listarAvaliacoes(View view) {

        Intent i = new Intent(this,ListarAvaliacoesActivity.class);
        i.putExtra(Escola.ID_ESCOLA,escola.getPk_escola());

        startActivity(i);
    }


}
