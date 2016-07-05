package dspm.dc.ufc.br.eduview;

import android.content.Context;
import android.location.Location;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.google.android.gms.maps.model.LatLng;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class ServerCallsHelper implements ObserverServer {
    private Handler handler;
    private Context callerContext;
    private Server server;



    public ServerCallsHelper(Context mainContext){
        handler = new Handler();
        callerContext = mainContext;
        server = new Server(mainContext);
        server.attachObserver(this);
    }

    public void getEscolas(LatLng posicao,int raio, int maximo){
        JSONObject object = new JSONObject();
        try {
            object.put("where","UPPER(nome) LIKE \'%ACA%\'");
            server.POST(server.HTTP + server.HOST + server.PORT + "/listescola/" + posicao.longitude + "/" + posicao.latitude + "/"+raio+"/"+maximo, object.toString());
        } catch (JSONException e) {
            e.printStackTrace();
        }
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
            ((MainActivity)callerContext).marcarEscolaNoMapa(e);
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
