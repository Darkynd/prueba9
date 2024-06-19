package es.uji.al341571.overwatch2app.network

import es.uji.al341571.overwatch2app.gameModesActivity.GameModeListItemResponse
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.Path
import retrofit2.http.Query

interface OverwatchAPI {

    @Headers("Accept: application/json")
    @GET("heroes/{hero_key}")
    suspend fun getHero(@Path("hero_key") name: String): HeroResponse

    @Headers("Accept: application/json")
    @GET("heroes")
    suspend fun getHeroList(): List<HeroListItemResponse>

    @Headers("Accept: application/json")
    @GET("roles")
    suspend fun getRolesList(): List<RoleListItemResponse>

    @Headers("Accept: application/json")
    @GET("heroes")
    suspend fun getHeroListByRole(@Query("role") role: String): List<HeroListItemResponse>

    @Headers("Accept: application/json")
    @GET("maps")
    suspend fun getMapsList(): List<MapListItemResponse>

    @Headers("Accept: application/json")
    @GET("gamemodes")
    suspend fun getGameModesList(): List<GameModeListItemResponse>

    @Headers("Accept: application/json")
    @GET("maps")
    suspend fun getMapsListByGameMode(@Query("gamemode") gamemode: String): List<MapListItemResponse>

    @Headers("Accept: application/json")
    @GET("players/{player_id}/stats/summary")
    suspend fun getPlayerStats(@Path("player_id") name: String): PlayerStatsResponse
}