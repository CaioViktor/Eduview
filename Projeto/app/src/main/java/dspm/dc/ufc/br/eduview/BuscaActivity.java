package dspm.dc.ufc.br.eduview;

import android.os.Handler;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.SearchView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class BuscaActivity extends AppCompatActivity implements ObserverServer{
    private SearchView caixaBusca;
    private ListView lista;
    private Handler handler = new Handler();
    private EscolaListAdapter adapter;
    private Server server;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_busca);
        server = new Server(this);
        server.attachObserver(this);

        caixaBusca = (SearchView)findViewById(R.id.busca_searchView);
        caixaBusca.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextChange(String newText) {
                //Log.e("onQueryTextChange", "called");
                return false;
            }

            @Override
            public boolean onQueryTextSubmit(String query) {


                // Do your task here
                JSONObject object = new JSONObject();
                try {
                    object.put("where","AND UPPER(e.nome) LIKE '%"+query.toUpperCase()+"%'");
                    server.POST(Server.HTTP + Server.HOST + Server.PORT + "/escola/nome/100/0", object.toString());
                } catch (JSONException e) {
                    e.printStackTrace();
                }

                return false;
            }

        });

        lista = (ListView)findViewById(R.id.busca_lista);

    }

    @Override
    public void notifyPOST(String response) {
        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1){
                Toast.makeText(this,"Ocorreu um erro interno no servidor",Toast.LENGTH_LONG).show();
            }else{
                Log.i("Busca",response);
                ArrayList<Escola> escolas = new ArrayList<>();
                for(int i = 1 ; i <= object.getInt("contador");i++){
                    Escola escola = new Escola(object.getString(i+""));
                    escolas.add(escola);
                }
                adapter = new EscolaListAdapter(this,escolas);
                lista.setAdapter(adapter);
            }

        }catch(Exception e){
            Toast.makeText(this,"Erro ao tratar a respota do servidor",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void notifyGET(String response) {

    }

    @Override
    public Handler getHandler() {
        return handler;
    }
}
