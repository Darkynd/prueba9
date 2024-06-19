package es.uji.al341571.overwatch2app.mapsActivity

import es.uji.al341571.overwatch2app.classes.Map

interface IMaps {
    fun showMapData(map: Map)
    fun showSearchError(error: Throwable)
}