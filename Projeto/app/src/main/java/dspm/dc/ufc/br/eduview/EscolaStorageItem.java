package dspm.dc.ufc.br.eduview;

public class EscolaStorageItem {
    private Escola escola;
    private boolean notificacoes;
    private String dataUltimaVerificacao;

    public static final String NOTIFICACOES = "notificacoes";
    public static final String DATA_ULTIMA_VERIFICACAO = "data_ultima_verificacao";

    public EscolaStorageItem(Escola escola, boolean notificacoes, String dataUltimaVerificacao) {
        this.escola = escola;
        this.notificacoes = notificacoes;
        this.dataUltimaVerificacao = dataUltimaVerificacao;
    }

    public Escola getEscola() {
        return escola;
    }

    public void setEscola(Escola escola) {
        this.escola = escola;
    }

    public boolean isNotificacoes() {
        return notificacoes;
    }

    public void setNotificacoes(boolean notificacoes) {
        this.notificacoes = notificacoes;
    }

    public String getDataUltimaVerificacao() {
        return dataUltimaVerificacao;
    }

    public void setDataUltimaVerificacao(String dataUltimaVerificacao) {
        this.dataUltimaVerificacao = dataUltimaVerificacao;
    }
}

