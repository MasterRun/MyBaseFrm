package com.jsongo.mybasefrm.view

import android.os.Bundle
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.jsongo.mybasefrm.BaseActivity
import com.jsongo.mybasefrm.R
import kotlinx.android.synthetic.main.activity_arcgis_demo.*
import kotlinx.android.synthetic.main.activity_base.*

class ArcgisDemoActivity : BaseActivity() {

    override var mainLayoutId = R.layout.activity_arcgis_demo
    override var useContainer2: Boolean = true

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        topbar.setTitle("Arcgis")
        smart_refresh_layout.isEnabled = false
        setSwipeBackEnable(false)

        setupMap()
    }

    private fun setupMap() {
        mapview?.let {

            val baseMapType = Basemap.Type.STREETS_VECTOR
            val latitude = 31.896261
            val longitude = 120.573642
            val levelOfDetail = 15
            val map = ArcGISMap(baseMapType, latitude, longitude, levelOfDetail)
            it.map = map
        }
    }

    override fun onResume() {
        super.onResume()
        mapview?.resume()
    }

    override fun onPause() {
        super.onPause()
        mapview?.pause()
    }

    override fun onDestroy() {
        mapview?.dispose()
        super.onDestroy()
    }
}
