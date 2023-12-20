package kr.ac.kumoh.ce.s20180904.s23termproject

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.launch
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class WordViewModel() : ViewModel() {
    private val SERVER_URL = "https://port-0-s23termbackend-5yc2g32mlomito0q.sel5.cloudtype.app/"
    private val wordApi: WordApi
    private val _wordList = MutableLiveData<List<Word>>()
    val wordList: LiveData<List<Word>>
        get() = _wordList

    init {
        val retrofit = Retrofit.Builder()
            .baseUrl(SERVER_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()

        wordApi = retrofit.create(WordApi::class.java)
        fetchData()
    }

    private fun fetchData() {
        // Coroutine 사용
        viewModelScope.launch {
            try {
                val response = wordApi.getwordLIst()
                _wordList.value = response
            } catch (e: Exception) {
                Log.e("fetchData()", e.toString())
            }
        }
    }
}