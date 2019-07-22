package com.jsongo.mybasefrm.view

import android.os.Bundle
import com.esri.arcgisruntime.layers.FeatureLayer
import com.esri.arcgisruntime.loadable.LoadStatus
import com.esri.arcgisruntime.mapping.ArcGISMap
import com.esri.arcgisruntime.mapping.Basemap
import com.esri.arcgisruntime.portal.Portal
import com.esri.arcgisruntime.portal.PortalItem
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
            val latitude = 34.09042//31.896261
            val longitude = -118.71511//120.573642
            val levelOfDetail = 15
            val map = ArcGISMap(baseMapType, latitude, longitude, levelOfDetail)
            it.map = map
            addLayer(map)
        }
    }

    private fun addLayer(map: ArcGISMap) {

        val itemId = "2e4b3df6ba4b44969a3bc9827de746b3"
        val portal = Portal("http://www.arcgis.com")
        val portalItem = PortalItem(portal, itemId)
        val featureLayer = FeatureLayer(portalItem, 0)
        featureLayer.addDoneLoadingListener {
            if (featureLayer.loadStatus == LoadStatus.LOADED) {
                map.operationalLayers.add(featureLayer)
            }
        }
        featureLayer.loadAsync()
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
