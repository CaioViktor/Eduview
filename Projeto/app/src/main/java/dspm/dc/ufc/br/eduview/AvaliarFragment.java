package dspm.dc.ufc.br.eduview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Toast;

public class AvaliarFragment extends DialogFragment implements AlertDialog.OnClickListener,View.OnClickListener,ObserverServer {

    Context callerContext;
    EditText avaliarT;
    RatingBar rb;


    public AvaliarFragment(Context c){
        callerContext = c;
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
                float nota = 2*rb.getRating();
                String texto = avaliarT.getText().toString();
                String data = (new EscolaStorageHelper(callerContext)).getDataAtual();

                Log.i("AvaliarFragment","Informações inseridas são:\n\tNota: "+nota+"\n\tComentario: "+texto+"\n\tData: "+data);
            }
        });
        builder.setNegativeButton("Cancelar", new DialogInterface.OnClickListener() {
            public void onClick(DialogInterface dialog, int which) {
                Log.i("AvaliarFragment","Usuário cancelou :(");
            }
        });



        //final Button avaliar = (Button)view.findViewById(R.id.botaoAvaliar);
        //Button cancelar = (Button)view.findViewById(R.id.botaoCancelar);

       /* avaliar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String texto = avaliarT.getText().toString();
                float nota = rb.getRating();
                String data = (new EscolaStorageHelper(callerContext)).getDataAtual();

                Log.i("AvaliarFragment","Dados inseridos:\n\tNota: "+nota+"\n\t"+"Comentário: "+texto+"\n\t"+"Data: "+data);
                //Avaliacao avaliacao = new Avaliacao(escola.getPk_escola(),-1,texto, data, String.valueOf(nota));

                //server.POST(Server.HTTP + Server.HOST + Server.PORT + "/avaliação/ideescola/limite/deslocamento",
                  //      avaliacao.toJson());

            }

        });*/
        return builder.create();
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
    public void onClick(View v) {

    }

    @Override
    public void onClick(DialogInterface dialog, int which) {

    }
}
