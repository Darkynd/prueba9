package es.uji.al341571.overwatch2app.roleActivity

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.classes.Role
import es.uji.al341571.overwatch2app.classes.RoleListItem
import es.uji.al341571.overwatch2app.network.OverwatchRepository
import kotlinx.coroutines.launch

class RoleViewModel: ViewModel() {

    var view: IRole? = null
        set(value) {
            field = value
            if (value != null) {
                // Cuando la vista está disponible, se actualiza la UI si la lista de roles ya está cargada
                if (_rolesList.value != null) {
                    updateRoleObject(currentHeroName, currentHeroRole)
                }
            }
        }

    private val _rolesList = MutableLiveData<List<RoleListItem>>()
    val rolesList: LiveData<List<RoleListItem>> get() = _rolesList

    private val _heroListByRole = MutableLiveData<List<HeroListItem>>()
    val heroListByRole: LiveData<List<HeroListItem>> get() = _heroListByRole

    private var currentHeroName: String = ""
    private var currentHeroRole: String = ""

    // Método para establecer las variables currentHeroName y currentHeroRole
    fun setCurrentHero(name: String, role: String) {
        currentHeroName = name
        currentHeroRole = role
    }

    // Método para cargar la lista de roles desde la API
    fun fetchRolesList() {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getRolesList()

                if (result.isSuccess) {
                    _rolesList.value = result.getOrNull() ?: emptyList()
                    updateRoleObject(currentHeroName, currentHeroRole)
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getRolesList()
                view?.showSearchError(e)
            }
        }
    }

    fun fetchHeroListByRole(heroRole: String) {
        viewModelScope.launch {
            try {
                val result = OverwatchRepository.getHeroListByRole(heroRole)

                if (result.isSuccess) {
                    _heroListByRole.value = result.getOrNull() ?: emptyList()
                    updateRoleObject(currentHeroName, currentHeroRole)
                } else {
                    // Handle failure case, if needed
                    result.exceptionOrNull()?.let { view?.showSearchError(it) }
                }
            } catch (e: Exception) {
                // Handle exceptions thrown by OverwatchRepository.getHeroListByRole()
                view?.showSearchError(e)
            }
        }
    }

    // Método para actualizar la UI con la descripción del rol
    private fun updateRoleObject(currentHeroName: String, currentHeroRole: String) {
        val description = getDescription(currentHeroRole)
        val role = Role(currentHeroName, currentHeroRole, description, _heroListByRole.value ?: emptyList())
        view?.showRoleData(role)
    }

    // Método para obtener la descripción del rol
    private fun getDescription(roleName: String): String {
        return when (roleName) {
            "Tank" -> _rolesList.value?.find { it.key == "tank" }?.description ?: ""
            "Damage" -> _rolesList.value?.find { it.key == "damage" }?.description ?: ""
            "Support" -> _rolesList.value?.find { it.key == "support" }?.description ?: ""
            else -> ""
        }
    }
}
