package dspm.dc.ufc.br.eduview;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkInfo;
import android.util.Log;

public class InternetChecker extends BroadcastReceiver {

    @Override
    public void onReceive(Context context, Intent intent) {
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

    }
    private void onInternetDisconnect(){
        Log.i("InternetChecker","Internet DISconnected");
    }

}
