package pt.ipg.livros;

import android.content.ContentProvider;
import android.content.ContentValues;
import android.content.UriMatcher;
import android.database.ContentObserver;
import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;
import android.net.Uri;
import android.os.Build;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

public class LivrosContentProvider extends ContentProvider {
    private static final String AUTHORITY = "pt.ipg.livros";
    private static final String CATEGORIAS = "categorias";
    private static final String LIVROS = "livros";

    private static final Uri ENDERECO_BASE = Uri.parse("content://" + AUTHORITY);
    public static final Uri ENDERECO_CATEGORIAS = Uri.withAppendedPath(ENDERECO_BASE, CATEGORIAS);
    public static final Uri ENDERECO_LIVROS = Uri.withAppendedPath(ENDERECO_BASE, LIVROS);

    private static final int URI_CATEGORIAS = 100;
    private static final int URI_ID_CATEGORIA = 101;
    private static final int URI_LIVROS = 200;
    private static final int URI_ID_LIVRO = 201;
    private static final String CURSOR_DIR = "vnd.android.cursor.dir/";
    private static final String CURSOR_ITEM = "vnd.android.cursor.item/";

    private BdLivrosOpenHelper openHelper;

    private UriMatcher getUriMatcher(){
        UriMatcher uriMatcher = new UriMatcher(UriMatcher.NO_MATCH);

        uriMatcher.addURI(AUTHORITY, CATEGORIAS, URI_CATEGORIAS);
        uriMatcher.addURI(AUTHORITY, CATEGORIAS + "/#", URI_ID_CATEGORIA);

        uriMatcher.addURI(AUTHORITY, LIVROS, URI_LIVROS);
        uriMatcher.addURI(AUTHORITY, LIVROS + "/#", URI_ID_LIVRO);

        return uriMatcher;
    }


    @Override
    public boolean onCreate() {
        openHelper = new BdLivrosOpenHelper(getContext());

        return true;
    }


    @Nullable
    @Override
    public Cursor query(@NonNull Uri uri, @Nullable String[] projection, @Nullable String selection, @Nullable String[] selectionArgs, @Nullable String sortOrder) {
        SQLiteDatabase bd = openHelper.getReadableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIAS:
                return new BdTableCategorias(bd).query(projection, selection, selectionArgs, null, null, sortOrder);

            case URI_ID_CATEGORIA:
                return new BdTableCategorias(bd).query(projection, BdTableCategorias._ID + "=?", new String[] { id }, null, null, sortOrder);

            case URI_LIVROS:
                return new BdTableLivros(bd).query(projection, selection, selectionArgs, null, null, sortOrder);

            case URI_ID_LIVRO:
                return new BdTableLivros(bd).query(projection, BdTableLivros._ID + "=?", new String[] { id }, null, null, sortOrder);

            default:
                throw new UnsupportedOperationException("Endereço query inválido: " + uri.getPath());
        }
    }


    @Nullable
    @Override
    public String getType(@NonNull Uri uri) {
        int codigoUri = getUriMatcher().match(uri);

        switch (codigoUri) {
            case URI_CATEGORIAS:
                return CURSOR_DIR + CATEGORIAS;
            case URI_ID_CATEGORIA:
                return CURSOR_ITEM + CATEGORIAS;
            case URI_LIVROS:
                return CURSOR_DIR + LIVROS;
            case URI_ID_LIVRO:
                return CURSOR_ITEM + LIVROS;
            default:
                return null;
        }
    }


    @Nullable
    @Override
    public Uri insert(@NonNull Uri uri, @Nullable ContentValues values) {
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        long id;

        switch (getUriMatcher().match(uri)) {
            case URI_CATEGORIAS:
                id = (new BdTableCategorias(bd).insert(values));
                break;

            case URI_LIVROS:
                id = (new BdTableLivros(bd).insert(values));
                break;

            default:
                throw new UnsupportedOperationException("Endereço insert inválido: " + uri.getPath());
        }

        if (id == -1) {
            throw new SQLException("Não foi possível inserir o registo: " + uri.getPath());
        }

        return Uri.withAppendedPath(uri, String.valueOf(id));
    }


    @Override
    public int delete(@NonNull Uri uri, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_ID_CATEGORIA:
                return new BdTableCategorias(bd).delete(BdTableCategorias._ID + "=?", new String[]{id});

            case URI_ID_LIVRO:
                return new BdTableLivros(bd).delete(BdTableLivros._ID + "=?", new String[] { id });

            default:
                throw new UnsupportedOperationException("Endereço delete inválido: " + uri.getPath());
        }
    }


    @Override
    public int update(@NonNull Uri uri, @Nullable ContentValues values, @Nullable String selection, @Nullable String[] selectionArgs) {
        SQLiteDatabase bd = openHelper.getWritableDatabase();

        String id = uri.getLastPathSegment();

        switch (getUriMatcher().match(uri)) {
            case URI_ID_CATEGORIA:
                return new BdTableCategorias(bd).update(values, BdTableCategorias._ID + "=?", new String[] { id });

            case URI_ID_LIVRO:
                return new BdTableLivros(bd).update(values,BdTableLivros._ID + "=?", new String[] { id });

            default:
                throw new UnsupportedOperationException("Endereço de update inválido: " + uri.getPath());
        }
    }
}
