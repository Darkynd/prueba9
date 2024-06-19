package es.uji.al341571.overwatch2app.heroesActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment

class HeroesActivityAbilitiesDialog: DialogFragment() {

    private lateinit var viewModel: HeroesViewModel

    fun setViewModel(viewModel: HeroesViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val heroName = arguments?.getString("heroName")?: ""
        val items = viewModel.getHeroAbilities().map { it.name }.toTypedArray()

        return buildDialog(heroName, items)
    }

    private fun buildDialog(heroName: String, items: Array<String>): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setTitle("Abilities of $heroName")
            setItems(items, null)
            setPositiveButton("OK", null)
            create()
        }
    }

    companion object {
        fun newInstance(heroName: String): HeroesActivityAbilitiesDialog {
            val args = Bundle().apply {
                putString("heroName", heroName)
            }
            return HeroesActivityAbilitiesDialog().apply {
                arguments = args
            }
        }
    }
}