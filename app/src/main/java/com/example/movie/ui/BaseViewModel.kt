package com.example.movie.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel

open class BaseViewModel : ViewModel() {
    protected val _errorState = MutableLiveData("")
    val errorState: LiveData<String> = _errorState
}