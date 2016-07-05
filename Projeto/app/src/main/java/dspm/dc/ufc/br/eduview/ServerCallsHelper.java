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

import dspm.dc.ufc.br.eduview.servercalls.*;


public class ServerCallsHelper{
    private Handler handler;
    private Context callerContext;

    public ServerCallsHelper(Context mainContext){
        handler = new Handler();
        callerContext = mainContext;
    }

    public void getEscolas(LatLng posicao,int raio, int maximo){
        Server server = new Server(callerContext);
        ServerCall sc = new CallEscolasProximas(callerContext,server,posicao,raio,maximo);
        server.attachObserver(sc);
        sc.call();
    }

}
