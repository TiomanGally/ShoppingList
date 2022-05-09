package de.novatec.domain

interface ItemService {

    suspend fun getAll(): List<ItemEntity>

    suspend fun findItem(item: String): ItemEntity?

    suspend fun persist(itemEntity: ItemEntity): ItemEntity

    suspend fun updateItem(oldItem: String, newItem: String)

    suspend fun delete(item: String)
}
