package es.uji.al341571.overwatch2app.roleActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.classes.HeroListItem
import es.uji.al341571.overwatch2app.databinding.RecyclerviewAllroleheroesItemBinding

class AllRoleHeroesAdapter(
    private var allHeroesRoleList: List<HeroListItem>,
    var currentHeroName: String?,
    val onItemClick: (HeroListItem)-> Unit)
    : RecyclerView.Adapter<AllRoleHeroesAdapter.AllRoleHeroesViewHolder>() {

    inner class AllRoleHeroesViewHolder(private val itemBinding: RecyclerviewAllroleheroesItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(heroItem: HeroListItem) {
            itemBinding.heroNameItem.text = heroItem.name

            // Determinar el color de texto basado en si es el héroe seleccionado o no
            val highlightColor = ContextCompat.getColor(itemBinding.root.context, R.color.grey)
            val defaultColor = ContextCompat.getColor(itemBinding.root.context, R.color.white)

            if (heroItem.name == currentHeroName) {
                itemBinding.heroNameItem.setTextColor(highlightColor)
            } else {
                itemBinding.heroNameItem.setTextColor(defaultColor)
            }

            itemBinding.root.setOnClickListener {
                onItemClick(heroItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllRoleHeroesViewHolder {
        val itemBinding = RecyclerviewAllroleheroesItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllRoleHeroesViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AllRoleHeroesViewHolder, position: Int) {
        val heroItem = allHeroesRoleList[position]
        holder.bindItem(heroItem)
    }

    override fun getItemCount(): Int {
        return allHeroesRoleList.size
    }

    // Método para actualizar los datos del adaptador
    fun setData(newList: List<HeroListItem>) {
        allHeroesRoleList = newList
        notifyDataSetChanged()
    }
}