package es.uji.al341571.overwatch2app.heroesActivity

import es.uji.al341571.overwatch2app.classes.Hero

interface IHeroes {
    fun showHeroData(hero: Hero)
    fun showSearchError(error: Throwable)
}