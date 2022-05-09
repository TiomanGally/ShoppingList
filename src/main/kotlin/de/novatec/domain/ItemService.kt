package de.novatec.domain

interface ItemService {

    suspend fun getAll(): List<ItemEntity>

    suspend fun findById(id: Long): ItemEntity?

    suspend fun create(itemEntity: ItemEntity): ItemEntity

    suspend fun updateItemById(id: Long, item: String)

    suspend fun deleteById(id: Long)
}
