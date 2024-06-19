package es.uji.al341571.overwatch2app.playersActivity

import es.uji.al341571.overwatch2app.classes.PlayerStats

interface IPlayers {
    fun showPlayerUserName(userName: String)
    fun showPlayerStatsData(playerStats: PlayerStats)
    fun showSearchError(error: Throwable)
}