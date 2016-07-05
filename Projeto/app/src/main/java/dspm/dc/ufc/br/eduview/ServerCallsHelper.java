package dspm.dc.ufc.br.eduview;

import android.content.Context;
import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

import dspm.dc.ufc.br.eduview.servercalls.CallEscolasProximas;
import dspm.dc.ufc.br.eduview.servercalls.ServerCall;


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
