package es.uji.al341571.overwatch2app.mapsActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.overwatch2app.classes.Map
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.network.OverwatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class MapsViewModel: ViewModel() {

    var view: IMaps? = null
        set(value) {
            field = value
            if(value != null)
                map?.let { displayMap(it) }
        }

    private var map: Map? = null
        set(value) {
            field = value
            if(value != null)
                displayMap(value)
        }

    private fun displayMap(map: Map) {
        view?.showMapData(map)
    }

    private var _mapsList = MutableLiveData<List<MapListItem>>()
    val mapsList: LiveData<List<MapListItem>> get() = _mapsList

    fun fetchMapList() {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getMapsList()

                if (result.isSuccess) {
                    _mapsList.value = result.getOrNull() ?: emptyList()
                    // Aquí puedes realizar alguna operación adicional si es necesario, como imprimir la lista
                    // printMapsList(result.getOrNull())
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getMapsList()
                view?.showSearchError(e)
            }
        }
    }

    private fun printMapsList(result: List<MapListItem>) {
        // Imprimir la lista por consola para verificar
        result.forEach { map ->
            println("Map name: ${map.name}, " +
                    "Screenshot: ${map.screenshot}, " +
                    "Gamemodes: ${map.gamemodesString}, " +
                    "Location: ${map.location}")
        }
    }
}