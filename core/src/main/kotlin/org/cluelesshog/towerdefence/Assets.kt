package org.cluelesshog.towerdefence

import com.badlogic.gdx.graphics.g2d.ParticleEffect
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
        get() = Sprite(AssetLoader.getTile("bullet")).apply { setSize(30f, 30f) }

    val effectExplosion
        get() = AssetLoader.getExplosionEffect()
}
