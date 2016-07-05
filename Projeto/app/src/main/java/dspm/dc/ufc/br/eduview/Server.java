package dspm.dc.ufc.br.eduview;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.util.Log;


import java.io.BufferedReader;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created by caio on 29/05/16.
 */
public class Server implements ObservableServer{
    public final static String HOST = "10.0.2.2";
    public final static String PORT = ":5000";
    public final static String HTTP = "http://";
    private ObserverServer chamador;
    private Context context;
    public Server(Context context){
        this.context = context;
    }

    public void attachObserver(ObserverServer observer){
        this.chamador = observer;
    }

    @Override
    public void detachObserver(ObserverServer observer) {
        chamador = null;
    }

    public void POST(final String url, final String json){
        if(!isConnected()){
            chamador.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    chamador.notifyPOST("{\"codigo\":1,\"mensagem\":\"Você não está conectado a rede\"}");
                }
            });
            return;
        }

        new Thread(new Runnable(){
            public void run(){
                String resultado = "";
//        Log.i("LOGP","Enviando!");
                URL link = null;
                HttpURLConnection con = null;
                try{
                    link = new URL(url);
                    con = (HttpURLConnection) link.openConnection();
                    con.setDoOutput(true);
                    con.setDoInput(true);
                    con.setRequestProperty("Content-Type", "application/json");
                    con.setRequestProperty("Accept", "application/json");
                    con.setRequestMethod("POST");

                    OutputStreamWriter wr = new OutputStreamWriter(con.getOutputStream());
//                    Log.i("LOGP",json);
                    if(json != null){
                        wr.write(json);
                        Log.i("LOGP", json);
                    }
                    else
                        wr.write("");
                    wr.flush();
                    wr.close();


                    final StringBuilder sb = new StringBuilder();
                    int HttpResult = con.getResponseCode();
                    if (HttpResult == HttpURLConnection.HTTP_OK) {
                        BufferedReader br = new BufferedReader(
                                new InputStreamReader(con.getInputStream(), "utf-8"));
                        String line = null;
                        while ((line = br.readLine()) != null) {
                            sb.append(line + "\n");
                        }
                        br.close();
                        resultado = sb.toString();

//                        Log.i("LOGP","Resultado: "+sb.toString());
                    } else {
//                        System.out.println(con.getResponseMessage());
                        resultado = "{\"codigo\":1,\"mensagem\":\"Erro ao fazer a requisição\"}";

                    }

                }catch(Exception e){
                    e.printStackTrace();
                    if(resultado.equals(""))
                        resultado = "{\"codigo\":1,\"mensagem\":\"Ocorreu um erro interno tente novamente mais tarde\"}";
                }
                finally {
                    if(con != null)
                        con.disconnect();
                    final String finalResultado = resultado;

                    (chamador.getHandler()).post(new Runnable(){
                        public void run(){
                            chamador.notifyPOST(finalResultado);
                        }
                    });
                }
            }
        }).start();

    }

    public void GET(final String url){
        Log.i("LOGP",url);
        if(!isConnected()){
            chamador.getHandler().post(new Runnable() {
                @Override
                public void run() {
                    chamador.notifyGET("{\"codigo\":1,\"mensagem\":\"Você não está conectado a rede\"}");
                }
            });
            return;
        }

        new Thread(new Runnable() {
            @Override
            public void run() {
                String conteudo = "";
//        Log.i("LOGP","FOI");
                HttpURLConnection connection = null;
                try {
                    URL link = new URL(url);
                    connection = (HttpURLConnection) link.openConnection();
                    connection.setReadTimeout(10000);
                    connection.setConnectTimeout(15000);
                    connection.setRequestMethod("GET");
                    connection.setDoInput(true);

//                    connection.setDoOutput(true);
//                    OutputStreamWriter wr = new OutputStreamWriter(connection.getOutputStream());
////                    Log.i("LOGP",objeto.toJson());
//                    if(json != null)
//                        wr.write(json);
//                    else
//                        wr.write("");
//                    wr.flush();
                    connection.connect();

                    InputStream is = connection.getInputStream();
                    BufferedReader leitor = new BufferedReader(new InputStreamReader(is, "UTF-8"));
                    String linha = leitor.readLine();
                    while (linha != null) {
                        conteudo += linha;
                        linha = leitor.readLine();
                    }

                }
                catch (Exception e){
                    e.printStackTrace();
                    if(conteudo.equals(""))
                        conteudo = "{\"codigo\":1,\"mensagem\":\"Ocorreu um erro interno tente novamente mais tarde\"}";
                }
                finally {
                    if(connection != null)
                        connection.disconnect();

                    final String finalConteudo = conteudo;
                    chamador.getHandler().post(new Runnable() {
                        @Override
                        public void run() {
                            chamador.notifyGET(finalConteudo);
                        }
                    });
                }
            }
        }).start();

    }
    public boolean isConnected(){
        ConnectivityManager conection = (ConnectivityManager)  context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = conection.getActiveNetworkInfo();
        if(info !=null && info.isConnected())
            return true;
        else
            return false;
    }


}

