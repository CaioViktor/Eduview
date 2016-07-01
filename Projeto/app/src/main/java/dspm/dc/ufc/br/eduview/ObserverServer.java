package dspm.dc.ufc.br.eduview;


import android.os.Handler;

/**
 * Created by caio on 24/06/16.
 */
public interface ObserverServer {
    void notifyPOST(String response);
    void notifyGET(String response);
    Handler getHandler();
}
