package dspm.dc.ufc.br.eduview;

import org.json.JSONObject;

/**
 * Created by caio on 05/07/16.
 */
public class Usuario {
    private int id_usuario;
    private String username;
    private String password;
    private String nome;
    private String email;

    public static final String ID_USUARIO = "id_usuario";
    public static final String USERNAME = "username";
    public static final String PASSWORD = "password";
    public static final String NOME = "nome";
    public static final String EMAIL = "email";

    
    public Usuario(int id,String username,String password,String nome, String email){
        this.id_usuario = id;
        this.username = username;
        this.password = password;
        this.nome = nome;
        this.email = email;
    }

    public Usuario(String json){
        try {
            JSONObject object = new JSONObject(json);
            id_usuario = object.getInt(ID_USUARIO);
            username = object.getString(USERNAME);
            password = object.getString(PASSWORD);
            nome = object.getString(NOME);
            email = object.getString(EMAIL);
        }catch (Exception e){
            e.printStackTrace();
        }
    }

    public String toJson(){
        JSONObject object = new JSONObject();
        try{
            object.put(ID_USUARIO,id_usuario);
            object.put(USERNAME,username);
            object.put(PASSWORD,password);
            object.put(NOME,nome);
            object.put(EMAIL,email);
        }catch(Exception e){
            e.printStackTrace();
        }
        return object.toString();
    }



    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
