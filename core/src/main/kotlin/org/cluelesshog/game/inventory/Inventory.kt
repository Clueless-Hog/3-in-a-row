package org.cluelesshog.game.inventory

import engine.GameSave
import engine.event.signal
import org.cluelesshog.game.inventory.event.ItemAdded
import org.cluelesshog.game.inventory.event.ItemRemoved
import kotlin.math.max

object Inventory: Iterable<InventorySlot> {
    private val slots = mutableListOf<InventorySlot>()
    private val storage = PersistentStorage
    private val lock = Any()

    init {
        // Читаем все элементы из хранилища и воссоздаем на их основе инвентарь с последнего запуска
        storage.loadInto(this)
    }

    fun store(item: Item, quantity: Int = 1): InventorySlot {
        val existingSlot = findSlot(item)
        if (existingSlot != null) {
            existingSlot.quantity += quantity

            signal(ItemAdded(item, quantity, existingSlot))

            return existingSlot
        }

        val freeSlot = getFreeSlot()
        freeSlot.item = item
        freeSlot.quantity = quantity

        signal(ItemAdded(item, quantity, freeSlot))

        return freeSlot
    }

    fun remove(item: Item, quantity: Int) {
        val existingSlot = slots.first { it.item?.name == item.name }
        require(existingSlot.quantity >= quantity)
        existingSlot.quantity -= quantity

        signal(ItemRemoved(item, quantity, existingSlot))
    }

    fun store(inSlot: Int, item: Item, quantity: Int): InventorySlot {
        require(inSlot > 0) { "Слоты должны быть пронумерованы натуральными числами" }
        val slot = get(inSlot)
        require(slot.isEmpty()) { "Нельзя поместить предмет в непустой слот" }
        slot.item = item
        slot.quantity = quantity

        return slot
    }

    fun get(slot: Int): InventorySlot {
        require(slot > 0) { "Слоты должны быть пронумерованы натуральными числами" }

        val index = slot - 1
        synchronized(lock) {
            return slots.getOrElse(index) {
                while (slots.size <= index) {
                    slots.add(InventorySlot.newEmpty(slot))
                }
                slots[index]
            }
        }
    }

    fun getFreeSlot(): InventorySlot {
        synchronized(lock) {
            val freeSlot = slots.firstOrNull { it.isEmpty() }
            if (freeSlot !== null) {
                return freeSlot
            }

            val cell = max(size(), 1)
            slots.add(InventorySlot.newEmpty(cell))

            return get(cell)
        }
    }

    override fun iterator(): Iterator<InventorySlot> {
        return slots.iterator()
    }

    fun findSlot(withItem: Item) = slots.firstOrNull { it.item?.name == withItem.name }

    fun size(): Int {
        return slots.size
    }

    fun save() {
        storage.saveFrom(this)
    }
}

private object PersistentStorage {
    private val storage = GameSave

    fun loadInto(inventory: Inventory) {
        require(inventory.size() == 0) { "Попытка загрузить состояние в непустой инвентарь приведет к неожиданному результату" }

        val persistedInventorySize = storage.getOrNull<Int>("inventory.size") ?: 0
        for (cell in 1..persistedInventorySize) {
            val key = cellKey(cell)
            val itemName = storage.getOrNull<String>("$key.name")
            if (itemName !== null) {
                val itemQuantity = storage.getOrNull<Int>("$key.quantity")!!
                inventory.store(cell, ItemRef.all[itemName]!!, itemQuantity)
            } else {
                // Создает пустой слот
                inventory.get(cell)
            }
        }
    }

    fun saveFrom(inventory: Inventory) {
        storage.persisting {
            // Сохраняем инфу о размере инвентаря. Нужно для обращения к сохраненным данным при следующей загрузке
            save("inventory.size", inventory.size())

            // Сохраняем содержимое инвентаря
            inventory.forEach { slot ->
                val key = cellKey(slot.cell)
                if (slot.isEmpty()) {
                    delete("$key.name")
                    delete("$key.quantity")
                }

                slot.item?.let {
                    save("$key.name", it.name)
                    save("$key.quantity", slot.quantity)
                }
            }
        }
    }

    // TODO обращение по хэщмэпу уязвимо к утраченным ключам. Нужно хранить референсы на ключи или переключиться на Json
    private fun cellKey(cell: Int) = "inventory.${cell}"
}
