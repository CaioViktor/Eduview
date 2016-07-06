package dspm.dc.ufc.br.eduview;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListView;

import java.util.ArrayList;
import java.util.List;

public class ListEscolas extends AppCompatActivity {
    private List<String> jsonEscolas;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_list_escolas);

        jsonEscolas = (List<String>) getIntent().getSerializableExtra("escolas");
        List<Escola> escolas = new ArrayList<>();

        for(String e : jsonEscolas){
            escolas.add(new Escola(e));
        }

        atualizarLista(escolas);
    }

    private void atualizarLista(List<Escola> escolas){

        EscolaListAdapter escolaListAdapter = new EscolaListAdapter(getApplicationContext(),escolas);

        ListView list = (ListView)findViewById(R.id.listEscolas);
        list.setAdapter(escolaListAdapter);
        list.setOnItemClickListener(new AdapterView.OnItemClickListener(){
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id){
                infoEscola(jsonEscolas.get(position));
            }
        });
    }
    
    public void infoEscola (String json) {
        Intent intent = new Intent(this, InfoEscola.class);
        intent.putExtra("json", json);
        startActivity(intent);
    }
}
