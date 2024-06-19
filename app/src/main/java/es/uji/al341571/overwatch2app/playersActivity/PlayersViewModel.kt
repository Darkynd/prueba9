package es.uji.al341571.overwatch2app.playersActivity

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.overwatch2app.classes.PlayerStats
import es.uji.al341571.overwatch2app.network.OverwatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class PlayersViewModel:  ViewModel() {

    var view: IPlayers? = null
        set(value) {
            field = value
            if(value != null)
                playerStats?.let { displayPlayerStats(it) }
        }

    private var playerStats: PlayerStats? = null
        set(value) {
            field = value
            if(value != null)
                displayPlayerStats(value)
        }

    private var userNameSuccess: String? = null

    private fun displayPlayerStats(playerStats: PlayerStats) {
        userNameSuccess?.let { view?.showPlayerUserName(it) }
        view?.showPlayerStatsData(playerStats)
    }

    fun onPlayerSearchRequested(playerId: String) {
        viewModelScope.launch(Dispatchers.Main) {
            OverwatchRepository.getPlayerStats(playerId)
                .onSuccess {
                    userNameSuccess = playerId
                    playerStats = it
                }
                .onFailure { view?.showSearchError(it) }
        }
    }
}