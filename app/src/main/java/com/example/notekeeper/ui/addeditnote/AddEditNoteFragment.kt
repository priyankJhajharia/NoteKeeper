package com.example.notekeeper.ui.addeditnote

import android.os.Bundle
import android.view.*
import android.widget.Toast
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.navigation.fragment.navArgs
import com.example.notekeeper.NoteKeeperApplication
import com.example.notekeeper.R
import com.example.notekeeper.data.local.Note
import com.example.notekeeper.databinding.FragmentAddEditNoteBinding
import com.example.notekeeper.ui.notes.NoteViewModel
import com.example.notekeeper.ui.notes.NoteViewModelFactory
import kotlinx.coroutines.launch

class AddEditNoteFragment : Fragment() {

    private var _binding: FragmentAddEditNoteBinding? = null
    private val binding get() = _binding!!

    private val args: AddEditNoteFragmentArgs by navArgs()

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteKeeperApplication).repository)
    }

    private var currentNote: Note? = null

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentAddEditNoteBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenu()

        if (args.noteId != -1) {
            lifecycleScope.launch {
                currentNote = viewModel.getNoteById(args.noteId)
                currentNote?.let {
                    binding.etTitle.setText(it.title)
                    binding.etContent.setText(it.content)
                }
            }
        }

        viewModel.navigateBack.observe(viewLifecycleOwner) { shouldNavigate ->
            if (shouldNavigate == true) {
                viewModel.onNavigatedBack()
                findNavController().navigateUp()
            }
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_add_edit_note, menu)
            }
            override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
                return when (menuItem.itemId) {
                    R.id.action_save -> { saveNote(); true }
                    else -> false
                }
            }
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun saveNote() {
        val title = binding.etTitle.text.toString().trim()
        val content = binding.etContent.text.toString().trim()

        if (title.isEmpty()) {
            binding.tilTitle.error = getString(R.string.title_empty_error)
            return
        }
        binding.tilTitle.error = null

        if (currentNote != null) {
            viewModel.updateNote(currentNote!!.copy(title = title, content = content, timestamp = System.currentTimeMillis()))
            Toast.makeText(requireContext(), R.string.note_updated, Toast.LENGTH_SHORT).show()
        } else {
            viewModel.insertNote(Note(title = title, content = content))
            Toast.makeText(requireContext(), R.string.note_saved, Toast.LENGTH_SHORT).show()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
