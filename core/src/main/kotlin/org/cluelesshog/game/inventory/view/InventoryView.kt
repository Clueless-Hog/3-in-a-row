package org.cluelesshog.game.inventory.view

import com.badlogic.gdx.scenes.scene2d.ui.Table
import engine.event.listen
import ktx.actors.onClick
import org.cluelesshog.game.inventory.Inventory
import org.cluelesshog.game.inventory.event.ItemAdded
import org.cluelesshog.game.inventory.event.ItemRemoved

class InventoryView(private val dimension: Int, private val slotSize: Float, private val inventory: Inventory) : Table() {
    private val padding = 5
    private var currentlySelectedSlot: SlotView? = null

    private var onSlotClicked: (SlotView) -> Unit = {}

    init {
        require(dimension > 0) { "Инвентарь должен иметь хотя бы одну ячейку" }

        setFillParent(true)
        setSize(
            dimension * (slotSize + padding) - padding,
            dimension * (slotSize + padding) - padding
        )

        for (row in 1 until dimension + 1) {
            for (col in 1 until dimension + 1) {
                add(createSlot(row, col)).size(slotSize)
            }
            row()
        }

        listen { it: ItemAdded ->
            val slotView = findActor<SlotView>("slot:${it.slot.cell}")
            slotView.update()
        }

        listen { it: ItemRemoved ->
            val slotView = findActor<SlotView>("slot:${it.slot.cell}")
            slotView.update()
        }
    }

    fun onSlotClicked(listener: (SlotView) -> Unit) {
        onSlotClicked = listener
    }

    private fun createSlot(row: Int, col: Int): SlotView {
        val cell = (row * dimension) - (dimension - col)

        return SlotView(inventory.get(cell)).also {
            it.name = "slot:$cell"
            it.onClick {
                if (!slot.isEmpty()) {
                    if (isHighlighted) {
                        unhighlight()
                        currentlySelectedSlot = null
                    } else {
                        highlight()
                        currentlySelectedSlot?.unhighlight()
                        currentlySelectedSlot = it
                    }
                    onSlotClicked(it)
                }
            }
        }
    }
}
