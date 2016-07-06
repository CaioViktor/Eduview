package dspm.dc.ufc.br.eduview;


import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class EscolaListAdapter extends BaseAdapter{


    private LayoutInflater mInflater;
    private List<Escola> itens;
    private Context context;

    private class ItemSuporte{
        TextView nome;
        TextView rede;
        TextView endereco;
        TextView telefone;
    }

    public EscolaListAdapter(Context context, List<Escola> itens) {
        this.context = context;
        this.itens = itens;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Escola getItem(int position) {
        return itens.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View view, ViewGroup parent) {
        ItemSuporte itemHolder;

        if(view == null){ //se a View nao estiver criada, infla o layout dela
            view = mInflater.inflate(R.layout.item_list, null);

            //cria o item de suporte
            itemHolder = new ItemSuporte();
            itemHolder.nome = ((TextView)view.findViewById(R.id.nomeEscola));
            itemHolder.rede = ((TextView)view.findViewById(R.id.redeEscola));
            itemHolder.endereco = ((TextView)view.findViewById(R.id.enderecoEscola));
            itemHolder.telefone = ((TextView)view.findViewById(R.id.telefoneEscola));

            //define os itens na view
            view.setTag(itemHolder);
        }else{
            itemHolder = (ItemSuporte) view.getTag();
        }

        //pega os dados da lista e define os valores nos itens:
        Escola item = itens.get(position);
        itemHolder.nome.setText(item.getNome());
        itemHolder.rede.setText("Rede: "+item.getRede());
        itemHolder.endereco.setText("Endereço: "+item.getRua()+", N:"+item.getNumero());
        itemHolder.telefone.setText("("+item.getDdd()+") "+item.getTelefone());

        return view;

    }
}
