package com.example.icarchecking.view.adapter

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AnimationUtils
import androidx.recyclerview.widget.RecyclerView
import com.example.icarchecking.R
import com.example.icarchecking.databinding.ItemHistoryBinding
import com.example.icarchecking.view.api.model.HistoryInfoModelRes
import com.example.icarchecking.view.callback.OnActionCallBack

class GroupAdapter(var context: Context, var listData: List<String>) :
    RecyclerView.Adapter<GroupAdapter.GroupHolder>(), View.OnClickListener {
    lateinit var callBack: OnActionCallBack
    private val status = BooleanArray(listData.size)
    val detailHistory = HashMap<String, HistoryInfoModelRes>()

    companion object {
        const val KEY_GET_DETAIL_HISTORY = "KEY_GET_DETAIL_HISTORY"
    }

    class GroupHolder(view: View) : RecyclerView.ViewHolder(view) {
        var binding: ItemHistoryBinding = ItemHistoryBinding.bind(view)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): GroupHolder {
        val view = LayoutInflater.from(context).inflate(R.layout.item_history, parent, false)
        return GroupHolder(view)
    }

    override fun onBindViewHolder(holder: GroupHolder, position: Int) {
        holder.binding.tvDate.text = listData[position]
        holder.binding.ivShow.tag = position

        holder.binding.ivReload.setOnClickListener(this)
        holder.binding.ivShow.setOnClickListener(this)
        holder.binding.ivTimeline.setOnClickListener(this)

        holder.binding.rvDetailHistory.visibility =
            if (!status[position]) View.GONE else View.VISIBLE
        holder.binding.ivReload.visibility =
            if (!status[position]) View.GONE else View.VISIBLE
        holder.binding.ivTimeline.visibility =
            if (!status[position]) View.GONE else View.VISIBLE
        holder.binding.ivShow.setImageLevel(if (!status[position]) 0 else 1)

        if (holder.binding.rvDetailHistory.visibility == View.VISIBLE) {
            showDetailHistory(listData[position])
        }
        showListDetail(listData[position], holder)
    }

    private fun showListDetail(day: String, holder: GroupHolder) {
        if (!detailHistory.containsKey(day)) return
        //Tao adapter cho RecycleView
        //Set adapter
    }

    private fun showDetailHistory(data: String) {
        if (detailHistory.containsKey(data)) return
        callBack.callBack(KEY_GET_DETAIL_HISTORY, data)
    }

    override fun getItemCount(): Int {
        return listData.size
    }

    override fun onClick(v: View?) {
        v!!.startAnimation(
            AnimationUtils.loadAnimation(
                context,
                androidx.appcompat.R.anim.abc_fade_in
            )
        )

        if (v.id == R.id.iv_show) {
            val pos = v.tag as Int
            status[pos] = !status[pos]
            notifyDataSetChanged()
        }
    }

    fun setDetailHistory(data: HistoryInfoModelRes, day: String) {
        detailHistory[day] = data
        notifyDataSetChanged()
    }
}