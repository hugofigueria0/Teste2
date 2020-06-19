package pt.ipg.livros;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.net.Uri;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.material.snackbar.Snackbar;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link EliminarLivroFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class EliminarLivroFragment extends Fragment {

    private TextView textViewTitulo;
    private TextView textViewCategoria;
    private Livro livro;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_eliminar_livro, container, false);
    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        Context context = getContext();

        MainActivity activity = (MainActivity) getActivity();
        activity.setFragmentActual(this);

        activity.setMenuActual(R.menu.menu_eliminar_livro);

        textViewTitulo = (TextView) view.findViewById(R.id.textViewTitulo);
        textViewCategoria = (TextView) view.findViewById(R.id.textViewCategoria);

        Button buttonEliminar = (Button) view.findViewById(R.id.buttonEliminar);
        buttonEliminar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                eliminar();
            }
        });

        Button buttonCancelar = (Button) view.findViewById(R.id.buttonCancelar);
        buttonCancelar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cancelar();
            }
        });

        livro = activity.getLivro();
        textViewTitulo.setText(livro.getTitulo());
        textViewCategoria.setText(livro.getCategoria());
    }

    public void cancelar() {
        NavController navController = NavHostFragment.findNavController(EliminarLivroFragment.this);
        navController.navigate(R.id.action_eliminarLivroFragment_to_ListaLivrosFragment);
    }

    public void eliminar() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());

        builder.setTitle("Eliminar Livro");
        builder.setMessage("Tem a certeza que pretende eliminar o livro '" + livro.getTitulo() + "'");
        builder.setIcon(R.drawable.ic_baseline_delete_24);
        builder.setPositiveButton("Sim, eliminar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                confirmaEliminar();
            }
        });

        builder.setNegativeButton("Não, cancelar", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                // cancelar
            }
        });

        builder.show();
    }

    private void confirmaEliminar() {
        try {
            Uri enderecoLivro = Uri.withAppendedPath(LivrosContentProvider.ENDERECO_LIVROS, String.valueOf(livro.getId()));

            int apagados = getActivity().getContentResolver().delete(enderecoLivro, null, null);

            if (apagados == 1) {
                Toast.makeText(getContext(), "Livro eliminado com sucesso", Toast.LENGTH_SHORT).show();
                NavController navController = NavHostFragment.findNavController(EliminarLivroFragment.this);
                navController.navigate(R.id.action_eliminarLivroFragment_to_ListaLivrosFragment);
                return;
            }
        } catch (Exception e) {
        }

        Snackbar.make(textViewTitulo, "Erro: Não foi possível eliminar o livro", Snackbar.LENGTH_INDEFINITE).show();
    }
}