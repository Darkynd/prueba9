package es.uji.al341571.overwatch2app.heroesActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import es.uji.al341571.overwatch2app.network.HitpointsResponse

class HeroesActivityHitpointsDialog: DialogFragment() {

    private lateinit var viewModel: HeroesViewModel

    fun setViewModel(viewModel: HeroesViewModel) {
        this.viewModel = viewModel
    }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val heroName = arguments?.getString("heroName")?: ""
        val items = viewModel.getHeroHitpointsArray()

        return buildDialog(heroName, items)
    }

    private fun buildDialog(heroName: String, items: Array<String>): Dialog {
        return AlertDialog.Builder(requireContext()).run {
            setTitle("Hitpoints of $heroName")
            setItems(items, null)
            setPositiveButton("OK", null)
            create()
        }
    }

    companion object {
        fun newInstance(heroName: String): HeroesActivityHitpointsDialog {
            val args = Bundle().apply {
                putString("heroName", heroName)
            }
            return HeroesActivityHitpointsDialog().apply {
                arguments = args
            }
        }
    }
}