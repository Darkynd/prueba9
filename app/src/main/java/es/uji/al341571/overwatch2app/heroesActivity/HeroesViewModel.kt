package es.uji.al341571.overwatch2app.heroesActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.overwatch2app.classes.Hero
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.network.AbilitiesResponse
import es.uji.al341571.overwatch2app.network.OverwatchRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class HeroesViewModel: ViewModel() {

    var view: IHeroes? = null
        set(value) {
            field = value
            if(value != null)
                hero?.let { displayHero(it) }
        }

    private var hero: Hero? = null
        set(value) {
            field = value
            if(value != null)
                displayHero(value)
        }

    private val _heroList = MutableLiveData<List<HeroListItem>>()
    val heroList: LiveData<List<HeroListItem>> get() = _heroList

    init {
        fetchHeroList()
    }

    private fun fetchHeroList() {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getHeroList()

                if (result.isSuccess) {
                    _heroList.value = result.getOrNull() ?: emptyList()
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getHeroList()
                view?.showSearchError(e)
            }
        }
    }

    fun getHeroAbilities(): List<AbilitiesResponse> {
        return hero?.abilities?: emptyList()
    }

    fun getHeroHitpointsArray(): Array<String> {
        val hitpoints = hero?.hitpoints // Suponiendo que este método devuelve un HitpointsResponse
        return arrayOf(
            "Health: ${hitpoints?.health}",
            "Armor: ${hitpoints?.armor}",
            "Shields: ${hitpoints?.shields}",
            "Total: ${hitpoints?.total}"
        )
    }

    fun getHeroRole(): String? {
        return hero?.role
    }

    fun setHeroKey(currentHeroName: String): String? {
        val heroes = _heroList.value

        // Manejando casos particulares
        val normalizedHeroName = when (currentHeroName.lowercase()) {
            "dva", "d va" -> "D.Va"
            "junkerqueen", "junker-queen" -> "Junker Queen"
            "soldier", "soldier 76", "soldier76" -> "Soldier: 76"
            "wrecking-ball", "wreckingball" -> "Wrecking Ball"
            "lucio" -> "Lúcio"
            else -> currentHeroName
        }

        // Buscar el héroe en la lista
        val hero = heroes?.find { it.name.equals(normalizedHeroName, ignoreCase = true) }
        return hero?.key
    }

    private fun displayHero(hero: Hero) {
        view?.showHeroData(hero)
    }

    fun onHeroSearchRequested(heroKey: String) {
        viewModelScope.launch(Dispatchers.Main) {
            OverwatchRepository.getHero(heroKey)
                .onSuccess { hero = it}
                .onFailure { view?.showSearchError(it) }
        }
    }
}