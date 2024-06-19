package es.uji.al341571.overwatch2app.classes

import es.uji.al341571.overwatch2app.network.AbilitiesResponse
import es.uji.al341571.overwatch2app.network.HitpointsResponse

class Hero (
    val name: String,
    val description: String,
    val portrait: String,
    val role: String,
    val location: String,
    val age: Int,
    val birthday: String?,
    val hitpoints: HitpointsResponse,
    val abilities: List<AbilitiesResponse>
)