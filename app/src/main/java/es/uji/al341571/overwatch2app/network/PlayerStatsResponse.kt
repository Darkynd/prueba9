package es.uji.al341571.overwatch2app.network

import com.squareup.moshi.Json

class PlayerStatsResponse(
    val general: General
)

data class General(
    val average: Average,
    @Json(name = "games_lost") val gamesLost: Int,
    @Json(name = "games_played") val gamesPlayed: Int,
    @Json(name = "games_won") val gamesWon: Int,
    val kda: Double,
    @Json(name = "time_played") val timePlayed: Long,
    val total: Total,
    val winrate: Double
)

data class Average(
    val assists: Double,
    val damage: Double,
    val deaths: Double,
    val eliminations: Double,
    val healing: Double
)

data class Total(
    val assists: Int,
    val damage: Long,
    val deaths: Int,
    val eliminations: Int,
    val healing: Long
)