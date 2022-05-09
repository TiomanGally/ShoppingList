package de.novatec.domain

import de.novatec.configuration.NoArgConstructor
import javax.persistence.Entity
import javax.persistence.GeneratedValue
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "items")
@NoArgConstructor
data class ItemEntity(
    @Id @GeneratedValue val id: Long? = null,
    val item: String
)
