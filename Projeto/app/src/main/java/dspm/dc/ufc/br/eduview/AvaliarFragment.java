package dspm.dc.ufc.br.eduview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

import org.json.JSONObject;

public class AvaliarFragment extends DialogFragment implements AlertDialog.OnClickListener,View.OnClickListener,ObserverServer {

    Context callerContext;
    EditText avaliarT;
    RatingBar rb;
    Server server;
    Escola escola;
    Usuario usuario;
    Handler handler = new Handler();

    public AvaliarFragment(Context c,Escola e,Usuario usuario){
        callerContext = c;
        escola = e;
        server = new Server(c);
        server.attachObserver(this);
        this.usuario = usuario;
    }

    @Override
    public Dialog onCreateDialog(Bundle savBundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Avaliação de Escola");

        View view = getActivity().getLayoutInflater().inflate(R.layout.dialog_avaliar,null);
        builder.setView(view);

        avaliarT = (EditText)view.findViewById(R.id.textoAvaliar);
        rb = (RatingBar)view.findViewById(R.id.ratingBar);

        builder.setPositiveButton("Enviar",new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                float nota = rb.getRating();
                String texto = avaliarT.getText().toString();
                String data = (new EscolaStorageHelper(callerContext)).getDataAtual();

                Log.i("AvaliarFragment","Informações inseridas são:\n\tNota: "+nota+"\n\tComentario: "+texto+"\n\tData: "+data);
                Avaliacao avaliacao = new Avaliacao(escola.getPk_escola(),usuario.getId_usuario(),texto,data,nota+"");
                server.POST(Server.HTTP+Server.HOST+Server.PORT+"/avaliacao/"+escola.getPk_escola()+"/1/1",avaliacao.toJson());

            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("AvaliarFragment","Usuário cancelou :(");
            }
        });




        return builder.create();
    }

    @Override
    public void notifyPOST(String response) {
        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1){
                Toast.makeText(callerContext.getApplicationContext(),"Erro ao avaliar no servidor",Toast.LENGTH_LONG).show();
            }else{
                Toast.makeText(callerContext.getApplicationContext(),"Avaliação feita com sucesso",Toast.LENGTH_LONG).show();
            }
        }catch(Exception e){
            Toast.makeText(callerContext.getApplicationContext(),"Ocorreu um erro na comunicação com servidor",Toast.LENGTH_LONG).show();
        }

    }

    @Override
    public void notifyGET(String response) {

    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    @Override
    public void onClick(View v) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
