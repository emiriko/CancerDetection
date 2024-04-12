package com.dicoding.asclepius.view.Predictions

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.dicoding.asclepius.data.local.entity.Prediction
import com.dicoding.asclepius.data.local.entity.PredictionEntity
import com.dicoding.asclepius.databinding.PredictionItemBinding

class PredictionAdapter : ListAdapter<PredictionEntity, PredictionAdapter.MyViewHolder>(DIFF_CALLBACK) {
    private lateinit var onItemClickCallback: OnItemClickCallback

    interface OnItemClickCallback {
        fun onItemClicked(data: PredictionEntity)
    }

    fun setOnItemClickCallback(onItemClickCallback: OnItemClickCallback) {
        this.onItemClickCallback = onItemClickCallback
    }
    
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val binding = PredictionItemBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return MyViewHolder(binding)
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        val prediction = getItem(position)
        holder.bind(prediction)
    }

    inner class MyViewHolder(val binding: PredictionItemBinding) : RecyclerView.ViewHolder(binding.root) {
        fun bind(article: PredictionEntity){
            binding.textPrediction.text = if (article.prediction == Prediction.CANCER) "Cancer" else "Non Cancer"
            binding.textConfidence.text = "${article.confidence}"
            Glide.with(binding.root).load(article.imagePath) // URL Gambar
                .into(binding.predictionImage)

            itemView.setOnClickListener {
                onItemClickCallback.onItemClicked(article)
            }
        }
    }

    companion object {
        val DIFF_CALLBACK = object : DiffUtil.ItemCallback<PredictionEntity>() {
            override fun areItemsTheSame(oldItem: PredictionEntity, newItem: PredictionEntity): Boolean {
                return oldItem == newItem
            }
            override fun areContentsTheSame(oldItem: PredictionEntity, newItem: PredictionEntity): Boolean {
                return oldItem == newItem
            }
        }
    }
}