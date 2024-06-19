package es.uji.al341571.overwatch2app.roleActivity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.classes.Role
import es.uji.al341571.overwatch2app.databinding.ActivityRoleBinding
import es.uji.al341571.overwatch2app.heroesActivity.HeroesActivity
import org.chromium.net.NetworkException
import java.util.Locale

class RoleActivity : AppCompatActivity(), IRole {

    companion object {
        const val HERO_NAME = "HERO_NAME"
        const val HERO_ROLE = "HERO_ROLE"
        const val IS_INTENT_FROM_HEROES_ACTIVITY = "IS_INTENT_FROM_HEROES_ACTIVITY"
    }

    private var selectedPosition: Int = RecyclerView.NO_POSITION
    private lateinit var allRoleHeroesAdapter: AllRoleHeroesAdapter

    private val viewModel: RoleViewModel by viewModels()
    private lateinit var binding: ActivityRoleBinding

    private lateinit var currentHeroName: String
    private lateinit var currentHeroRole: String
    private var isIntentFromHeroesActivity: Boolean = false

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityRoleBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Configuración del RecyclerView y el Adaptador
        allRoleHeroesAdapter = AllRoleHeroesAdapter(emptyList(), null) { heroItem ->
            onHeroItemClicked(heroItem)
        }
        binding.allRoleHeroesRecyclerView.apply {
            layoutManager = LinearLayoutManager(context)
            adapter = allRoleHeroesAdapter
        }

        binding.overwatchLogoImageBackButton.setOnClickListener {
            val intent = Intent(this, HeroesActivity::class.java)
            intent.putExtra(HeroesActivity.HERO_NAME, currentHeroName)
            intent.putExtra(HeroesActivity.IS_INTENT_FROM_ROLE_ACTIVITY, true)
            startActivity(intent)
        }

        // Intents
        // Intent: From Heroes Activity
        currentHeroName = intent.getStringExtra(HERO_NAME).toString()
        currentHeroRole = intent.getStringExtra(HERO_ROLE).toString()

        updateUI()
    }

    override fun onResume() {
        super.onResume()
        viewModel.view = this
    }

    override fun onPause() {
        super.onPause()
        viewModel.view = null
    }

    private fun updateUI() {
        viewModel.setCurrentHero(currentHeroName, currentHeroRole)
        viewModel.fetchRolesList()
        viewModel.fetchHeroListByRole(currentHeroRole.lowercase())
    }

    override fun showRoleData(role: Role) {
        with(binding) {
            heroNameDisplay.text = role.heroName
            heroRoleDisplay.text = role.roleName
            allRoleHeroesTitle.text = "All ${role.roleName} Heroes:"
            roleDescriptionDisplay.text = role.roleDescription
        }

        // Actualizar datos del adaptador con la nueva lista de héroes y el héroe seleccionado
        allRoleHeroesAdapter.currentHeroName = currentHeroName
        allRoleHeroesAdapter.setData(role.heroListByRole)
    }

    private fun onHeroItemClicked(heroItemClicked: HeroListItem) {
        currentHeroName = heroItemClicked.name
        currentHeroRole = heroItemClicked.role.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase(Locale.getDefault()) else it.toString()
        }

        updateUI()

        // Actualizar la posición seleccionada en el adaptador
        selectedPosition = binding.allRoleHeroesRecyclerView.findViewHolderForAdapterPosition(selectedPosition)?.adapterPosition
            ?: RecyclerView.NO_POSITION

        // Notificar al adaptador sobre el cambio de héroe seleccionado
        //allRoleHeroesAdapter.currentHeroName = currentHeroName
        //allRoleHeroesAdapter.notifyDataSetChanged()
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
}