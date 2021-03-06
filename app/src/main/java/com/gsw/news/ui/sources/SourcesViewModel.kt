package com.gsw.news.ui.sources

import android.util.Log
import androidx.lifecycle.MutableLiveData
import com.gsw.news.base.BaseViewModel
import com.gsw.news.models.response.SourcesItem
import com.gsw.news.other.Resource
import com.gsw.news.repo.callGetSources

class SourcesViewModel : BaseViewModel() {

    val dataGetSources: MutableLiveData<List<SourcesItem>> by lazy { MutableLiveData() }

    fun getSources(query: Map<String, String>) {
        launch {
            callGetSources(query = query)
                .doOnSubscribe { loading.value = true }
                .doOnTerminate { loading.value = false }
                .subscribe({
                    Log.d("Ganang", "getSources: $it")
                    if (it.isSuccessful) {
                        it.body()?.apply {
                            if (status == "ok") {
                                if (!sources.isNullOrEmpty()) {
                                    dataGetSources.value = sources
                                } else {
                                    error.value = "Data Empty"
                                }
                            } else {
                                error.value = "Error"
                            }
                        }
                    } else if (it.code() == 429) {
                        error.value = "Harap coba lagi nanti"
                    } else {
                        error.value = it.message()
                    }
                }, {
                    error.value = it.message
                })
        }
    }

}