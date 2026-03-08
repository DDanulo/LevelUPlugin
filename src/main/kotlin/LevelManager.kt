package org.example

import com.intellij.openapi.components.PersistentStateComponent
import com.intellij.openapi.components.Service
import com.intellij.openapi.components.State
import com.intellij.openapi.components.Storage

@Service
@State(
    name = "LevelPluginSettings",
    storages = [Storage("LevelPluginData.xml")]
)
class LevelManager : PersistentStateComponent<LevelManager.PluginState> {

    var currentLevel: Int = 1
    var currentExp: Int = 0
    var expToNextLevel: Int = 100

    class PluginState {
        var savedLevel: Int = 1
        var savedExp: Int = 0
        var savedExpToNext: Int = 100
    }

    override fun getState(): PluginState {
        val state = PluginState()
        state.savedLevel = currentLevel
        state.savedExp = currentExp
        state.savedExpToNext = expToNextLevel
        return state
    }

    override fun loadState(state: PluginState) {
        currentLevel = state.savedLevel
        currentExp = state.savedExp
        expToNextLevel = state.savedExpToNext
    }

    fun addExp(amount: Int): Boolean {
        currentExp += amount
        return checkLevelUp()
    }

    private fun checkLevelUp(): Boolean {
        var leveledUp = false
        while (currentExp >= expToNextLevel) {
            currentExp -= expToNextLevel
            currentLevel++
            expToNextLevel = calculateNextLevelExp()
            leveledUp = true
        }
        return leveledUp
    }

    private fun calculateNextLevelExp(): Int {
        return currentLevel * 100
    }
}