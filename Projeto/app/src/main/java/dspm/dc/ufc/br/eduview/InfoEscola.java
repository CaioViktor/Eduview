package dspm.dc.ufc.br.eduview;

import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.text.format.DateFormat;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.GridLayout;
import android.widget.HorizontalScrollView;
import android.widget.RatingBar;
import android.widget.ScrollView;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.Date;

public class InfoEscola extends AppCompatActivity {

    private Escola escola;
    private String[] tag = {"Nome","Rua","Numero","Bairro","Cep","DDD","Telefone","Email",
            "Situação","Prédio próprio","Acessibilidade","Rede","Atendimento Especializado ",
            "Refeitório","Auditório","Laboratório Informatica","Laboratório Ciências",
            "Quadra coberta","Quadra descoberta","Pátio coberto","Pátio descoberto",
            "Parque infantil","Biblioteca","Número de salas","Alimentação","Água","Energia",
            "Internet","Quantidade de computadores "};

    public Dialog dialog;
    public RatingBar rb;
    EditText avaliarT;

    private Server server;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_escola);

        server = new Server(this);
        dialog = new Dialog(InfoEscola.this);

        ScrollView sv = (ScrollView) findViewById(R.id.scrollViewInfo);
        HorizontalScrollView hsv = (HorizontalScrollView) findViewById(R.id.horizontalScrollView);
        GridLayout gl = new GridLayout(this);
        hsv.addView(gl);


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

    public void avaliarEscola(View view) {
        dialog.setTitle("Avalie aqui!");
        dialog.setContentView(R.layout.dialog_avaliar);
        dialog.show();

        final Button avaliar = (Button) dialog.findViewById(R.id.botãoAvaliar);
        Button cancelar = (Button) dialog.findViewById(R.id.botaoCancelar);

        avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                avaliarT = (EditText) dialog.findViewById(R.id.textoAvaliar);
                String texto = avaliarT.getText().toString();

                rb = (RatingBar) dialog.findViewById(R.id.ratingBar);
                float nota = rb.getRating();

                String data = getDateTime();

                Avaliacao avaliacao = new Avaliacao(escola.getPk_escola(), , texto, data, nota);

                server.POST(Server.HTTP + Server.HOST + Server.PORT + "/avaliação/ideescola/limite/deslocamento",
                        avaliacao.toJson());

            }

        });

        cancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dialog.cancel();
            }
        });

    }

    public void listarAvaliacoes(View view) {
    }

    private String getDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("dd/MM/yyyy HH:mm:ss");
        Date date = new Date();
        return dateFormat.format(date);
    }
}
