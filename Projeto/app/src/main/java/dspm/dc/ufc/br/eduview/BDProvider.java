package dspm.dc.ufc.br.eduview;

import android.content.ContentProvider;
import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.content.UriMatcher;
import android.database.Cursor;
import android.database.sqlite.SQLiteConstraintException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.database.sqlite.SQLiteQueryBuilder;
import android.net.Uri;
import android.support.annotation.Nullable;
import android.util.Log;


import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class BDProvider extends ContentProvider{
    static final String PROVIDER_NAME = "dspm.dc.ufc.br.eduview.provider.bd";
    static final String URL_ESCOLAS = "content://" + PROVIDER_NAME + "/escolas";
    static final String URL_AVALIACOES = "content://" + PROVIDER_NAME + "/avaliacoes";
    static public final Uri CONTENT_URI_ESCOLAS = Uri.parse(URL_ESCOLAS);
    static public final Uri CONTENT_URI_AVALIACOES = Uri.parse(URL_AVALIACOES);

    private static HashMap<String, String> ESCOLAS_PROJECTION_MAP;
    private static HashMap<String, String> AVALIACOES_PROJECTION_MAP;

    static final int ESCOLAS = 1;
    static final int AVALIACOES = 2;

    static final UriMatcher uriMatcher;

    static {
        uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);
        uriMatcher.addURI(PROVIDER_NAME, "escolas", ESCOLAS);
        uriMatcher.addURI(PROVIDER_NAME, "avaliacoes",AVALIACOES);
    }

    private SQLiteDatabase database;
    static final String DATABASE_NAME = "EduView.db";
    static final int DATABASE_VERSION = 6;

    static final String ESCOLA_TABLE_NAME = "escolas";
    static final String CREATE_DB_TABLE_ESCOLAS = "create table " + ESCOLA_TABLE_NAME +
            " ("+ Escola.ID_ESCOLA +" integer primary key," +
            Escola.JSON_ESCOLA+" text," +
            EscolaStorageItem.NOTIFICACOES+" integer, " +
            EscolaStorageItem.DATA_ULTIMA_VERIFICACAO+" text )";

    static final String AVALIACOES_TABLE_NAME = "avaliacoes";
    static final String CREATE_DB_TABLE_AVALIACOES = "create table " + AVALIACOES_TABLE_NAME +
            " ( id integer primary key autoincrement, "+
            Avaliacao.JSON + " text)";


    @Override
    public boolean onCreate() {
        Log.i("BDProvider","String de criacao da tabela escola: \n"+CREATE_DB_TABLE_ESCOLAS);
        Log.i("BDProvider","String de criacao da tabela avaliacao: \n"+CREATE_DB_TABLE_AVALIACOES);
        Context context = getContext();
        DatabaseHelper helper = new DatabaseHelper(context);
        database = helper.getWritableDatabase();

        Log.i("BDProvider","BD está ok!");

        return (database==null) ? false : true;
    }

    @Nullable
    @Override
    public String getType(Uri uri) {
        switch (uriMatcher.match(uri)) {
            case ESCOLAS:
                return "vnd.android.cursor.dir/vnd.dspm.dc.ufc.br.eduview.escolas";
            case AVALIACOES:
                return "vnd.android.cursor.dir/vnd.dspm.dc.ufc.br.eduview.avaliacoes";
            default:
                throw new IllegalArgumentException("Unsupported URI: " + uri);
        }
    }

    @Nullable
    @Override
    public Cursor query(Uri uri, String[] projection, String selection, String[] selectionArgs, String sortOrder) {
        SQLiteQueryBuilder queryBuilder = new SQLiteQueryBuilder();

        switch(uriMatcher.match(uri)) {
            case ESCOLAS:
                queryBuilder.setTables(ESCOLA_TABLE_NAME);
                queryBuilder.setProjectionMap(ESCOLAS_PROJECTION_MAP);
                break;
            case AVALIACOES:
                queryBuilder.setTables(AVALIACOES_TABLE_NAME);
                queryBuilder.setProjectionMap(AVALIACOES_PROJECTION_MAP);
                break;
            default:
                throw new IllegalArgumentException("In Query, Unknown URI : " + uri);
        }

        Cursor cursor = queryBuilder.query(database,projection,selection, selectionArgs,null,null,null);
        cursor.setNotificationUri(getContext().getContentResolver(),uri);
        return cursor;
    }

    @Nullable
    @Override
    public Uri insert(Uri uri, ContentValues values) {

        try {
            switch (uriMatcher.match(uri)) {
                case ESCOLAS:
                        long rowIDEsc = database.insertOrThrow(ESCOLA_TABLE_NAME, "", values);

                        if (rowIDEsc > 0) {
                            Uri uriAux = ContentUris.withAppendedId(CONTENT_URI_ESCOLAS, rowIDEsc);
                            getContext().getContentResolver().notifyChange(uriAux, null);
                            return uriAux;
                        }

                    break;
                case AVALIACOES:
                    long rowIDAval = database.insertOrThrow(AVALIACOES_TABLE_NAME, "", values);
                    if (rowIDAval > 0) {
                        Uri uriAux = ContentUris.withAppendedId(CONTENT_URI_AVALIACOES, rowIDAval);
                        getContext().getContentResolver().notifyChange(uriAux, null);
                        return uriAux;
                    }
                    break;
            }
        }catch(SQLiteConstraintException e){
            Log.i("BDProvider",e.getStackTrace().toString());
        }

        return null;
    }

    @Override
    public int delete(Uri uri, String selection, String[] selectionArgs) {
        int count = 0;
        switch (uriMatcher.match(uri)){
            case ESCOLAS:
                count = database.delete(ESCOLA_TABLE_NAME,selection,selectionArgs);
                break;
            case AVALIACOES:
                count = database.delete(AVALIACOES_TABLE_NAME,selection,selectionArgs);
                break;
            default:
                throw new IllegalArgumentException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);

        return count;
    }

    @Override
    public int update(Uri uri, ContentValues values, String selection, String[] selectionArgs) {
        int count = 0;

        switch(uriMatcher.match(uri)){
            case ESCOLAS:
                count = database.update(ESCOLA_TABLE_NAME,values,selection,selectionArgs);
                break;
            default: throw new IllegalArgumentException("Unknown URI "+uri);
        }
        getContext().getContentResolver().notifyChange(uri,null);
        return count;
    }

    private static class DatabaseHelper extends SQLiteOpenHelper {

        DatabaseHelper(Context context) {
            super(context, DATABASE_NAME, null, DATABASE_VERSION);
        }

        @Override
        public void onCreate(SQLiteDatabase database) {
            database.execSQL(CREATE_DB_TABLE_ESCOLAS);
            database.execSQL(CREATE_DB_TABLE_AVALIACOES);
        }

        @Override
        public void onUpgrade(SQLiteDatabase database, int oldVersion, int newVersion) {
            database.execSQL("drop table if exists " + ESCOLA_TABLE_NAME);
            database.execSQL("drop table if exists " + AVALIACOES_TABLE_NAME);
            onCreate(database);
        }
    }
}
