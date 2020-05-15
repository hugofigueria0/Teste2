package pt.ipg.livros;

import android.content.Context;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.test.platform.app.InstrumentationRegistry;
import androidx.test.ext.junit.runners.AndroidJUnit4;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;

import static org.junit.Assert.*;

/**
 * Instrumented test, which will execute on an Android device.
 *
 * @see <a href="http://d.android.com/tools/testing">Testing documentation</a>
 */
@RunWith(AndroidJUnit4.class)
public class BdLivrosTest {
    @Before
    public void apagaBaseDados() {
        getTargetContext().deleteDatabase(BdLivrosOpenHelper.NOME_BASE_DADOS);
    }

    @Test
    public void consegueAbrirBaseDados() {
        // Context of the app under test.
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getReadableDatabase();
        assertTrue(bdLivros.isOpen());
        bdLivros.close();
    }

    private Context getTargetContext() {
        return InstrumentationRegistry.getInstrumentation().getTargetContext();
    }

    @Test
    public void consegueInserirCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        Categoria categoria = new Categoria();
        categoria.setDescricao("Ação");

        long id = tabelaCategorias.insert(Converte.categoriaToContentValues(categoria));
        assertNotEquals(-1, id);

        bdLivros.close();
    }

    @Test
    public void consegueLerCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        Cursor cursor = tabelaCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        int registos = cursor.getCount();
        cursor.close();

        Categoria categoria = new Categoria();
        categoria.setDescricao("Sci-fi");

        long id = tabelaCategorias.insert(Converte.categoriaToContentValues(categoria));
        assertNotEquals(-1, id);

        cursor = tabelaCategorias.query(BdTableCategorias.TODOS_CAMPOS, null, null, null, null, null);
        assertEquals(registos + 1, cursor.getCount());
        cursor.close();

        bdLivros.close();
    }

    @Test
    public void consegueAtualizarCategorias() {
        Context appContext = getTargetContext();

        BdLivrosOpenHelper openHelper = new BdLivrosOpenHelper(appContext);
        SQLiteDatabase bdLivros = openHelper.getWritableDatabase();

        BdTableCategorias tabelaCategorias = new BdTableCategorias(bdLivros);

        Categoria categoria = new Categoria();
        categoria.setDescricao("Romanc");

        long id = tabelaCategorias.insert(Converte.categoriaToContentValues(categoria));
        assertNotEquals(-1, id);

        categoria.setDescricao("Romance");
        int registosAfetados = tabelaCategorias.update(Converte.categoriaToContentValues(categoria), BdTableCategorias._ID + "=?", new String[]{String.valueOf(id)});
        assertEquals(1, registosAfetados);

        bdLivros.close();
    }
}
