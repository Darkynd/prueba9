package es.uji.al341571.overwatch2app.gameModesActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import androidx.recyclerview.widget.LinearLayoutManager
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.classes.GameMode
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.classes.Utils
import es.uji.al341571.overwatch2app.databinding.ActivityGameModesBinding
import es.uji.al341571.overwatch2app.mapsActivity.MapsActivity
import org.chromium.net.NetworkException

class GameModesActivity : AppCompatActivity(), IGameModes {

    companion object {
        const val MAP_NAME = "MAP_NAME"
        const val GAMEMODE_NAME = "GAMEMODE_NAME"
        const val IS_INTENT_FROM_MAPS_ACTIVITY = "IS_INTENT_FROM_MAPS_ACTIVITY"
    }

    private lateinit var currentGamemode: String
    private lateinit var currentMapName: String

    private lateinit var binding: ActivityGameModesBinding
    private val viewModel: GameModesViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityGameModesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.overwatchLogoImageBackButton.setOnClickListener {
            val intent = Intent(this, MapsActivity::class.java)

            intent.putExtra(MapsActivity.MAP_NAME, currentMapName)
            startActivity(intent)
        }

        // Intents
        // Intent: From Maps Activity
        currentMapName = intent.getStringExtra(MAP_NAME).toString()
        currentGamemode = intent.getStringExtra(GAMEMODE_NAME).toString()

        updateUI()
    }

    private fun updateUI() {
        viewModel.setCurrentGameMode(currentGamemode)
        viewModel.fetchGameModesList()
        viewModel.fetchMapsListByGameMode(currentGamemode)
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    override fun showGameModeData(gameMode: GameMode) {
        //printGameModeObject(gameMode)
        //printGameModesList()

        binding.gameModeNameDisplay.text = gameMode.name
        showGameModeIcon(gameMode.icon)
        binding.gameModeDescriptionDisplay.text = gameMode.description
        binding.allGameModeMapsTitle.text = getString(
            R.string.allGameModeMapsTitleFormatted,
            currentGamemode.replaceFirstChar { it.uppercaseChar() })


        binding.allGameModeMapsRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = AllMapsByGameModeAdapter(gameMode.mapListByGameMode, currentMapName) { mapItem ->
                onMapItemClicked(mapItem)
            }
        }
    }

    private fun showGameModeIcon(imageUrl: String) {
        val utils = Utils()
        // Obtener la referencia al ImageView donde se mostrará el icono
        val imageView = binding.gameModeIconDisplay
        // Llamar a la función de utilidad para cargar el SVG desde la URL y mostrarlo en el ImageView
        utils.loadSvgFromUrl(imageUrl, imageView)
    }

    private fun onMapItemClicked(mapItemClicked: MapListItem) {

        currentMapName = mapItemClicked.name

        val intent = Intent(this, MapsActivity::class.java)
        intent.putExtra(MapsActivity.MAP_NAME, currentMapName)
        startActivity(intent)
    }

    override fun showSearchError(error: Throwable) {
        // Log the error
        Log.e("GameModesActivity", "Error during Game Mode search", error)
        // Handle the error based on its type
        if (error is NetworkException) {
            // Specific network error
            showToast("Network error: ${error.message}")
        } else {
            val errorMessage = error.message?: "Unknown error"
            showToast(errorMessage)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun printGameModesList() {
        // Imprimir la lista por consola para verificar
        viewModel.gameModesList.observe(this, Observer { gamemodes ->
            gamemodes.forEach { gamemode ->
                Log.d("GameModesActivity",
                    "Gamemode key: ${gamemode.key}, " +
                         "Gamemode name: ${gamemode.name}, " +
                         "Gamemode icon: ${gamemode.icon}, " +
                         "Gamemode description: ${gamemode.description}")
            }
        })
    }

    private fun printGameModeObject(gameMode: GameMode) {
        println("Check - GameMode key: ${gameMode.key}")
        println("Check - GameMode name: ${gameMode.name}")
        println("Check - GameMode icon: ${gameMode.icon}")
        println("Check - GameMode description: ${gameMode.description}")

        gameMode.mapListByGameMode.forEach { mapListItem ->
            println("Check - Map Name: ${mapListItem.name}")
            println("Check - Screenshot URL: ${mapListItem.screenshot}")
            println("Check - Game Modes: ${mapListItem.gamemodesString}")
            println("Check - Location: ${mapListItem.location}")
            println("Check - -----------------------------------")
        }
    }
}