package pt.ipg.livros;

import android.os.Bundle;

import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.view.Menu;
import android.view.MenuItem;

public class MainActivity extends AppCompatActivity {
    private Fragment fragmentActual = null;
    private int menuActual = R.menu.menu_lista_livros;

    public void setFragmentActual(Fragment fragmentActual) {
        this.fragmentActual = fragmentActual;
    }

    public void setMenuActual(int menuActual) {
        if (menuActual != this.menuActual) {
            this.menuActual = menuActual;
            invalidateOptionsMenu();
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(menuActual, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        } else if (menuActual == R.menu.menu_lista_livros) {
            if (processaOpcoesMenuListaLivros(id)) return true;
        } else if (menuActual == R.menu.menu_inserir_livro) {
            if (processaOpcoesMenuInserirLivro(id)) return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean processaOpcoesMenuInserirLivro(int id) {
        AdicionarLivroFragment adicionarLivroFragment = (AdicionarLivroFragment) fragmentActual;

        if (id == R.id.action_guardar) {
            adicionarLivroFragment.guardar();
            return true;
        } else if (id == R.id.action_cancelar) {
            adicionarLivroFragment.cancelar();
            return true;
        }

        return false;
    }

    private boolean processaOpcoesMenuListaLivros(int id) {
        ListaLivrosFragment listaLivrosFragment = (ListaLivrosFragment) fragmentActual;

        if (id == R.id.action_inserir_livro) {
            listaLivrosFragment.novoLivro();
            return true;
        } else if (id == R.id.action_alterar_livro) {
            listaLivrosFragment.alteraLivro();
            return true;
        } else if (id == R.id.action_eliminar_livro) {
            listaLivrosFragment.eliminaLivro();
            return true;
        }

        return false;
    }
}
