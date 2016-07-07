package dspm.dc.ufc.br.eduview.servercalls;


import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

import dspm.dc.ufc.br.eduview.BDProvider;
import dspm.dc.ufc.br.eduview.Escola;
import dspm.dc.ufc.br.eduview.EscolaStorageItem;
import dspm.dc.ufc.br.eduview.MainActivity;
import dspm.dc.ufc.br.eduview.Server;

public class CallEscolasProximas extends ServerCall{

    private LatLng posicao;
    private int raio;
    private int maximo;
    private Handler handler = new Handler();
    private Context callerContext;
    private Server server;

    public CallEscolasProximas(Context callerContext, Server server, LatLng posicao, int raio, int maximo) {
        this.server = server;
        this.callerContext = callerContext;
        this.posicao = posicao;
        this.raio = raio;
        this.maximo = maximo;
    }
    @Override
    public void call() {
        JSONObject object = new JSONObject();
        /*try {
            object.put("where","UPPER(nome) LIKE \'%ACA%\'");
            server.POST(server.HTTP + server.HOST + server.PORT + "/listescola/" + posicao.longitude + "/" + posicao.latitude + "/"+raio+"/"+maximo, object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }*/
        server.POST(server.HTTP + server.HOST + server.PORT + "/listescola/" + posicao.latitude + "/" + posicao.longitude+ "/"+raio+"/"+maximo, null);

    }
    @Override
    public void notifyPOST(String response) {
        ArrayList<Escola> escolas = new ArrayList<>();

        try{
            JSONObject jsonObject = new JSONObject(response);
            if(jsonObject.has("codigo") && jsonObject.getInt("codigo") == 1)
                Toast.makeText(callerContext,"Ocorreu um erro interno",Toast.LENGTH_LONG);
            else{
                int size = jsonObject.getInt("contador");
                for(int i = 1; i <= size; i++){
                    Escola escola = new Escola(jsonObject.getJSONObject(i+"").toString());
                    escolas.add(escola);
                }

            }
        }catch(Exception e){
            e.printStackTrace();
        }
        for(Escola e : escolas){
            Log.i("LOGP",e.getBairro());
            //Adicionar ao BD:
            ContentValues values = new ContentValues();
            values.put(Escola.ID_ESCOLA,e.getPk_escola());
            values.put(Escola.JSON_ESCOLA,e.getJsonConstructor());
            values.put(EscolaStorageItem.NOTIFICACOES,0);
            values.put(EscolaStorageItem.DATA_ULTIMA_VERIFICACAO,"null");
            Uri uri = callerContext.getContentResolver().insert(BDProvider.CONTENT_URI_ESCOLAS, values);

            //Marcar no mapa:
            ((MainActivity)callerContext).marcarEscolaNoMapa(e);
        }

    }

    @Override
    public void notifyGET(String response) {

    }

    @Override
    public Handler getHandler(){
        return this.handler;
    }

}