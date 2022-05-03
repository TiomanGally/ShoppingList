package de.novatec.domain

interface ItemService {

    suspend fun getAll(): List<ItemEntity>

    suspend fun getByItem(item: String): ItemEntity

    suspend fun persist(itemEntity: ItemEntity): ItemEntity

    suspend fun update(itemEntity: ItemEntity): Int

    suspend fun delete(itemEntity: ItemEntity)
}
