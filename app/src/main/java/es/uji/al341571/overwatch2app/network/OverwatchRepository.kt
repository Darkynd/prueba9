package es.uji.al341571.overwatch2app.network

import com.squareup.moshi.Moshi
import com.squareup.moshi.kotlin.reflect.KotlinJsonAdapterFactory
import es.uji.al341571.overwatch2app.classes.GameModeListItem
import es.uji.al341571.overwatch2app.classes.Hero
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.classes.PlayerStats
import es.uji.al341571.overwatch2app.classes.RoleListItem
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object OverwatchRepository {

    // ***** With CacheAPI ***** //
    private val cachedAPI: CachedAPI

    init {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        val overwatchAPI = Retrofit.Builder()
            .baseUrl("https://overfast-api.tekrop.fr/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OverwatchAPI::class.java)

        cachedAPI = CachedAPI(overwatchAPI)
    }

    suspend fun getHero(heroName: String) = cachedAPI.getHero(heroName.lowercase())
    suspend fun getHeroList() = cachedAPI.getHeroList()
    suspend fun getRolesList() = cachedAPI.getRolesList()
    suspend fun getHeroListByRole(heroRole: String) = cachedAPI.getHeroListByRole(heroRole.lowercase())
    suspend fun getMapsList() = cachedAPI.getMapsList()
    suspend fun getGameModesList() = cachedAPI.getGameModesList()
    suspend fun getMapsListByGameMode(gameModeKey: String) = cachedAPI.getMapsListByGameMode(gameModeKey)
    suspend fun getPlayerStats(playerId: String) = cachedAPI.getPlayerStats(playerId)

    /*private val api: OverwatchAPI

    init {
        val moshi = Moshi.Builder().addLast(KotlinJsonAdapterFactory()).build()
        api = Retrofit.Builder()
            .baseUrl("https://overfast-api.tekrop.fr/")
            .addConverterFactory(MoshiConverterFactory.create(moshi))
            .build()
            .create(OverwatchAPI::class.java)
    }*/
    
    /*suspend fun getHero(heroName: String) = try {
        withContext(Dispatchers.IO) {
            val heroResponse = api.getHero(heroName.lowercase())
            with(heroResponse) {
                Result.success(
                    Hero(
                        name,
                        description,
                        portrait,
                        role,
                        location,
                        age,
                        birthday,
                        hitpoints,
                        abilities)
                )
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }*/

    /*suspend fun getHeroList() = try {
        withContext(Dispatchers.IO) {
            val heroListResponse = api.getHeroList()
            heroListResponse.map { response ->
                HeroListItem(
                    response.key,
                    response.name,
                    response.role
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getRolesList() = try {
        withContext(Dispatchers.IO) {
            val rolesListResponse = api.getRolesList()
            rolesListResponse.map { response ->
                RoleListItem(
                    response.key,
                    response.name,
                    response.icon,
                    response.description,
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getHeroListByRole(heroRole: String) = try {
        withContext(Dispatchers.IO) {
            val heroListByRoleResponses = api.getHeroListByRole(heroRole)
            heroListByRoleResponses.map { response ->
                HeroListItem(
                    response.key,
                    response.name,
                    response.role
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getMapsList() = try {
        withContext(Dispatchers.IO) {
            val mapListItemResponses = api.getMapsList()
            mapListItemResponses.map { response ->
                val gamemodesString = if (response.gamemodes.size > 1) {
                    response.gamemodes.joinToString(", ")
                } else {
                    response.gamemodes.firstOrNull() ?: ""
                }

                MapListItem(
                    response.name,
                    response.screenshot,
                    gamemodesString,  // Aquí asignamos gamemodesString en lugar de response.gamemodes
                    response.location
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getGameModesList() = try {
        withContext(Dispatchers.IO) {
            val gameModeItemResponses = api.getGameModesList()
            gameModeItemResponses.map { response ->
                GameModeListItem(
                    response.key,
                    response.name,
                    response.icon,
                    response.description
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getMapsListByGameMode(gameModeKey: String) = try {
        withContext(Dispatchers.IO) {
            val mapsListByRoleItemResponses = api.getMapsListByGameMode(gameModeKey)
            mapsListByRoleItemResponses.map { response ->

                val gamemodesString = if (response.gamemodes.size > 1) {
                    response.gamemodes.joinToString(", ")
                } else {
                    response.gamemodes.firstOrNull() ?: ""
                }

                MapListItem(
                    response.name,
                    response.screenshot,
                    gamemodesString,
                    response.location
                )
            }
        }
    } catch (e: Exception) {
        emptyList()
    }*/

    /*suspend fun getPlayerStats(playerId: String) = try {
        withContext(Dispatchers.IO) {
            val playerStatsResponse = api.getPlayerStats(playerId)
            with(playerStatsResponse) {
                Result.success(
                    PlayerStats(general)
                )
            }
        }
    } catch (e: Exception) {
        Result.failure(e)
    }*/
}