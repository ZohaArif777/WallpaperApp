package com.wallpaper.base_app.localization


import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.getDrawable
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.RecyclerView.Adapter
import com.wallpaper.R
import java.util.*

class LocaleAdapter(
    val context: Context,
    private var localeList: ArrayList<LocaleModel>,
    private var listener: OnItemsClickListener1
) :
    Adapter<LocaleAdapter.ViewHolder>() {

    private var selectedItemPosition: Int = RecyclerView.NO_POSITION
    private var filteredList: ArrayList<LocaleModel> = localeList

    interface OnItemsClickListener1 {
        fun onItemClick(pos: Int)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item_localization, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        if (position < 0 || position >= localeList.size) {
            return
        }
        if (localeList[position].isSelected) {
            holder.imgChk.setImageDrawable(getDrawable(context,R.drawable.selected))
        } else {
            holder.imgChk.setImageDrawable(getDrawable(context, R.drawable.unselected))
        }
        holder.itemView.setOnClickListener {
            selectedItemPosition = position
            for (item in localeList) {
                item.isSelected = false
            }
            localeList[position].isSelected = true
            listener.onItemClick(position)
            notifyDataSetChanged()
        }
        holder.bind(localeList[position])
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val imgChk: ImageView = itemView.findViewById(R.id.item_state)
        val language:TextView=itemView.findViewById(R.id.language_name)
        fun bind(localeModel:LocaleModel) {
            language.text=localeModel.language
        }
    }

    data class LocaleModel(
        var language: String,
        var langCode: String,
        var isSelected: Boolean
    )
}