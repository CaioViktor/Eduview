package dspm.dc.ufc.br.eduview;

import org.json.JSONObject;

public class Escola {

    private String nome;
    private String rua;
    private String bairro;
    private int numero;
    private int ddd;
    private int telefone;
    private String email;
    private String situacao;
    private String predio_proprio;
    private String acessibilidade;
    private String rede;
    private String atendimento_especializado;
    private String refeitorio;
    private String auditorio;
    private String laboratorio_informatica;
    private String laboratorio_ciencias;
    private String quadra_coberta;
    private String quadra_descoberta;
    private String patio_coberto;
    private String patio_descoberto;
    private String parque_infantil;
    private String biblioteca;
    private int numero_salas;
    private String alimentacao;
    private String agua;
    private String energia;
    private String internet;
    private int quantidade_computadores;
    private int pk_escola;
    private String cep;
    private float latitude;
    private float longitude;

    public Escola() {


    }

    public Escola(String nome, String rua, String bairro, int numero, int ddd, int telefone, String email, String situacao, String predio_proprio, String acessibilidade, String rede, String atendimento_especializado, String refeitorio, String auditorio, String laboratorio_informatica, String laboratorio_ciencias, String quadra_coberta, String quadra_descoberta, String patio_coberto, String patio_descoberto, String parque_infantil, String biblioteca, int numero_salas, String alimentacao, String agua, String energia, String internet, int quantidade_computadores, int pk_escola, String cep, float latitude, float longitude) {
        this.nome = nome;
        this.rua = rua;
        this.bairro = bairro;
        this.numero = numero;
        this.ddd = ddd;
        this.telefone = telefone;
        this.email = email;
        this.situacao = situacao;
        this.predio_proprio = predio_proprio;
        this.acessibilidade = acessibilidade;
        this.rede = rede;
        this.atendimento_especializado = atendimento_especializado;
        this.refeitorio = refeitorio;
        this.auditorio = auditorio;
        this.laboratorio_informatica = laboratorio_informatica;
        this.laboratorio_ciencias = laboratorio_ciencias;
        this.quadra_coberta = quadra_coberta;
        this.quadra_descoberta = quadra_descoberta;
        this.patio_coberto = patio_coberto;
        this.patio_descoberto = patio_descoberto;
        this.parque_infantil = parque_infantil;
        this.biblioteca = biblioteca;
        this.numero_salas = numero_salas;
        this.alimentacao = alimentacao;
        this.agua = agua;
        this.energia = energia;
        this.internet = internet;
        this.quantidade_computadores = quantidade_computadores;
        this.pk_escola = pk_escola;
        this.cep = cep;
        this.latitude = latitude;
        this.longitude = longitude;
    }

    public Escola(String json){
        try{
            //TODO: Nos casos onde o atributo não é do tipo string é preciso fazer um tratamento, pois pode vir com o valor S/N, SN, S ou alguma outra string louca que represente que não tem
            //TODO:Para resolver esse problema indico que todos os atributos seja do tipo string
            JSONObject object = new JSONObject(json);
            this.nome = object.getString("nome");
            this.rua = object.getString("rua");
            this.bairro = object.getString("bairro");
            this.numero = object.getInt("numero");
            this.ddd = object.getInt("ddd");
            this.telefone = object.getInt("telefone");
            this.email = object.getString("email");
            this.situacao = object.getString("situacao");
            this.predio_proprio = object.getString("predio_proprio");
            this.acessibilidade = object.getString("acessibilidade");
            this.rede = object.getString("rede");
            this.atendimento_especializado = object.getString("atendimento_especializado");
            this.refeitorio = object.getString("refeitorio");
            this.auditorio = object.getString("auditorio");
            this.laboratorio_informatica = object.getString("laboratorio_informatica");
            this.laboratorio_ciencias = object.getString("laboratorio_ciencias");
            this.quadra_coberta = object.getString("quadra_coberta");
            this.quadra_descoberta = object.getString("quadra_descoberta");
            this.patio_coberto = object.getString("patio_coberto");
            this.patio_descoberto = object.getString("patio_descoberto");
            this.parque_infantil = object.getString("parque_infantil");
            this.biblioteca = object.getString("biblioteca");
            this.numero_salas = object.getInt("numero_salas");
            this.alimentacao = object.getString("alimentacao");
            this.agua = object.getString("agua");
            this.energia = object.getString("energia");
            this.internet = object.getString("internet");
            this.quantidade_computadores = object.getInt("quantidade_computadores");
            this.pk_escola = object.getInt("pk_escola");
            this.cep = object.getString("cep");
            this.latitude = Float.parseFloat(object.getString("latitude").replace(",","."));
            this.longitude = Float.parseFloat(object.getString("longitude").replace(",","."));
        }catch(Exception e){
            e.printStackTrace();
        }
    }

