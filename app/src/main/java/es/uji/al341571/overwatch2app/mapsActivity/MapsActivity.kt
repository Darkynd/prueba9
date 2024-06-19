package es.uji.al341571.overwatch2app.mapsActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.SpannableString
import android.text.Spanned
import android.text.method.LinkMovementMethod
import android.text.style.ClickableSpan
import android.util.Log
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import es.uji.al341571.overwatch2app.R

import es.uji.al341571.overwatch2app.classes.Map
import es.uji.al341571.overwatch2app.databinding.ActivityMapsBinding
import es.uji.al341571.overwatch2app.gameModesActivity.GameModesActivity
import es.uji.al341571.overwatch2app.menuActivity.MenuActivity
import es.uji.al341571.overwatch2app.roleActivity.RoleActivity
import org.chromium.net.NetworkException

class MapsActivity : AppCompatActivity(), IMaps {

    private val viewModel: MapsViewModel by viewModels()
    private lateinit var binding: ActivityMapsBinding

    private lateinit var adapter: ArrayAdapter<String> // Adaptador para el Spinner
    private lateinit var currentGamemode: String
    private lateinit var currentMapName: String

    companion object {
        const val MAP_NAME = "MAP_NAME"
        const val IS_INTENT_FROM_GAMEMODES_ACTIVITY = "IS_INTENT_FROM_GAMEMODES_ACTIVITY"
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMapsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.overwatchLogoImageBackButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        // Intents
        // Intent: From GameModes Activity
        currentMapName = intent.getStringExtra(MAP_NAME).toString()

        updateUI()
    }

    private fun setSpinnerMaps() {
        // Inicialización del ArrayAdapter vacío inicialmente
        adapter = ArrayAdapter(this, R.layout.spinner_item_layout, R.id.mapNameItem, mutableListOf())
        adapter.setDropDownViewResource(R.layout.spinner_dropdown_item)
        binding.mapNameDisplaySpinner.adapter = adapter

        // Observar cambios en la lista de mapas y actualizar el adaptador
        viewModel.mapsList.observe(this, Observer { maps ->
            val mapNames = maps.map { it.name } // Obtener nombres de los mapas
            adapter.clear() // Limpiar el adaptador antes de añadir nuevos elementos
            adapter.addAll(mapNames) // Añadir nombres de los mapas al adaptador
            adapter.notifyDataSetChanged() // Notificar cambios al adaptador

            // Establecer la selección inicial solo si currentMapName está definido y existe en la lista de mapas
            if (currentMapName.isNotEmpty()) {
                val initialPosition = adapter.getPosition(currentMapName)
                if (initialPosition != -1) {
                    binding.mapNameDisplaySpinner.setSelection(initialPosition)
                }
            }
        })
    }

    private fun setSpinnerSelection() {
        binding.mapNameDisplaySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedMapName = parent?.getItemAtPosition(position).toString()
                // Buscar el mapa seleccionado por su nombre
                val selectedMap = viewModel.mapsList.value?.find { it.name == selectedMapName }
                selectedMap?.let { map ->
                    // Actualizar los TextViews con la información del mapa seleccionado
                    showMapScreenShot(map.screenshot)
                    binding.mapLocationDisplay.text = map.location
                    setClickableGameModes(map.gamemodesString)  // Usa el nuevo método
                }
            }
            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showMapScreenShot(imageUrl: String) {
        Glide.with(this@MapsActivity)
            .load(imageUrl)
            .fitCenter()
            .into(binding.mapScreenshotImageView)
    }

    private fun formatGameModes(gameModes: String): String {
        val gameModesArray = gameModes.split(", ")
        val formattedModes = gameModesArray.map { mode ->
            mode.split(" ").joinToString(" ") { it.lowercase().replaceFirstChar { char -> char.uppercase() } }
        }
        return formattedModes.joinToString(", ")
    }

    private fun setClickableGameModes(gameModes: String) {
        val formattedModes = formatGameModes(gameModes)

        val spannableString = SpannableString(formattedModes)

        var startIndex = 0
        formattedModes.split(", ").forEach { gameModeItem ->
            val endIndex = startIndex + gameModeItem.length

            val clickableSpan = object : ClickableSpan() {
                override fun onClick(widget: View) {
                    val intent = Intent(this@MapsActivity, GameModesActivity::class.java)
                    val selectedMapName = binding.mapNameDisplaySpinner.selectedItem.toString()
                    currentGamemode = gameModeItem.lowercase()
                    intent.putExtra(GameModesActivity.MAP_NAME, selectedMapName)
                    intent.putExtra(GameModesActivity.GAMEMODE_NAME, gameModeItem.lowercase())
                    intent.putExtra(GameModesActivity.IS_INTENT_FROM_MAPS_ACTIVITY, true)
                    startActivity(intent)
                }
            }

            spannableString.setSpan(clickableSpan, startIndex, endIndex, Spanned.SPAN_EXCLUSIVE_EXCLUSIVE)

            startIndex = endIndex + 2 // +2 for ", " separator
        }

        binding.gameModeMapDisplay.text = spannableString
        binding.gameModeMapDisplay.movementMethod = LinkMovementMethod.getInstance()
    }

    private fun updateUI() {
        //printMapsList()
        setSpinnerMaps()
        setSpinnerSelection()
        viewModel.fetchMapList()
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    override fun showMapData(map: Map) {

    }

    override fun showSearchError(error: Throwable) {
        // Log the error
        Log.e("HeroesActivity", "Error during Hero search", error)
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

    private fun printMapsList() {
        viewModel.mapsList.observe(this, Observer { maps ->
            // Aquí puedes actualizar la interfaz de usuario con la nueva lista de maps
            maps.forEach { map ->
                Log.d("MapsActivity", "Map name: ${map.name}, " +
                        "Screenshot: ${map.screenshot}, " +
                        "Gamemodes: ${map.gamemodesString}, " +
                        "Location: ${map.location}")
            }
        })
    }
}