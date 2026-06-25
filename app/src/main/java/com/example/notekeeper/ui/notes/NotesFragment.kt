package com.example.notekeeper.ui.notes

import android.os.Bundle
import android.view.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.lifecycle.Lifecycle
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.notekeeper.NoteKeeperApplication
import com.example.notekeeper.R
import com.example.notekeeper.databinding.FragmentNotesBinding

class NotesFragment : Fragment() {

    private var _binding: FragmentNotesBinding? = null
    private val binding get() = _binding!!

    private val viewModel: NoteViewModel by viewModels {
        NoteViewModelFactory((requireActivity().application as NoteKeeperApplication).repository)
    }

    private lateinit var adapter: NotesAdapter

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View {
        _binding = FragmentNotesBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupFab()
        setupMenu()
        observeNotes()
    }

    private fun setupRecyclerView() {
        adapter = NotesAdapter(
            onNoteClick = { note ->
                val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                    noteId = note.id, title = getString(R.string.edit_note)
                )
                findNavController().navigate(action)
            },
            onNoteLongClick = { note ->
                AlertDialog.Builder(requireContext())
                    .setTitle(R.string.delete_note)
                    .setMessage(R.string.delete_confirm)
                    .setPositiveButton(R.string.delete) { _, _ -> viewModel.deleteNoteById(note.id) }
                    .setNegativeButton(R.string.cancel, null)
                    .show()
                true
            }
        )
        binding.rvNotes.apply {
            this.adapter = this@NotesFragment.adapter
            layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            setHasFixedSize(false)
        }
    }

    private fun setupFab() {
        binding.fabAddNote.setOnClickListener {
            val action = NotesFragmentDirections.actionNotesFragmentToAddEditNoteFragment(
                noteId = -1, title = getString(R.string.new_note)
            )
            findNavController().navigate(action)
        }
    }

    private fun setupMenu() {
        val menuHost: MenuHost = requireActivity()
        menuHost.addMenuProvider(object : MenuProvider {
            override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
                menuInflater.inflate(R.menu.menu_notes, menu)
                val searchView = menu.findItem(R.id.action_search).actionView as SearchView
                searchView.queryHint = getString(R.string.search_hint)
                searchView.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
                    override fun onQueryTextSubmit(query: String?) = false
                    override fun onQueryTextChange(newText: String?): Boolean {
                        viewModel.setSearchQuery(newText.orEmpty())
                        return true
                    }
                })
            }
            override fun onMenuItemSelected(menuItem: MenuItem) = false
        }, viewLifecycleOwner, Lifecycle.State.RESUMED)
    }

    private fun observeNotes() {
        viewModel.notes.observe(viewLifecycleOwner) { notes ->
            adapter.submitList(notes)
            binding.layoutEmpty.visibility = if (notes.isEmpty()) View.VISIBLE else View.GONE
            binding.rvNotes.visibility = if (notes.isEmpty()) View.GONE else View.VISIBLE
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}
