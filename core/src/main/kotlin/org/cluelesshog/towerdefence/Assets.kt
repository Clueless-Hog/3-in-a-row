package org.cluelesshog.towerdefence

import org.cluelesshog.engine.asset.AssetLoader
import com.badlogic.gdx.graphics.g2d.Sprite

object Assets {
    val tower
        get() = Sprite(AssetLoader.getTile("fireTower"))
    val towerSpot
        get() = Sprite(AssetLoader.getTile("towerSpot"))
    val enemy
        get() = Sprite(AssetLoader.getTile("enemy"))
    val bullet
        get() = Sprite(AssetLoader.getTile("bullet")).apply { setSize(50f, 50f) }
}
