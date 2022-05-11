package de.novatec.domain.shoppinglist

import de.novatec.configuration.NoArgConstructor
import de.novatec.domain.item.ItemEntity
import javax.persistence.*

@Entity
@Table(name = "shopping_list")
@NoArgConstructor
data class ShoppingListEntity(

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "shopping_list_items",
        joinColumns = [JoinColumn(name = "shopping_list_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "item_id", referencedColumnName = "id")],
    )
    val items: List<ItemEntity> = listOf(),

    @Id @GeneratedValue val id: Long = 0,
)
