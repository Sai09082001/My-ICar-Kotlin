package com.example.icarchecking.view.dialog

import android.content.Context
import android.view.View
import com.example.icarchecking.R
import com.example.icarchecking.databinding.ViewCarInfoBinding
import com.example.icarchecking.view.api.model.entities.CarInfoEntity
import com.example.icarchecking.view.callback.OnActionCallBack
import com.example.icarchecking.view.viewmodel.BaseViewModel

class CarInfoDialog(context: Context, data: CarInfoEntity, callBack: OnActionCallBack) :
    BaseDialog<ViewCarInfoBinding, BaseViewModel, CarInfoEntity>(
        context,
        data,
        R.style.dialog_style_anim
    ) {
    init {
        mCallBack = callBack
    }

    companion object {
        const val KEY_SHOW_TRACKING = "KEY_SHOW_TRACKING"
        const val KEY_SHOW_HISTORY = "KEY_SHOW_HISTORY"
    }

    override fun initViews() {
        mBinding.ivBackPressDialog.setOnClickListener {
            dismiss()
        }

        mBinding.tvCarNameInfo.text = mData?.carBrand
        mBinding.tvCarNumber.text = mData?.carNumber
        mBinding.tvCarStatusInfo.text =
            if (mData?.activeStatus.equals("offline")) "Dừng đỗ" else "Đang di chuyển"
        mBinding.tvCarSpeedInfo.text = mData?.lastSpeed
        mBinding.tvCarLocationInfo.text = mData?.lastAddress
        mBinding.tvMonitorCarBtnInfo.setOnClickListener {
            gotoTrackingScreen(mData)
        }
        mBinding.tvTimeLineBtnInfo.setOnClickListener {
            gotoHistoryScreen(mData)
        }
    }

    private fun gotoHistoryScreen(mData: CarInfoEntity?) {
        mCallBack?.callBack(KEY_SHOW_HISTORY, mData)
        dismiss()
    }

    private fun gotoTrackingScreen(mData: CarInfoEntity?) {
        mCallBack?.callBack(KEY_SHOW_TRACKING, mData)
        dismiss()
    }

    override fun getLayoutId(): Int {
        return R.layout.view_car_info
    }

    override fun initViewBinding(view: View?): ViewCarInfoBinding {
        return ViewCarInfoBinding.bind(view!!)
    }

    fun reload(tag: CarInfoEntity) {
        mData = tag
        initViews()
    }
}