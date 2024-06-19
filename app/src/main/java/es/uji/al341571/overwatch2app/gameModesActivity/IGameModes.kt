package es.uji.al341571.overwatch2app.gameModesActivity

import es.uji.al341571.overwatch2app.classes.GameMode

interface IGameModes {
    fun showGameModeData(gameMode: GameMode)
    fun showSearchError(error: Throwable)
}