package es.uji.al341571.overwatch2app.gameModesActivity

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.classes.MapListItem
import es.uji.al341571.overwatch2app.databinding.RecyclerviewAllgamemodemapsItemBinding

class AllMapsByGameModeAdapter (
    private val mapListByGameMode: List<MapListItem>,
    var currentMapName: String?,
    val onItemClick: (MapListItem)-> Unit)
    : RecyclerView.Adapter<AllMapsByGameModeAdapter.AllMapsByGameModeViewHolder>() {

    inner class AllMapsByGameModeViewHolder(private val itemBinding: RecyclerviewAllgamemodemapsItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bindItem(mapItem: MapListItem) {
            itemBinding.mapNameItem.text = mapItem.name

            val highlightColor = ContextCompat.getColor(itemBinding.root.context, R.color.grey)

            if (mapItem.name == currentMapName) {
                itemBinding.mapNameItem.setTextColor(highlightColor)
            }

            itemBinding.root.setOnClickListener {
                onItemClick(mapItem)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AllMapsByGameModeViewHolder {
        val itemBinding = RecyclerviewAllgamemodemapsItemBinding.inflate(
            LayoutInflater.from(parent.context),
            parent,
            false
        )
        return AllMapsByGameModeViewHolder(itemBinding)
    }

    override fun onBindViewHolder(holder: AllMapsByGameModeViewHolder, position: Int) {
        val mapItem = mapListByGameMode[position]
        holder.bindItem(mapItem)
    }

    override fun getItemCount(): Int {
        return mapListByGameMode.size
    }

}