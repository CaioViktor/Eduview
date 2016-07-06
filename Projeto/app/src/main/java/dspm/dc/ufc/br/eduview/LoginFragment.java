package dspm.dc.ufc.br.eduview;

import android.app.AlertDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONObject;



public class LoginFragment extends DialogFragment implements AlertDialog.OnClickListener,View.OnClickListener,ObserverServer{
    private EditText username;
    private EditText password;
    private Button botao;
    private Server server;
    private TextView cadastrar;
    private Handler handler = new Handler();
    private FazLogin context;
    public LoginFragment(FazLogin context){
        this.context = context;
    }
    @Override
    public Dialog onCreateDialog(Bundle bundle){
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());

        builder.setTitle("Login de usuário");
        builder.setNegativeButton("Cancelar", null);

        View view = getActivity().getLayoutInflater().inflate(R.layout.fragment_login,null);
        builder.setView(view);

        username = (EditText)view.findViewById(R.id.login_username);
        password = (EditText)view.findViewById(R.id.login_password);
        botao = (Button)view.findViewById(R.id.login_botao);
        cadastrar = (TextView)view.findViewById(R.id.login_cadastrar);

        botao.setOnClickListener(this);
        cadastrar.setOnClickListener(this);

        server = new Server(getActivity());
        server.attachObserver(this);

        return builder.create();

    }
    @Override
    public void onClick(DialogInterface dialog, int which) {

    }

    @Override
    public void onClick(View v) {
        if(v.equals(botao)){
            if(!username.getText().toString().equals("")){
                if(!password.getText().toString().equals("")){
                    server.GET(Server.HTTP+Server.HOST+Server.PORT+"/usuario/"+username.getText().toString());
                }else{
                    password.setError("Este campo é obrigatório");
                }
            }else{
                username.setError("Este campo é obrigatório");
            }
        }
        else if(v.equals(cadastrar)){
            CadastroUsuarioFragment fragment = new CadastroUsuarioFragment(this);
            fragment.show(((AppCompatActivity)getActivity()).getSupportFragmentManager(),"Cadastro");
        }

    }

    @Override
    public void notifyPOST(String response) {

    }

    @Override
    public void notifyGET(String response) {
        try{
            JSONObject object = new JSONObject(response);
            if(object.has("codigo") && object.getInt("codigo") == 1)
                Toast.makeText(getActivity().getApplicationContext(), "Usuário não cadastrado", Toast.LENGTH_LONG);
            else{
                if(object.has("1")){
                    Usuario usuario = new Usuario(object.getString("1"));
                    if(usuario.getPassword().equals(password.getText().toString())){
                        SharedPreferences preferences = getActivity().getSharedPreferences(getString(R.string.app_name), 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        editor.putString(MainActivity.USUARIO,usuario.toJson());
                        editor.commit();
                        getActivity().getFragmentManager().beginTransaction().remove(this).commit();
                        context.logar();
                    }
                    else{
                        Toast.makeText(getActivity().getApplicationContext(), "Usuário ou senha incorretos", Toast.LENGTH_LONG);
                    }
                }
            }
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    @Override
    public Handler getHandler() {
        return handler;
    }

    public void confirmaCadastro(String user,String pass){
        username.setText(user);
        password.setText(pass);
        onClick(botao);
    }
}
