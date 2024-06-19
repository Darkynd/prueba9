package es.uji.al341571.overwatch2app.playersActivity

import es.uji.al341571.overwatch2app.classes.PlayerStats

class PlayerStatsChainTree(
    val nodeName: String,
    val value: Any? = null,
    val isTitle: Boolean = nodeName.contains("Stats"),
    var isLastChild: Boolean = false
) {
    val childrenNodesList = mutableListOf<PlayerStatsChainTree>()
    var parent: PlayerStatsChainTree? = null
    private var totalNodes: Int = 1 // Total nodes number counter, initialized to 1 for the current node

    fun addChildren(child: PlayerStatsChainTree) {
        child.parent = this
        childrenNodesList.add(child)
        updateTotalNodes()

        // Actualiza isLastChild para cada nodo hijo
        childrenNodesList.forEachIndexed { index, node ->
            node.isLastChild = index == childrenNodesList.lastIndex
        }
    }

    private fun updateTotalNodes() {
        totalNodes = 1 // Reset the counter
        for (childNode in childrenNodesList) {
            totalNodes += childNode.calculateTotalNodes() // Add nodes of the children
        }
        parent?.updateTotalNodes() // Update the counter in the parent if it exists
    }

    private fun calculateTotalNodes(): Int {
        var count = 1 // Counter for the current node
        for (childNode in childrenNodesList) {
            count += childNode.calculateTotalNodes() // Recursively adds the child nodes
        }
        return count
    }

    fun isRoot(): Boolean {
        return parent == null
    }
}

fun generateStatsTree(playerStats: PlayerStats): PlayerStatsChainTree {
    val average = playerStats.general.average
    val total = playerStats.general.total

    val rootNode = PlayerStatsChainTree("General Stats")
    rootNode.addChildren(PlayerStatsChainTree("Games Played", playerStats.general.gamesPlayed))
    rootNode.addChildren(PlayerStatsChainTree("Games Won", playerStats.general.gamesWon))
    rootNode.addChildren(PlayerStatsChainTree("Games Lost", playerStats.general.gamesLost))
    rootNode.addChildren(PlayerStatsChainTree("Time Played", playerStats.general.timePlayed))
    rootNode.addChildren(PlayerStatsChainTree("KDA", playerStats.general.kda))
    rootNode.addChildren(PlayerStatsChainTree("Winrate", playerStats.general.winrate))

    val totalNode = PlayerStatsChainTree("Total Stats")
    totalNode.addChildren(PlayerStatsChainTree("Eliminations", total.eliminations))
    totalNode.addChildren(PlayerStatsChainTree("Assists", total.assists))
    totalNode.addChildren(PlayerStatsChainTree("Deaths", total.deaths))
    totalNode.addChildren(PlayerStatsChainTree("Damage", total.damage))
    totalNode.addChildren(PlayerStatsChainTree("Healing", total.healing))
    rootNode.addChildren(totalNode)

    val averageNode = PlayerStatsChainTree("Average Stats")
    averageNode.addChildren(PlayerStatsChainTree("Eliminations", average.eliminations))
    averageNode.addChildren(PlayerStatsChainTree("Assists", average.assists))
    averageNode.addChildren(PlayerStatsChainTree("Deaths", average.deaths))
    averageNode.addChildren(PlayerStatsChainTree("Damage", average.damage))
    averageNode.addChildren(PlayerStatsChainTree("Healing", average.healing))
    rootNode.addChildren(averageNode)

    return rootNode
}


