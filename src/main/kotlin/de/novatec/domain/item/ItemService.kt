package de.novatec.domain.item

import io.smallrye.mutiny.Uni

interface ItemService {

    fun getAll(): Uni<List<ItemEntity>>

    fun findById(id: Long): Uni<ItemEntity?>

    fun findByItems(items: List<String>): Uni<List<ItemEntity>>

    fun create(itemEntity: ItemEntity): Uni<ItemEntity>

    fun updateItemById(id: Long, item: String): Uni<Int>

    fun deleteById(id: Long): Uni<Boolean>
}
