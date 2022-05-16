package de.novatec.domain.recipe

import io.smallrye.mutiny.Uni

interface RecipeService {

    fun getAll(): Uni<List<Recipe>>

    fun findById(id: Long, fetchItems: Boolean = false): Uni<RecipeEntity>

    fun create(title: String): Uni<RecipeEntity>

    suspend fun update(id: Long, title: String?, items: List<Long>?)
}
