package es.uji.al341571.overwatch2app.heroesActivity

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.Observer
import com.bumptech.glide.Glide
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.classes.Hero
import es.uji.al341571.overwatch2app.databinding.ActivityHeroesBinding
import es.uji.al341571.overwatch2app.menuActivity.MenuActivity
import org.chromium.net.NetworkException

class HeroesActivity : AppCompatActivity(), IHeroes {

    companion object {
        const val HERO_NAME = "HERO_NAME"
        const val IS_INTENT_FROM_ROLE_ACTIVITY = "IS_INTENT_FROM_ROLE_ACTIVITY"
    }

    private lateinit var binding: ActivityHeroesBinding
    private val viewModel: HeroesViewModel by viewModels()

    private lateinit var currentHeroName: String
    private lateinit var currentHeroKey: String
    private var isIntentFromRoleActivity: Boolean = false


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityHeroesBinding.inflate(layoutInflater)
        setContentView(binding.root)

        binding.searchHeroEditText.addTextChangedListener { _ ->
            binding.searchHeroEditText.setTextColor(Color.WHITE)
        }

        binding.searchHeroButton.setOnClickListener {
            val heroNameEntered = binding.searchHeroEditText.text.toString()
            currentHeroKey = viewModel.setHeroKey(heroNameEntered).toString()
            viewModel.onHeroSearchRequested(currentHeroKey)
        }

        binding.heroPortrait.setOnClickListener {
            val dialogPortraitsFragment = HeroesActivityPortraitsDialog()
            val heroList = viewModel.heroList.value ?: emptyList()
            dialogPortraitsFragment.setViewModel(viewModel)
            dialogPortraitsFragment.setHeroList(heroList)
            dialogPortraitsFragment.show(supportFragmentManager, "portraits_dialog")
        }

        binding.abilitiesButton.setOnClickListener {
            if(binding.heroNameDisplay.text != getString(R.string.heroNameDisplayDefaultText)) {
                val dialogAbilitiesFragment = HeroesActivityAbilitiesDialog.newInstance(binding.heroNameDisplay.text.toString())
                dialogAbilitiesFragment.setViewModel(viewModel)
                dialogAbilitiesFragment.show(supportFragmentManager, "abilities_dialog")
            } else {
                showToast("Unknown Hero")
            }
        }

        binding.hitPointsButton.setOnClickListener {
            if(binding.heroNameDisplay.text != getString(R.string.heroNameDisplayDefaultText)) {
                val dialogStatsFragment = HeroesActivityHitpointsDialog.newInstance(binding.heroNameDisplay.text.toString())
                dialogStatsFragment.setViewModel(viewModel)
                dialogStatsFragment.show(supportFragmentManager, "stats_dialog")
            } else {
                showToast("Unknown Hero")
            }
        }

        binding.roleButton.setOnClickListener {
            if(binding.heroNameDisplay.text != getString(R.string.heroNameDisplayDefaultText)) {
                val dialogRoleFragment = HeroesActivityRoleDialog.newInstance(binding.heroNameDisplay.text.toString())
                dialogRoleFragment.setViewModel(viewModel)
                dialogRoleFragment.show(supportFragmentManager, "role_dialog")
            } else {
                showToast("Unknown Hero")
            }
        }

        binding.overwatchLogoImageBackButton.setOnClickListener {
            val intent = Intent(this, MenuActivity::class.java)
            startActivity(intent)
        }

        // Intents
        // Intent: From Role Activity
        currentHeroName = intent.getStringExtra(HERO_NAME).toString()
        isIntentFromRoleActivity = intent.getBooleanExtra(IS_INTENT_FROM_ROLE_ACTIVITY, false)

        if(isIntentFromRoleActivity) {
            viewModel.heroList.observe(this, Observer { heroList ->
                heroList?.let {
                    currentHeroKey = viewModel.setHeroKey(currentHeroName).toString()
                    viewModel.onHeroSearchRequested(currentHeroKey)
                }
            })
            isIntentFromRoleActivity = false
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    override fun showHeroData(hero: Hero) {
        binding.heroNameDisplay.text = hero.name
        showHeroPortrait(hero.portrait)
        binding.descriptionDisplay.text =
            getString(R.string.heroDescriptionQuoted, hero.description)
        binding.heroAgeDisplay.text = hero.age.toString()
        binding.heroBirthdayDisplay.text = hero.birthday ?: "N/A"
        binding.heroLocationDisplay.text = hero.location
    }

    private fun showHeroPortrait(imageUrl: String) {
        Glide.with(this@HeroesActivity)
            .load(imageUrl)
            .fitCenter()
            .into(binding.heroPortrait)
        binding.searchHeroEditText.text.clear()
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
        resetHero()
    }

    private fun showToast(message: String) {
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }

    private fun resetHero() {
        with (binding) {
            searchHeroEditText.setTextColor(Color.RED)
            heroPortrait.setImageResource(R.drawable.overwatch_logo)
            heroNameDisplay.text = getString(R.string.heroNameDisplayDefaultText)
            descriptionDisplay.text = ""
            heroAgeDisplay.text = getString(R.string.heroAgeDisplayDefaultText)
            heroBirthdayDisplay.text = getString(R.string.heroBirthdayDisplayDefaultText)
            heroLocationDisplay.text = getString(R.string.heroLocationDisplayDefaultText)
        }
    }
}