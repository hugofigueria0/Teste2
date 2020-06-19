package pt.ipg.livros;

import android.content.Context;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.loader.app.LoaderManager;
import androidx.navigation.NavController;
import androidx.navigation.fragment.NavHostFragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;

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

        livro = activity.getLivro();
        textViewTitulo.setText(livro.getTitulo());
        textViewCategoria.setText(livro.getCategoria());
    }

    public void cancelar() {
        NavController navController = NavHostFragment.findNavController(EliminarLivroFragment.this);
        navController.navigate(R.id.action_eliminarLivroFragment_to_ListaLivrosFragment);
    }

    public void eliminar() {

    }
}