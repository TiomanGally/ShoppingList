package de.novatec.domain.recipe

import de.novatec.configuration.NoArgConstructor
import de.novatec.domain.item.ItemEntity
import javax.persistence.*

@Entity
@Table(name = "recipe")
@NoArgConstructor
data class RecipeEntity(

    val title: String,

    @OneToMany(fetch = FetchType.LAZY)
    @JoinTable(
        name = "recipe_items",
        joinColumns = [JoinColumn(name = "recipe_id", referencedColumnName = "id")],
        inverseJoinColumns = [JoinColumn(name = "item_id", referencedColumnName = "id")],
    )
    val items: List<ItemEntity> = listOf(),

    @Id @GeneratedValue val id: Long = 0,
)
