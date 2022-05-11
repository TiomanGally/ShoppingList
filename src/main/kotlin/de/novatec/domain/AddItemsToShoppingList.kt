package de.novatec.domain

interface AddItemsToShoppingList {

    suspend operator fun invoke(shoppingListId: Long, items: List<String>)
}
