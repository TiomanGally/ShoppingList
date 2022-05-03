package de.novatec.domain

import de.novatec.configuration.NoArgConstructor
import javax.persistence.Entity
import javax.persistence.Id
import javax.persistence.Table

@Entity
@Table(name = "items")
@NoArgConstructor
data class ItemEntity(
    @Id val item: String
)
