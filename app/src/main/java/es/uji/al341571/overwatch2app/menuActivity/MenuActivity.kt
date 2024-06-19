package es.uji.al341571.overwatch2app.menuActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import es.uji.al341571.overwatch2app.databinding.ActivityMenuBinding
import es.uji.al341571.overwatch2app.heroesActivity.HeroesActivity
import es.uji.al341571.overwatch2app.mapsActivity.MapsActivity
import es.uji.al341571.overwatch2app.playersActivity.PlayersActivity

class MenuActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMenuBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMenuBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Buttons Functionality
        // Heroes Button
        binding.heroesButton.setOnClickListener {
            showToast(binding.heroesButton.text.toString())
            val intent = Intent(this, HeroesActivity::class.java)
            startActivity(intent)
        }
        // Maps Button
        binding.mapsButton.setOnClickListener {
            showToast(binding.mapsButton.text.toString())
            val intent = Intent(this, MapsActivity::class.java)
            startActivity(intent)
        }

        binding.playersButton.setOnClickListener {
            showToast(binding.playersButton.text.toString())
            val intent = Intent(this, PlayersActivity::class.java)
            startActivity(intent)
        }
    }

    private fun showToast(message: String) {
        Toast.makeText(this, "$message button was pressed", Toast.LENGTH_SHORT).show()
    }
}