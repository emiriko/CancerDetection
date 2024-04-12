package com.dicoding.asclepius.view.News;

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.liveData
import androidx.lifecycle.viewModelScope
import com.dicoding.asclepius.data.remote.response.ArticlesItem
import com.dicoding.asclepius.data.remote.service.APIConfig
import com.dicoding.asclepius.helper.Result
import kotlinx.coroutines.launch

class NewsViewModel: ViewModel() {
    
    private val _news = MutableLiveData<Result<List<ArticlesItem?>?>>()
    
    val news: LiveData<Result<List<ArticlesItem?>?>> 
        get() = _news
    
    init {
        getNews()
    }
    
    private fun getNews() { 
        viewModelScope.launch { 
            _news.postValue(Result.Loading)
            val response = APIConfig.getApiService().getTopHeadlines(COUNTRY_ID)
            if (response.status == "ok" && response.totalResults != 0) {
                _news.postValue(Result.Success(response.articles))
            } else {
                _news.postValue(Result.Error("Failed to get data"))
            }        
        
        }
    }
    
    companion object {
        const val COUNTRY_ID = "us"
    }
}
