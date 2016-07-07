package dspm.dc.ufc.br.eduview;

import android.content.BroadcastReceiver;
import android.content.ContentValues;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class InternetChecker extends BroadcastReceiver implements ObserverServer {
    Context context;
    Handler handler = new Handler();
    Server server;

    @Override
    public void onReceive(Context context, Intent intent) {
        this.context = context;
        Log.i("InternetChecker","Recebeu do Broadcaste Receiver");

        ConnectivityManager connectivityManager = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);

        //Estou usando um método deprecated porque a API alvo (17) ainda a usa
        NetworkInfo[] infos = connectivityManager.getAllNetworkInfo();
        for(int i=0;i<infos.length;i++){
            if(infos[i]!=null && infos[i].getState() == NetworkInfo.State.CONNECTED){
                Log.i("InternetChecker",infos[i].getExtraInfo());
            }
        }
        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();//Active network info
        if(networkInfo!=null && networkInfo.isConnected()){
            onInternetConnected();
        }else{
            onInternetDisconnect();
        }
    }

    private void onInternetConnected(){
        Log.i("InternetChecker","Internet Connected");
        AvaliacaoStorageHelper ash = new AvaliacaoStorageHelper(context);

        List<Avaliacao> avaliacoes = ash.getAllAvaliacoesSalvas();

        server = new Server(context);
        server.attachObserver(this);


        for(Avaliacao a: avaliacoes){
            Log.i("InternetChecker", "Vai enviar avaliacao " + a.getNota() + " " + a.getTexto());

            server.POST(Server.HTTP + Server.HOST + Server.PORT + "/avaliacao/" + a.getId_escola() + "/1/1", a.toJson());

        }


    }
    private void onInternetDisconnect(){
        Log.i("InternetChecker","Internet DISconnected");
    }

    @Override
    public void notifyPOST(String response){

        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1){
                if(object.has("mensagem") && object.getString("mensagem").equals("Você não está conectado a rede")){
                    Toast.makeText(context.getApplicationContext(), "Você não está conectado. A Avaliação será enviada quando a rede estiver disponível.", Toast.LENGTH_LONG).show();
                }
                Toast.makeText(context.getApplicationContext(),"Erro ao avaliar no servidor",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(context.getApplicationContext(), "Avaliação feita com sucesso", Toast.LENGTH_LONG).show();

                String selector = " "+Avaliacao.JSON+"="+object.getString("json");
                Log.i("JsonAvaliacao", object.getString("json"));
                List<Avaliacao> l = (new AvaliacaoStorageHelper(context)).getAllAvaliacoesSalvas();
                for(Avaliacao aa : l){
                    Log.i("JsonAvaliacao", aa.toJson());
                }
                selector.replace("\"", "\'");
                String selectorFinal= "\""+selector+"\"";
                Log.i("JsonAvaliacao","selector final: "+ selectorFinal);

                context.getContentResolver().delete(BDProvider.CONTENT_URI_AVALIACOES,selectorFinal,null);

            }
        }catch(Exception e){
            Toast.makeText(context.getApplicationContext(),"Ocorreu um erro na comunicação com servidor",Toast.LENGTH_LONG).show();
            e.printStackTrace();
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