    public String toJson(){
        JSONObject object = new JSONObject();
        String json = "";
        try{
            object.put("nome",this.nome);
            object.put("rua",this.rua);
            object.put("bairro",this.bairro);
            object.put("numero",this.numero);
            object.put("ddd",this.ddd);
            object.put("telefone",this.telefone);
            object.put("email",this.email);
            object.put("situacao",this.situacao);
            object.put("predio_proprio",this.predio_proprio);
            object.put("acessibilidade",this.acessibilidade);
            object.put("rede",this.rede);
            object.put("atendimento_especializado",this.atendimento_especializado);
            object.put("refeitorio",this.refeitorio);
            object.put("auditorio",this.auditorio);
            object.put("laboratorio_informatica",this.laboratorio_informatica);
            object.put("laboratorio_ciencias",this.laboratorio_ciencias);
            object.put("quadra_coberta",this.quadra_coberta);
            object.put("quadra_descoberta",this.quadra_descoberta);
            object.put("patio_coberto",this.patio_coberto);
            object.put("patio_descoberto",this.patio_descoberto);
            object.put("parque_infantil",this.parque_infantil);
            object.put("biblioteca",this.biblioteca);
            object.put("numero_salas",this.numero_salas);
            object.put("alimentacao",this.alimentacao);
            object.put("agua",this.agua);
            object.put("energia",this.energia);
            object.put("internet",this.internet);
            object.put("quantidade_computadores",this.quantidade_computadores);
            object.put("pk_escola",this.pk_escola);
            object.put("cep",this.cep);
            object.put("latitude",this.latitude);
            object.put("longitude",this.longitude);
        }catch(Exception e){
            e.printStackTrace();
        }

        json = object.toString();
        return json;
    }

    public String getNome() {
        return nome;
    }

    public void setNome(String nome) {
        this.nome = nome;
    }

    public String getRua() {
        return rua;
    }

    public void setRua(String rua) {
        this.rua = rua;
    }

    public String getBairro() {
        return bairro;
    }

    public void setBairro(String bairro) {
        this.bairro = bairro;
    }

    public int getNumero() {
        return numero;
    }

    public void setNumero(int numero) {
        this.numero = numero;
    }

    public int getDdd() {
        return ddd;
    }

    public void setDdd(int ddd) {
        this.ddd = ddd;
    }

    public int getTelefone() {
        return telefone;
    }

    public void setTelefone(int telefone) {
        this.telefone = telefone;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getSituacao() {
        return situacao;
    }

    public void setSituacao(String situacao) {
        this.situacao = situacao;
    }

    public String getPredio_proprio() {
        return predio_proprio;
    }

    public void setPredio_proprio(String predio_proprio) {
        this.predio_proprio = predio_proprio;
    }

    public String getAcessibilidade() {
        return acessibilidade;
    }

    public void setAcessibilidade(String acessibilidade) {
        this.acessibilidade = acessibilidade;
    }

    public String getRede() {
        return rede;
    }

    public void setRede(String rede) {
        this.rede = rede;
    }

    public String getAtendimento_especializado() {
        return atendimento_especializado;
    }

    public void setAtendimento_especializado(String atendimento_especializado) {
        this.atendimento_especializado = atendimento_especializado;
    }

    public String getRefeitorio() {
        return refeitorio;
    }

    public void setRefeitorio(String refeitorio) {
        this.refeitorio = refeitorio;
    }

    public String getAuditorio() {
        return auditorio;
    }

    public void setAuditorio(String auditorio) {
        this.auditorio = auditorio;
    }

    public String getLaboratorio_informatica() {
        return laboratorio_informatica;
    }

    public void setLaboratorio_informatica(String laboratorio_informatica) {
        this.laboratorio_informatica = laboratorio_informatica;
    }

    public String getLaboratorio_ciencias() {
        return laboratorio_ciencias;
    }

    public void setLaboratorio_ciencias(String laboratorio_ciencias) {
        this.laboratorio_ciencias = laboratorio_ciencias;
    }

    public String getQuadra_coberta() {
        return quadra_coberta;
    }

    public void setQuadra_coberta(String quadra_coberta) {
        this.quadra_coberta = quadra_coberta;
    }

    public String getQuadra_descoberta() {
        return quadra_descoberta;
    }

    public void setQuadra_descoberta(String quadra_descoberta) {
        this.quadra_descoberta = quadra_descoberta;
    }

    public String getPatio_coberto() {
        return patio_coberto;
    }

    public void setPatio_coberto(String patio_coberto) {
        this.patio_coberto = patio_coberto;
    }

    public String getPatio_descoberto() {
        return patio_descoberto;
    }

    public void setPatio_descoberto(String patio_descoberto) {
        this.patio_descoberto = patio_descoberto;
    }

    public String getParque_infantil() {
        return parque_infantil;
    }

    public void setParque_infantil(String parque_infantil) {
        this.parque_infantil = parque_infantil;
    }

    public String getBiblioteca() {
        return biblioteca;
    }

    public void setBiblioteca(String biblioteca) {
        this.biblioteca = biblioteca;
    }

    public int getNumero_salas() {
        return numero_salas;
    }

    public void setNumero_salas(int numero_salas) {
        this.numero_salas = numero_salas;
    }

    public String getAlimentacao() {
        return alimentacao;
    }

    public void setAlimentacao(String alimentacao) {
        this.alimentacao = alimentacao;
    }

    public String getAgua() {
        return agua;
    }

    public void setAgua(String agua) {
        this.agua = agua;
    }

    public String getEnergia() {
        return energia;
    }

    public void setEnergia(String energia) {
        this.energia = energia;
    }

    public String getInternet() {
        return internet;
    }

    public void setInternet(String internet) {
        this.internet = internet;
    }

    public int getQuantidade_computadores() {
        return quantidade_computadores;
    }

    public void setQuantidade_computadores(int quantidade_computadores) {
        this.quantidade_computadores = quantidade_computadores;
    }

    public int getPk_escola() {
        return pk_escola;
    }

    public void setPk_escola(int pk_escola) {
        this.pk_escola = pk_escola;
    }

    public String getCep() {
        return cep;
    }

    public void setCep(String cep) {
        this.cep = cep;
    }

    public float getLatitude() {
        return latitude;
    }

    public void setLatitude(float latitude) {
        this.latitude = latitude;
    }

    public float getLongitude() {
        return longitude;
    }

    public void setLongitude(float longitude) {
        this.longitude = longitude;
    }
}