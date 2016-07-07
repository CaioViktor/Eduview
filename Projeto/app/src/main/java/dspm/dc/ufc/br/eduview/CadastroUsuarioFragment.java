package dspm.dc.ufc.br.eduview;


import android.app.AlertDialog;
import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.DialogFragment;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.json.JSONObject;


public class CadastroUsuarioFragment extends DialogFragment implements AlertDialog.OnClickListener,View.OnClickListener,ObserverServer{
    private EditText username;
    private EditText password;
    private EditText passwordConfirma;
    private EditText nome;
    private EditText email;
    private Button botao;
    private Server server;
    private Handler handler = new Handler();
    private LoginFragment login;
    public CadastroUsuarioFragment(LoginFragment loginFragment){
        login = loginFragment;
    }
    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Cadastro de usuário");
        builder.setNegativeButton("Cancelar", null);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_cadastro_usuario,null);
        builder.setView(view);
        username = (EditText)view.findViewById(R.id.cadastro_username);
        password = (EditText)view.findViewById(R.id.cadastro_password);
        passwordConfirma = (EditText)view.findViewById(R.id.cadastro_password_confirma);
        nome = (EditText)view.findViewById(R.id.cadastro_nome);
        email = (EditText)view.findViewById(R.id.cadastro_email);
        botao = (Button)view.findViewById(R.id.cadastro_botao);
        botao.setOnClickListener(this);
        server = new Server(getActivity());
        server.attachObserver(this);

        return builder.create();

    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onClick(View v) {
        if(!username.getText().toString().equals("")){
            if(!password.getText().toString().equals("")){
                if(!passwordConfirma.getText().toString().equals("")){
                    if(!nome.getText().toString().equals("")){
                        if(password.getText().toString().equals(passwordConfirma.getText().toString())){
                            Usuario usuario = new Usuario(0,username.getText().toString(),password.getText().toString(),nome.getText().toString(),email.getText().toString());
                            Log.i("LOGP",usuario.toJson()+"\n"+Server.HTTP+Server.HOST+Server.PORT+"/usuario/u");
                            server.POST(Server.HTTP + Server.HOST + Server.PORT + "/usuario/u", usuario.toJson());
                        }else{
                            passwordConfirma.setError("Senhas não batem");
                        }
                    }else{
                        nome.setError("Este campo é obrigatório");
                    }
                }else{
                    passwordConfirma.setError("Este campo é obrigatório");
                }
            }else{
                password.setError("Este campo é obrigatório");
            }
        }else{
            username.setError("Este campo é obrigatório");
        }
    }

    @Override
    public void notifyPOST(String response) {
        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1)
                Toast.makeText(getActivity().getApplicationContext(),"Erro ao cadastrar usuário",Toast.LENGTH_LONG).show();
            else{
                Toast.makeText(getActivity().getApplicationContext(),"Usuário cadastrado com sucesso",Toast.LENGTH_LONG).show();
                getActivity().getSupportFragmentManager().beginTransaction().remove(this).commit();
                login.confirmaCadastro(username.getText().toString(),password.getText().toString());
            }
        }catch(Exception e){
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
