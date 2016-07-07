package dspm.dc.ufc.br.eduview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ListarAvaliacoesActivity extends AppCompatActivity implements ObserverServer{

    private Server server;
    private Handler handler = new Handler();


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_listar_avaliacoes);


        server = new Server(this);
        server.attachObserver(this);

        int numeroComentarios = 100;
        int deslocamento = 0;
        String url = Server.HTTP+Server.HOST+Server.PORT + "/avaliacao/"+getIntent().getIntExtra(Escola.ID_ESCOLA,-1)+"/"+numeroComentarios+"/"+deslocamento;
        Log.i("ListarAvaliacoes",url);
        server.GET(url);
    }

    private void atualizarLista(List<Avaliacao> avaliacoes){

        AvaliacaoListAdapter avaliacaoListAdapter = new AvaliacaoListAdapter(getApplicationContext(),avaliacoes);

        ListView list = (ListView)findViewById(R.id.listAvaliacoes);
        list.setAdapter(avaliacaoListAdapter);
    }


    @Override
    public void notifyPOST(String response) {

    }

    @Override
    public void notifyGET(String response) {
        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1){
                Toast.makeText(this,"Erro ao receber as avaliacoes",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(this,"Recebeu as avaliacoes do servidor",Toast.LENGTH_LONG).show();
                List<Avaliacao> avaliacoes = new ArrayList<>();

                for(int i = 1;i <= object.getInt("contador");i++){
                    Avaliacao avaliacao = new Avaliacao(object.getString(""+i));

                    avaliacoes.add(avaliacao);
                }

                atualizarLista(avaliacoes);

            }
        }catch(Exception e){
            e.printStackTrace();
            Toast.makeText(this,"Ocorreu um erro na comunicação com servidor",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public Handler getHandler() {
        return this.handler;
    }
}
