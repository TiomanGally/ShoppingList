package de.novatec.port

import de.novatec.domain.recipe.Recipe
import de.novatec.domain.recipe.RecipeEntity
import de.novatec.domain.recipe.RecipeService
import io.quarkus.hibernate.reactive.panache.common.runtime.ReactiveTransactional
import io.smallrye.mutiny.Uni
import org.jboss.resteasy.reactive.RestPath
import org.jboss.resteasy.reactive.RestResponse
import java.net.URI
import javax.transaction.Transactional
import javax.ws.rs.GET
import javax.ws.rs.PATCH
import javax.ws.rs.POST
import javax.ws.rs.Path

@Path("/recipes")
class RecipeController(
    private val recipeService: RecipeService
) {

    @GET
    fun getAll(): Uni<RestResponse<List<Recipe>>> {
        return recipeService
            .getAll()
            .onItem().transform { RestResponse.ok(it) }
    }

    @GET
    @Path("/{id}")
    fun findById(
        @RestPath("id") id: Long,
    ): Uni<RestResponse<RecipeEntity>> {
        return recipeService
            .findById(id, true)
            .onItem().transform { RestResponse.ok(it) }
    }

    @POST
    @Path("/{title}")
    @ReactiveTransactional
    fun create(
        @RestPath("title") title: String
    ): Uni<RestResponse<RecipeEntity>> {
        return recipeService
            .create(title)
            .onItem().transform { URI.create("/${it.id}") }
            .onItem().transform { RestResponse.created(it) }
    }

    @PATCH
    @Path("/{id}")
    @Transactional
    suspend fun updateRecipe(
        @RestPath("id") id: Long,
        updateRecipe: UpdateRecipe
    ): RestResponse<RecipeEntity> {
        return recipeService
            .update(id, updateRecipe.title, updateRecipe.items)
            .let { RestResponse.noContent() }
    }

    data class UpdateRecipe(
        val title: String? = null,
        val items: List<Long>? = null
    )
}
