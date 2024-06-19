package es.uji.al341571.overwatch2app.network

class HeroResponse (
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

data class AbilitiesResponse (val name: String)

data class HitpointsResponse (
    val health: Int,
    val armor: Int,
    val shields: Int,
    val total: Int
)
