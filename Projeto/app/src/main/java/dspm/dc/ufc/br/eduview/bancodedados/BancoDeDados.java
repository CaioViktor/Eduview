package dspm.dc.ufc.br.eduview.bancodedados;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.util.Log;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

import dspm.dc.ufc.br.eduview.Avaliacao;
import dspm.dc.ufc.br.eduview.Escola;
import dspm.dc.ufc.br.eduview.Usuario;

public class BancoDeDados extends SQLiteOpenHelper {

    public static final String DATABASE_NAME = "EduView.db";
    public static final int DATABASE_VERSION = 1;

    public BancoDeDados(Context context) {
        super(context, DATABASE_NAME, null, DATABASE_VERSION);
    }

    public BancoDeDados(Context context, String name, SQLiteDatabase.CursorFactory factory, int version) {
        super(context, name, factory, version);
    }

    @Override
    public void onCreate(SQLiteDatabase db) {
        Log.i("BancoDeDados","Entrou no onCreate");
        StringBuffer sql = new StringBuffer();

        //Criar tabela escola:
        sql.append("create table escola (");
        sql.append(Escola.ID_ESCOLA+" integer primary key,");
        sql.append(Escola.JSON_ESCOLA+" text) ");

        Log.i("BancoDeDados","String de criacao: "+sql.toString());
        db.execSQL(sql.toString());

        sql = new StringBuffer();
        //Criar tabela usuario:
        sql.append("create table usuario (");
        sql.append(Usuario.ID_USUARIO+" integer primary key autoincrement,");
        sql.append(Usuario.USERNAME+" text,");
        sql.append(Usuario.PASSWORD+" text,");
        sql.append(Usuario.NOME+" text,");
        sql.append(Usuario.EMAIL+" text) ");

        Log.i("BancoDeDados","String de criacao: "+sql.toString());
        db.execSQL(sql.toString());

        sql = new StringBuffer();
        //Criar tabela avaliacao:
        sql.append("create table avaliacao (");
        sql.append(Avaliacao.ID_ESCOLA+" integer,");
        sql.append(Avaliacao.ID_USUARIO+" integer,");
        sql.append(Avaliacao.TEXTO+" text,");
        sql.append(Avaliacao.DATA+" text,");
        sql.append(Avaliacao.NOTA+" text,");
        sql.append("primary key( "+Avaliacao.ID_ESCOLA+","+Avaliacao.ID_USUARIO+" ))");



        Log.i("BancoDeDados","String de criacao: "+sql.toString());
        db.execSQL(sql.toString());

    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
        db.execSQL("drop table if exists escola");
        db.execSQL("drop table if exists usuario");
        db.execSQL("drop table if exists avaliacao");
        onCreate(db);
    }
}