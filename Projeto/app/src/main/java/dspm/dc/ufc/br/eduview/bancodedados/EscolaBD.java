package dspm.dc.ufc.br.eduview.bancodedados;


import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

import dspm.dc.ufc.br.eduview.Escola;

public class EscolaBD {
    BancoDeDados bd;
    public EscolaBD(BancoDeDados bd){
        this.bd = bd;
    }

    public boolean create(Escola e){
        SQLiteDatabase db = this.bd.getWritableDatabase();
        ContentValues contentValues = new ContentValues();
        contentValues.put(Escola.ID_ESCOLA, e.getPk_escola());
        contentValues.put(Escola.JSON_ESCOLA, e.getJsonConstructor());
        long id = db.insert("escola", null, contentValues);
        Log.i("SQLite", "create id = " + id);
        if(id==-1){
            return false;
        }else{
            return true;
        }

    }

    public Escola retrieve(int id) {


        return null;
    }

    public List<Escola> list(){
        List<Escola> escolas = null;
        SQLiteDatabase db = this.bd.getReadableDatabase();
        Cursor result = db.rawQuery("select * from escola",null);
        if(result != null && result.getCount()>0){
            escolas = new ArrayList<Escola>();
            result.moveToFirst();
            do{
                Escola e = new Escola(result.getString(result.getColumnIndex(Escola.JSON_ESCOLA)));

                escolas.add(e);
            } while (result.moveToNext());
        }
        return escolas;
    }




}
