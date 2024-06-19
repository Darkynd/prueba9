package es.uji.al341571.overwatch2app.heroesActivity

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.activityViewModels
import es.uji.al341571.overwatch2app.classes.HeroListItem

class HeroesActivityPortraitsDialog: DialogFragment() {

    private lateinit var viewModel: HeroesViewModel
    private lateinit var heroList: List<HeroListItem>

    fun setViewModel(viewModel: HeroesViewModel) { this.viewModel = viewModel }
    fun setHeroList(heroList: List<HeroListItem>) { this.heroList = heroList }

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val keys = heroList.map { it.key }.toTypedArray()
        val items = heroList.map { it.name }.toTypedArray()

        return AlertDialog.Builder(requireContext()).run {
            setTitle("Select a Hero")
            setItems(items) { _, which ->
                viewModel.onHeroSearchRequested(keys[which])
            }
            setPositiveButton("OK", null)
            create()
        }
    }
}