package com.example.mynotez.ui;

import android.os.Bundle;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.ViewModelProvider;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;
import com.example.mynotez.R;
import com.example.mynotez.databinding.FragmentAddEditNoteBinding;
import com.example.mynotez.model.Note;
import com.example.mynotez.viewmodel.NoteViewModel;
import com.google.android.material.snackbar.Snackbar;

public class AddEditNoteFragment extends Fragment {
    private FragmentAddEditNoteBinding binding;
    private NoteViewModel viewModel;
    private int noteId = -1;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = FragmentAddEditNoteBinding.inflate(inflater, container, false);
        return binding.getRoot();
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        viewModel = new ViewModelProvider(this).get(NoteViewModel.class);
        NavController navController = Navigation.findNavController(view);
        Bundle args = getArguments();
        if (args != null && args.containsKey("noteId")) {
            noteId = args.getInt("noteId", -1);
            viewModel.getNoteById(noteId).observe(getViewLifecycleOwner(), note -> {
                if (note != null) {
                    binding.editTitle.setText(note.getTitle());
                    binding.editContent.setText(note.getContent());
                }
            });
        }

        binding.btnSave.setOnClickListener(v -> {
            String title = binding.editTitle.getText().toString().trim();
            String content = binding.editContent.getText().toString().trim();
            if (TextUtils.isEmpty(title) && TextUtils.isEmpty(content)) {
                Snackbar.make(v, "Note cannot be empty", Snackbar.LENGTH_SHORT).show();
                return;
            }
            long timestamp = System.currentTimeMillis();
            if (noteId == -1) {
                // New note
                Note newNote = new Note(title, content, timestamp);
                viewModel.insert(newNote);
            } else {
                // Update existing
                Note updated = new Note(title, content, timestamp);
                updated.setId(noteId);
                viewModel.update(updated);
            }
            navController.popBackStack();
        });
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
        binding = null;
    }
}
