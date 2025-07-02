package org.cluelesshog.towerdefence.scene.ingame

import com.badlogic.gdx.graphics.g2d.Batch
import com.badlogic.gdx.graphics.g2d.Sprite
import com.badlogic.gdx.scenes.scene2d.Actor
import com.badlogic.gdx.scenes.scene2d.Event
import com.badlogic.gdx.scenes.scene2d.InputEvent
import com.badlogic.gdx.scenes.scene2d.utils.ClickListener
import org.cluelesshog.engine.actor.SpritedActor
import org.cluelesshog.towerdefence.Assets
import org.cluelesshog.engine.geometry.Position

data class TowerSpotClicked(val towerSpot: TowerSpot) : Event() {
    init {
        target = towerSpot
    }
}

class TowerSpot(position: Position, texture: Sprite = Assets.towerSpot) : SpritedActor(position, texture) {

    init {
        addListener(
            object : ClickListener() {
                override fun clicked(event: InputEvent?, x: Float, y: Float) {
                    fire(TowerSpotClicked(this@TowerSpot))
                }
            }
        )
    }
}
