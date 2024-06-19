package es.uji.al341571.overwatch2app.playersActivity

import android.content.Context
import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.recyclerview.widget.LinearLayoutManager
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.classes.PlayerStats
import es.uji.al341571.overwatch2app.databinding.ActivityPlayersBinding
import es.uji.al341571.overwatch2app.menuActivity.MenuActivity
import org.chromium.net.NetworkException

class PlayersActivity : AppCompatActivity(), IPlayers {

    private lateinit var binding: ActivityPlayersBinding
    private val viewModel: PlayersViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityPlayersBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchPlayerEditText.addTextChangedListener { _ ->
            binding.searchPlayerEditText.setTextColor(Color.WHITE)
        }

        binding.searchPlayerButton.setOnClickListener {
            val userNameEntered = binding.searchPlayerEditText.text.toString()
            viewModel.onPlayerSearchRequested(userNameEntered)
            hideKeyboard()
        }

        binding.overwatchLogoImageBackButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }
    }

    private fun hideKeyboard() {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        inputMethodManager.hideSoftInputFromWindow(binding.searchPlayerEditText.windowToken, 0)
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    override fun showPlayerUserName(userName: String) {
        binding.searchPlayerEditText.text.clear()
        binding.userNameDisplay.text = userName
    }

    override fun showPlayerStatsData(playerStats: PlayerStats) {
        binding.playerStatsTitle.visibility = View.GONE
        binding.noStatsDisplay.visibility = View.GONE

        println("Check - ${playerStats.general}")

        val playerStatsChainTree = generateStatsTree(playerStats)

        binding.playerStatsTreeRecyclerView.apply {
            layoutManager = LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)
            adapter = PlayerStatsTreeAdapter(playerStatsChainTree) /*{ node ->
                // Handle node click if necessary
            }*/
        }
    }

    private fun onPlayerStatsNodeClicked(node: PlayerStatsChainTree) {
        println("Check - Node cliked")
    }

    override fun showSearchError(error: Throwable) {
        // Log the error
        Log.e("PlayersActivity", "Error during Player search", error)
        // Handle the error based on its type
        if (error is NetworkException) {
            // Specific network error
            showToast("Network error: ${error.message}")
        } else {
            val errorMessage = error.message?: "Unknown error"
            showToast(errorMessage)
        }
        resetPlayer()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun resetPlayer() {
        with (binding) {
            searchPlayerEditText.setTextColor(Color.RED)
            userNameDisplay.text= getString(R.string.userNameDisplayDefaultText)
            binding.playerStatsTitle.visibility = View.VISIBLE
            binding.noStatsDisplay.visibility = View.VISIBLE
            playerStatsTreeRecyclerView.adapter = null
        }
    }
}