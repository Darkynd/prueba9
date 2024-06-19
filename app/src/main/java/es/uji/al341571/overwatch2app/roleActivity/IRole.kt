package es.uji.al341571.overwatch2app.roleActivity

import es.uji.al341571.overwatch2app.classes.Hero
import es.uji.al341571.overwatch2app.classes.Role

interface IRole {
    fun showRoleData(role: Role)
    fun showSearchError(error: Throwable)
}