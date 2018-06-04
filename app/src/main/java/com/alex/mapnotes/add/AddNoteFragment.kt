package com.alex.mapnotes.add

import android.location.Geocoder
import android.os.Bundle
import android.support.v4.app.Fragment
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.alex.mapnotes.R
import com.alex.mapnotes.data.formatter.FullAddressFormatter
import com.alex.mapnotes.data.provider.AddressLocationProvider
import com.alex.mapnotes.data.repository.FirebaseAuthRepository
import com.alex.mapnotes.data.repository.FirebaseNotesRepository
import kotlinx.android.synthetic.main.fragment_add_note.*
import kotlinx.android.synthetic.main.fragment_add_note.view.*
import java.util.*

class AddNoteFragment: Fragment(), AddNoteView {
    private val geocoder by lazy { Geocoder(this.context, Locale.getDefault()) }
    private val locationProvider by lazy { AddressLocationProvider(this.context!!) }
    private val locationFormatter by lazy { FullAddressFormatter(geocoder) }
    private val notesRepository by lazy { FirebaseNotesRepository() }
    private val authRepository by lazy { FirebaseAuthRepository() }
    private val presenter by lazy {
        AddNotePresenter(locationProvider,
                         locationFormatter,
                         authRepository,
                         notesRepository)
    }

    override fun onCreateView(inflater: LayoutInflater,
                              container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val rootView = inflater.inflate(R.layout.fragment_add_note, container, false)

        rootView.note.addTextChangedListener(object : TextWatcher {
            override fun afterTextChanged(s: Editable?) {
            }

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                rootView.add.isEnabled = !s.isNullOrEmpty()
            }
        })
        rootView.add.setOnClickListener {
            val text = rootView.note.text.toString()
            if (text.isNotEmpty()) {
                presenter.addNote(text)
            }
        }
        return rootView
    }

    override fun onStart() {
        super.onStart()
        presenter.onAttach(this)
    }

    override fun onResume() {
        super.onResume()
        presenter.getCurrentLocation()
    }

    override fun clearNoteText() {
        note.text.clear()
    }

    override fun displayCurrentLocation(address: String) {
        currentLocation.text = address
    }

    override fun onStop() {
        super.onStop()
        presenter.onDetach()
    }
}