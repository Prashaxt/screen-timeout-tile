package com.example.screentimeouttile

import android.provider.Settings
import android.service.quicksettings.Tile
import android.service.quicksettings.TileService

class TimeoutTileService : TileService() {

    // Timeout values in milliseconds
    private val timeoutValues = intArrayOf(
        15000,    // 15 seconds
        30000,    // 30 seconds
        60000,    // 1 minute
        120000,   // 2 minutes
        300000,   // 5 minutes
        600000,   // 10 minutes

    )

    // Matching icon resource IDs for each timeout level
    private val timeoutIcons = intArrayOf(
        R.drawable.ic_moon_15s,   // 15s
        R.drawable.ic_moon_30s,   // 30s
        R.drawable.ic_moon_1m,   // 1min
        R.drawable.ic_moon_2m,   // 2min
        R.drawable.ic_moon_5m,   // 5min
        R.drawable.ic_moon_10m,   // 10mi
    )

    // SharedPreferences key
    companion object {
        private const val PREFS_NAME = "timeout_prefs"
        private const val KEY_INDEX  = "current_index"
        private const val INDEX_CUSTOM = -1
    }

    // Get saved index, default to 0
    private fun getSavedIndex(): Int {
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        return prefs.getInt(KEY_INDEX, 0)
    }

    // Save current index
    private fun saveIndex(index: Int) {
        getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
            .edit()
            .putInt(KEY_INDEX, index)
            .apply()
    }

    private fun showCustomTile() {
        val tile = qsTile ?: return
        tile.icon = android.graphics.drawable.Icon.createWithResource(
            this,
            R.drawable.ic_moon_unknown
        )
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }

    private fun setInactiveTile() {
        val tile = qsTile ?: return
        tile.icon = android.graphics.drawable.Icon.createWithResource(
            this,
            R.drawable.ic_moon_disabled
        )
        tile.state = Tile.STATE_INACTIVE
        tile.updateTile()
    }

    // Called when tile becomes visible — sync icon to current system value
    override fun onStartListening() {
        super.onStartListening()
        if (!Settings.System.canWrite(this)) {
            setInactiveTile()
            return
        }

        // Check if tile is paused
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val tileEnabled = prefs.getBoolean("tile_enabled", true)
        if (!tileEnabled) {
            setInactiveTile()
            return
        }

        val systemTimeout = Settings.System.getInt(
            contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            timeoutValues[0]
        )

        val matchedIndex = timeoutValues.indexOfFirst { it == systemTimeout }
            .takeIf { it >= 0 } ?: INDEX_CUSTOM

        if (matchedIndex == INDEX_CUSTOM) {
            showCustomTile()
        } else {
            saveIndex(matchedIndex)
            updateTile(matchedIndex)
        }
    }

    // Called when user taps the tile
    override fun onClick() {
        super.onClick()

        // Check permission is still granted
        if (!Settings.System.canWrite(this)) {
            return
        }

        // Check if tile is paused
        val prefs = getSharedPreferences(PREFS_NAME, MODE_PRIVATE)
        val tileEnabled = prefs.getBoolean("tile_enabled", true)
        if (!tileEnabled) return

        // Advance to next index, wrap around at end
        val nextIndex = (getSavedIndex() + 1) % timeoutValues.size

        // Write new timeout to system
        Settings.System.putInt(
            contentResolver,
            Settings.System.SCREEN_OFF_TIMEOUT,
            timeoutValues[nextIndex]
        )

        // Save new index
        saveIndex(nextIndex)

        // Update tile icon
        updateTile(nextIndex)
    }

    // Updates the tile icon based on current index
    private fun updateTile(index: Int) {
        val tile = qsTile ?: return
        tile.icon = android.graphics.drawable.Icon.createWithResource(
            this,
            timeoutIcons[index]
        )
        tile.state = Tile.STATE_ACTIVE
        tile.updateTile()
    }
}