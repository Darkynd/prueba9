package es.uji.al341571.overwatch2app.playersActivity

import android.text.Spannable
import android.text.SpannableString
import android.text.style.ForegroundColorSpan
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import es.uji.al341571.overwatch2app.R
import es.uji.al341571.overwatch2app.databinding.RecyclerviewPlayerstatItemBinding
import java.util.concurrent.atomic.AtomicInteger

class PlayerStatsTreeAdapter(
    private val rootNode: PlayerStatsChainTree,
    //private val onNodeClicked: (PlayerStatsChainTree) -> Unit
) : RecyclerView.Adapter<PlayerStatsTreeAdapter.PlayerStatsTreeViewHolder>() {

    inner class PlayerStatsTreeViewHolder(private val itemBinding: RecyclerviewPlayerstatItemBinding) :
        RecyclerView.ViewHolder(itemBinding.root) {

        fun bind(node: PlayerStatsChainTree) {
            val formattedText = formatNodeName(node)
            formatNodeColor(formattedText, node)

            /*itemBinding.root.setOnClickListener {
                onNodeClicked(node)
            }*/
        }

        private fun formatNodeName(node: PlayerStatsChainTree): String {
            val depth = calculateNodeDepth(node)

            return when {
                node.isRoot() -> {
                    "${node.nodeName}: "
                }
                depth == 1 -> {
                    val indentation = "    ".repeat(depth - 1)
                    val prefix = if (node.isLastChild) "└── " else "├── "
                    if (node.isTitle) "$indentation$prefix${node.nodeName}: "
                    else "$indentation$prefix${node.nodeName}: ${node.value ?: "N/A"}"
                }
                else -> {
                    val indentation = "         ".repeat(depth - 1)
                    val prefix = if (node.isLastChild) "└── " else "├── "
                    val line = if (!node.parent!!.isLastChild) "│" else ""
                    "$line$indentation$prefix${node.nodeName}: ${node.value ?: "N/A"}"
                }
            }
        }

        private fun formatNodeColor(formattedText: String, node: PlayerStatsChainTree) {
            val highlightColor = ContextCompat.getColor(itemBinding.root.context, R.color.orange)
            val defaultColor = ContextCompat.getColor(itemBinding.root.context, R.color.white)

            if (node.isTitle) {
                val spannableString = SpannableString(formattedText)

                // Encuentra el índice de inicio y fin de node.nodeName
                val nodeNameStart = formattedText.indexOf(node.nodeName)
                val nodeNameEnd = nodeNameStart + node.nodeName.length

                // Aplica el color solo a node.nodeName
                spannableString.setSpan(
                    ForegroundColorSpan(highlightColor),
                    nodeNameStart,
                    nodeNameEnd,
                    Spannable.SPAN_EXCLUSIVE_EXCLUSIVE
                )

                itemBinding.playerStatItem.text = spannableString
            } else {
                itemBinding.playerStatItem.text = formattedText
                itemBinding.playerStatItem.setTextColor(defaultColor)
            }
        }

        private fun calculateNodeDepth(node: PlayerStatsChainTree): Int {
            var depth = 0
            var currentNode = node
            while (!currentNode.isRoot()) {
                depth++
                currentNode = currentNode.parent ?: break
            }
            return depth
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PlayerStatsTreeViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val binding = RecyclerviewPlayerstatItemBinding.inflate(inflater, parent, false)
        return PlayerStatsTreeViewHolder(binding)
    }

    override fun onBindViewHolder(holder: PlayerStatsTreeViewHolder, position: Int) {
        val currentNode = getNodeAtPosition(rootNode, position)
        currentNode?.let {
            holder.bind(it)
        }
    }

    override fun getItemCount(): Int {
        return countNodes(rootNode)
    }

    private fun countNodes(node: PlayerStatsChainTree?): Int {
        if (node == null) return 0
        var count = 1
        for (childNode in node.childrenNodesList) {
            count += countNodes(childNode)
        }
        return count
    }

    private fun getNodeAtPosition(node: PlayerStatsChainTree, position: Int): PlayerStatsChainTree? {
        val index = AtomicInteger(0)
        return getNodeAtPositionRecursive(node, position, index)
    }

    private fun getNodeAtPositionRecursive(node: PlayerStatsChainTree, position: Int, index: AtomicInteger): PlayerStatsChainTree? {
        if (index.get() == position) {
            return node
        }
        for (childNode in node.childrenNodesList) {
            index.incrementAndGet()
            val result = getNodeAtPositionRecursive(childNode, position, index)
            if (result != null) {
                return result
            }
        }
        return null
    }
}
