package es.uji.al341571.overwatch2app.network

import es.uji.al341571.overwatch2app.classes.GameModeListItem
import es.uji.al341571.overwatch2app.classes.Hero
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.classes.PlayerStats
import es.uji.al341571.overwatch2app.classes.RoleListItem

class CachedAPI(private val overwatchAPI: OverwatchAPI) {

    private val heroCache: MutableMap<String, Hero> = mutableMapOf()
    private val heroListCache: MutableList<HeroListItem> = mutableListOf()
    private val rolesListCache: MutableList<RoleListItem> = mutableListOf()
    private val heroListByRoleCache: MutableMap<String, List<HeroListItem>> = mutableMapOf()
    private val mapsListCache: MutableList<MapListItem> = mutableListOf()
    private val gameModesListCache: MutableList<GameModeListItem> = mutableListOf()
    private val mapsListByGameModeCache: MutableMap<String, List<MapListItem>> = mutableMapOf()
    private val playerStatsCache: MutableMap<String, PlayerStats> = mutableMapOf()

    suspend fun getHero(heroName: String): Result<Hero> {
        val cachedHero = heroCache[heroName]
        if (cachedHero != null) {
            return Result.success(cachedHero)
        }

        return try {
            val heroResponse = overwatchAPI.getHero(heroName)
            val hero = mapHeroResponseToHero(heroResponse)
            heroCache[heroName] = hero
            Result.success(hero)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapHeroResponseToHero(heroResponse: HeroResponse): Hero {
        return with(heroResponse) {
            Hero(
                name,
                description,
                portrait,
                role,
                location,
                age,
                birthday,
                hitpoints,
                abilities
            )
        }
    }

    suspend fun getPlayerStats(playerId: String): Result<PlayerStats> {
        val cachedPlayerStats = playerStatsCache[playerId]
        if (cachedPlayerStats != null) {
            return Result.success(cachedPlayerStats)
        }

        return try {
            val playerStatsResponse = overwatchAPI.getPlayerStats(playerId)
            val playerStats = mapPlayerStatsResponseToPlayerStats(playerStatsResponse)
            playerStatsCache[playerId] = playerStats
            Result.success(playerStats)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private fun mapPlayerStatsResponseToPlayerStats(playerStatsResponse: PlayerStatsResponse): PlayerStats {
        return with(playerStatsResponse) {
            PlayerStats(
                general
            )
        }
    }

    suspend fun getHeroList(): Result<List<HeroListItem>> {
        if (heroListCache.isNotEmpty()) {
            return Result.success(heroListCache)
        }

        return try {
            val heroListResponse = overwatchAPI.getHeroList()
            val heroList = heroListResponse.map { response ->
                HeroListItem(
                    response.key,
                    response.name,
                    response.role
                )
            }
            heroListCache.addAll(heroList)
            Result.success(heroList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getRolesList(): Result<List<RoleListItem>> {
        if (rolesListCache.isNotEmpty()) {
            return Result.success(rolesListCache)
        }

        return try {
            val rolesListResponse = overwatchAPI.getRolesList()
            val rolesList = rolesListResponse.map { response ->
                RoleListItem(
                    response.key,
                    response.name,
                    response.icon,
                    response.description,
                )
            }
            rolesListCache.addAll(rolesList)
            Result.success(rolesList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getHeroListByRole(heroRole: String): Result<List<HeroListItem>> {
        val cachedHeroListByRole = heroListByRoleCache[heroRole]
        if (cachedHeroListByRole != null) {
            return Result.success(cachedHeroListByRole)
        }

        return try {
            val heroListByRoleResponse = overwatchAPI.getHeroListByRole(heroRole)
            val heroListByRole = heroListByRoleResponse.map { response ->
                HeroListItem(
                    response.key,
                    response.name,
                    response.role
                )
            }
            heroListByRoleCache[heroRole] = heroListByRole
            Result.success(heroListByRole)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMapsList(): Result<List<MapListItem>> {
        if (mapsListCache.isNotEmpty()) {
            return Result.success(mapsListCache)
        }

        return try {
            val mapListItemResponses = overwatchAPI.getMapsList()
            val mapsList = mapListItemResponses.map { response ->
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
            mapsListCache.addAll(mapsList)
            Result.success(mapsList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getGameModesList(): Result<List<GameModeListItem>> {
        if (gameModesListCache.isNotEmpty()) {
            return Result.success(gameModesListCache)
        }

        return try {
            val gameModeItemResponses = overwatchAPI.getGameModesList()
            val gameModesList = gameModeItemResponses.map { response ->
                GameModeListItem(
                    response.key,
                    response.name,
                    response.icon,
                    response.description
                )
            }
            gameModesListCache.addAll(gameModesList)
            Result.success(gameModesList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    suspend fun getMapsListByGameMode(gameModeKey: String): Result<List<MapListItem>> {
        val cachedMapsListByGameMode = mapsListByGameModeCache[gameModeKey]
        if (cachedMapsListByGameMode != null) {
            return Result.success(cachedMapsListByGameMode)
        }

        return try {
            val mapsListByRoleItemResponses = overwatchAPI.getMapsListByGameMode(gameModeKey)
            val mapsList = mapsListByRoleItemResponses.map { response ->
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
            mapsListByGameModeCache[gameModeKey] = mapsList
            Result.success(mapsList)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}
