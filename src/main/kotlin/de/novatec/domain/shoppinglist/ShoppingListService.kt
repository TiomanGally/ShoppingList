package de.novatec.domain.shoppinglist

import io.smallrye.mutiny.Uni

interface ShoppingListService {

    fun create(shoppingList: ShoppingListEntity): Uni<ShoppingListEntity>

    fun findById(id: Long, fetchItems: Boolean = false): Uni<ShoppingListEntity?>

    fun deleteById(id: Long): Uni<Boolean>
}
