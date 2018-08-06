package com.alex.mapnotes

import com.alex.mapnotes.add.AddNoteMvpPresenter
import com.alex.mapnotes.add.AddNotePresenter
import com.alex.mapnotes.data.formatter.LocationFormatter
import com.alex.mapnotes.data.provider.LocationProvider
import com.alex.mapnotes.data.repository.NotesRepository
import com.alex.mapnotes.data.repository.UserRepository
import com.alex.mapnotes.home.HomeMvpPresenter
import com.alex.mapnotes.home.HomePresenter
import com.alex.mapnotes.login.signin.SignInMvpPresenter
import com.alex.mapnotes.login.signin.SignInPresenter
import com.alex.mapnotes.login.signup.SignUpMvpPresenter
import com.alex.mapnotes.login.signup.SignUpPresenter
import io.mockk.mockk
import org.koin.dsl.module.applicationContext

val testAppModule = applicationContext {

    bean { AppExecutors() }

    bean <UserRepository> { mockk() }

    bean <NotesRepository> { mockk() }

    bean <LocationProvider> { mockk() }

    bean <LocationFormatter> { mockk() }

    factory { SignInPresenter(get(), get()) as SignInMvpPresenter }

    factory { SignUpPresenter(get(), get()) as SignUpMvpPresenter }

    factory { HomePresenter(get(), get()) as HomeMvpPresenter }

    factory { AddNotePresenter(get(), get(), get(), get(), get()) as AddNoteMvpPresenter }
}