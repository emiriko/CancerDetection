package com.dicoding.asclepius.view.News

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.asclepius.databinding.ActivityNewsBinding
import com.dicoding.asclepius.helper.Result
import com.google.android.material.snackbar.Snackbar

class NewsActivity : AppCompatActivity() {
    private lateinit var binding: ActivityNewsBinding
    
    private val newsViewModel by viewModels<NewsViewModel>()
    
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityNewsBinding.inflate(layoutInflater)
        
        setContentView(binding.root)

        newsViewModel.news.observe(this) { result ->
            when (result) {
                is Result.Loading -> {
                    binding.progressBar.visibility = View.VISIBLE
                }
                is Result.Success -> {
                    binding.progressBar.visibility = View.GONE
                    val adapter = NewsAdapter()
                    val newData = result.data?.filter { 
                        it?.urlToImage != null                     
                    }
                    adapter.submitList(newData)
                    binding.rvArticle.adapter = adapter
                }
                is Result.Error -> {
                    binding.progressBar.visibility = View.GONE
                    Snackbar.make(binding.root, result.error, Snackbar.LENGTH_SHORT).show()
                }
            }
        }
        
        val layoutManager = LinearLayoutManager(this)
        binding.rvArticle.layoutManager = layoutManager
        val itemDecoration = DividerItemDecoration(this, layoutManager.orientation)
        binding.rvArticle.addItemDecoration(itemDecoration)
    }
    
}