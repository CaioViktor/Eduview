package dspm.dc.ufc.br.eduview.servercalls;


import android.os.Handler;

import com.google.android.gms.maps.model.LatLng;

public class CallEscolasProximas extends ServerCall{
    //private
    public CallEscolasProximas(LatLng posicao, int raio, int maximo) {
        super();
    }

    @Override
    public void notifyPOST(String response) {

    }

    @Override
    public void notifyGET(String response) {

    }

    @Override
    public Handler getHandler() {
        return null;
    }

    @Override
    public void call() {

    }
}
