package com.alex.mapnotes.search

import android.content.Intent
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.widget.DividerItemDecoration
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import com.alex.mapnotes.AppExecutors
import com.alex.mapnotes.home.DISPLAY_LOCATION
import com.alex.mapnotes.home.EXTRA_NOTE
import com.alex.mapnotes.R
import com.alex.mapnotes.data.formatter.CoordinateFormatter
import com.alex.mapnotes.data.formatter.LatLonFormatter
import com.alex.mapnotes.data.repository.FirebaseNotesRepository
import com.alex.mapnotes.data.repository.FirebaseUserRepository
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.model.Note
import com.alex.mapnotes.search.adapter.NotesAdapter
import kotlinx.android.synthetic.main.fragment_search_notes.view.*

class SearchNotesFragment : Fragment(), SearchNotesView {
    private lateinit var adapter: NotesAdapter
    private val coordinateFormatter: LatLonFormatter by lazy { CoordinateFormatter() }
    private val userRepository: UserRepository by lazy { FirebaseUserRepository(appExecutors) }
    private val appExecutors: AppExecutors by lazy { AppExecutors() }
    private val notesRepository: NotesRepository by lazy { FirebaseNotesRepository(appExecutors) }
    private val presenter: SearchNotesMvpPresenter by lazy {
        SearchNotesPresenter(this.context!!, userRepository, notesRepository, appExecutors)
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {

        val rootView = inflater.inflate(R.layout.fragment_search_notes, container, false)
        rootView.searchOptions.adapter = ArrayAdapter.createFromResource(
                activity,
                R.array.search_options,
                android.R.layout.simple_dropdown_item_1line)
        adapter = NotesAdapter(coordinateFormatter) {
            val broadcastManager = LocalBroadcastManager.getInstance(this.context!!)
            val intent = Intent(DISPLAY_LOCATION)
            intent.apply {
                putExtra(EXTRA_NOTE, it)
            }
            broadcastManager.sendBroadcast(intent)
        }
        val layoutManager = LinearLayoutManager(activity)
        rootView.recyclerView.layoutManager = layoutManager
        rootView.recyclerView.addItemDecoration(
                DividerItemDecoration(rootView.recyclerView.context, layoutManager.orientation))
        rootView.recyclerView.adapter = adapter

        rootView.searchButton.setOnClickListener {
            presenter.searchNotes(rootView.searchText.text.toString(), rootView.searchOptions.selectedItemPosition)
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getNotes()
    }

    override fun clearSearchResults() {
        adapter.clear()
    }

    override fun displayNote(note: Note) {
        adapter.addNote(note)
    }

    override fun onStop() {
        presenter.toString()
        super.onStop()
    }
}
