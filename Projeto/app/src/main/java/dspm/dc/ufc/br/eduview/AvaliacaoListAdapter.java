package dspm.dc.ufc.br.eduview;


import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import java.util.List;

public class AvaliacaoListAdapter extends BaseAdapter{


    private LayoutInflater mInflater;
    private List<Avaliacao> itens;
    private Context context;

    private class ItemSuporte{
        RatingBar nota;
        TextView comentario;
    }

    public AvaliacaoListAdapter(Context context, List<Avaliacao> itens) {
        this.context = context;
        this.itens = itens;
        this.mInflater = LayoutInflater.from(context);

    }

    @Override
    public int getCount() {
        return itens.size();
    }

    @Override
    public Avaliacao getItem(int position) {
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
            view = mInflater.inflate(R.layout.item_list_avaliacao, null);

            //cria o item de suporte
            itemHolder = new ItemSuporte();
            itemHolder.nota = ((RatingBar) view.findViewById(R.id.notaAvaliacaoList));
            itemHolder.comentario = ((TextView)view.findViewById(R.id.comentarioAvaliacaoList));


            //define os itens na view
            view.setTag(itemHolder);
        }else{
            itemHolder = (ItemSuporte) view.getTag();
        }

        //pega os dados da lista e define os valores nos itens:
        Avaliacao item = itens.get(position);
        float nota = (float) (Integer.valueOf(item.getNota()));
        itemHolder.nota.setMax(10);
        itemHolder.nota.setNumStars((int)nota);

        itemHolder.comentario.setText(item.getTexto());

        Log.i("AvaliacaoAdapter",String.valueOf(nota));
        Log.i("AvaliacaoAdapter",item.getTexto());

        return view;

    }
}
