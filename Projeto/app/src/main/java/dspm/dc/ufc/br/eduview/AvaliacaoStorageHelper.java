package dspm.dc.ufc.br.eduview;


import android.content.Context;
import android.database.Cursor;
import android.net.Uri;

import java.util.ArrayList;
import java.util.List;

public class AvaliacaoStorageHelper{

    Context callerContext;

    public AvaliacaoStorageHelper(Context callerContext) {
        this.callerContext = callerContext;
    }

    public List<Avaliacao> getAllAvaliacoesSalvas(){
        List<Avaliacao> result = new ArrayList<>();

        String URL = BDProvider.URL_AVALIACOES;
        Uri avaliacoesUri = Uri.parse(URL);

        Cursor cursor = callerContext.getContentResolver().query(avaliacoesUri,null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                Avaliacao avaliacao = new Avaliacao(cursor.getString(cursor.getColumnIndex(Avaliacao.JSON)));

                result.add(avaliacao);

            } while (cursor.moveToNext());
        }

        return result;
    }
}
