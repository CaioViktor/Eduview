package dspm.dc.ufc.br.eduview;


import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Log;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;

public class EscolaStorageHelper {
    private Context callerContext;

    public EscolaStorageHelper(Context callerContext) {
        this.callerContext = callerContext;
    }

    public List<EscolaStorageItem> getAllEscolas(){
        List<EscolaStorageItem> result = new ArrayList<>();

        String URL = BDProvider.URL_ESCOLAS;
        Uri escolasUri = Uri.parse(URL);

        Cursor cursor = callerContext.getContentResolver().query(escolasUri,null,null,null,null);

        if (cursor.moveToFirst()) {
            do {
                Escola e = new Escola(cursor.getString(cursor.getColumnIndex(Escola.JSON_ESCOLA)));
                int notificacoes = cursor.getInt(cursor.getColumnIndex(EscolaStorageItem.NOTIFICACOES));
                Boolean b;
                if (notificacoes == 0) {
                    b = new Boolean(false);
                } else {
                    b = new Boolean(true);
                }
                String data = cursor.getString(cursor.getColumnIndex(EscolaStorageItem.DATA_ULTIMA_VERIFICACAO));
                result.add(new EscolaStorageItem(e,b,data));

            } while (cursor.moveToNext());
        }

        return result;
    }

    public void setEscolaMarcada(Escola e,boolean marcada){
        ContentValues values = new ContentValues();
        values.put(Escola.ID_ESCOLA,e.getPk_escola());
        values.put(Escola.JSON_ESCOLA,e.getJsonConstructor());
        int notificar = 0;
        if(marcada){notificar = 1;}
        values.put(EscolaStorageItem.NOTIFICACOES,notificar);
        Calendar c = Calendar.getInstance();
        String dataAtual = ""+c.get(Calendar.YEAR)+"-"+(c.get(Calendar.MONTH)+1)+"-"+c.get(Calendar.DAY_OF_MONTH);
        values.put(EscolaStorageItem.DATA_ULTIMA_VERIFICACAO,dataAtual);

        Uri escolasURI = Uri.parse(BDProvider.URL_ESCOLAS);
        String selector = Escola.ID_ESCOLA+"="+e.getPk_escola();
        int count = callerContext.getContentResolver().update(escolasURI,values,selector,null);

        Log.i("EscolaStorageHelper","Resultado do update: count="+count);

    }

}
