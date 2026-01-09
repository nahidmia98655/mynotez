package com.example.mynotez.ui;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.Navigation;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import com.example.mynotez.R;
import com.example.mynotez.adapter.NoteAdapter;
import com.example.mynotez.databinding.FragmentNoteListBinding;
import com.example.mynotez.viewmodel.NoteViewModel;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

public class NoteListFragment extends Fragment {
    private FragmentNoteListBinding binding;
    private NoteViewModel viewModel;
    private NoteAdapter adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentNoteListBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        adapter = new NoteAdapter(note -> {
            Bundle args = new Bundle();
            args.putInt("noteId", note.getId());
            Navigation.findNavController(view).navigate(R.id.action_noteListFragment_to_addEditNoteFragment, args);
        });
        binding.recyclerView.setLayoutManager(new LinearLayoutManager(requireContext()));
        binding.recyclerView.addItemDecoration(new DividerItemDecoration(requireContext(), DividerItemDecoration.VERTICAL));
        binding.recyclerView.setAdapter(adapter);

        viewModel.getAllNotes().observe(getViewLifecycleOwner(), notes -> adapter.submitList(notes));

        binding.fabAddNote.setOnClickListener(v ->
                Navigation.findNavController(v).navigate(R.id.action_noteListFragment_to_addEditNoteFragment));
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
