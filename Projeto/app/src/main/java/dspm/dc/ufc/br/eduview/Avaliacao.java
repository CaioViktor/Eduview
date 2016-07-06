package dspm.dc.ufc.br.eduview;

import org.json.JSONObject;

/**
 * Created by caio on 05/07/16.
 */
public class Avaliacao {
    private int id_escola;
    private int id_usuario;
    private String texto;
    private String data;
    private String nota;

    public static final String ID_ESCOLA = "id_escola";
    public static final String ID_USUARIO = "id_usuario";
    public static final String TEXTO = "texto";
    public static final String DATA = "data";
    public static final String NOTA = "nota";
    public static final String JSON = "json";

    public Avaliacao(int id_escola, int id_usuario, String texto, String data, String nota){
        this.id_escola = id_escola;
        this.id_usuario = id_usuario;
        this.texto = texto;
        this.data = data;
        this.nota = nota;
    }

    public Avaliacao(String json){
        try {
            JSONObject object = new JSONObject(json);
            this.id_escola = object.getInt(ID_ESCOLA);
            this.id_usuario = object.getInt(ID_USUARIO);
            this.texto = object.getString(TEXTO);
            this.data = object.getString(DATA);
            this.nota = object.getString(NOTA);
            
        }catch(Exception e){
            e.printStackTrace();
        }

    }

    public String toJson(){
        JSONObject object = new JSONObject();
        try {
            object.put(ID_ESCOLA, id_escola);
            object.put(ID_USUARIO, id_usuario);
            object.put(TEXTO, texto);
            object.put(DATA, data);
            object.put(NOTA, nota);
        }catch(Exception e){
            e.printStackTrace();
        }
        return object.toString();
    }

    public int getId_escola() {
        return id_escola;
    }

    public void setId_escola(int id_escola) {
        this.id_escola = id_escola;
    }

    public int getId_usuario() {
        return id_usuario;
    }

    public void setId_usuario(int id_usuario) {
        this.id_usuario = id_usuario;
    }

    public String getTexto() {
        return texto;
    }

    public void setTexto(String texto) {
        this.texto = texto;
    }

    public String getData() {
        return data;
    }

    public void setData(String data) {
        this.data = data;
    }

    public String getNota() {
        return nota;
    }

    public void setNota(String nota) {
        this.nota = nota;
    }
}
