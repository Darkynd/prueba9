package es.uji.al341571.overwatch2app.heroesActivity

import android.app.AlertDialog
import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import es.uji.al341571.overwatch2app.roleActivity.RoleActivity

class HeroesActivityRoleDialog: DialogFragment() {

    private lateinit var viewModel: HeroesViewModel

    fun setViewModel(viewModel: HeroesViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val heroName = arguments?.getString("heroName")?: ""
        val item = viewModel.getHeroRole()?.capitalizeFirstLetter()

        return buildDialog(heroName, item)
    }

    private fun buildDialog(heroName: String, item: String?): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setTitle("Role of $heroName")
            setMessage(item)
            setPositiveButton("GO TO ${item?.uppercase()} ROLE") { _, _ ->
                val intent = Intent(requireContext(), RoleActivity::class.java)
                intent.putExtra(RoleActivity.HERO_NAME, heroName)
                intent.putExtra(RoleActivity.HERO_ROLE, item)
                intent.putExtra(RoleActivity.IS_INTENT_FROM_HEROES_ACTIVITY, true)
                startActivity(intent)
            }
            setNegativeButton("QUIT", null)
            create()
        }
    }

    companion object {
        fun newInstance(heroName: String): HeroesActivityRoleDialog {
            val args = Bundle().apply {
                putString("heroName", heroName)
            }
            return HeroesActivityRoleDialog().apply {
                arguments = args
            }
        }
    }

    // Extension function to capitalize the first letter of a string
    private fun String.capitalizeFirstLetter(): String {
        return this.replaceFirstChar {
            if (it.isLowerCase()) it.titlecase() else it.toString()
        }
    }
}