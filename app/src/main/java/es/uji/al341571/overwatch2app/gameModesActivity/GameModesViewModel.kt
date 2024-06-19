package es.uji.al341571.overwatch2app.gameModesActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.overwatch2app.classes.GameMode
import es.uji.al341571.overwatch2app.classes.GameModeListItem
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.network.OverwatchRepository
import kotlinx.coroutines.launch

class GameModesViewModel: ViewModel() {

    var view: IGameModes? = null
        set(value) {
            field = value
            if(value != null)
                gameMode?.let { displayGameMode(it) }
        }

    private var gameMode: GameMode? = null
        set(value) {
            field = value
            if(value != null)
                displayGameMode(value)
        }

    private var currentGamemode: String = ""

    fun setCurrentGameMode(name: String) {
        currentGamemode = name
    }

    private fun displayGameMode(gameMode: GameMode) {
        view?.showGameModeData(gameMode)
    }

    private var _gameModesList = MutableLiveData<List<GameModeListItem>>()
    val gameModesList: LiveData<List<GameModeListItem>> get() = _gameModesList

    fun fetchGameModesList() {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getGameModesList()

                if (result.isSuccess) {
                    _gameModesList.value = result.getOrNull() ?: emptyList()
                    // Aquí puedes realizar alguna operación adicional si es necesario, como imprimir la lista
                    // printGameModesList(result.getOrNull())
                    updateGameModeObject(currentGamemode)
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getGameModesList()
                view?.showSearchError(e)
            }
        }
    }

    private var _mapsListByGameMode = MutableLiveData<List<MapListItem>>()
    val mapsListByGameMode: LiveData<List<MapListItem>> get() = _mapsListByGameMode

    fun fetchMapsListByGameMode(gameModeKey: String) {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getMapsListByGameMode(gameModeKey)

                if (result.isSuccess) {
                    _mapsListByGameMode.value = result.getOrNull() ?: emptyList()
                    // Aquí puedes realizar alguna operación adicional si es necesario, como imprimir la lista
                    // printMapsListByGameMode(result.getOrNull())
                    updateGameModeObject(currentGamemode)
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getMapsListByGameMode()
                view?.showSearchError(e)
            }
        }
    }

    private fun updateGameModeObject(currentGamemode: String) {
        gameModesList.value?.firstOrNull { it.key == currentGamemode }?.let { gameModeListItem ->
            gameMode = GameMode(
                gameModeListItem.key,
                gameModeListItem.name,
                gameModeListItem.icon,
                gameModeListItem.description,
                _mapsListByGameMode.value ?: emptyList()
            )
        }
    }

    private fun printGameModesList(result: List<GameModeListItem>) {
        // Imprimir la lista por consola para verificar
        result.forEach { gamemode ->
            println("Gamemode key: ${gamemode.key}, " +
                    "Gamemode name: ${gamemode.name}, " +
                    "Gamemode icon: ${gamemode.icon}, " +
                    "Gamemode description: ${gamemode.description}")
        }
    }

    private fun printMapsListByGameMode(result: List<MapListItem>) {
        // Imprimir la lista por consola para verificar
        result.forEach { map ->
            println("map name: ${map.name}, " +
                    "map screenshot: ${map.screenshot}, " +
                    "map gamemodesString: ${map.gamemodesString}, " +
                    "map location: ${map.location}")
        }
    }
}