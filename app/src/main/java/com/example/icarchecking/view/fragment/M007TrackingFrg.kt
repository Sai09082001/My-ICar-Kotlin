package com.example.icarchecking.view.fragment

import android.view.View
import com.example.icarchecking.R
import com.example.icarchecking.databinding.FrgM007TrackingBinding
import com.example.icarchecking.view.MapManager
import com.example.icarchecking.view.api.model.entities.CarInfoEntity
import com.example.icarchecking.view.viewmodel.CommonViewModel
import com.google.android.gms.maps.SupportMapFragment

class M007TrackingFrg : BaseFragment<FrgM007TrackingBinding, CommonViewModel>() {
    companion object {
        val TAG: String = M007TrackingFrg::class.java.name
    }

    override fun initViews() {
        val mapFrg = childFragmentManager.findFragmentById(R.id.frg_map) as SupportMapFragment
        mapFrg.getMapAsync {
            MapManager.getInstance().mMapTracking = it
            MapManager.getInstance().initMap(MapManager.getInstance().mMapTracking)

            if (mData != null) {
                val carInfo = mData as CarInfoEntity
                binding?.includeActionbar?.tvTitle?.text = carInfo.carNumber
                MapManager.getInstance().showCarTracking(carInfo)
            }
        }
    }

    override fun doClickView(v: View?) {
        when (v?.id) {
            R.id.iv_back -> {
                callBack?.showFrg(TAG, M003MenuFrg.TAG, false)
            }
        }
    }

    override fun initBinding(mRootView: View): FrgM007TrackingBinding {
        return FrgM007TrackingBinding.bind(mRootView)
    }

    override fun getViewModelClass(): Class<CommonViewModel> {
        return CommonViewModel::class.java
    }

    override fun getLayoutId(): Int {
        return R.layout.frg_m007_tracking
    }
}
