package com.example.icarchecking.view.fragment

import android.util.Log
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.icarchecking.R
import com.example.icarchecking.databinding.FrgM008HistoryBinding
import com.example.icarchecking.view.adapter.GroupAdapter
import com.example.icarchecking.view.api.model.GroupDayModelRes
import com.example.icarchecking.view.api.model.HistoryInfoModelRes
import com.example.icarchecking.view.api.model.entities.CarInfoEntity
import com.example.icarchecking.view.callback.OnActionCallBack
import com.example.icarchecking.view.viewmodel.M008HistoryViewModel

class M008HistoryFrg : BaseFragment<FrgM008HistoryBinding, M008HistoryViewModel>() {
    lateinit var day: String

    companion object {
        val TAG: String = M008HistoryFrg::class.java.name
    }

    override fun initViews() {
        if (mData != null) {
            val carInfo = mData as CarInfoEntity
            mViewModel.getGroupDay(carInfo.id!!)
        }
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                callBack?.showFrg(TAG, M003MenuFrg.TAG, false)
            }
        }
    }

    override fun callBack(key: String, data: Any?) {
        super.callBack(key, data)
        if (key == M008HistoryViewModel.API_KEY_GET_GROUP_DAY) {
            showListGroupDay(data)
        } else if (key == M008HistoryViewModel.API_KEY_GET_HISTORY) {
            showListHistoryInDay(data)
        }
    }

    private fun showListHistoryInDay(data: Any?) {
        if (data == null) return
        Log.i(TAG, "showListHistoryInDay: " + (data as HistoryInfoModelRes).data?.size)
        (binding?.rvHistory?.adapter as GroupAdapter).setDetailHistory(data, day)
    }

    private fun showListGroupDay(data: Any?) {
        if (data == null) return
        Log.i(TAG, "showListGroupDay: " + (data as GroupDayModelRes).data?.size)
        binding?.rvHistory?.layoutManager = LinearLayoutManager(mContext)
        binding?.rvHistory?.adapter = GroupAdapter(mContext, data.data!!)
        (binding?.rvHistory?.adapter as GroupAdapter).callBack = object : OnActionCallBack {
            override fun callBack(key: String, data: Any?) {
                day = data.toString()
                if (key == GroupAdapter.KEY_GET_DETAIL_HISTORY) {
                    mViewModel.getHistory(
                        (mData as CarInfoEntity).id!!,
                        "00:00 $data",
                        "23:59 $data"
                    )
                }
            }
        }
    }


    override fun initBinding(mRootView: View): FrgM008HistoryBinding {
        return FrgM008HistoryBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<M008HistoryViewModel> {
        return M008HistoryViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m008_history
    }
}
