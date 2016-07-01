package dspm.dc.ufc.br.eduview;

/**
 * Created by caio on 24/06/16.
 */
public interface ObservableServer {
    void POST(String url, String json);
    void GET(String url);

    void attachObserver(ObserverServer observer);
    void detachObserver(ObserverServer observer);
}
