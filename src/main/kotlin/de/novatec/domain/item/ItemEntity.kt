package de.novatec.domain.item

import de.novatec.configuration.NoArgConstructor
import javax.persistence.*

@Entity
@Table(name = "items")
@NoArgConstructor
data class ItemEntity(
    @Column(unique = true)
    val item: String,

    @Id @GeneratedValue val id: Long? = null,
)
