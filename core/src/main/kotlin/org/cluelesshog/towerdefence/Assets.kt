package org.cluelesshog.towerdefence

import com.badlogic.gdx.audio.Music
import com.badlogic.gdx.audio.Sound
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

    val soundBulletShot: Sound by lazy { AssetLoader.getSound("mixkit-game-gun-shot-1662") }
    val soundBulletExplosion: Sound by lazy { AssetLoader.getSound("mixkit-falling-hit-757") }
    val musicMain: Music by lazy { AssetLoader.getMusic("mixkit-game-level-music-689") }
}
